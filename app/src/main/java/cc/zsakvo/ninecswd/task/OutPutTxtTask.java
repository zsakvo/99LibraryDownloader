package cc.zsakvo.ninecswd.task;

import android.os.AsyncTask;

import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.utils.TxtUtils;

/**
 * Created by akvo on 2018/3/20.
 */

public class OutPutTxtTask extends AsyncTask<String,Integer,Integer> {

    private String bookName;
    private Interface.OutPutTxtFinish opf;

    public OutPutTxtTask(String bookName, Interface.OutPutTxtFinish opf){
        this.bookName = bookName;
        this.opf = opf;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        TxtUtils.generateTXT (bookName,strings[0]);
        opf.outPutSuccess (1);
        return null;
    }
}
