package cc.zsakvo.ninecswd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import cc.zsakvo.ninecswd.classes.DownloadDetails;
import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.listener.OnDataFinishedListener;
import cc.zsakvo.ninecswd.task.DownloadTask;
import cc.zsakvo.ninecswd.task.GetDownloadInfoTask;
import cc.zsakvo.ninecswd.task.GetBookDetailTask;
import cc.zsakvo.ninecswd.task.OutPutTxtTask;
import cc.zsakvo.ninecswd.task.SetCoverTask;
import cc.zsakvo.ninecswd.utils.DialogUtils;
import cc.zsakvo.ninecswd.utils.EpubUtils;
import cc.zsakvo.ninecswd.utils.SplitUtil;
import io.simi.nob.NOBKit;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener,OnDataFinishedListener,EpubUtils.getResult,Interface.GetBookDetailFinish,OnRefreshListener,Interface.OutPutTxtFinish{

    Toolbar toolbar;
    TextView tv_title,tv_intro,tv_detail,tv_dlTXT,tv_dlEpub,tv_dlNob;
    ImageView iv_cover;
    RefreshLayout refreshLayout;
    String url;
    CardView cv;
    FloatingActionButton fab;
    Dialog loadingDialog;
    ConcurrentHashMap<Integer,String> ch = new ConcurrentHashMap<> ();
    private int nowNum;
    private int allNum;
    private List<Integer> chapterIDs;
    private int whatGenerate = 0;
    private Boolean loadOK = false;
    DialogUtils du;
    Boolean hasPermissions = false;
    DownloadDetails downloadDetails = null;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_book_detail);
        toolbar = (Toolbar)findViewById(R.id.bdToolBar);
        toolbar.setTitle("书籍详情");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        url = getIntent().getStringExtra("url");
        tv_title = (TextView)findViewById(R.id.bdTitle);
        tv_intro = (TextView)findViewById(R.id.bdIntro);
        tv_detail = (TextView)findViewById(R.id.bdDetail);
        iv_cover = (ImageView)findViewById(R.id.bdCover);
        iv_cover.setOnClickListener (this);
        du = new DialogUtils (this,loadingDialog);

        cv = (CardView)findViewById (R.id.dl_card);
        tv_dlTXT = (TextView)findViewById (R.id.dl_txt);
        tv_dlTXT.setOnClickListener (this);
        tv_dlEpub = (TextView)findViewById (R.id.dl_epub);
        tv_dlEpub.setOnClickListener (this);
        tv_dlNob = (TextView) findViewById (R.id.dl_nob);
        tv_dlNob.setOnClickListener (this);

        refreshLayout = (RefreshLayout) findViewById (R.id.bdRefreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableAutoLoadmore (true);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        refreshLayout.autoRefresh ();
        verifyStoragePermissions(BookDetailActivity.this);
    }

    private void beginDownload(){
        remindStoragePermissions(BookDetailActivity.this);
        if (hasPermissions) {
            nowNum = 0;
            GetDownloadInfoTask gdi = new GetDownloadInfoTask (du);
            gdi.setOnDataFinishedListener (this);
            gdi.execute (url);
        }else {
            Snackbar.make (toolbar,"未授予存储权限，无法下载书籍！",Snackbar.LENGTH_LONG).show ();
            verifyStoragePermissions (BookDetailActivity.this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dl_txt:
                whatGenerate = 0;
                beginDownload();
                break;
            case R.id.dl_epub:
                whatGenerate = 1;
                beginDownload();
                break;
            case R.id.dl_nob:
                whatGenerate = 2;
                beginDownload();
                break;
            case R.id.bdCover:
                Intent intent = new Intent (BookDetailActivity.this,ChangeCoverActivity.class);
                intent.putExtra ("str",tv_title.getText ());
                startActivity (intent);
        }
    }

    public  void verifyStoragePermissions(Activity activity) {

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

    public void remindStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                Snackbar.make (toolbar,"未授予存储权限，将无法下载书籍！",Snackbar.LENGTH_LONG).show ();
            }else {
                hasPermissions = true;
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
        for (int[] integers: SplitUtil.splitChaIDsByNum (downloadDetails.getChapterIDs (),3)){
            new DownloadTask (downloadDetails.getBookID (),
                    ch,
                    du,
                    allNum,
                    nowNum,
                    whatGenerate,
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
        List<String> chapters = new ArrayList<> ();
        if (nowNum>=allNum){
            du.setDialogTitle ("正在写出数据……");
            for (int id:chapterIDs){
                chapters.add (ch.get (id));
            }
            switch (whatGenerate){
                case 0:
                    StringBuilder sb = new StringBuilder ();
                    for (String s:chapters){
                        sb.append (s);
                    }
                    new OutPutTxtTask (downloadDetails.getBookName (),this).execute (sb.toString ());
                    break;
                case 1:
                    new EpubUtils (downloadDetails.getBookID (),
                            downloadDetails.getBookName (),
                            downloadDetails.getBookAuthor (),
                            downloadDetails.getBookCoverURL (),
                            chapters,
                            downloadDetails.getTitles (),
                            this).generateEpub ();
                    break;
                case 2:

                     NOBKit.Builder builder = new NOBKit
                            .Builder (Environment.getExternalStorageDirectory().getPath()+"/99lib/Nob/"+downloadDetails.getBookName ())
                            .cover (((BitmapDrawable) ((ImageView) iv_cover).getDrawable()).getBitmap())
                            .metadata (downloadDetails.getBookName (), downloadDetails.getBookAuthor ())
                            .putChapters (downloadDetails.getTitles (),chapters);
                     isGenerOk (1);
                     builder.build (downloadDetails.getBookName ());
                     break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish ();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void scanFile() {
        final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        final Uri contentUri = Uri.fromFile(new File (Environment.getExternalStorageDirectory().getPath()+"/99lib/Nob/"+downloadDetails.getBookName ()+"/"+downloadDetails.getBookName ()+".nob"));
        scanIntent.setData(contentUri);
        sendBroadcast(scanIntent);
    }

    @Override
    public void isGenerOk(int i) {
        if (i==1){
            du.concelDialog ();
            scanFile ();
            Snackbar.make (toolbar,"书籍下载完毕！",Snackbar.LENGTH_LONG).show ();
        }
    }

    @Override
    public void GetFailed() {
        refreshLayout.finishRefresh (false);
        Snackbar.make (toolbar,"数据获取失败，请检查网络连接或重试",Snackbar.LENGTH_LONG).show ();
    }

    @Override
    public void GetSuccessful(String...strings) {
        tv_title.setText (strings[0]);
        tv_intro.setText (strings[1]);
        tv_detail.setText (strings[2]);
//        new SetCoverTask (new Interface.GetCover () {
//            @Override
//            public void GetCoverOK(Bitmap bitmap) {
//                iv_cover.setImageBitmap (bitmap);
//                cv.setVisibility (View.VISIBLE);
//                cv.bringToFront ();
//                refreshLayout.finishRefresh (500);
//            }
//        }).execute (strings[3]);
        Glide.with(BookDetailActivity.this).load(strings[3]).into(iv_cover);
        cv.setVisibility (View.VISIBLE);
        cv.bringToFront ();
        refreshLayout.finishRefresh (500);
    }

    GetBookDetailTask gbd;

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        gbd = new GetBookDetailTask (this);
        gbd.execute (url);
    }

    @Override
    public void outPutFailed() {

    }

    @Override
    public void outPutSuccess(int i) {
        switch (i){
            case 1:
                du.concelDialog ();
                Snackbar.make (toolbar,"书籍下载完毕！",Snackbar.LENGTH_LONG).show ();
                break;
        }
    }

    @Override
    protected void onDestroy(){
        if (gbd != null && gbd.getStatus() != AsyncTask.Status.FINISHED)
            gbd.cancel(true);
        super.onDestroy();
    }
}
