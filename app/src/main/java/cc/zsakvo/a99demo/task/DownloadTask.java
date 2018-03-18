package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import java.util.concurrent.ConcurrentHashMap;

import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.utils.DecodeUtils;
import cc.zsakvo.a99demo.utils.DialogUtils;

/**
 * Created by akvo on 2018/3/17.
 */

public class DownloadTask extends AsyncTask<Integer,Integer,Integer> {

    private int downloadsNum;
    private ConcurrentHashMap<Integer,String> ch;
    private String bookID;
    private DialogUtils du;
    private int allNum;

    public DownloadTask(String bookID, ConcurrentHashMap<Integer,String> ch, DialogUtils du,int allNum){
        this.bookID = bookID;
        this.ch = ch;
        this.du = du;
        this.allNum = allNum;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        String hostURL = "http://www.99lib.net/book/";
        this.downloadsNum = integers.length;
        int p = 1;
        for (int i:integers){
            String url = hostURL +bookID+"/"+i+".htm";
            ch.put (i,DecodeUtils.url (url));
            publishProgress();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer...integers){
        super.onProgressUpdate (integers);
        du.addProgress (allNum);
    }
}
