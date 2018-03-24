package cc.zsakvo.ninecswd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.github.nukc.stateview.StateView;

import cc.zsakvo.ninecswd.adapter.MyViewpagerAdapter;
import cc.zsakvo.ninecswd.fragment.ArticleFragment;
import cc.zsakvo.ninecswd.fragment.BaseFragment;
import cc.zsakvo.ninecswd.fragment.BookStoreFragment;
import cc.zsakvo.ninecswd.fragment.CategoryFragment;
import km.lmy.searchview.SearchView;


public class MainActivity extends AppCompatActivity{
    TabLayout tabLayout;
    ViewPager viewPager;
    String[] mTitle;
    BaseFragment[] mBaseFragment;
    Toolbar toolbar;
    SearchView searchView;
    MyViewpagerAdapter mMyViewpagerAdapter;
    private long firstClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        searchView=(SearchView)findViewById(R.id.searchView);
        setSupportActionBar(toolbar);
        initView();
        initDatas();
        initListener();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.m_search:
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                break;
//            case R.id.m_setting:
//                Snackbar.make(toolbar,"设置",Snackbar.LENGTH_LONG).show();
//                break;
//            case R.id.m_about:
//                Snackbar.make(toolbar,"关于程序",Snackbar.LENGTH_LONG).show();
//                StateView mStateView = StateView.inject(this);
//                mStateView.setRetryResource (R.layout.layout_retry);
//                break;
        }
        return true;
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.vp_content);
    }

    private void initDatas() {

        //为标题赋值
        mTitle = new String[]{
                "书库",
                "分类",
                "文库"
        };
        mBaseFragment = new BaseFragment[]{
                new BookStoreFragment(),
                new CategoryFragment (),
                new ArticleFragment()
        };

        setTabAndViewPager();
    }

    private void setTabAndViewPager() {
        mMyViewpagerAdapter = new MyViewpagerAdapter(getSupportFragmentManager(),mTitle);
        viewPager.setAdapter(mMyViewpagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager,true);
    }

    private void initListener() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-firstClick>2000){
                firstClick=System.currentTimeMillis();
                Snackbar.make(toolbar,"再按一次退出",Snackbar.LENGTH_LONG).show();
            }else{
                System.exit(0);
            }
            return true;
        }
        return false;
    }
}