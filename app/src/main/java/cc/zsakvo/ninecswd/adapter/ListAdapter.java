package cc.zsakvo.ninecswd.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.zsakvo.ninecswd.R;
import cc.zsakvo.ninecswd.classes.BookList;
import cc.zsakvo.ninecswd.listener.ItemClickListener;


/**
 * Created by akvo on 2018/2/9.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<BookList> novelList;
    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener listener){
        this.mItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemClickListener mClickListener;
        TextView tv_book_name;
        TextView tv_book_intro;
        TextView tv_book_detail;
        public ViewHolder(View view,ItemClickListener clickListener){
            super(view);
            this.mClickListener = clickListener;
            view.findViewById(R.id.blist).setOnClickListener(this);
            tv_book_name = (TextView)view.findViewById(R.id.blist_name);
            tv_book_intro = (TextView)view.findViewById(R.id.blist_intro);
            tv_book_detail = (TextView)view.findViewById(R.id.blist_detail);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.blist:
                    if(mClickListener != null){
                        mClickListener.onItemClick(v,getAdapterPosition());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public ListAdapter(List<BookList> novelList){
        this.novelList = novelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booklist,parent,false);
        ViewHolder holder = new ViewHolder(view,mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        BookList listDetail = this.novelList.get(position);
        holder.tv_book_intro.setText(listDetail.getList_intro());
        holder.tv_book_name.setText(listDetail.getList_name());
        holder.tv_book_detail.setText(listDetail.getList_detail());
    }

    @Override
    public int getItemCount(){
        return this.novelList.size();
    }
}
