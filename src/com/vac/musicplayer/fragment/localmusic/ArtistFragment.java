package com.vac.musicplayer.fragment.localmusic;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Artist;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.loader.MusicLoader;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;

public class ArtistFragment extends Fragment implements LoaderCallbacks<List<Music>>, OnItemClickListener,OnPlayMusicStateListener{

	private static final String TAG = ArtistFragment.class.getSimpleName();
	private ListView artist_listView;
	private Artist artist;
	private MusicListAdapter mAdapter=null;
	private static final int PRIVATE_LOCAL_MUSIC=100;
	
	private boolean isHasList =false;
	private MusicServiceBinder mBinder;
	private Bundle currentMusicBundle;
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.artist_activity, null);
		Bundle bundle = getArguments();
		artist = (Artist) bundle.get(Constant.ARTIST_LISTVIEW_ITEM);
		initView(view);
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			getLoaderManager().initLoader(PRIVATE_LOCAL_MUSIC, null, this);
		}else{//SD卡不可用
			Toast.makeText(getActivity(), "SD卡不可用", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initView(View view){
		artist_listView = (ListView) view.findViewById(R.id.artist_activity_listview);
		mAdapter  = new MusicListAdapter(getActivity());
		artist_listView.setAdapter(mAdapter);
		artist_listView.setOnItemClickListener(this);
	}
	
	@Override
	public void onLoadFinished(
			android.support.v4.content.Loader<List<Music>> arg0,
			List<Music> arg1) {
		isHasList =true;
		mAdapter.setData(arg1);
		mCurrentMusicList.clear();
		mCurrentMusicList.addAll(arg1);
		if(currentMusicBundle!=null){
			Log.d(TAG, "在此onServiceConnected设置currentMusicBundle");
			Music mMusic = null;
			
			int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
			Log.v(TAG, "LocalMusicFragment.class====+currentPlayState==="+currentPlayState);
			
			mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
			
			if(mMusic!=null){
				int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
				Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
				if(currentPlayState==PlayState.Playing){
					mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,currentPlayPosition);
				}else{
					mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,currentPlayPosition);
				}
				int offest = artist_listView.getHeight();
				artist_listView.setSelectionFromTop(currentPlayPosition,offest/2);
			}
		}
	}
	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<List<Music>> arg0) {
		
	}
	@Override
	public android.support.v4.content.Loader<List<Music>> onCreateLoader(
			int arg0, Bundle arg1) {
		StringBuffer sb = new StringBuffer();
		sb.append("1=1");
		sb.append(" and " +Media.ARTIST + " = '"
				+ artist.getArtistName() + "'");
		return new MusicLoader(getActivity(),sb.toString(),null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}

	@Override
	public void onStart() {
		super.onStart();
		getActivity().bindService(new Intent(getActivity(),MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			Log.v(TAG, "LocalMusicFragment onServiceConnected");
			mBinder = (MusicServiceBinder) binder;
			
			currentMusicBundle = mBinder.getCurrentPlayMusicInfo();
			
			mBinder.registerOnPlayMusicStateListener(ArtistFragment.this);
			if(currentMusicBundle!=null){
				Log.d(TAG, "在此onServiceConnected设置currentMusicBundle");
				Music mMusic = null;
				
				int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
				Log.v(TAG, "LocalMusicFragment.class====+currentPlayState==="+currentPlayState);
//				if(currentPlayState==PlayState.Stopped){
//					int SharePosition = PreferHelper.readInt(getActivity(), Constant.SHARE_NMAE_MUSIC,
//							Constant.SHARE_NMAE_MUSIC_POSITION, -1);
//					mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,SharePosition);
//					int offest = artist_listView.getHeight();
//					artist_listView.setSelectionFromTop(SharePosition,offest/2);
//				}
				
				
				mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
				
				if(mMusic!=null){
					int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
					Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
					if(currentPlayState==PlayState.Playing){
						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,currentPlayPosition);
					}else{
						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,currentPlayPosition);
					}
					int offest = artist_listView.getHeight();
					artist_listView.setSelectionFromTop(currentPlayPosition,offest/2);
				}
			}
		}
	};
	
	@Override
	public void onStop() {
		super.onStop();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(this);
		}
		if(mServiceConnection!=null){
			Log.v(TAG, "LocalMusicFragment中解绑服务");
			getActivity().unbindService(mServiceConnection);
			isHasList =false;
		}
	}
	
	@Override
	public void onMuiscPlayed(Music music) {
		if(mCurrentMusicList!=null){
			Log.d(TAG, "localMusicFragment中的MusicPlayed---位置是："+	MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
			mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START, 
					MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
		}
	}
	@Override
	public void onMusicPaused(Music music) {
		if(mCurrentMusicList!=null){
			Log.d(TAG, "localMusicFragment中的onMusicPaused---位置是："+	MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
			mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,
					MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
		}
	}
	@Override
	public void onMusicStoped() {
		
	}
	@Override
	public void onPlayModeChanged(int playMode) {
		
	}
	@Override
	public void onNewSongPlayed(Music music, int position) {
		if(mCurrentMusicList!=null)
			mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START, 
					MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
	}
	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Log.d(TAG, "位置是："+arg1+",isHasList="+isHasList);
		
		if(isHasList&&mBinder!=null){
			mBinder.setCurrentPlayList(mCurrentMusicList);
		}
		isHasList =false;
		Intent intent = new Intent(getActivity(),MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY);
		intent.putExtra(Constant.CLICK_MUSIC_LIST, true);
		intent.putExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, mAdapter.getItemId(position));
		intent.putExtra(Constant.MUSIC_LIST_TYPE, artist.getArtistName());
		getActivity().startService(intent);
	}
	
	/**
	 * 播放全部
	 * @param view
	 */
	public void playAllMusic(View view){
		
		
		if(isHasList&&mBinder!=null){
			mBinder.setCurrentPlayList(mCurrentMusicList);
		}
		isHasList =false;
		Intent intent = new Intent(getActivity(),MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY);
		intent.putExtra(Constant.CLICK_MUSIC_LIST, true);
		intent.putExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, mAdapter.getItemId(0));
		intent.putExtra(Constant.MUSIC_LIST_TYPE, artist.getArtistName());
		getActivity().startService(intent);
	}
}
