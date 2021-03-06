package cc.zsakvo.ninecswd.listener;

import cc.zsakvo.ninecswd.classes.DownloadDetails;

/**
 * Created by akvo on 2018/2/22.
 */

public interface OnDataFinishedListener {
    public void onDataSuccessfully(Object data);
    public void onDataSuccessfully(DownloadDetails data);
    public void onDownloadFinishedNum(int num);
    public void onDataFailed();
}
