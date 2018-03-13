package cc.zsakvo.a99demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import cc.zsakvo.a99demo.CategoryActivity;
import cc.zsakvo.a99demo.R;
import cc.zsakvo.a99demo.adapter.CategoryAdapter;
import cc.zsakvo.a99demo.listener.ItemClickListener;

/**
 * Created by akvo on 2018/2/18.
 */

public class CategoryFragment  extends BaseFragment implements ItemClickListener {

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    String[] categorys = new String[] {"传记回忆","英文读本","杂文随笔","现代小说","言情小说",
    "历史小说","侦探推理","惊悚悬疑","网络玄幻","寓言童话","青春都市","社会心理","外国小说"
            ,"科幻小说","当代小说","武侠小说","纪实报告","中国历史","世界历史","科普学习"
            ,"诗歌戏曲","宗教哲学","文学理论","日语读物","作品集","国学/古籍","战争军事"
            ,"政治经济","古典文学","官场小说","轻小说"};

    public static CategoryFragment newInstance(int sectionNumber){
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt("section_number",sectionNumber);
        return fragment;
    }

    @Override
    public void onItemClick(View view, int postion) {
        String str = categorys[postion];
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("url",str);
        startActivity(intent);
        Snackbar.make(recyclerView,str,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public View bindLayout(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_category,null);
        return mRootView;
    }

    @Override
    public void initView() {

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
//        System.out.println("aaa");
    }

    @Override
    protected void initData() {
        recyclerView = mRootView.findViewById(R.id.category_list);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new cc.zsakvo.a99demo.adapter.CategoryAdapter(categorys);
        adapter.setOnItemClickListener(CategoryFragment.this);
        recyclerView.setAdapter(adapter);
    }
}
