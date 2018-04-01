package cc.zsakvo.ninecswd.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cc.zsakvo.ninecswd.BookDetailActivity;
import cc.zsakvo.ninecswd.R;
import cc.zsakvo.ninecswd.application.MyApplication;
import cc.zsakvo.ninecswd.listener.ItemClickListener;

/**
 * Created by akvo on 2018/3/31.
 */

public class CoverListAdapter extends RecyclerView.Adapter<CoverListAdapter.ViewHolder> {

    private ItemClickListener mItemClickListener;
    private List<String> cover_url;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coverlist,parent,false);
        ViewHolder holder = new ViewHolder(view,mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String category = this.cover_url.get (position);
        try {
            Glide.with (MyApplication.getContext ()).load (category).into (holder.iv);
        }catch (Exception e){
            Glide.with (MyApplication.getContext ()).load (category).into (holder.iv);
        }
    }


    @Override
    public int getItemCount() {
        return this.cover_url.size ();
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public CoverListAdapter(List<String> cover_url){
        this.cover_url = cover_url;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener mClickListener;
        DisplayMetrics dm;
        ImageView iv;

        public ViewHolder(View view,ItemClickListener clickListener){
            super(view);
            this.mClickListener = clickListener;
            view.findViewById(R.id.cover_list).setOnClickListener(this);
            dm = view.getContext().getResources().getDisplayMetrics();
            int a = dm.widthPixels;
            int b = dm.heightPixels;
            iv = (ImageView) view.findViewById(R.id.cover_t);
            ViewGroup.LayoutParams para = iv.getLayoutParams();
            float width = (a-96)/3;
            para.width = (int)width;
            para.height = (int)(width*1.435F);
            iv.setLayoutParams (para);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cover_list:
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
