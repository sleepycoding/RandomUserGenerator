package com.sleepycoder.randomuserapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv_name, tv_email, tv_gender, tv_birthdate, tv_age;
    ImageView iv_user_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_gender = findViewById(R.id.tv_gender);
        tv_birthdate = findViewById(R.id.tv_birthdate);
        tv_age = findViewById(R.id.tv_age);
        iv_user_picture = findViewById(R.id.iv_user_picture);

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
                    publishProgress(name, 0);

                    String email = user.getString("email");
                    publishProgress(email, 1);

                    String gender = user.getString("gender");
                    publishProgress(gender, 2);

                    String image_url = user.getJSONObject("picture").getString("medium");
                    publishProgress(image_url, 3);

                    String birthdate = user.getJSONObject("dob").getString("date");
                    publishProgress(birthdate, 4);

                    String age = user.getJSONObject("dob").getString("age");
                    publishProgress(age, 5);

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
                    tv_name.setText("Name: " + values[0].toString());
                    break;
                case 1:
                    tv_email.setText("Email: " + values[0].toString());
                    break;
                case 2:
                    tv_gender.setText("Gender: " + values[0].toString());
                    break;
                case 3:
                    Picasso.get().load(values[0].toString()).into(iv_user_picture);
                case 4:
                    tv_birthdate.setText("Date: " + values[0]);
                case 5:
                    tv_age.setText("Age: " + values[0]);
            }
        }
    }

}
