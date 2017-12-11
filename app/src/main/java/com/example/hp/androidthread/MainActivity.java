package com.example.hp.androidthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);

        Button threadLoadButton = (Button) findViewById(R.id.threadLoadButton);
        threadLoadButton.setOnClickListener(this);

        Button asyncLoadButton = (Button) findViewById(R.id.asyncLoadButton);
        asyncLoadButton.setOnClickListener(this);
    }

    private void threadImage() throws IOException {
        final URL url = new URL("http://www.guyandtheblog.com/wp-content/uploads/2016/03/2426_S1230P0226_L_COMPO_RENDER_0143R-1.jpg");

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap finalBmp = bmp;
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(finalBmp);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.threadLoadButton:

                try {
                    threadImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.asyncLoadButton:

                new LoadImageTask().execute("http://www.majorcineplex.com/uploads/content/images/secret-life-of-pets-happy-easter.jpg");
            default:
                break;
        }
    }

    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            imageView.setImageBitmap(result);
        }
    }
}