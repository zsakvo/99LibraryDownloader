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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.listener.ItemClickListener;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.task.GetSearchListTask;
import km.lmy.searchview.SearchView;


public class SearchActivity extends AppCompatActivity implements ItemClickListener,View.OnClickListener,Interface.GetSearch,OnRefreshListener,OnLoadmoreListener{

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
    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar)findViewById(R.id.s_toolBar);
        searchView = (SearchView)findViewById(R.id.s_searchView);
//        fab = (FloatingActionButton)findViewById(R.id.s_fab);
//        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("搜索书籍");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
//        mStateView = StateView.inject(findViewById (R.id.coord));
        recyclerView = (RecyclerView)findViewById(R.id.s_novel_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.removeAllViewsInLayout();
        adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
        adapter.setOnItemClickListener(SearchActivity.this);
        recyclerView.setAdapter(adapter);
        refreshLayout = (RefreshLayout)findViewById(R.id.s_refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        searchView.defaultState (SearchView.OPEN);
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
//        searchView.open ();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.s_search:
                searchView.autoOpenOrClose();
                break;
            case android.R.id.home:
                finish ();
                break;
        }
        return true;
    }

    private void initSearchView(){
        searchView.setHintText ("请输入关键字，如 人间椅子");
//        searchView.setSearchEditText ("我的");
        searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
            @Override
            public void onClick(String historyStr, int position) {
                searchView.close();
                searchStr = historyStr;
                page = 1;
                isInit = true;
                onRefresh (refreshLayout);
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
                onRefresh (refreshLayout);
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
        GetSearchListTask gst = new GetSearchListTask (this);
        gst.execute (url,page);
        }

    @Override
    public void onBackPressed(){
        if (searchView.isOpen()){
            searchView.close();
            if (listDetails.size ()==0){
                super.onBackPressed ();
            }
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
//            case R.id.s_fab:
//                adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
//                adapter.setOnItemClickListener(SearchActivity.this);
//                recyclerView.setAdapter(adapter);
//                break;
            default:
                break;
        }
    }

    @Override
    public void GetOK(List<BookList> listDetails,int totalPages) {
        this.listDetails.addAll (listDetails);
        if (totalPages!=0) {
            this.totalPage = totalPages;
        }
        adapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing ()){
            refreshLayout.finishRefresh (500);
        }else {
            refreshLayout.finishLoadmore (500);
        }
    }

    @Override
    public void GetFailed() {
        refreshLayout.finishLoadmoreWithNoMoreData ();
        if (refreshLayout.isRefreshing ()){
            refreshLayout.finishRefresh (false);
        }else {
            refreshLayout.finishLoadmore (false);
        }
        Snackbar.make (recyclerView,"数据获取失败，请检查网络连接或换个关键词~",Snackbar.LENGTH_LONG).show ();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        Log.e ("onLoadmore: ",page+"ppp"+totalPage );
        if (page>=totalPage){
            refreshLayout.finishLoadmoreWithNoMoreData ();
            Snackbar.make (recyclerView,"已经是最后一页了！",Snackbar.LENGTH_LONG).show ();
        }else {
            page++;
            searchBook();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        listDetails.clear ();
        recyclerView.setAdapter (adapter);
        refreshlayout.resetNoMoreData ();
        searchBook();
    }


}
