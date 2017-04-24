package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.example.nghi.music.Activity.object.PlayList;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghi on 1/7/17.
 */

public class PlaylistAdapter extends BaseAdapter<PlayList> {
    private ItemFilter mFilter;
    private MenuItem.OnMenuItemClickListener listener;

    public PlaylistAdapter(List<PlayList> playLists, Context context) {
        super(context, playLists);
    }

    public void setOnMenuItemClick(MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_new_list, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PlaylistViewHolder(view);
    }

    public class PlaylistViewHolder extends RecyclerViewHodler<PlayList> implements View.OnCreateContextMenuListener {
        TextView mTvPlayList;
        TextView mTVSongNumber;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnCreateContextMenuListener(this);
            mTvPlayList = (TextView) itemView.findViewById(R.id.tvNameList);
            mTVSongNumber = (TextView) itemView.findViewById(R.id.tvNumberOfSong);

        }

        @Override
        public void bindData(PlayList item, int position) {
            mTvPlayList.setText(item.getNamePlayList());
            mTVSongNumber.setText(item.getNumberOfSong() + " songs");
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem rename = menu.add(0, 0, 0, "Rename");
            MenuItem add = menu.add(0, 1, 1, "Add song");
            MenuItem delete = menu.add(0, 2, 2, "Delete");
            add.setOnMenuItemClickListener(listener);
            rename.setOnMenuItemClickListener(listener);
            delete.setOnMenuItemClickListener(listener);

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
            if (constraint != null && constraint.length() > 0) {
                List<PlayList> tempList = new ArrayList<PlayList>();

                for (PlayList item : mList) {
                    if (item.getNamePlayList().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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
            mListFilter = (List<PlayList>) results.values;
            notifyDataSetChanged();
        }
    }


}
