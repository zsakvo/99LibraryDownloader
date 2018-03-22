package cc.zsakvo.a99demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;

import cc.zsakvo.a99demo.task.GetArticleContentTask;

public class ArticleActivity extends AppCompatActivity {

    Toolbar toolbar;
    private StateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_demo);
        toolbar = (Toolbar)findViewById (R.id.dmToolBar);
        toolbar.setTitle ("文库");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        String url = getIntent ().getStringExtra ("url");
        String title = getIntent ().getStringExtra ("title");
        TextView tv_article = (TextView)findViewById (R.id.dmarticle);
        GetArticleContentTask gct = new GetArticleContentTask (tv_article,toolbar,mStateView);
        gct.execute (url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish ();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
