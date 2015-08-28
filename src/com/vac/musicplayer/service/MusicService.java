package com.vac.musicplayer.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.AudioFocusHelper;
import com.vac.musicplayer.listener.AudioFocusHelper.MusicFocusable;

public class MusicService extends Service implements OnPreparedListener,OnCompletionListener
	,OnErrorListener,MusicFocusable{

	private static final String TAG = MusicService.class.getSimpleName();

	private MusicServiceBinder mBinder =new MusicServiceBinder();
	
	/**当前的播放列表*/
	private ArrayList<Music> mCurrentPlayList = new ArrayList<Music>();
	
	
	/**各个消息类型*/
	public static final String ACTION_INIT="com.vac.musicplayer.service.initservice";
	public static final String ACTION_PLAY ="com.vac.musicplayer.service.play";
	public static final String ACTION_PAUSE = "com.vac.musicplayer.service.pause";
	public static final String ACTION_STOP ="com.vac.musicplayer.service.stop";
	public static final String ACTION_PRIVIOUS= "com.vac.musicplayer.service.privious";
	public static final String ACTION_NEXT ="com.vac.musicplayer.service.next";
	
	/**播放状态*/
	static class PlayState{
		public static final int Stopped =0;
		public static final int Prepraing=1;
		public static final int Playing=2;
		public static final int Paused =3;
	}
	
	/**服务中正在播放的音乐*/
	private Music mPlayingMusic = null;
	
	/**服务中正在播放的音乐的位置*/
	private int mPlayingMusicPostion = -1;
	
	/**服务中音乐播放的当前状态*/
	private int mState = PlayState.Stopped;
	
	/**服务中的音乐播放对象*/
	private MediaPlayer mPlayer=null;
	
	/**音频焦点辅助类*/
	private AudioFocusHelper mAudioFocusHelper =null;
	
	/** 丢失音频焦点，我们为媒体播放设置一个低音量(1.0f为最大)，而不是停止播放*/
	private static final float DUCK_VOLUME_LOW = 0.1f;
	private static final float DUCK_VOLUME_HIGH= 1.0f;
	
	/**
	 * 绑定服务时 本地服务的Binder对象
	 * @author vac
	 *
	 */
	public class MusicServiceBinder extends Binder{
		
		/**
		 * 设置当前的播放列表
		 * @param list 从别处传过来的音乐集合
		 */
		public void setCurrentPlayList(List<Music> list){
			mCurrentPlayList.clear();
			if(list!=null){
				mCurrentPlayList.addAll(list);
			}
		}
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "service onCreate");
		
		mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);

		mBinder.setCurrentPlayList(MusicListAdapter.getData());
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "service onStartCommand");
		String action = intent.getAction();
		if(action.equals(ACTION_PLAY)){//播放音乐的消息
			requestToPlay();
		}
		return START_NOT_STICKY;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "service onBind");
		return mBinder;//在此返回service的Binder对象
	}
	
	/**
	 * 去开始播放音乐
	 */
	private void requestToPlay(){
		mAudioFocusHelper.tryToGetAudioFocus();
		
		mPlayingMusicPostion = 0;//从头开始播放音乐
		playSong();
	}
	
	private void playSong(){
		mPlayingMusic = mCurrentPlayList.get(mPlayingMusicPostion);
		mState = PlayState.Stopped;
		
		createMediaPlayerIfNeed();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			Log.i(TAG, ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, mPlayingMusic.getId())+"-000");
			mPlayer.setDataSource(getApplicationContext(), ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, mPlayingMusic.getId()));
			mState = PlayState.Prepraing;
			mPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "paly song Exception"+e.getMessage());
		} 
	}
	
	private void createMediaPlayerIfNeed(){
		if(mPlayer==null){
			mPlayer = new MediaPlayer();
			mPlayer.setOnPreparedListener(this);
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnErrorListener(this);
		}else{
			mPlayer.reset();
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "service onUnbind");
		return super.onUnbind(intent);
	}
	@Override
	public void onDestroy() {
		Log.i(TAG, "service onDestroy");
		super.onDestroy();
	}


	/**
	 * MeidaPlayer发生了错误调用此方法,错误发生后停止播放.
	 */
	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		return false;
	}


	/**
	 * MeidaPlayer完成了一首歌曲的播放回调
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
	}


	/**
	 * MediaPlayer准备好完成时回调
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		Log.i(TAG, "onPrepared");
		mState = PlayState.Playing;
		configAndStartMediaPlayer();
	}
	
	/**
	 * 根据音频焦点的设置重新设置MediaPlayer的参数，然后启动或者重启它。 如果我们拥有音频焦点，则正常播放;
	 * 如果没有音频焦点，根据当前的焦点设置将MediaPlayer切换为“暂停”状态或者低声播放。
	 * 这个方法已经假设mPlayer不为空，所以如果要调用此方法，确保正确的使用它。
	 */
	private void configAndStartMediaPlayer(){
		Log.i(TAG, "configAndStartMediaPlayer");
		if(mAudioFocusHelper.getAudioFocus()==AudioFocusHelper.NoFocusNoDuck){//失去音频焦点且找不回
			//如果丢失了音频焦点也不允许低声播放，我们必须让播放暂停，即使mState处于State.Playing状态。
			// 但是我们并不修改mState的状态，因为我们会在获得音频焦点时返回立即返回播放状态。
			if(mPlayer.isPlaying()){
				mPlayer.pause();
			}
			return;
		}else if(mAudioFocusHelper.getAudioFocus()==AudioFocusHelper.NoFocusCanDuck){//失去了音频焦点，但是能马上找回来
			mPlayer.setVolume(DUCK_VOLUME_LOW, DUCK_VOLUME_LOW);// 设置一个低音量播放
		}else{
			mPlayer.setVolume(DUCK_VOLUME_HIGH, DUCK_VOLUME_HIGH);//设置最大音量播放
		}
		
		if(!mPlayer.isPlaying()){
			Log.i(TAG, "start");
			mPlayer.start();
		}
		
	}


	/**
	 * 音频焦点获取到了
	 */
	@Override
	public void onGainedAudioFocus() {
		Log.i(TAG, "onGainedAudioFocus");
		if(mState == PlayState.Playing){
			configAndStartMediaPlayer();
		}
	}


	/**
	 * 失去了音频焦点
	 */
	@Override
	public void onLostAudioFocus() {
		Log.i(TAG, "onLostAudioFocus");
		if(mPlayer!=null&&mPlayer.isPlaying()){
			configAndStartMediaPlayer();
		}
	}
}
