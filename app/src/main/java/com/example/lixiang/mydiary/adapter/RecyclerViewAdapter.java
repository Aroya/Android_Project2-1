package com.example.lixiang.mydiary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.model.Diary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerArrayAdapter<Diary, RecyclerViewAdapter.ViewHolder>  implements Filterable ,View.OnClickListener{
    private List<Diary> mItems;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private ItemOnLongClickListener itemListener;

    public RecyclerViewAdapter(List<Diary> list) {
        super(list);
        mItems = new ArrayList<>();
        mItems.addAll(list);
    }

    public void setList(List<Diary> list){
        mItems.clear();
        mItems.addAll(list);
    }

    public void setItemListener(ItemOnLongClickListener listener) {
        itemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Diary item = mItems.get(position);
        if (item == null){
            return;
        }
        holder.mDate.setText(item.getDate());
        holder.mWeek.setText(item.getWeek());
        holder.mContent.setText(item.getContent());
        holder.itemView.setTag(item);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemListener.itemLongClick(getItem(position));
                return false;
            }
        });
        if (item.getTop() == 1) {
            holder.mCircle.setImageResource(R.drawable.circle_orange);
        } else {
            holder.mCircle.setImageResource(R.drawable.circle);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public Filter getFilter() {
        return new DiaryFilter();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null){
            return;
        }
        mOnItemClickListener.onItemClick(v,(Diary)v.getTag());
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDate;
        public TextView mWeek;
        public TextView mContent;
        public ImageView mCircle;

        public ViewHolder(View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.tv_date);
            mWeek = (TextView) itemView.findViewById(R.id.tv_week);
            mContent = (TextView) itemView.findViewById(R.id.tv_content);
            mCircle = (ImageView) itemView.findViewById(R.id.main_iv_circle);
        }
    }

    public class DiaryFilter extends Filter {
        private List<Diary> mFilterItems;

        private DiaryFilter(){
            super();
            mFilterItems = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence){
            mFilterItems.clear();
            FilterResults filterResults = new FilterResults();
            if (charSequence.length() == 0){
                mFilterItems.addAll(mItems);
            }
            else {
                for (Iterator<Diary> iter = mItems.iterator(); iter.hasNext();){
                    Diary diary = iter.next();
                    if (diary.getContent().contains(charSequence)){
                        mFilterItems.add(diary);
                    }
                }
            }
            filterResults.values = mFilterItems;
            filterResults.count = mFilterItems.size();
            return  filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mItems.clear();
            mItems.addAll((ArrayList<Diary>) results.values);
            notifyDataSetChanged();
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Diary diary);
    }

    public static interface ItemOnLongClickListener {
        void itemLongClick(Diary diary);
    }
}
