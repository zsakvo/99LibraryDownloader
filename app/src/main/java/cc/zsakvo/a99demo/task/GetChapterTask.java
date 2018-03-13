package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/2/22.
 */

public class GetChapterTask extends AsyncTask<Object,Void,String> {

    private OnDataFinishedListener onDataFinishedListener;
    private ConcurrentHashMap<String,String> chm;

    public GetChapterTask(ConcurrentHashMap<String,String> chm){
        this.chm = chm;
    }

    @Override
    protected String doInBackground(Object ... objects) {
        String[] urlStr = (String [])objects[0];
        String bookID = (String)objects[1];
        for (String s:urlStr){
            String url = "http://www.99lib.net/book/"+bookID+"/"+s+".htm";
            chm.put (s,DecodeUtils.url (url));
        }
        return null;
    }

    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute (s);
        onDataFinishedListener.onDataSuccessfully(s);
    }
}
