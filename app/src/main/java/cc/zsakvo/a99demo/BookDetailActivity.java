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

import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.phrase.Book;
import cc.zsakvo.a99demo.task.GetDownloadInfoTask;
import cc.zsakvo.a99demo.task.GetBookDetailTask;
import cc.zsakvo.a99demo.utils.DialogUtils;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    String title,intro,detail,coverUrl;
    Document doc;
    TextView tv_title,tv_intro,tv_detail;
    ImageView iv_cover;
    String url;
    FloatingActionButton fab;
    Book book;
    Dialog loadingDialog;


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
        new GetBookDetailTask (tv_title,tv_intro,tv_detail,iv_cover).execute (url);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bdFab:
                verifyStoragePermissions(BookDetailActivity.this);
                DialogUtils du = new DialogUtils (this,loadingDialog);
                du.initDialog ();
                du.setDialogTitle ("222233334445");
                GetDownloadInfoTask gdi = new GetDownloadInfoTask ();
                gdi.setOnDataFinishedListener (new OnDataFinishedListener () {
                    @Override
                    public void onDataSuccessfully(Object data) {
                        downloadDetails = (DownloadDetails)data;
                        Log.e ("onDataSuccessfully: ",downloadDetails.getBookName () );
                    }

                    @Override
                    public void onDataFailed() {

                    }
                });

                gdi.execute (url);
//                GetDownloadInfoTask dt = new GetDownloadInfoTask ();
//                initDialog();
//                setDialogTitle("下载章节中……");
//                dt.execute (url);
//                dt.setOnDataFinishedListener (new OnDataFinishedListener () {
//                    @Override
//                    public void onDataSuccessfully(Object data) {
//                         if ((int)data==1){
//                             loadingDialog.dismiss();
//                             Snackbar.make(fab,"下载完毕！",Snackbar.LENGTH_LONG).show();
//                         }
//                    }
//
//                    @Override
//                    public void onDataFailed() {
//
//                    }
//                });
//                break;
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

//    class PhraseBookDetail {
//        private String bookID;
//        private String bookName;
//        private String bookAuthor;
//        private String bookCoverURL;
//        private List<String> chapters;
//        private List<String> titles;
//
//        public void deal(){
//            initDialog();
//            setDialogTitle("下载章节中……");
//            bookID = url.replace("http://www.99lib.net/book/","").replace("/index.htm","");
//            Message msg = new Message();
//            @SuppressLint("HandlerLeak")
//            android.os.Handler handler = new android.os.Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    switch (msg.what) {
//                        case 0:
////                            book = new Book(bookID,bookName,bookAuthor,bookCoverURL,chapters,titles);
//                            setDialogTitle("正在下载……");
//                            new EpubUtils(bookID,bookName,bookAuthor,bookCoverURL,chapters,titles).generateEpub();
//                            loadingDialog.dismiss();
//                            Snackbar.make(fab,"下载完毕！",Snackbar.LENGTH_LONG).show();
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            };
//            new getResult(handler,msg).start();
//        }
//
//        class getResult extends Thread{
//            Handler handler;
//            Message msg;
//            public getResult(Handler handler, Message msg){
//                this.handler = handler;
//                this.msg = msg;
//            }
//
//            private String splitElement(Element element){
//                Elements es = element.select("a");
//                String str = "";
//                for (Element e:es){
//                    String tmp = e.text()+" ";
//                    str = str + tmp;
//                }
//                element.select("a").remove();
//                String tmp = element.text()+" ";
//                str = tmp+str;
//                return str;
//            }
//
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    int p = 1;
//                    Document doc = Jsoup.connect(url).timeout(20000).get();
//                    bookName = doc.selectFirst("h2").text();
//                    bookAuthor = splitElement(doc.selectFirst("h4"));
//                    bookCoverURL = "http://www.99lib.net"+doc.selectFirst("img").attr("src");
//                    Elements elements_drags = doc.getElementById("right").select("dd");
//                    titles = new ArrayList<>();
//                    chapters = new ArrayList<>();
//                    for (Element e:elements_drags){
//                        titles.add(e.selectFirst("a").text());
//                        chapters.add(DecodeUtils.url("http://www.99lib.net"+e.selectFirst("a").attr("href")));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                msg.what = 0;
//                handler.sendMessage(msg);
//            }
//        }
//
//    }

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
}
