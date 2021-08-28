package com.runtimeterror.rekindle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;
    public DownloadSpriteTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            return BitmapFactory.decodeStream(url.openStream());
        }
        catch (IOException e) {
            Log.e(Constants.TAG, "Download image error", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // load the bitmap into the ImageView!
        this.imageView.setImageBitmap(bitmap);
    }
}
