package com.example.dlimagethread;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String URL = "https://p4.wallpaperbetter.com/wallpaper/291/663/679/stones-background-stones-spa-wallpaper-preview.jpg";
        startDownloadImage(URL);
    }

    protected void startDownloadImage(String imgURL) {
        String destFilename = UUID.randomUUID().toString() +
                imgURL.substring(imgURL.lastIndexOf("."));
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFilename );

        // creating a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                //download file from background thread
                if (downloadImage(imgURL, destFile)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //use downloaded file
                            Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());
                            ImageView imageView = findViewById(R.id.imageView);
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        }).start();
    }

    //download file and store in internal storage
    protected boolean downloadImage(String imgURL, File destFile) {
        try {
            URL url = new URL(imgURL);
            URLConnection conn = url.openConnection();

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(destFile);

            byte[] buf = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }

            out.close();
            in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}