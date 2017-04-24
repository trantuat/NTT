package com.example.nghi.music.Activity.fragments;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nghi.music.Activity.activity.SongActivity;
import com.example.nghi.music.Activity.adapter.PlaylistAdapter;
import com.example.nghi.music.Activity.listener.RecycleTouchListener;
import com.example.nghi.music.Activity.adapter.SongForPlayListAdapter;
import com.example.nghi.music.Activity.database.MyDatabase;
import com.example.nghi.music.Activity.object.Music;
import com.example.nghi.music.Activity.object.PlayList;
import com.example.nghi.music.Activity.service.Constant;
import com.example.nghi.music.Activity.utils.DatabaseUtil;
import com.example.nghi.music.Activity.utils.Utils;
import com.example.nghi.music.R;

import java.util.ArrayList;
import java.util.List;


public class PlaylistFragment extends BaseFragment implements MenuItem.OnMenuItemClickListener{
    private LinearLayout mLayout;
    private Button mBtnOk, mBtnCancel;
    private EditText mEditName;
    private TextView mTvTittleAlert;
    private MyDatabase db;
    private RecyclerView mRecyclerView;
    private List<PlayList> mPlayLists = new ArrayList<>();
    private List<Music> mMusics = new ArrayList<>();
    private PlaylistAdapter mAdapter;
    private List<Music> mListChoise;
    private PlayList mPlaylistSelected;

    public void search(String string){
        mAdapter.getFilter().filter(string);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_list, container, false);
        mDbUtil = new DatabaseUtil();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);
        mRecyclerView.setHasFixedSize(true);
        db = new MyDatabase(getContext());
        loadData();
        mAdapter = new PlaylistAdapter(mPlayLists, view.getContext());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecycleTouchListener(getContext(), mRecyclerView, new RecycleTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), SongActivity.class);
                intent.putExtra(Constant.KEY, mAdapter.getItem(position));
                intent.putExtra(Constant.TAG, Constant.TAG_PLAYLIST);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                mPlaylistSelected = mPlayLists.get(position);
            }
        }));
        mLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        registerForContextMenu(mRecyclerView);
        mAdapter.setOnMenuItemClick(this);

        return view;
    }

    private void loadData() {
        if (mPlayLists != null) {
            mPlayLists.clear();
        }
        mPlayLists.addAll(db.getData());
        if (mAdapter != null) mAdapter.notifyDataSetChanged();

    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        view.setAlpha(0.8f);
        mEditName = (EditText) view.findViewById(R.id.edtNamePlaylist);
        mTvTittleAlert = (TextView) view.findViewById(R.id.tvTitle);
        mBtnOk = (Button) view.findViewById(R.id.btnOK);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
        mTvTittleAlert.setText(getString(R.string.title_dialog_create));
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        dialog.setCancelable(false);
        Utils.showKeyboard(mEditName, getContext());
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playlist = mEditName.getText().toString();
                if (isExistNamePlaylist(playlist)) {
                    Utils.errorAlert(getContext(), getString(R.string.title_errors), getString(R.string.message_name_playlist_is_existed));
                } else if (playlist.trim().equals("")) {
                    Utils.errorAlert(getContext(),  getString(R.string.title_errors),  getString(R.string.message__name_playlist_is_null));
                } else {
                    Utils.hideKeyboard(mEditName, getContext());
                    dialog.dismiss();
                    int id;
                    id = db.addPlayList(new PlayList(mEditName.getText().toString()));
                    if (id > 0) createChooseSong(id);
                }

            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(mEditName, getContext());
                dialog.dismiss();
            }
        });
    }

    private boolean isExistNamePlaylist(String playlist) {
        boolean check = false;
        if (db.isExistedInTable(playlist, MyDatabase.TABLE_PLAYLIST)) {
            check = true;
        }
        return check;

    }

    private void createChooseSong(final int id) {
        Button mBtnOK, mBtnCancel;
        TextView mTvTittleAlert;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_song, null);
        view.setAlpha(0.8f);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rc_song);
        recyclerView.setHasFixedSize(true);

        mListChoise = mDbUtil.getMusic(getContext());
        mMusics.clear();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        SongForPlayListAdapter adapter = new SongForPlayListAdapter(mListChoise, getContext());
        recyclerView.setAdapter(adapter);


        mTvTittleAlert = (TextView) view.findViewById(R.id.tv_title);
        mBtnOK = (Button) view.findViewById(R.id.btn_OK);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        adapter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int i = (int) compoundButton.getTag();
                if (b) {
                    if (mMusics != null && !mMusics.contains(mListChoise.get(i))) {
                        mMusics.add(mListChoise.get(i));
                    }
                } else {
                    if (mMusics != null && mMusics.contains(mListChoise.get(i))) {
                        mMusics.remove(mListChoise.get(i));
                    }
                }
            }
        });


        mTvTittleAlert.setText(getString(R.string.titile_dialog_chose_song));
        builder.setView(view);
        final Dialog dialog = builder.show();
        dialog.setCancelable(false);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMusics.size() == 0) return;
                db.addListMusic(mMusics, id);
                db.insertNumberSong(mMusics.size(), id);
                loadData();
                dialog.dismiss();
            }

        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                dialog.dismiss();
            }
        });
    }


    private void renamePlaylist() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog,null );
        view.setAlpha(0.8f);
        mEditName = (EditText) view.findViewById(R.id.edtNamePlaylist);
        mTvTittleAlert = (TextView) view.findViewById(R.id.tvTitle);
        mBtnOk = (Button) view.findViewById(R.id.btnOK);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);

        mEditName.setText(mPlaylistSelected.getNamePlayList());
        mEditName.setSelectAllOnFocus(true);
        Utils.showKeyboard(mEditName, getContext());

        mTvTittleAlert.setText(getString(R.string.title_dialog_create));
        builder.setView(view);
        final AlertDialog dialog = builder.show();
        dialog.setCancelable(false);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPl = mEditName.getText().toString();
                if (isExistNamePlaylist(newPl)) {
                    Utils.errorAlert(getContext(), getString(R.string.title_errors), getString(R.string.message_name_playlist_is_existed));
                } else if (newPl.trim().equals("")) {
                    Utils.errorAlert(getContext(),  getString(R.string.title_errors),  getString(R.string.message__name_playlist_is_null));
                } else{
                    Utils.hideKeyboard(mEditName, getContext());
                    db.renamePlaylist(mPlaylistSelected.getNamePlayList(), newPl);
                    loadData();
                    dialog.dismiss();

                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(mEditName, getContext());
                dialog.dismiss();
            }
        });
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                renamePlaylist();
                break;
            case 1:
                addSong();
                break;
            case 2:
                db.deletePlayList(mPlaylistSelected);
                loadData();
                break;
        }
        return false;
    }

    private void playAll() {

    }
    private void addSong() {

    }
}

