package com.example.nghi.music.Activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.R;

import java.util.List;

/**
 * Created by Nghi on 1/5/17.
 */

public class SongForPlayListAdapter extends BaseAdapter<Music> {
    private CompoundButton.OnCheckedChangeListener listener;

    public SongForPlayListAdapter(List<Music> musicList, Context context) {
        super(context,musicList);

    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener){
        this.listener = listener;
    }

    @Override
    public PlaylistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_list_song_in_playlist,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PlaylistItemViewHolder(view);
    }



    @Override
    public Filter getFilter() {
        return null;
    }

    public class PlaylistItemViewHolder extends RecyclerViewHodler<Music> {
        TextView mTvTitle;
        CheckBox mCheckBox;

        public PlaylistItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }

        @Override
        public void bindData(Music item, int position) {
            mTvTitle.setText(item.getName());
            mCheckBox.setChecked(false);
            mCheckBox.setTag(position);
            mCheckBox.setOnCheckedChangeListener(listener);
        }
    }
}
