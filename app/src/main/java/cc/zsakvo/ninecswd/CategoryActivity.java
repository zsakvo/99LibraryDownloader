package cc.zsakvo.ninecswd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cc.zsakvo.ninecswd.classes.BookList;
import cc.zsakvo.ninecswd.listener.Interface;
import cc.zsakvo.ninecswd.listener.ItemClickListener;
import cc.zsakvo.ninecswd.task.GetCategoryListTask;
import cc.zsakvo.ninecswd.adapter.ListAdapter;


public class CategoryActivity extends AppCompatActivity implements ItemClickListener,View.OnClickListener,OnRefreshListener,OnLoadmoreListener,Interface.GetCategoryList{

    private RecyclerView recyclerView;
    Toolbar toolbar;
    FloatingActionButton fab;
    private List<BookList> listDetails = new ArrayList<>();
    ListAdapter adapter;
    int page =1;
    String searchStr;
    Boolean isInit;
    int totalPage = 1;
    String str;
    String baseUrl = "http://www.99lib.net/book/index.php?type=";
    String Url;
    RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        this.str = getIntent().getStringExtra("url");
        toolbar = (Toolbar)findViewById(R.id.c_toolBar);
//        fab = (FloatingActionButton)findViewById(R.id.c_fab);
        toolbar.setTitle(str);
//        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("分类目录");
        if (getSupportActionBar() != null) {
            getSupportActionBar ().setDisplayHomeAsUpEnabled (true);
        }
        recyclerView = (RecyclerView)findViewById(R.id.c_novel_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ListAdapter (listDetails);
        adapter.setOnItemClickListener(CategoryActivity.this);
        recyclerView.setAdapter(adapter);
        refreshLayout = (RefreshLayout)findViewById(R.id.c_refreshLayout);
        refreshLayout.setOnRefreshListener (this);
        refreshLayout.setOnLoadmoreListener (this);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        refreshLayout.autoRefresh ();
    }

    private void initView(){

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void searchBook(){
        toolbar.setTitle(str);
        Url = baseUrl+str+"&page="+page;
        GetCategoryListTask gct = new GetCategoryListTask (totalPage, listDetails, adapter,this);
        gct.execute (Url,page);
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
//            case R.id.c_fab:
//                adapter = new ListAdapter(listDetails);
//                adapter.setOnItemClickListener(CategoryActivity.this);
//                recyclerView.setAdapter(adapter);
//                break;
            default:
                break;
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

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        searchBook();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        listDetails.clear ();
        recyclerView.setAdapter (adapter);
        page = 1;
        searchBook();
    }

    @Override
    public void GetOK(List<BookList> listDetails) {
        this.listDetails.addAll (listDetails);
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
        Snackbar.make (recyclerView,"数据获取失败，请检查网络连接或重试",Snackbar.LENGTH_LONG).show ();
    }
}
