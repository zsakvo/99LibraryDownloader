package cc.zsakvo.ninecswd;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.task.GetArticleContentTask;

public class ArticleActivity extends AppCompatActivity implements Interface.GetArticle,OnRefreshListener{

    Toolbar toolbar;
    private StateView mStateView;
    RefreshLayout refreshLayout;
    TextView tv_article;
    String url;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_article);
        toolbar = (Toolbar)findViewById (R.id.atToolBar);
        toolbar.setTitle ("文库");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        url = getIntent ().getStringExtra ("url");
        String title = getIntent ().getStringExtra ("title");
        tv_article = (TextView)findViewById (R.id.article);
        sv = (ScrollView)findViewById (R.id.at_scroll);
        refreshLayout = (RefreshLayout)findViewById (R.id.atRefreshLayout);
        refreshLayout.setOnRefreshListener (this);
        refreshLayout.setEnableAutoLoadmore (true);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        refreshLayout.autoRefresh ();
//        GetArticleContentTask gct = new GetArticleContentTask (tv_article,toolbar,mStateView,this);
//        gct.execute (url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish ();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void GetResult(Boolean result) {
        if (result){
            sv.setVisibility (View.VISIBLE);
            refreshLayout.finishRefresh (300);
        }else {
            refreshLayout.finishRefresh (false);
            Snackbar.make (toolbar,"数据获取失败，请检查网络连接后重试！",Snackbar.LENGTH_LONG).show ();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        GetArticleContentTask gct = new GetArticleContentTask (tv_article,toolbar,mStateView,ArticleActivity.this);
        gct.execute (url);
    }
}
