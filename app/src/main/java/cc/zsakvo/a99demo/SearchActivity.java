package cc.zsakvo.a99demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.ItemClickListener;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.task.GetSearchListTask;
import km.lmy.searchview.SearchView;


public class SearchActivity extends AppCompatActivity implements ItemClickListener,View.OnClickListener{

    private RecyclerView recyclerView;
    private StateView mStateView;
    SearchView searchView;
    Toolbar toolbar;
    FloatingActionButton fab;
    private List<BookList> listDetails = new ArrayList<>();
    cc.zsakvo.a99demo.adapter.ListAdapter adapter;
    int page;
    String searchStr;
    Boolean isInit;
    int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar)findViewById(R.id.s_toolBar);
        searchView = (SearchView)findViewById(R.id.s_searchView);
        fab = (FloatingActionButton)findViewById(R.id.s_fab);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("搜索书籍");
        mStateView = StateView.inject(findViewById (R.id.coord));
        recyclerView = (RecyclerView)findViewById(R.id.s_novel_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.removeAllViewsInLayout();
        adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
        adapter.setOnItemClickListener(SearchActivity.this);
        recyclerView.setAdapter(adapter);
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.s_refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                searchBook();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                System.out.println (totalPage);
                if (page<totalPage) {
                    page++;
                    searchBook();
                    refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
                }else {
                    Snackbar.make(fab,"已经是最后一页了啦!",Snackbar.LENGTH_LONG).show();
                    refreshlayout.finishLoadmore(false/*,false*/);//传入false表示加载失败
                }
            }
        });
    }

    private void initView(){

    }

    @Override
    public void onResume(){
        super.onResume();
        initSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        searchView.autoOpenOrClose();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.s_search:
                searchView.autoOpenOrClose();
                break;
        }
        return true;
    }

    private void initSearchView(){
        List<String> historyList = new ArrayList<>();
        historyList.add("白夜追凶");
        historyList.add("人间椅子");
        historyList.add("两分铜币");
        historyList.add("芭提雅血咒");
        //设置全新的历史记录列表
        searchView.setNewHistoryList(historyList);
        searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
            @Override
            public void onClick(String historyStr, int position) {
                searchView.close();
                searchStr = historyStr;
                page = 1;
                isInit = true;
                searchBook();
            }
        });

        //设置软键盘搜索按钮点击事件
        searchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
            @Override
            public void onSearchAction(String searchText) {
                searchView.close();
                searchView.addOneHistory(searchText);
                page = 1;
                isInit = true;
                searchStr = searchText;
                searchBook();
            }
        });
    }

    private Boolean canSearch(String value) {
        Boolean hasEng=false;
        Boolean hasChi=false;
        Boolean result=false;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
                hasChi = true;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
                hasEng = true;
            }
        }
        if (hasChi&&hasEng){
            result = true;
        }else if (valueLength>=4){
            result = true;
        }
        return result;
    }

    private void searchBook(){
        if (!canSearch(searchStr)){
            Snackbar.make(fab,"搜索关键字不得少于2个中文或4个英文！",Snackbar.LENGTH_LONG).show();
            return;
        }
        toolbar.setTitle("搜索："+searchStr);
        String url = "http://www.99lib.net/book/search.php?keyword="+searchStr+"&page="+page;
        GetSearchListTask gst = new GetSearchListTask (totalPage, listDetails, adapter,mStateView);
        gst.setOnDataFinishedListener (new OnDataFinishedListener () {
            @Override
            public void onDataSuccessfully(Object data) {
                totalPage = (int)data;
            }

            @Override
            public void onDataFailed() {

            }
        });
        gst.execute (url,page);
        }

    @Override
    public void onBackPressed(){
        if (searchView.isOpen()){
            searchView.close();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemClick(View view,final int position){
        String url = String.valueOf(listDetails.get(position).getBook_url());
        Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.s_fab:
                adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
                adapter.setOnItemClickListener(SearchActivity.this);
                recyclerView.setAdapter(adapter);
                break;
            default:
                break;
        }
    }
}