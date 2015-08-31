package com.vac.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vac.musicplayer.MainActivity;
import com.vac.musicplayer.PlayMusic;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.loader.MusicLoader;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.PreferHelper;

public class LocalMusicfra extends Fragment implements android.widget.AdapterView.OnItemClickListener,OnPlayMusicStateListener {

	private static final int PRIVATE_LOCAL_MUSIC=0;
	private static final String TAG = LocalMusicfra.class.getSimpleName();
	private ListView listView;
	private MusicListAdapter mAdapter=null;
	
	private onMusicTotalCountListener musicTotalListener=null;
	
	private Activity mMainActivity;
	
	private MusicServiceBinder mBinder=null;
	
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	
	private boolean isHasList =false;
	private Bundle currentMusicBundle = null;
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			Log.v(TAG, "LocalMusicFragment onServiceConnected");
			mBinder = (MusicServiceBinder) binder;
			
			currentMusicBundle = mBinder.getCurrentPlayMusicInfo();
			
			mBinder.registerOnPlayMusicStateListener(LocalMusicfra.this);
			if(currentMusicBundle!=null){
				Log.d(TAG, "在此onServiceConnected设置currentMusicBundle");
				Music mMusic = null;
				
				int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
				Log.v(TAG, "LocalMusicFragment.class====+currentPlayState==="+currentPlayState);
				if(currentPlayState==PlayState.Stopped){
					int SharePosition = PreferHelper.readInt(getActivity(), Constant.SHARE_NMAE_MUSIC,
							Constant.SHARE_NMAE_MUSIC_POSITION, -1);
					mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,SharePosition);
				}
				
				
				mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
				
				if(mMusic!=null){
					int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
					Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
					if(currentPlayState==PlayState.Playing){
						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,currentPlayPosition);
					}else{
						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,currentPlayPosition);
					}
				}
			}
		}
	};
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof MainActivity){
			musicTotalListener = (onMusicTotalCountListener) activity;
			mMainActivity = activity;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.locamusic_fra, null);
		
		initView(view);
		
		return view;
	}
	private void initView(View v){
		listView = (ListView) v.findViewById(R.id.localmusic_lv);
		mAdapter = new MusicListAdapter(getActivity());
		listView.setAdapter(mAdapter);
		
		listView.setOnItemClickListener(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			getLoaderManager().initLoader(PRIVATE_LOCAL_MUSIC, null, new LoaderCallbacks<List<Music>>() {

				@Override
				public Loader<List<Music>> onCreateLoader(int id, Bundle bundle) {
					Log.i(TAG, "onCreateLoader");
					return new MusicLoader(getActivity(),null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				}

				@Override
				public void onLoadFinished(Loader<List<Music>> loader,
						List<Music> data) {
					Log.i(TAG, "onLoadFinished---------------"+Thread.currentThread().getName());
					mCurrentMusicList.clear();
					if(data!=null&&data.size()>0){
						mCurrentMusicList.addAll(data);
						mAdapter.setData(data);
						musicTotalListener.musicTotalCount(data.size());//设置音乐的总数，接口回调给宿主MainActivity.class
//						Log.v(TAG, "LocalMusicFragment中绑定服务");
					}
					if(data!=null&&mBinder!=null&&currentMusicBundle!=null){
						Log.d(TAG, "在此onLoadFinished设置currentMusicBundle");
						Music mMusic = null;
						
						int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
						Log.v(TAG, "LocalMusicFragment.class====+currentPlayState==="+currentPlayState);
						if(currentPlayState==PlayState.Stopped){
							int SharePosition = PreferHelper.readInt(getActivity(), Constant.SHARE_NMAE_MUSIC,
									Constant.SHARE_NMAE_MUSIC_POSITION, -1);
							mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,SharePosition);
						}
						
						
						mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
						
						if(mMusic!=null){
							int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
							Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
							if(currentPlayState==PlayState.Playing){
								mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,currentPlayPosition);
							}else{
								mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,currentPlayPosition);
							}
						}
					}
					
				}

				@Override
				public void onLoaderReset(Loader<List<Music>> arg0) {
					Log.i(TAG, "onLoaderReset");
					mAdapter.setData(null);
				}

				
			});
		}else{//SD卡不可用
			Toast.makeText(getActivity(), "SD卡不可用", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	/**
	 * 这是当AsyncTaskLoader扫描完本地的音乐文件后的回调接口，返回音乐的总条数
	 * @author vac
	 *
	 */
	public interface onMusicTotalCountListener{
		public abstract void musicTotalCount(int total);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(mServiceConnection!=null)
			Log.v(TAG, "在onStart中的绑定服务======================");
			getActivity().bindService(new Intent(getActivity(),MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,position);
		if(!isHasList){
			Log.v(TAG, "mBinder.setCurrentPlayList(data)");
			mBinder.setCurrentPlayList(mCurrentMusicList);
			isHasList=true;
		}
		Intent intent = new Intent(getActivity(),MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY);
		intent.putExtra(Constant.CLICK_MUSIC_LIST, true);
		intent.putExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, mAdapter.getItemId(position));
		getActivity().startService(intent);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(this);
		}
		if(mServiceConnection!=null){
			Log.v(TAG, "LocalMusicFragment中解绑服务");
			mMainActivity.unbindService(mServiceConnection);
		}
	}
	

	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		
	}
	
	@Override
	public void onPlayModeChanged(int playMode) {
		
	}
	
	@Override
	public void onNewSongPlayed(Music music,int position) {
		if(mCurrentMusicList!=null)
		mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START, 
				MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
	}
	
	@Override
	public void onMusicStoped() {
		
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
	public void onMuiscPlayed(Music music) {
		if(mCurrentMusicList!=null){
			Log.d(TAG, "localMusicFragment中的MusicPlayed---位置是："+	MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
			mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START, 
					MusicService.findPositionByMusicId(mCurrentMusicList,music.getId()));
		}
	}
}
