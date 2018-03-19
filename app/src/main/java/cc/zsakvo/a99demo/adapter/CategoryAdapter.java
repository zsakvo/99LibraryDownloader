package cc.zsakvo.a99demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

import cc.zsakvo.a99demo.R;
import cc.zsakvo.a99demo.listener.ItemClickListener;

/**
 * Created by akvo on 2018/2/18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private String[] categorys;
    private ItemClickListener mItemClickListener;

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categorylist,parent,false);
        CategoryAdapter.ViewHolder holder = new CategoryAdapter.ViewHolder(view,mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        String category = this.categorys[position];
        holder.tv.setText(category);
    }

    @Override
    public int getItemCount() {
        return this.categorys.length;
    }

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public CategoryAdapter(String[] categorys){
        this.categorys = categorys;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener mClickListener;
        DisplayMetrics dm;
        TextView tv;

        public ViewHolder(View view,ItemClickListener clickListener){
            super(view);
            this.mClickListener = clickListener;
            view.findViewById(R.id.clist).setOnClickListener(this);
            dm = view.getContext().getResources().getDisplayMetrics();
            int a = dm.widthPixels;
            int b = dm.heightPixels;
            tv = (TextView) view.findViewById(R.id.category_t);
            tv.setHeight(b/11);
            tv.setWidth(a/3);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.clist:
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
