package cc.zsakvo.a99demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import cc.zsakvo.a99demo.R;
import cc.zsakvo.a99demo.classes.ArticleList;
import cc.zsakvo.a99demo.classes.BookList;
import cc.zsakvo.a99demo.listener.ItemClickListener;

/**
 * Created by akvo on 2018/2/19.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    private List<ArticleList> articleList;
    private ItemClickListener mItemClickListener;

    public ArticleAdapter(List<ArticleList> articleList){
        this.articleList = articleList;
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articlelist,parent,false);
        ViewHolder holder = new ViewHolder(view,mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticleList listDetail = this.articleList.get(position);
        holder.tv_article_name.setText(listDetail.getArticleName());
        holder.tv_article_intro.setText(listDetail.getArticleAuthor());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener mClickListener;
        TextView tv_article_name;
        TextView tv_article_intro;

        public ViewHolder(View view,ItemClickListener clickListener){
            super(view);
            this.mClickListener = clickListener;
            view.findViewById(R.id.alist).setOnClickListener(this);
            tv_article_name = view.findViewById(R.id.alist_name);
            tv_article_intro = view.findViewById(R.id.alist_intro);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.alist:
                    if(mClickListener != null){
                        mClickListener.onItemClick(v,getAdapterPosition());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
