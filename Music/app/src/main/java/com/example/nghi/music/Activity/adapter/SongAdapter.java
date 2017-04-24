package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghi on 1/5/17.
 */

public class SongAdapter extends BaseAdapter<Music> {
    private ItemFilter mFilter;

    public SongAdapter(Context context, List<Music> musics) {
        super(context, musics);
    }

    @Override
    public RecyclerViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_list_song,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new MusicViewHolder(view);
    }

    public int getPositionSongPlay(int position){
        if (mListFilter.size() == mList.size())
            return position;
        return mList.indexOf(mListFilter.get(position));
    }


    public class MusicViewHolder extends RecyclerViewHodler<Music> {
        TextView mTvName;
        TextView mTvSinger;

        public MusicViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvSinger = (TextView) itemView.findViewById(R.id.tvSinger);
        }

        @Override
        public void bindData(Music item, int position) {
            mTvName.setText(item.getName());
            mTvSinger.setText(item.getSinger());
        }
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }

        return mFilter;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                List<Music> tempList = new ArrayList<Music>();

                for (Music item : mList) {
                    if (item.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        tempList.add(item);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mList.size();
                filterResults.values = mList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListFilter = (List<Music>) results.values;
            notifyDataSetChanged();
        }
    }


}


