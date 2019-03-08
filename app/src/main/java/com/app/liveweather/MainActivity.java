package com.app.liveweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    EditText editText;
    DownloadTask downloadTask;
    public void findWeather(View view)
    {
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        downloadTask=new DownloadTask();

        try {
            String s= null;
            s = URLEncoder.encode(editText.getText().toString().trim(),"UTF-8");
            downloadTask.execute("https://api.openweathermap.org/data/2.5/weather?q="+s+"&appid=bbd42895bf28a09b43a22c6ec781b1e0");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView2);
    }
    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection httpURLConnection=null;

            try {
                url=new URL(urls[0]);
                httpURLConnection=(HttpURLConnection) url.openConnection();
                InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
                int data=inputStreamReader.read();
                while(data!=-1)
                {
                    char in=(char)data;
                    result += in;
                    data =inputStreamReader.read();
                }
                return result;
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Error in Finding Weather", Toast.LENGTH_LONG).show();
            }

            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String msg="";
            try {
                JSONObject jsonObject=new JSONObject(result);
                String weather=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(weather);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject part=jsonArray.getJSONObject(i);
                    String main=part.getString("main");
                    String des=part.getString("description");
                    if(main!="" & des!="")
                    {
                        msg+="Description -> "+des+"\n";
                    }
                }
                if(msg!=""){

                    textView.setText(msg);}
                else
                    Toast.makeText(getApplicationContext(),"Error in Finding Weather", Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
