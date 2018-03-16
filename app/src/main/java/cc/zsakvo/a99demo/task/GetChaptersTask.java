package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.listener.OnDatasFinishedListener;
import cc.zsakvo.a99demo.utils.DecodeUtils;

/**
 * Created by akvo on 2018/3/16.
 */

public class GetChaptersTask extends AsyncTask<String,Void,String[]>{

    private int num;
    private String[] chapt;

    public GetChaptersTask(int num,String[] chapt){
        this.num = num;
        this.chapt = chapt;
    }

    private OnDatasFinishedListener onDatasFinishedListener;

    @Override
    protected String[] doInBackground(String... strings) {
        String[] strs = new String[strings.length];
        int i = 0;
        for (String url:strings){
            strs[i] =  (DecodeUtils.url (url));
            i++;
        }
        return strs;
    }

    @Override
    protected void onPostExecute(String...strings){
        super.onPostExecute (strings);
        Log.e ( "onPostExecute: ",strings[0] );
        chapt[num] = strings[0];
//        onDatasFinishedListener.onDatasSuccessfully(new Object[]{num,strings});
    }

    public void setOnDatasFinishedListener(
            OnDatasFinishedListener onDatasFinishedListener) {
        this.onDatasFinishedListener = onDatasFinishedListener;
    }
}
