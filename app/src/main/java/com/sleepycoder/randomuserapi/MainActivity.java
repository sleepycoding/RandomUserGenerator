package com.sleepycoder.randomuserapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


                while (is.read(buffer) > 0) {
                    sb.append(new String(buffer));
                }

                publishProgress(sb);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            Toast.makeText(MainActivity.this, values[0].toString(), Toast.LENGTH_SHORT).show();;
        }
    }

}
