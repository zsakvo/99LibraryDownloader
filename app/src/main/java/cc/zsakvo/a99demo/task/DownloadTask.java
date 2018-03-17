package cc.zsakvo.a99demo.task;

import android.os.AsyncTask;

import cc.zsakvo.a99demo.classes.DownloadDetails;

/**
 * Created by akvo on 2018/3/17.
 */

public class DownloadTask extends AsyncTask<DownloadDetails,Integer,Integer> {
    @Override
    protected Integer doInBackground(DownloadDetails... downloadDetails) {
        DownloadDetails dd =  downloadDetails[0];

        return 0;
    }
}
