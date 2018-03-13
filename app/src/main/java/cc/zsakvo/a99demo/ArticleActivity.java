package cc.zsakvo.a99demo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.zzhoujay.richtext.RichText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import cc.zsakvo.a99demo.utils.DecodeUtils;

public class ArticleActivity extends AppCompatActivity {

    private String url;
    private StateView mStateView;
    TextView tv_article;
    String content;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        tv_article = (TextView)findViewById(R.id.article);
        toolbar = (Toolbar)findViewById (R.id.atToolBar);
        toolbar.setTitle ("文库");
        setSupportActionBar(toolbar);
        mStateView = StateView.inject(findViewById (R.id.atToolBar));
        this.url = getIntent().getStringExtra("url");
        Message msg = new Message();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        RichText.fromHtml(content).into(tv_article);
                        break;
                }
            }
        };
        new getResult (handler,msg).start ();
    }

    class getResult extends Thread{
        Handler handler;
        Message msg;

        public getResult(Handler handler,Message msg){
            this.handler = handler;
            this.msg = msg;
        }

        @Override
        public void run() {
            super.run();
            try {
//                Document doc = Jsoup.connect(url).timeout(20000).get();
//                Element e = doc.getElementById("content");
                content = DecodeUtils.url (url);
//                e.select ("a").remove ();
//
//                content = doc.getElementById("content").html();
            } catch (Exception e) {
                e.printStackTrace();
            }

            msg.what = 1;
            handler.sendMessage(msg);

        }
    }
}
