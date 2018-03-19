package cc.zsakvo.a99demo;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.phrase.Book;
import cc.zsakvo.a99demo.task.DownloadTask;
import cc.zsakvo.a99demo.task.GetDownloadInfoTask;
import cc.zsakvo.a99demo.task.GetBookDetailTask;
import cc.zsakvo.a99demo.utils.DialogUtils;
import cc.zsakvo.a99demo.utils.EpubUtils;
import cc.zsakvo.a99demo.utils.SplitUtil;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener,OnDataFinishedListener,EpubUtils.getResult{

    Toolbar toolbar;
    String title,intro,detail,coverUrl;
    Document doc;
    TextView tv_title,tv_intro,tv_detail;
    ImageView iv_cover;
    String url;
    FloatingActionButton fab;
    Book book;
    Dialog loadingDialog;
    ConcurrentHashMap<Integer,String> ch = new ConcurrentHashMap<> ();
    private int nowNum;
    private int allNum;
    private List<Integer> chapterIDs;
    private List<String> chapters;
    DialogUtils du;
    DownloadDetails downloadDetails = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        toolbar = (Toolbar)findViewById(R.id.bdToolBar);
        fab = (FloatingActionButton)findViewById(R.id.bdFab);
        fab.setOnClickListener(this);
        toolbar.setTitle("书籍详情");
        url = getIntent().getStringExtra("url");
        tv_title = (TextView)findViewById(R.id.bdTitle);
        tv_intro = (TextView)findViewById(R.id.bdIntro);
        tv_detail = (TextView)findViewById(R.id.bdDetail);
        iv_cover = (ImageView)findViewById(R.id.bdCover);
        du = new DialogUtils (this,loadingDialog);
        new GetBookDetailTask (tv_title,tv_intro,tv_detail,iv_cover).execute (url);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bdFab:
                verifyStoragePermissions(BookDetailActivity.this);
                GetDownloadInfoTask gdi = new GetDownloadInfoTask (du);
                gdi.setOnDataFinishedListener (this);
//                gdi.setOnDataFinishedListener (new OnDataFinishedListener () {
//                    @Override
//                    public void onDataSuccessfully(Object data) {
//                        downloadDetails = (DownloadDetails)data;
//                        du.setAllNum (downloadDetails.getChapterIDs ().size ());
//                        for (int[] integers:SplitUtil.splitChaIDsByNum (downloadDetails.getChapterIDs (),3)){
//                            new DownloadTask (downloadDetails.getBookID (),
//                                ch,
//                                du,
//                                downloadDetails.getChapterIDs ().size (),nowNum)
//                            .executeOnExecutor (THREAD_POOL_EXECUTOR,integers);
//                        }
//                    }
//
//                    @Override
//                    public void onDataFailed() {
//
//                    }
//                });
                gdi.execute (url);
        }
    }



    private void initDialog(){
        LayoutInflater inflater = LayoutInflater.from(BookDetailActivity.this);
        View v = inflater.inflate(R.layout.dialog, null); // 得到加载view
        DisplayMetrics displaymetrics = new DisplayMetrics();
        BookDetailActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        v.setMinimumWidth((int) (screenWidth * 0.8));
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dia_lay); // 加载布局
        loadingDialog = new Dialog(BookDetailActivity.this); // 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 不可以用"返回键"取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        loadingDialog.show();
    }

    private void setDialogTitle(String title){
        TextView d_tv = loadingDialog.findViewById(R.id.dia_text);
        d_tv.setText(title);
    }


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSuccessfully(Object data) {

    }

    @Override
    public void onDataSuccessfully(DownloadDetails downloadDetails) {
        this.downloadDetails = downloadDetails;
        chapterIDs = downloadDetails.getChapterIDs ();
        allNum = chapterIDs.size ();
        du.setAllNum (allNum);
        for (int[] integers:SplitUtil.splitChaIDsByNum (downloadDetails.getChapterIDs (),3)){
            new DownloadTask (downloadDetails.getBookID (),
                    ch,
                    du,
                    allNum,
                    nowNum,
                    this)
                    .executeOnExecutor (THREAD_POOL_EXECUTOR,integers);
        }
    }

    @Override
    public void onDataFailed() {

    }

    @Override
    public void onDownloadFinishedNum(int num){
        nowNum+=num;
        chapters = new ArrayList<> ();
        if (nowNum>=allNum){
            du.setDialogTitle ("正在写出数据……");
            for (int id:chapterIDs){
                chapters.add (ch.get (id));
            }
            new EpubUtils (downloadDetails.getBookID (),
                    downloadDetails.getBookName (),
                    downloadDetails.getBookAuthor (),
                    downloadDetails.getBookCoverURL (),
                    chapters,
                    downloadDetails.getTitles (),
                    this).generateEpub ();
        }
    }

    @Override
    public void isGenerOk(int i) {
        if (i==1){
            du.concelDialog ();
            Snackbar.make (fab,"书籍下载完毕！",Snackbar.LENGTH_LONG).show ();
        }
    }
}
