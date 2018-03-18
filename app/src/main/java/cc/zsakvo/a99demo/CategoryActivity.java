package cc.zsakvo.a99demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.classes.DownloadDetails;
import cc.zsakvo.a99demo.listener.ItemClickListener;
import cc.zsakvo.a99demo.listener.OnDataFinishedListener;
import cc.zsakvo.a99demo.task.GetCategoryListTask;
import cc.zsakvo.a99demo.task.GetSearchListTask;
import km.lmy.searchview.SearchView;


public class CategoryActivity extends AppCompatActivity implements ItemClickListener,View.OnClickListener{

    private RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    private List<BookList> listDetails = new ArrayList<>();
    cc.zsakvo.a99demo.adapter.ListAdapter adapter;
    int page =1;
    String searchStr;
    Boolean isInit;
    int totalPage = 1;
    String str;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        this.str = getIntent().getStringExtra("url");
        toolbar = (Toolbar)findViewById(R.id.c_toolBar);
        fab = (FloatingActionButton)findViewById(R.id.c_fab);
        toolbar.setTitle(str);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("分类目录");
        recyclerView = (RecyclerView)findViewById(R.id.c_novel_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
        adapter.setOnItemClickListener(CategoryActivity.this);
        recyclerView.setAdapter(adapter);
        searchBook();
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.c_refreshLayout);
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
    }

    private void searchBook(){
        toolbar.setTitle(str);
        String url = "http://www.99lib.net/book/index.php?type="+str+"&page="+page;
        GetCategoryListTask gct = new GetCategoryListTask (totalPage, listDetails, adapter);
        gct.setOnDataFinishedListener (new OnDataFinishedListener () {
            @Override
            public void onDataSuccessfully(Object data) {
                totalPage = (int)data;
            }

            @Override
            public void onDataSuccessfully(DownloadDetails data) {

            }

            @Override
            public void onDataFailed() {

            }
            @Override
            public void onDownloadFinishedNum(int num){

            }
        });
        gct.execute (url,page);
        }

    @Override
    public void onItemClick(View view,final int position){
        String url = String.valueOf(listDetails.get(position).getBook_url());
        Intent intent = new Intent(CategoryActivity.this, BookDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.c_fab:
                adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
                adapter.setOnItemClickListener(CategoryActivity.this);
                recyclerView.setAdapter(adapter);
                break;
            default:
                break;
        }
    }
}
