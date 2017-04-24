package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.nghi.music.Activity.object.Artist;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghi on 1/5/17.
 */

public class ArtistAdapter extends BaseAdapter<Artist> {
    private ItemFilter mFilter;

    public ArtistAdapter(List<Artist> artistList, Context context) {
        super(context,artistList);
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_list_artist,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ArtistViewHolder(view);
    }



    public class ArtistViewHolder extends RecyclerViewHodler<Artist> {
        TextView mTvNumberSong;
        TextView mTvSinger;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTvNumberSong = (TextView) itemView.findViewById(R.id.tvNumberOfSong);
            mTvSinger = (TextView) itemView.findViewById(R.id.tvSinger);
        }

        @Override
        public void bindData(Artist item, int position) {
            mTvSinger.setText(item.getNameSinger());
            mTvNumberSong.setText(item.getNumberOfSong()+" songs");
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
                List<Artist> tempList = new ArrayList<Artist>();

                for (Artist item : mList) {
                    if (item.getNameSinger().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            mListFilter = (List<Artist>) results.values;
            notifyDataSetChanged();
        }
    }

}
