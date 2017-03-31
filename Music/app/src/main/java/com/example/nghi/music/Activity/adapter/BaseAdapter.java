package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filterable;

import java.util.List;

/**
 * Created by Tran Tuat on 1/15/2017.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.RecyclerViewHodler> implements Filterable {
    protected List<T> mListFilter;
    protected List<T> mList;
    protected Context mContext;

    public BaseAdapter(Context context, List<T> list) {
        this.mListFilter = list;
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mListFilter.size();
    }

    public T getItem(int position) {
        return mListFilter.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHodler holder, int position) {
        holder.bindData(getItem(position), position);
    }

    public abstract class RecyclerViewHodler<T> extends RecyclerView.ViewHolder {

        public RecyclerViewHodler(View itemView) {
            super(itemView);
            itemView.setClickable(true);
        }

        public abstract void bindData(T item, int position);
    }
}
