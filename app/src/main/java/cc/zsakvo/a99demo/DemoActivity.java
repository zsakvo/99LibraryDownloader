package cc.zsakvo.a99demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;

import cc.zsakvo.a99demo.task.GetArticleContentTask;

public class DemoActivity extends AppCompatActivity {

    Toolbar toolbar;
    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_demo);
        mStateView = StateView.inject(findViewById (R.id.dmToolBar));
        toolbar = (Toolbar)findViewById (R.id.dmToolBar);
        toolbar.setTitle ("文库");
        setSupportActionBar(toolbar);
        String url = getIntent ().getStringExtra ("url");
        String title = getIntent ().getStringExtra ("title");
//        String author = getIntent ().getStringExtra ("author");
//        toolbar.setSubtitle (title);
        TextView tv_article = (TextView)findViewById (R.id.dmarticle);
        GetArticleContentTask gct = new GetArticleContentTask (tv_article,toolbar,mStateView);
        gct.execute (url);
    }
}
