package com.example.weatherapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void getWeather(View  view)
    {
        try {
            String encode = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            downloadTask task = new downloadTask();
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + encode + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            //To hide keyboard after hitting button

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext() , "Could not find weather :(" , Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public class downloadTask extends AsyncTask<String , Void  , String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url = null;
            try {

                String result="";
                url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1)
                {
                    char x = (char) data;
                    result += x;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext() , "could not find Weather :(" , Toast.LENGTH_LONG).show();
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weaterInfo = jsonObject.getString("weather");

                Log.i("weather content" , weaterInfo);

                JSONArray jsonArray = new JSONArray(weaterInfo);

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String mssg="";

                    String main = jsonPart.getString("main");
                    String desc = jsonPart.getString("description");

                    if(!main.equals("") && !desc.equals(""))
                      mssg += main + " : " + desc + "\r\n";
                    if(!mssg.equals(""))
                    textView.setText(mssg);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext() , "Could not find weather :(" , Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editTextTextPersonName2);
        textView = (TextView) findViewById(R.id.weather_info);

    }
}