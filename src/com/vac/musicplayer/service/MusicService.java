package com.vac.musicplayer.service;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.widget.Toast;

import com.vac.musicplayer.PlayMusic;
import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.LyricSentence;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.AudioFocusHelper;
import com.vac.musicplayer.listener.AudioFocusHelper.MusicFocusable;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.utils.LyricDownloadManager;
import com.vac.musicplayer.utils.LyricLoadHelper;
import com.vac.musicplayer.utils.LyricLoadHelper.LyricListener;
import com.vac.musicplayer.utils.PreferHelper;

public class MusicService extends Service implements OnPreparedListener,OnCompletionListener
	,OnErrorListener,MusicFocusable,LyricListener{

	private static final String TAG = MusicService.class.getSimpleName();

	private MusicServiceBinder mBinder =new MusicServiceBinder();
	
	/**当前的播放列表*/
	private ArrayList<Music> mCurrentPlayList = new ArrayList<Music>();
	
	/**播放状态接口的集合*/
	private List<OnPlayMusicStateListener> mPlayMusicStateListenerList = new ArrayList<OnPlayMusicStateListener>();
	
	/**各个消息类型*/
	public static final String ACTION_INIT="com.vac.musicplayer.service.initservice";
	public static final String ACTION_PLAY ="com.vac.musicplayer.service.play";
	public static final String ACTION_PAUSE = "com.vac.musicplayer.service.pause";
	public static final String ACTION_STOP ="com.vac.musicplayer.service.stop";
	public static final String ACTION_PRIVIOUS= "com.vac.musicplayer.service.privious";
	public static final String ACTION_NEXT ="com.vac.musicplayer.service.next";
	
	
	/**handler消息类型*/
	public static final int MESSAGE_UPDATE_PLAYING_SONG_PROGRESS = 1;
	public static final int MESSAGE_ON_LYRIC_LOADED = 2;
	public static final int MESSAGE_ON_LYRIC_SENTENCE_CHANGED = 3;
	
	/**播放状态*/
	public static class PlayState{
		public static final int Stopped =0;
		public static final int Prepraing=1;
		public static final int Playing=2;
		public static final int Paused =3;
	}
	
	/**服务中正在播放的音乐*/
	private Music mPlayingMusic = null;
	
	/**服务中正在播放的音乐的位置*/
	private int mPlayingMusicPostion = -1;
	
	/**用户点击列表产生的在本播放队列的要播放音乐的位置*/
	private int mRequestMusicPosition =-1;
	
	/**服务中音乐播放的当前状态*/
	private int mState = PlayState.Stopped;
	
	/**服务中的音乐播放对象*/
	private MediaPlayer mPlayer=null;
	
	/**音频焦点辅助类*/
	private AudioFocusHelper mAudioFocusHelper =null;
	
	/** 丢失音频焦点，我们为媒体播放设置一个低音量(1.0f为最大)，而不是停止播放*/
	private static final float DUCK_VOLUME_LOW = 0.1f;
	private static final float DUCK_VOLUME_HIGH= 1.0f;
	
	/**记录当前的播放音乐的ID*/
	private long mRequestPlayMusicId = -1l;
	
	/**通知*/
	private Notification mNotification=null;
	
	/**通知的管理类*/
	private NotificationManager mNotificationManager=null;
	
	/**通知的id*/
	private static final int NOTIFICATION_ID =1000;
	
	/**当前的播放模式*/
	private int mPlayMode =1;
	
	public static class PlayMode{
		/**单曲循环*/
		public static final int REPEAT_SINGLE=0;
		
		/**列表循环*/
		public static final int REPEAT =1;
		
		/**顺序播放*/
		public static final int SEQUENTIAL = 2;
		
		/**随机播放*/
		public static final int SHUFFLE = 3;
	}
	
	/**歌词的回调接口*/
	private LyricListener mLyricListener = null;
	
	/** 句子集合 */
	private ArrayList<LyricSentence> mLyricSentences = new ArrayList<LyricSentence>();
	
	/** 当前正在播放的歌词句子的在句子集合中的索引号 */
	private int mIndexOfCurrentSentence = -1;
	
	/**歌词帮助类*/
	private LyricLoadHelper mLyricLoadHelper = new LyricLoadHelper();
	
	/**发送消息的handler*/
	private ServiceHandler mServiceHandler = new ServiceHandler(this);
	
	private boolean mHasLyric=false;
	
	private LyricDownloadManager mLyricDownloadManager = null;
	
	private static class ServiceHandler extends Handler{
		
		WeakReference<MusicService> mServiceWeakReference=null;
		MusicService mMusicService=null;
		public ServiceHandler(MusicService musicService){
			mServiceWeakReference  = new WeakReference<MusicService>(musicService);
			mMusicService = mServiceWeakReference.get();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_UPDATE_PLAYING_SONG_PROGRESS://更新进度
				if(mMusicService.mState==PlayState.Playing){
					int currentProgressMilli =mMusicService.mPlayer.getCurrentPosition();
					for(int i=0;i<mMusicService.mPlayMusicStateListenerList.size();i++){
						mMusicService.mPlayMusicStateListenerList.get(i).onPlayProgressUpdate(currentProgressMilli);
					}
					
					if(mMusicService.mHasLyric){
						mMusicService.mLyricLoadHelper.notifyTime(currentProgressMilli);
					}
					mMusicService.mServiceHandler.sendEmptyMessageDelayed(MESSAGE_UPDATE_PLAYING_SONG_PROGRESS, 500);
				}
				break;
			case MESSAGE_ON_LYRIC_LOADED:
				if (mMusicService.mLyricListener != null) {
					mMusicService.mLyricListener.onLyricLoaded((List<LyricSentence>) msg.obj, msg.arg1);
				}
				break;
			case MESSAGE_ON_LYRIC_SENTENCE_CHANGED:
				if (mMusicService.mLyricListener != null) {
					mMusicService.mLyricListener.onLyricSentenceChanged(msg.arg1);
				}
				break;
			default:
				break;
			}
		}
	}
	
	
	/**
	 * 绑定服务时 本地服务的Binder对象
	 * @author vac
	 *
	 */
	public class MusicServiceBinder extends Binder{
		
		/**
		 * 当绑定服务 连接到服务时，用Binder对象在此注册一个 音乐播放状态 回调接口
		 * @param listener OnPlayMusicStateListener音乐播放状态接口
		 */
		public void registerOnPlayMusicStateListener(OnPlayMusicStateListener listener){
			mPlayMusicStateListenerList.add(listener);//此处用集合将listener添加进去 为了是防止多处注册该接口
		}
		
		/**
		 * 取消绑定时，将音乐播放状态接口 移除
		 * @param listener
		 */
		public void unRegisterPlayMusicStateListener(OnPlayMusicStateListener listener){
			mPlayMusicStateListenerList.remove(listener);
		}
		
		public void registerLyricListener(LyricListener listener) {
			mLyricListener = listener;
		}

		public void unRegisterLyricListener() {
			mLyricListener = null;
		}
		
		/**
		 * 获取当前的播放 相关信息
		 * @return
		 */
		public Bundle getCurrentPlayMusicInfo(){
			Bundle bundle = new Bundle();
			Music _music = null;
			int currentPlayProgress = 0;//当前播放的进度
			
			if(mState==PlayState.Playing||mState==PlayState.Paused){//只有处于播放或暂停状态才返回Music信息
				_music = mPlayingMusic;
				currentPlayProgress = mPlayer.getCurrentPosition();
			}
			bundle.putParcelable(Constant.PLAYING_MUSIC_ITEM, _music);//将Music返回
			bundle.putInt(Constant.PLAYING_MUSIC_STATE, mState);//将当前的播放状态返回
			bundle.putInt(Constant.PLAYING_MUSIC_PROGRESS, currentPlayProgress);//将当前的播放进度返回 ，如果是非播放暂停状态，则返回0
			bundle.putInt(Constant.PLAYING_MUSIC_PLAYMODE, mPlayMode);
			bundle.putInt(Constant.PLAYING_MUSIC_POSITION_IN_LIST, mPlayingMusicPostion);//将当前的播放的音乐在播放列表中的位置返回
			bundle.putParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST, mCurrentPlayList);//将当前的播放列表返回
			return bundle;
		}
		
		/** 如果当前正在播放歌曲，通知LyricListener载入歌词 */
		public void requestLoadLyric() {
			Log.i(TAG, "requestLoadLyric");
			if (mPlayingMusic != null && mState != PlayState.Stopped) {
				Log.i(TAG, "requestLoadLyric--->loadLyric");
				loadLyric(mPlayingMusic.getData());
				mLyricLoadHelper.notifyTime(mPlayer.getCurrentPosition());
			}
		}
		
		/**
		 * 设置当前的播放列表
		 * @param list 从别处传过来的音乐集合
		 */
		public void setCurrentPlayList(List<Music> list){
			mCurrentPlayList.clear();
			if(list!=null){
				mCurrentPlayList.addAll(list);
			}
			
			Log.v(TAG, "当前服务的音乐播放列表"+mCurrentPlayList.size());
		}
		
		/**
		 * 下一首
		 */
		public void playNext(){
			requestToPlayNext(true);
		}
		
		/**
		 * 上一首
		 */
		public void playPre(){
			requestToPlayPrevious(true);
		}
		
		/**
		 * 暂停
		 */
		public void playPause(){
			requestToPause();
		}
		
		/**
		 * 播放
		 */
		public void playToPlay(){
			if(mRequestMusicPosition==-1){
				mRequestMusicPosition= PreferHelper.readInt(getApplicationContext(), Constant.SHARE_NMAE_MUSIC,
						Constant.SHARE_NMAE_MUSIC_POSITION, -1);
			}
			
			if(mRequestMusicPosition!=-1){
				mRequestPlayMusicId = mCurrentPlayList.get(mRequestMusicPosition).getId();
				requestToPlay();
			}
		
		}
		
		/**
		 * 让MediaPlayer将当前播放跳转到指定播放位置
		 * 
		 * @param milliSeconds
		 *            指定的已播放的毫秒数
		 * */
		public void seekToSpecifiedPosition(int milliSeconds) {
			if (mState != PlayState.Stopped) {
				mPlayer.seekTo(milliSeconds);
			}
		}
		
		/**
		 * 改变播放模式
		 */
		public void changePlayMode(){
			mPlayMode = (mPlayMode+1)%4;
			if(mPlayer!=null){
				if(mPlayMode==PlayMode.REPEAT_SINGLE){
					mPlayer.setLooping(true);
				}else{
					mPlayer.setLooping(false);
				}
			}
			
			for(int i=0;i<mPlayMusicStateListenerList.size();i++){
				mPlayMusicStateListenerList.get(i).onPlayModeChanged(mPlayMode);
			}
		}
		
		/**
		 * 移除当前队列的歌曲 
		 */
		public void removeMusicFromCurrentList(long musicId){
			for(int i=0;i<mCurrentPlayList.size();i++){
				if(mCurrentPlayList.get(i).getId()==musicId){
					if(i<mPlayingMusicPostion){
						mRequestMusicPosition = mPlayingMusicPostion--;
					}else if(i==mPlayingMusicPostion){
						mCurrentPlayList.remove(i);
						playSong();
						return;
					}
					mCurrentPlayList.remove(i);		
					break;
					
					
				}
			}
			
			if(mCurrentPlayList.size()==0){
				requestToStop();
			}else if(musicId ==mPlayingMusic.getId()){
				--mPlayingMusicPostion;
				mRequestMusicPosition--;

			}
		}
		
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "service onCreate");
		
		mAudioFocusHelper = new AudioFocusHelper(getApplicationContext(), this);

		mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
		
		mLyricLoadHelper.setLyricListener(MusicService.this);
		mLyricDownloadManager = new LyricDownloadManager(getApplicationContext());
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "service onStartCommand");
		String action = intent.getAction();
		if(action.equals(ACTION_PLAY)){//播放音乐的消息

			if(intent.getBooleanExtra(Constant.CLICK_MUSIC_LIST, false)){//如果是从播放列表点击
				mRequestPlayMusicId = intent.getLongExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, -1);
				mRequestMusicPosition = findPositionByMusicId(mCurrentPlayList, mRequestPlayMusicId);
			}else{
				mRequestPlayMusicId = mCurrentPlayList.get(mRequestMusicPosition).getId();
			}
			
			if(mRequestMusicPosition!=-1){
				requestToPlay();
			}
		}else if(action.equals(ACTION_NEXT)){//播放下一首
			requestToPlayNext(true);
		}else if(action.equals(ACTION_PRIVIOUS)){//播放上一首
			requestToPlayPrevious(true);
		}else if(action.equals(ACTION_PAUSE)){//暂停
			requestToPause();
		}
		
		return START_NOT_STICKY;
	}
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "service onBind");
		return mBinder;//在此返回service的Binder对象
	}
	
	
	/**
	 * 根据传递过来的已播放的毫秒数，计算应当对应到句子集合中的哪一句，再通知监听者播放到的位置。
	 * 
	 * @param millisecond
	 *            已播放的毫秒数
	 */
	public void notifyTime(long millisecond) {
		// Log.i(TAG, "notifyTime");
		if (mCurrentPlayList.size()>0 && mLyricSentences != null && mLyricSentences.size() != 0) {
			int newLyricIndex = seekSentenceIndex(millisecond);
			if (newLyricIndex != -1 && newLyricIndex != mIndexOfCurrentSentence) {// 如果找到的歌词和现在的不是一句。
				if (mLyricListener != null) {
					// 告诉一声，歌词已经变成另外一句啦！
					mLyricListener.onLyricSentenceChanged(newLyricIndex);
				}
				mIndexOfCurrentSentence = newLyricIndex;
			}
		}
	}
	
	private int seekSentenceIndex(long millisecond) {
		int findStart = 0;
		if (mIndexOfCurrentSentence >= 0) {
			// 如果已经指定了歌词，则现在位置开始
			findStart = mIndexOfCurrentSentence;
		}

		try {
			long lyricTime = mLyricSentences.get(findStart).getStartTime();

			if (millisecond > lyricTime) { // 如果想要查找的时间在现在字幕的时间之后
				// 如果开始位置经是最后一句了，直接返回最后一句。
				if (findStart == (mLyricSentences.size() - 1)) {
					return findStart;
				}
				int new_index = findStart + 1;
				// 找到第一句开始时间大于输入时间的歌词
				while (new_index < mLyricSentences.size()
						&& mLyricSentences.get(new_index).getStartTime() <= millisecond) {
					++new_index;
				}
				// 这句歌词的前一句就是我们要找的了。
				return new_index - 1;
			} else if (millisecond < lyricTime) { // 如果想要查找的时间在现在字幕的时间之前
				// 如果开始位置经是第一句了，直接返回第一句。
				if (findStart == 0)
					return 0;

				int new_index = findStart - 1;
				// 找到开始时间小于输入时间的歌词
				while (new_index > 0
						&& mLyricSentences.get(new_index).getStartTime() > millisecond) {
					--new_index;
				}
				// 就是它了。
				return new_index;
			} else {
				// 不用找了
				return findStart;
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			Log.i(TAG, "新的歌词载入了，所以产生了越界错误，不用理会，返回0");
			return 0;
		}
	}
	
	/**
	 * 请求一个播放音乐的动作
	 * 需要 mRequestMusicPosition  和 mRequestPlayMusicId
	 * 
	 */
	private void requestToPlay(){
		Log.d(TAG, "这是请求播放动作时的音乐播放状态-------="+mState
				+",请求的音乐播放位置mRequestMusicPosition="+mRequestMusicPosition
				+",当前服务中的音乐播放位置mPlayingMusicPostion="+mPlayingMusicPostion);
		mAudioFocusHelper.tryToGetAudioFocus();
		if(mState==PlayState.Stopped){//如果播放器处于停止状态
			mPlayingMusicPostion = mRequestMusicPosition;//播放用户请求的歌曲
			playSong();//开始播放音乐
			Log.d(TAG, "播放器处于停止状态,开始播放音乐");
		}else if(mState==PlayState.Paused){//如果播放器处于暂停状态
			if(mPlayingMusicPostion==mRequestMusicPosition){//用户请求的播放歌曲 和 当前播放歌曲相同
				//继续播放音乐吧，由暂停状态转成播放状态
				mState =PlayState.Playing;
				setServiceAsForeground(mPlayingMusic.getTitle() + " (playing)");
				configAndStartMediaPlayer();
				Log.d(TAG, "播放器处于暂停状态,播放歌曲 和 当前播放歌曲相同,继续播放音乐吧，由暂停状态转成播放状态");
			}else if(mRequestPlayMusicId != mPlayingMusic.getId()){//用户请求的播放歌曲 和 当前播放歌曲 不 相同
				mPlayingMusicPostion = mRequestMusicPosition;//播放用户请求的歌曲
				playSong();//开始播放音乐
				Log.d(TAG, "播放器处于暂停状态,播放歌曲 和 当前播放歌曲  不  相同,播放用户请求的歌曲");
			}
		}else if(mState==PlayState.Playing){//如果播放器处于播放状态
			if(mPlayingMusicPostion==mRequestMusicPosition){//用户请求的播放歌曲 和 当前播放歌曲相同
				//暂停播放音乐吧，由播放状态转成暂停状态
				requestToPause();
				Log.d(TAG, "播放器处于播放状态,播放歌曲 和 当前播放歌曲相同,暂停播放音乐吧，由播放状态转成暂停状态");
				return;
			}else if(mRequestPlayMusicId != mPlayingMusic.getId()){//用户请求的播放歌曲 和 当前播放歌曲 不 相同
				mPlayingMusicPostion = mRequestMusicPosition;//播放用户请求的歌曲
				playSong();//开始播放音乐
				Log.d(TAG, "播放器处于播放状态,播放歌曲 和 当前播放歌曲不 相同,播放用户请求的歌曲");
			}
		}
		
		/**通知回调接口，开始播放*/
		for(int i=0;i<mPlayMusicStateListenerList.size();i++){
			mPlayMusicStateListenerList.get(i).onMuiscPlayed(mPlayingMusic);
		}
		
		if (!mServiceHandler.hasMessages(MESSAGE_UPDATE_PLAYING_SONG_PROGRESS)) {
			mServiceHandler
					.sendEmptyMessage(MESSAGE_UPDATE_PLAYING_SONG_PROGRESS);
		}
	}
	
	
	/**
	 * 请求一个暂停的动作
	 */
	private void requestToPause(){
		Log.d(TAG, "音乐暂停---requestToPause,mState="+mState);
		if(mState==PlayState.Playing){
			mState =PlayState.Paused;
			mPlayer.pause();
			releaseResource(false);//MeidaPlayer对象不释放
		}
		
		for(int i =0;i<mPlayMusicStateListenerList.size();i++){
			mPlayMusicStateListenerList.get(i).onMusicPaused(mPlayingMusic);
		}
	}
	
	/**
	 * 请求播放下一首的动作（暂不考虑 播放模式的问题）
	 * @author vac
	 * @param isUserClick 
	 * 					true 是用户手动点击播放下一首
	 * 					false 当歌曲播放完成后，自动播放下一首
	 */
	private void requestToPlayNext(boolean isUserClick){
		if(mState!=PlayState.Prepraing&&mCurrentPlayList.size()>0){
			
			switch (mPlayMode) {
			case PlayMode.REPEAT:
				mRequestMusicPosition =( mPlayingMusicPostion+1)%mCurrentPlayList.size();
				break;
			case PlayMode.REPEAT_SINGLE:
				if(isUserClick){
					mRequestMusicPosition = (mPlayingMusicPostion + 1) % mCurrentPlayList.size();
				}else{
					mPlayer.setLooping(true);
				}
				break;
			case PlayMode.SEQUENTIAL:
				mRequestMusicPosition = (mPlayingMusicPostion+1)%mCurrentPlayList.size();
				if(mRequestMusicPosition==0){
					if(isUserClick){
						mRequestMusicPosition=0;
					}else{
						mRequestMusicPosition = mCurrentPlayList.size()-1;
						requestToStop();
						return;
					}
				}
				break;
			case PlayMode.SHUFFLE:
				mRequestMusicPosition = new Random().nextInt(mCurrentPlayList.size());
				break;

			default:
				break;
			}
			
			mRequestPlayMusicId = mCurrentPlayList.get(mRequestMusicPosition).getId();
			requestToPlay();
		}
	
	}
	
	/**
	 * 请求播放上一首的动作（暂不考虑 播放模式的问题）
	 * @param isUserClick
	 * 					true 是用户手动点击播放上一首
	 * 					false 当歌曲播放完成后，自动播放上一首
	 */
	private void requestToPlayPrevious(boolean isUserClick){
		if(mState!=PlayState.Prepraing&&mCurrentPlayList.size()>0){
			if(--mRequestMusicPosition<0){
				mRequestMusicPosition = mCurrentPlayList.size()-1;//如果是第一首歌曲，那么将从最后一首歌曲开始播放
			}
			mRequestPlayMusicId = mCurrentPlayList.get(mRequestMusicPosition).getId();
			requestToPlay();
		}
	}
	
	/**
	 * 请求一个播放停止的动作
	 */
	private void requestToStop(){
		requestToStop(false);
	}
	
	
	/**
	 * 请求一个播放停止的动作
	 * @param isFource 强制停止
	 */
	private void requestToStop(boolean isFource){
		if(mState ==PlayState.Playing||mState ==PlayState.Prepraing||isFource){
			mState = PlayState.Stopped;
			mRequestMusicPosition = 0;
			mPlayingMusicPostion =0;
			mRequestPlayMusicId = -1;
			releaseResource(true);//释放掉MediaPlayer对象
			mAudioFocusHelper.giveUpAudioFocus();
		}

		// 通知所有的观察者音乐停止播放了
		for (int i = 0; i < mPlayMusicStateListenerList.size(); i++) {
			mPlayMusicStateListenerList.get(i).onMusicStoped();
		}
	}
	
	
	/**
	 * 释放所有的资源（MediaPlayer由isReleaseMediaPlayer决定是否释放）
	 * @param isRealseMediaPlayer 是否释放MediaPlayer对象
	 */
	private void releaseResource(boolean isReleaseMediaPlayer){
		stopForeground(true);
		if(mPlayer!=null&&isReleaseMediaPlayer){
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	/***
	 * 更新通知栏
	 * @param text
	 */
	private void updateNotification(String text){
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(),PlayMusic.class), PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(getApplicationContext(),
				"yufengvac的音乐", text, pIntent);
		mNotificationManager.notify(NOTIFICATION_ID,mNotification );
	}
	
	/**
	 * 将本服务设置为“前台服务”。“前台服务”是一个与用户正在交互的服务， 必须在通知栏显示一个通知表示正在交互
	 */
	private void setServiceAsForeground(String text){
		Intent intent = new Intent(getApplicationContext(),PlayMusic.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification = new Notification();
		mNotification.tickerText = text;
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.flags |=Notification.FLAG_ONGOING_EVENT;
		mNotification.setLatestEventInfo(getApplicationContext(), "yufengvac的音乐", text, pIntent);
		startForeground(NOTIFICATION_ID, mNotification);
	}
	
	
	/***
	 * @author vac
	 * @description 根据歌曲的id，在当前的播放列表中找到 播放的位置
	 * @param list 当前的播放列表
	 * @param _id 需要寻找的歌曲Id
	 * @return int 该_id歌曲在当前播放列表中的位置   -1表示未找到
	 * @date 2015年8月29日17:00:24
	 */
	public static int findPositionByMusicId(List<Music> list,long _id){
		if(list!=null){
			for(int i=0;i<list.size();i++){
				if(list.get(i).getId()==_id){
					return i;
				}
			}
		}
		return -1;
	}
	
	/**
	 *播放在播放列表mCurrentPlayList中mPlayingMusicPosition位置 的音乐
	 */
	private void playSong(){
		mPlayingMusic = mCurrentPlayList.get(mPlayingMusicPostion);
		mState = PlayState.Stopped;
		releaseResource(false);
		
		createMediaPlayerIfNeed();
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			Log.i(TAG, ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, mPlayingMusic.getId())+"-000");
			mPlayer.setDataSource(getApplicationContext(), ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, mPlayingMusic.getId()));
			mState = PlayState.Prepraing;
			if (mLyricListener != null) {
				loadLyric(mPlayingMusic.getData());
			}
			
			setServiceAsForeground(mPlayingMusic.getTitle() + " (loading)");
			
			mPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "paly song Exception"+e.getMessage());
		}
		
		for(int i =0;i<mPlayMusicStateListenerList.size();i++){
			mPlayMusicStateListenerList.get(i).onNewSongPlayed(mPlayingMusic,mPlayingMusicPostion);
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
		mState = PlayState.Stopped;
		releaseResource(true);
		mAudioFocusHelper.giveUpAudioFocus();
		if (mState != PlayState.Stopped) {
			requestToStop();
		}
		mLyricListener=null;
		mPlayMusicStateListenerList.clear();
		mPlayMusicStateListenerList=null;
		
	}


	/**
	 * MeidaPlayer发生了错误调用此方法,错误发生后停止播放.
	 */
	@Override
	public boolean onError(MediaPlayer arg0, int what, int extra) {
		Toast.makeText(getApplicationContext(),
				"Media player error! Resetting.", Toast.LENGTH_SHORT).show();
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			Log.e(TAG, "Error: "
					+ "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK"
					+ ", extra=" + String.valueOf(extra));
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Log.e(TAG, "Error: " + "MEDIA_ERROR_SERVER_DIED" + ", extra="
					+ String.valueOf(extra));
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Log.e(TAG,
					"Error: " + "MEDIA_ERROR_UNKNOWN" + ", extra="
							+ String.valueOf(extra));
			break;
		case -38:
			Log.e(TAG,
					"Error: what"
							+ what
							+ ", extra="
							+ extra
							+ ",see at http://blog.sina.com.cn/s/blog_632b619d01012991.html");
			break;
		default:
			Log.e(TAG, "Error: what" + what + ", extra=" + extra);
			break;
		}

		requestToStop(false);

		return true; // true表示我们处理了发生的错误
	}


	/**
	 * MeidaPlayer完成了一首歌曲的播放回调
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		mHasLyric = false;
		mLyricLoadHelper.setIndexOfCurrentSentence(-1);
		requestToPlayNext(false);
	}


	/**
	 * MediaPlayer准备好完成时回调
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		Log.i(TAG, "onPrepared");
		mState = PlayState.Playing;
		updateNotification(mPlayingMusic.getTitle() + " (playing)");
		configAndStartMediaPlayer();
		if (!mServiceHandler.hasMessages(MESSAGE_UPDATE_PLAYING_SONG_PROGRESS)) {
			mServiceHandler
					.sendEmptyMessage(MESSAGE_UPDATE_PLAYING_SONG_PROGRESS);
		}
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
			PreferHelper.write(getApplicationContext(), Constant.SHARE_NMAE_MUSIC,Constant.SHARE_NMAE_MUSIC_POSITION,mPlayingMusicPostion);
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


	/**
	 * 读取歌词文件
	 * 
	 * @param path
	 *            歌曲文件的路径
	 */
	private void loadLyric(String path) {
		// 取得歌曲同目录下的歌词文件绝对路径
		String lyricFilePath = Constant.LYRIC_SAVE_FOLDER_PATH + "/"
				+ mPlayingMusic.getTitle() + "_" + mPlayingMusic.getArtist()
				+ ".lrc";
		
		File lyricfile = new File(lyricFilePath);
		
		if (lyricfile.exists()) {
			// 本地有歌词，直接读取
			Log.i(TAG, "loadLyric()--->原生---本地有歌词，直接读取");
			mHasLyric = mLyricLoadHelper.loadLyric(lyricFilePath);
		} else{
			if(mPlayingMusic.getTitle().contains("-")){//周杰伦-三年二班
				String[] str = mPlayingMusic.getTitle().split("-");
				String newSingerName = str[0].trim();
				String newMusicName = str[1].trim();
				String lyricFilePath_new = Constant.LYRIC_SAVE_FOLDER_PATH + "/"
						+ newMusicName+ "_" + newSingerName + ".lrc";
				if(new File(lyricFilePath_new).exists()){
					// 本地有歌词，直接读取
					Log.i(TAG, "lyricFilePath_new--->将歌名拆开后，本地有歌词，直接读取");
					mHasLyric = mLyricLoadHelper.loadLyric(lyricFilePath_new);
				}else{
					// 尝试网络获取歌词
					Log.i(TAG, "loadLyric()--->将歌名拆开后，本地无歌词，尝试从网络获取");
					new LyricDownloadAsyncTask().execute(newMusicName,newSingerName);
				}
			}else{
				// 尝试网络获取歌词
				Log.i(TAG, "loadLyric()--->原生---本地无歌词，尝试从网络获取");
				new LyricDownloadAsyncTask().execute(mPlayingMusic.getTitle(),mPlayingMusic.getArtist());
			}
		}  
		
	}
	
	class LyricDownloadAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// 从网络获取歌词，然后保存到本地
			String lyricFilePath = mLyricDownloadManager.searchLyricFromWeb(
					params[0], params[1]);
			Log.i(TAG, "网络获取歌词完毕，返回本地歌词路径lyricFilePath:" + lyricFilePath);
			
			//尝试别的下载方式
			if(lyricFilePath==null){
				lyricFilePath =mLyricDownloadManager.downloadLyricJson(params[0], params[1]);
			}
			// 返回本地歌词路径
			return lyricFilePath;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "网络获取歌词完毕，歌词保存路径:" + result);
			// 读取保存到本地的歌曲
			mHasLyric = mLyricLoadHelper.loadLyric(result);
		};

	};

	@Override
	public void onLyricLoaded(List<LyricSentence> lyricSentences, int index) {
		if (mLyricListener != null) {
			mLyricListener.onLyricLoaded(lyricSentences, index);
		} else {
			// 来自客户端的LyricListener还没来的及注册，就延迟一会等它注册好了再把参数传递给它
			mServiceHandler.sendMessageDelayed(Message.obtain(null,
					MESSAGE_ON_LYRIC_LOADED, index, 0, lyricSentences), 500);
		}
	}

	@Override
	public void onLyricSentenceChanged(int indexOfCurSentence) {
		if (mLyricListener != null) {
			mLyricListener.onLyricSentenceChanged(indexOfCurSentence);
		} else {
			// 来自客户端的LyricListener还没来的及注册，就延迟一会等它注册好了再把参数传递给它
			mServiceHandler.sendMessageDelayed(Message.obtain(null,
					MESSAGE_ON_LYRIC_LOADED, indexOfCurSentence, 0), 500);
		}
	}
}
