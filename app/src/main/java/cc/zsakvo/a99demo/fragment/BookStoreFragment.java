package cc.zsakvo.a99demo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.github.nukc.stateview.StateView;
import com.scwang.smartrefresh.header.MaterialHeader;
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

import cc.zsakvo.a99demo.BookDetailActivity;
import cc.zsakvo.a99demo.R;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.listener.ItemClickListener;
import cc.zsakvo.a99demo.task.GetArticleContentTask;
import cc.zsakvo.a99demo.task.GetBookListTask;


/**
 * Created by akvo on 2018/1/7.
 */

public class BookStoreFragment extends BaseFragment implements View.OnClickListener,OnRefreshListener,OnLoadmoreListener,Interface.GetBookListFinish{
    private RecyclerView recyclerView;
    private StateView mStateView;
    private List<BookList> listDetails = new ArrayList<>();
    cc.zsakvo.a99demo.adapter.ListAdapter adapter;
    FloatingActionButton fab;
    int page = 1;
    private RefreshLayout refreshLayout;
    private String baseURL = "http://www.99lib.net/book/index.php?page=";

    public BookStoreFragment(){

    }

    public static BookStoreFragment newInstance(int sectionNumber){
        BookStoreFragment fragment = new BookStoreFragment();
        Bundle args = new Bundle();
        args.putInt("section_number",sectionNumber);
        return fragment;
    }

    @SuppressLint("InflateParams")
    @Override
    public View bindLayout(LayoutInflater layoutInflater){
        mRootView = layoutInflater.inflate(R.layout.fragment_99lib,null);
        mStateView = StateView.inject(mRootView, true);
//        recyclerView = mRootView.findViewById(R.id.nn_novel_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
//        adapter.setOnItemClickListener(BookStoreFragment.this);
//        recyclerView.setAdapter(adapter);
        return mRootView;
    }

    @Override
    public void initView() {
        recyclerView = mRootView.findViewById(R.id.nn_novel_list);
        fab = (FloatingActionButton)mRootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new cc.zsakvo.a99demo.adapter.ListAdapter(listDetails);
        adapter.setOnItemClickListener(BookStoreFragment.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData(){
        refreshLayout = (RefreshLayout) mRootView.findViewById (R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener (this);
        refreshLayout.setEnableAutoLoadmore (true);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        refreshLayout.autoRefresh ();
    }

    @Override
    public void onItemClick(View view,final int position){
        String url = String.valueOf(listDetails.get(position).getBook_url());
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                recyclerView.setAdapter (adapter);
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        listDetails.clear ();
        new GetBookListTask (BookStoreFragment.this).execute (baseURL+page);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        new GetBookListTask (BookStoreFragment.this).execute (baseURL+page);
    }

    @Override
    public void bookList(List<BookList> listDetails) {
        this.listDetails.addAll (listDetails);
        adapter.notifyDataSetChanged ();
        if (refreshLayout.isRefreshing ()){
            refreshLayout.finishRefresh (2000);
        }else {
            refreshLayout.finishLoadmore (2000);
        }
    }

    @Override
    public void booksGetFailed(){
        refreshLayout.finishLoadmoreWithNoMoreData ();
        if (refreshLayout.isRefreshing ()){
            refreshLayout.finishRefresh (2000);
        }else {
            refreshLayout.finishLoadmore (2000);
        }
        Snackbar.make (fab,"数据获取失败，请检查网络连接或重试",Snackbar.LENGTH_LONG).show ();
    }
}
