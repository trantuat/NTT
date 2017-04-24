package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.nghi.music.Activity.object.Album;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghi on 1/5/17.
 */

public class AlbumAdapter extends BaseAdapter<Album> {

    private ItemFilter mFilter;

    public AlbumAdapter(List<Album> albumList, Context context) {
        super(context,albumList);
    }

    @Override
    public RecyclerViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_list_album,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new AlbumsViewHolder(view);
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }

        return mFilter;
    }



    public class AlbumsViewHolder extends RecyclerViewHodler<Album> {
        TextView mTvAlbum;
        TextView mTvSinger;

        public AlbumsViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTvAlbum = (TextView) itemView.findViewById(R.id.tvAlbum);
            mTvSinger = (TextView) itemView.findViewById(R.id.tvSinger);
        }

        @Override
        public void bindData(Album item, int position) {
            mTvAlbum.setText(item.getAlbumName());
            mTvSinger.setText(item.getSinger());
        }
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                List<Album> tempList = new ArrayList<Album>();

                for (Album item : mList) {
                    if (item.getAlbumName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            mListFilter = (List<Album>) results.values;
            notifyDataSetChanged();
        }
    }

}
