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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

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

import cc.zsakvo.a99demo.ArticleActivity;
import cc.zsakvo.a99demo.CategoryActivity;
import cc.zsakvo.a99demo.DemoActivity;
import cc.zsakvo.a99demo.R;
import cc.zsakvo.a99demo.adapter.ArticleAdapter;
import cc.zsakvo.a99demo.classes.ArticleList;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.Interface;
import cc.zsakvo.a99demo.listener.ItemClickListener;
import cc.zsakvo.a99demo.task.GetArticleListTask;

/**
 * Created by akvo on 2018/2/19.
 */

public class ArticleFragment extends BaseFragment implements View.OnClickListener,OnRefreshListener,OnLoadmoreListener,Interface.GetArticleList{

    private RecyclerView recyclerView;
    private StateView mStateView;
    private ArticleAdapter adapter;
    private RefreshLayout refreshLayout;
    private List<ArticleList> listDetails = new ArrayList<>();
    int page = 1;
    String baseUrl = "http://www.99lib.net/article/index.php?page=";
    int totalPage;

    public static ArticleFragment newInstance(int sectionNumber){
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt("section_number",sectionNumber);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        String url = String.valueOf(listDetails.get(postion).getArticleUrl());
        Intent intent = new Intent (getActivity (), DemoActivity.class);
        intent.putExtra ("title",listDetails.get(postion).getArticleName ());
        intent.putExtra ("url",url);
        startActivity (intent);
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_article,null);
        mStateView = StateView.inject(mRootView, true);
        return mRootView;
    }

    @Override
    public void initView() {
        refreshLayout = (RefreshLayout)mRootView.findViewById(R.id.arefreshLayout);
        recyclerView = mRootView.findViewById(R.id.nn_article_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(listDetails);
        adapter.setOnItemClickListener(ArticleFragment.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        refreshLayout.setOnRefreshListener (this);
        refreshLayout.setOnLoadmoreListener (this);
        MaterialHeader mMaterialHeader = (MaterialHeader) refreshLayout.getRefreshHeader ();
        refreshLayout.autoRefresh ();
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        new GetArticleListTask (this).execute (baseUrl+page);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        new GetArticleListTask (this).execute (baseUrl+page);
    }

    @Override
    public void GetOK(List<ArticleList> listDetails, int totalPages) {
        this.listDetails.addAll (listDetails);
        this.totalPage = totalPages;
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
