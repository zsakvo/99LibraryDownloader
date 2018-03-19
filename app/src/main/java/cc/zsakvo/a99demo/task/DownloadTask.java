package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.utils.DecodeUtils;
import cc.zsakvo.a99demo.utils.DialogUtils;

/**
 * Created by akvo on 2018/3/17.
 */

public class DownloadTask extends AsyncTask<int[],Integer,Integer> {

    private int downloadsNum;
    private ConcurrentHashMap<Integer,String> ch;
    private String bookID;
    private DialogUtils du;
    private int allNum;
    private int nowNum;
    private OnDataFinishedListener onDataFinishedListener;

    public DownloadTask(String bookID, ConcurrentHashMap<Integer,String> ch, DialogUtils du,int allNum,int nowNum,OnDataFinishedListener onDataFinishedListener){
        this.bookID = bookID;
        this.ch = ch;
        this.du = du;
        this.allNum = allNum;
        this.nowNum = nowNum;
        this.onDataFinishedListener = onDataFinishedListener;
    }

    @Override
    protected Integer doInBackground(int[]... ints) {
        String hostURL = "http://www.99lib.net/book/";
        int[] integers = ints[0];
        this.downloadsNum = integers.length;
        int p = 1;
        for (int i:integers){
            String url = hostURL +bookID+"/"+i+".htm";
            if (i==0){
                continue;
            }
            ch.put (i,DecodeUtils.url (url));
            publishProgress();
        }
        nowNum+=integers.length;
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer...integers){
        super.onProgressUpdate (integers);
        du.addProgress (allNum);
    }

    @Override
    protected void onPostExecute(Integer integer){
        super.onPostExecute (integer);
        onDataFinishedListener.onDownloadFinishedNum (this.downloadsNum);
    }
}
