package com.sleepycoder.randomuserapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv_name, tv_email, tv_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_gender = findViewById(R.id.tv_gender);

        findViewById(R.id.btn_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkTask nt = new NetworkTask();
                nt.execute();
            }
        });
    }


    class NetworkTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                URL url = new URL("https://randomuser.me/api/");
                InputStream is = url.openStream();
                byte[] buffer = new byte[4096];
                StringBuilder sb = new StringBuilder("");


                while (is.read(buffer) != -1) {
                    sb.append(new String(buffer));
                }

                Log.i("apiresponse", sb.toString());

                try {
                    JSONObject obj = new JSONObject(sb.toString());
                    JSONArray results = obj.getJSONArray("results");
                    JSONObject user = results.getJSONObject(0);
                    JSONObject nameObj = user.getJSONObject("name");
                    String name = nameObj.getString("title") + ". " + nameObj.getString("first") + " " + nameObj.getString("last");
                    String email = user.getString("email");
                    String gender = user.getString("gender");
                    publishProgress(name, 0);
                    publishProgress(email, 1);
                    publishProgress(gender, 2);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            Toast.makeText(MainActivity.this, values[0].toString(), Toast.LENGTH_SHORT).show();
            switch (Integer.parseInt(values[1] + "")) {
                case 0:
                    tv_name.setText(values[0].toString());
                    break;
                case 1:
                    tv_email.setText(values[0].toString());
                    break;
                case 2:
                    tv_gender.setText(values[0].toString());
                    break;
            }
        }
    }

}
