package com.vac.musicplayer.fragment.recentmusicplay;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.utils.JsonCacheFileUtils;

public class RecentMusicPlayFra extends Fragment implements OnClickListener,OnPlayMusicStateListener{

	private ListView mRecentListview;
	private MusicListAdapter mMusicAdapter;
	private ArrayList<Music> mRecentMusicList;
	private LinearLayout play_all_linear;
	private MusicServiceBinder mBinder;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recent_music_play_fra, null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		mRecentListview = (ListView) view.findViewById(R.id.recent_music_play_fra_listview);
		
		mMusicAdapter = new MusicListAdapter(getActivity());
		mRecentListview.setAdapter(mMusicAdapter);
		mRecentMusicList = JsonCacheFileUtils.readRecentPlayMusic();
		mMusicAdapter.setData(mRecentMusicList);
		
		play_all_linear = (LinearLayout) view.findViewById(R.id.recent_music_play_fra_playall_linea);
		play_all_linear.setOnClickListener(this);
	}
	public void setBinder(MusicServiceBinder binder){
		this.mBinder = binder;
	}
	@Override
	public void onResume() {
		super.onResume();
		mBinder.registerOnPlayMusicStateListener(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mBinder.unRegisterPlayMusicStateListener(this);
	}
	@Override
	public void onClick(View arg0) {
		mBinder.setCurrentPlayList(mRecentMusicList);
		mBinder.resetAndPlayFirstSong();
	}
	@Override
	public void onMuiscPlayed(Music music) {
		
	}
	@Override
	public void onMusicPaused(Music music) {
		
	}
	@Override
	public void onMusicStoped() {
		
	}
	@Override
	public void onPlayModeChanged(int playMode) {
		
	}
	@Override
	public void onNewSongPlayed(Music music, int position) {
		
	}
	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		
	}
}
