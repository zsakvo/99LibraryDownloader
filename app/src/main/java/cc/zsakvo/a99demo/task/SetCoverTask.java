package cc.zsakvo.a99demo.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cc.zsakvo.a99demo.listener.Interface;

/**
 * Created by akvo on 2018/3/21.
 */

public class SetCoverTask extends AsyncTask<String, Void, Bitmap> {

    Interface.GetCover gc;

    public SetCoverTask(Interface.GetCover gc){
        this.gc = gc;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String imgUrl = strings[0];
        Bitmap bitmap;
        try {
            URL url = new URL (imgUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection ();
            connection.setConnectTimeout (20000);
            connection.setRequestMethod ("GET");
            if (connection.getResponseCode ()==200){
                InputStream in = connection.getInputStream ();
                bitmap = BitmapFactory.decodeStream (in);
            }else {
                bitmap = null;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        gc.GetCoverOK (bitmap);
    }
}
