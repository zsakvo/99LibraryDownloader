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

import com.github.nukc.stateview.StateView;
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
import cc.zsakvo.a99demo.listener.ItemClickListener;

/**
 * Created by akvo on 2018/2/19.
 */

public class ArticleFragment extends BaseFragment implements ItemClickListener,View.OnClickListener{

    private RecyclerView recyclerView;
    private StateView mStateView;
    private ArticleAdapter adapter;
    private List<ArticleList> listDetails = new ArrayList<>();
    int page = 1;
    int totalPage;
    FloatingActionButton fab;

    public static ArticleFragment newInstance(int sectionNumber){
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt("section_number",sectionNumber);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.afab:
                adapter = new ArticleAdapter(listDetails);
                adapter.setOnItemClickListener(ArticleFragment.this);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int postion) {
        String url = String.valueOf(listDetails.get(postion).getArticleUrl());
//        Snackbar.make(recyclerView,url,Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent (getActivity (), DemoActivity.class);
        intent.putExtra ("title",listDetails.get(postion).getArticleName ());
        intent.putExtra ("url",url);
        startActivity (intent);
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_article,null);
        mStateView = StateView.inject(mRootView, true);
        RefreshLayout refreshLayout = (RefreshLayout)mRootView.findViewById(R.id.arefreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                setDatas();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                setDatas();
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });
        return mRootView;
    }

    @Override
    public void initView() {
        recyclerView = mRootView.findViewById(R.id.nn_article_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(listDetails);
        adapter.setOnItemClickListener(ArticleFragment.this);
        recyclerView.setAdapter(adapter);
        fab = (FloatingActionButton)mRootView.findViewById(R.id.afab);
        fab.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        page = 1;
        setDatas();
    }

    private void setDatas(){
        Message msg = new Message();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (page == 1) {
                            adapter = new ArticleAdapter(listDetails);
                            adapter.setOnItemClickListener(ArticleFragment.this);
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    default:
                        break;
                }
            }
        };
        new getResult(handler, msg).start();
    }


    class getResult extends Thread{
        Handler handler;
        Message message;
        getResult(Handler handler, Message message){
            this.handler = handler;
            this.message = message;
        }

        @Override
        public void run() {
            super.run();
            String url = "http://www.99lib.net/article/index.php?page="+page;
            try {
                Document doc = Jsoup.connect(url).timeout(20000).get();
                if (page==1){
                    listDetails.clear();
                    totalPage = Integer.parseInt(doc.selectFirst("span.total").text().replace("1/",""));
                }
                Element element = doc.selectFirst("ul.list_box");
                Elements ele_li = element.select("li");
                for (Element e:ele_li){
                    Element n = e.select("a").get(1);
                    String author = e.selectFirst("span").text().replace("(","").replace(")","");
                    String title = n.text();
                    String artical_url = "http://www.99lib.net"+n.attr("href");
                    listDetails.add(new ArticleList(title,author,artical_url));
                }
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
