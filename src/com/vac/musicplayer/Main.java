package com.vac.musicplayer;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.fragment.TabMainFra;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.loader.MusicLoader;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.PreferHelper;

public class Main extends FragmentActivity implements OnPlayMusicStateListener{
	private final static String TAG = Main.class.getSimpleName();
	private static final int PRIVATE_LOCAL_MUSIC=100;
	
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	
	private ProgressBar main_progress;
	
	private int SharePosition = -2;
	private Music mMusic;
	private TextView song_name_textview,artist_name_textview;
	private ImageView main_play_imageview;
	
	private MusicServiceBinder mBinder=null;
	private Bundle currentMusicBundle = null;
	
	private LinearLayout main_content;
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder binder) {
			Log.v(TAG, "LocalMusicFragment onServiceConnected");
			mBinder = (MusicServiceBinder) binder;
			
			currentMusicBundle = mBinder.getCurrentPlayMusicInfo();
			
			mBinder.registerOnPlayMusicStateListener(Main.this);
			toLoadMusic();
		}
	};
	
	public MusicServiceBinder getBinder(){
		return mBinder;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		TabMainFra tmf = new TabMainFra();
		ft.replace(R.id.main_content,tmf);
		ft.addToBackStack("tabmainfra");
		ft.commit();
	}
	private void initView() {
	
		main_progress= (ProgressBar) findViewById(R.id.main_progressbar);
		main_progress.setMax(300);
		
		song_name_textview = (TextView) findViewById(R.id.main_song_name);
		artist_name_textview = (TextView) findViewById(R.id.main_song_artist);
		
		main_play_imageview = (ImageView) findViewById(R.id.main_play_imageview);
		
		main_content = (LinearLayout) findViewById(R.id.main_content);
	}
	
	@Override
	protected void onStart() {
		Log.v(TAG, "MainActivity-->OnStart===============startService");
		super.onStart();
		Intent intent = new Intent(Main.this,MusicService.class);
		intent.setAction(MusicService.ACTION_INIT);
		intent.putExtra(Constant.CLICK_MUSIC_LIST, false);
		startService(intent);//启动服务
		
		if(mServiceConnection!=null){
			Log.v(TAG, "在onStart中的绑定服务======================");
			boolean isOk = bindService(new Intent(this,MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
			Log.v(TAG, "绑定service是否成功："+isOk);
		}
//		SDUtils.checkFile(Environment.getExternalStorageDirectory().getAbsoluteFile());
	}
//	@Override
//	public void onSkinChange(int coloValue,String url) {
//		content.setBackgroundColor(coloValue);
//	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(this);
		}
		if(mServiceConnection!=null){
			unbindService(mServiceConnection);
		}
	}
	/**开始load音乐*/
	private void toLoadMusic(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			getSupportLoaderManager().initLoader(PRIVATE_LOCAL_MUSIC, null, new LoaderCallbacks<List<Music>>() {

				@Override
				public Loader<List<Music>> onCreateLoader(int id, Bundle bundle) {
					Log.i(TAG, "onCreateLoader");
					
					return new MusicLoader(Main.this,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				}

				@Override
				public void onLoadFinished(Loader<List<Music>> loader,
						List<Music> data) {
					Log.i(TAG, "onLoadFinished---------------"+Thread.currentThread().getName());
					mCurrentMusicList.clear();
					if(data!=null&&data.size()>0){
						mCurrentMusicList.addAll(data);
						Log.i(TAG, "main中的音乐列表数是："+mCurrentMusicList.size());
						mBinder.setCurrentPlayList(data);
						
//						((TextView)(fraList.get(0).getView().findViewById(R.id.my_music_fra_totalnumber))).setText(data.size()+"首");
					}
					if(data!=null&&mBinder!=null){
						Log.d(TAG, "在此onLoadFinished设置currentMusicBundle");
						Music mMusic = null;
						currentMusicBundle = mBinder.getCurrentPlayMusicInfo();
						int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
						Log.v(TAG, "Main.class====+currentPlayState==="+currentPlayState);
						if(currentPlayState==PlayState.Stopped){
							SharePosition = PreferHelper.readInt(Main.this, Constant.SHARE_NMAE_MUSIC,
									Constant.SHARE_NMAE_MUSIC_POSITION, -1);
						}
						if (SharePosition==-1) {
							song_name_textview.setText(mCurrentMusicList.get(0).getTitle());
							artist_name_textview.setText(mCurrentMusicList.get(0).getArtist());
						}else if (SharePosition!=-2&&SharePosition>=0) {
							song_name_textview.setText(mCurrentMusicList.get(SharePosition).getTitle());
							artist_name_textview.setText(mCurrentMusicList.get(SharePosition).getArtist());
						}
						
						mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
						
						if(mMusic!=null){
							int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
							Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
//							
						}
					}
					
				}
				
				@Override
				public void onLoaderReset(Loader<List<Music>> arg0) {
					Log.i(TAG, "onLoaderReset");
//					mAdapter.setData(null);
				}

				
			});
		}else{//SD卡不可用
			
		}
	}
	/***
	 * @author vac
	 * @description 播放音乐
	 * @param view
	 */
	public void playSong(View view){
		if (SharePosition==-1) {
			playSongInPosition(mCurrentMusicList.get(0).getId());
		}else{
			int share = PreferHelper.readInt(Main.this, Constant.SHARE_NMAE_MUSIC,
					Constant.SHARE_NMAE_MUSIC_POSITION, -1);
			playSongInPosition(mCurrentMusicList.get(share).getId());
		}
	}
	
	private void playSongInPosition(long id){
		Intent intent = new Intent(this,MusicService.class);
		intent.setAction(MusicService.ACTION_PLAY);
		intent.putExtra(Constant.CLICK_MUSIC_LIST, true);
		intent.putExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, id);
		intent.putExtra(Constant.MUSIC_LIST_TYPE, Constant.DEFAULT_MUSIC_LIST_TYPE);
		startService(intent);
	}
	
	/**
	 * @author vac
	 * @description 打开播放音乐界面
	 * @param view
	 */
	public void toPlayActivity(View view){
		Intent intent = new Intent(Main.this,PlayMusic.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_bottom_in, 0);
	}
	
	
	/***
	 * @author vac
	 * @description 打开当前的播放队列
	 * @param view
	 * @date 2015年12月17日10:20:48
	 */
	public void openPlayingSongQueue(View view){
		//打开当前的播放列表
		Intent intent = new Intent(Main.this,PlayMusicQueue.class);
		startActivity(intent);
	}
	
	@Override
	public void onMuiscPlayed(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMuiscPlayed");
		mMusic = music;
		song_name_textview.setText(mMusic.getTitle());
		artist_name_textview.setText(mMusic.getArtist());
		main_play_imageview.setImageResource(R.drawable.img_media_controller_play);
	}
	@Override
	public void onMusicPaused(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMusicPaused");
		main_play_imageview.setImageResource(R.drawable.img_media_controller_pause);
	}
	@Override
	public void onMusicStoped() {
		
	}
	@Override
	public void onPlayModeChanged(int playMode) {
		
	}
	@Override
	public void onNewSongPlayed(Music music, int position) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMuiscPlayed");
		mMusic = music;
		song_name_textview.setText(mMusic.getTitle());
		artist_name_textview.setText(mMusic.getArtist());
	}
	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		main_progress.setProgress((int)(currenMillis*1.0/mMusic.getDuration() *main_progress.getMax()));
	}

}
