package com.vac.musicplayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.vac.musicplayer.adapter.LyricAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.LyricSentence;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayMode;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.LyricLoadHelper.LyricListener;
import com.vac.musicplayer.utils.MyImageLoader;
import com.vac.musicplayer.utils.PreferHelper;
import com.vac.musicplayer.utils.TimeHelper;

public class PlayMusic extends Activity implements OnPlayMusicStateListener,OnClickListener{


	private static final String TAG = PlayMusic.class.getSimpleName();
	private MusicServiceBinder mBinder = null;
	private List<Music> playMusicList=null;
	public static final int MSG_SET_LYRIC_INDEX = 1;
	
	private Music mMusic=null;//当前播放音乐
	
	private boolean isPlaying=false;//是否在播放音乐
	
	/**音乐名称、音乐歌手、音乐所属专辑*/
	private TextView play_music_title,play_music_artist,play_music_album;
	
	/**上一首、播放or暂停、下一首*/
	private Button play_music_pre,play_music_pause,play_music_next;
	
	/**音乐播放的当前时间、总时间*/
	private TextView play_music_curtime,play_music_endtime;
	
	/**音乐播放的当前位置、音乐总数*/
	private TextView play_music_cursong,play_music_totalsong;
	
	/**音乐播放的进度指示条*/
	private SeekBar play_music_progressBar;
	
	/**音乐播放模式    打开当前的播放列表*/
	private ImageButton play_music_playmode,play_music_opencurrentlist;
	
	/**歌词show*/
	private ListView play_music_listview_lyricshow;
	
	/**歌词适配器*/
	private LyricAdapter mAdapter=null;
	
	/**空歌词*/
	private TextView play_music_emptylyric;
	
	/**背景*/
	private ImageView play_music_content;
	
	private MyImageLoader imageLoader;

	private ClientIncomingHandler mHandler = new ClientIncomingHandler(this);

	/** 处理来自服务端的消息 */
	private static class ClientIncomingHandler extends Handler {
		// 使用弱引用，避免Handler造成的内存泄露(Message持有Handler的引用，内部定义的Handler类持有外部类的引用)
		WeakReference<PlayMusic> mFragmentWeakReference = null;
		PlayMusic mActivity = null;

		public ClientIncomingHandler(PlayMusic a) {
			mFragmentWeakReference = new WeakReference<PlayMusic>(a);
			mActivity = mFragmentWeakReference.get();
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SET_LYRIC_INDEX:
				if (mActivity.mAdapter.isEmpty()) {
					Log.i(TAG, "歌词为空");
					mActivity.play_music_emptylyric
							.setText("暂无歌词");
				} else {
					mActivity.play_music_listview_lyricshow.setSelectionFromTop(msg.arg1,
							mActivity.play_music_listview_lyricshow.getHeight() / 2);
				}
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	}
	
	
	private ServiceConnection mServiceConn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i(TAG, "serviceConnected");
			mBinder = (MusicServiceBinder) binder;
			mBinder.registerOnPlayMusicStateListener(PlayMusic.this);
		
//			mBinder.setCurrentPlayList(playMusicList);
			
			/**传递LyricListener对象给Service，以便歌词发生变化时通知本Activity*/
			mBinder.registerLyricListener(mLyricListener);
			
			/**通知载入歌词*/
			mBinder.requestLoadLyric();
			
			/**初始化当前的播放信息*/
			initCurrentPlayMusicInfo(mBinder.getCurrentPlayMusicInfo());
			
			if(imageLoader!=null&&mMusic!=null){
				//加载图片
				imageLoader.setAlphaImageView(play_music_content,mMusic.getArtist());
			}
		}
	};
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		initView();
		playMusicList = (List<Music>) getIntent().getSerializableExtra(Constant.PLAYLIST_MUISC);
		
		mAdapter = new LyricAdapter(PlayMusic.this);
		play_music_listview_lyricshow.setAdapter(mAdapter);
		play_music_listview_lyricshow.setEmptyView(play_music_emptylyric);
//		play_music_listview_lyricshow.startAnimation(AnimationUtils.loadAnimation(this,
//				android.R.anim.fade_in));
		imageLoader = new MyImageLoader(getResources().getDisplayMetrics().heightPixels,getResources().getDisplayMetrics().widthPixels);
	}
	
	/**
	 * 初始化view
	 */
	private void initView(){
		play_music_title = (TextView) findViewById(R.id.play_music_name);
		play_music_artist = (TextView) findViewById(R.id.play_music_singer);
		play_music_album = (TextView) findViewById(R.id.play_music_album);
		
		play_music_pre = (Button) findViewById(R.id.play_music_pre);
		play_music_next = (Button) findViewById(R.id.play_music_next);
		play_music_pause = (Button) findViewById(R.id.play_music_play);
		play_music_pre.setOnClickListener(this);
		play_music_next.setOnClickListener(this);
		play_music_pause.setOnClickListener(this);
		
		play_music_curtime = (TextView) findViewById(R.id.play_music_curtime);
		play_music_endtime = (TextView) findViewById(R.id.play_music_endtime);
		
		play_music_cursong = (TextView) findViewById(R.id.play_music_cursong);
		play_music_totalsong = (TextView) findViewById(R.id.play_music_totalsong);
		
		play_music_progressBar = (SeekBar) findViewById(R.id.play_music_seekbar);
		play_music_progressBar.setMax(300);
		
		play_music_progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(mBinder!=null&&mMusic!=null){
					mBinder.seekToSpecifiedPosition(seekBar.getProgress()* (int) mMusic.getDuration()/ seekBar.getMax());
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
				if(arg2&&mMusic!=null){
					play_music_curtime.setText(TimeHelper.milliSecondsToFormatTimeString(
							progress* ((int)(mMusic.getDuration()*1.0/play_music_progressBar.getMax()))));
				}
			}
		});
		
		play_music_playmode = (ImageButton) findViewById(R.id.play_music_playmode);
		play_music_playmode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mBinder.changePlayMode();
			}
		});
		
		
		play_music_opencurrentlist =(ImageButton) findViewById(R.id.play_music_opencurrentlist);
		play_music_opencurrentlist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//打开当前的播放列表
				Intent intent = new Intent(PlayMusic.this,PlayMusicQueue.class);
				startActivity(intent);
			}
		});
		
		play_music_listview_lyricshow = (ListView) findViewById(R.id.play_music_listview_lyricshow);
		
		play_music_emptylyric = (TextView) findViewById(R.id.play_music_lyric_empty);
		
		
		play_music_content = (ImageView) findViewById(R.id.play_music_content);
		
	}
	
	/**
	 * 返回
	 * @param view
	 */
	public void playMusicClick(View view){
		finish();
	}
	
	/**
	 * 初始化当前的播放信息
	 * @param bundle 含有音乐播放信息的Bundle
	 * @date 2015年8月30日11:50:47
	 */
	private void initCurrentPlayMusicInfo(Bundle bundle){
		int currentPlayState = bundle.getInt(Constant.PLAYING_MUSIC_STATE);
	
		if(currentPlayState==PlayState.Stopped){
			mBinder.setCurrentPlayList(playMusicList);
			int sharePosition = PreferHelper.readInt(PlayMusic.this, Constant.SHARE_NMAE_MUSIC,
					Constant.SHARE_NMAE_MUSIC_POSITION, -1);
			if(sharePosition!=-1){
				play_music_title.setText(playMusicList.get(sharePosition).getTitle());
				play_music_artist.setText(playMusicList.get(sharePosition).getArtist());

				play_music_album.setText(playMusicList.get(sharePosition).getAlbum());
				isPlaying =false;
				play_music_pause.setBackgroundResource(R.drawable.play_music_play_sele);
				play_music_cursong.setText((sharePosition+1)+"");
			}
		}
		
		
		ArrayList<Music> currentPlayMusicList = bundle.getParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST);
		playMusicList = currentPlayMusicList;
		mMusic = bundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
		
		int currentPlayProgress = bundle.getInt(Constant.PLAYING_MUSIC_PROGRESS);
		int currentPlayPosition = bundle.getInt(Constant.PLAYING_MUSIC_POSITION_IN_LIST);
		int currentPlayMode = bundle.getInt(Constant.PLAYING_MUSIC_PLAYMODE);

		if(currentPlayState==PlayState.Playing||currentPlayState==PlayState.Prepraing){
			play_music_pause.setBackgroundResource(R.drawable.play_music_pause_sele);
			isPlaying=true;
		}else if(currentPlayState!=PlayState.Stopped){
			isPlaying =false;
			play_music_pause.setBackgroundResource(R.drawable.play_music_play_sele);
		}
		if(mMusic!=null){
			Log.i(TAG, mMusic.toString());
			play_music_title.setText(mMusic.getTitle());
			play_music_artist.setText(mMusic.getArtist());

			play_music_album.setText(mMusic.getAlbum());
			
			play_music_endtime.setText(TimeHelper.milliSecondsToFormatTimeString(mMusic.getDuration()));
			play_music_curtime.setText(TimeHelper.milliSecondsToFormatTimeString(currentPlayProgress));
			play_music_progressBar.setProgress((int)(currentPlayProgress*1.0/mMusic.getDuration() *play_music_progressBar.getMax()));
		}
		
		play_music_cursong.setText((currentPlayPosition+1)+"");
		
		
		play_music_totalsong.setText(currentPlayMusicList.size()+"");
		setCurrentPlayModeBg(currentPlayMode);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//绑定音乐播放的服务
		boolean isbind =bindService(new Intent(PlayMusic.this, MusicService.class), mServiceConn, Context.BIND_AUTO_CREATE);
		
		Log.v(TAG, "绑定"+isbind);
	}
	
	
	@Override
	public void onMuiscPlayed(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMuiscPlayed");
		isPlaying =true;
		play_music_pause.setBackgroundResource(R.drawable.play_music_pause_sele);

		if(imageLoader!=null){
			imageLoader.toLoadingLocalPic();
		}
	}


	@Override
	public void onMusicPaused(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMusicPaused");
		isPlaying =false;
		play_music_pause.setBackgroundResource(R.drawable.play_music_play_sele);
		
		if(imageLoader!=null){
			imageLoader.cancleLoading();
		}
	}

	@Override
	public void onMusicStoped() {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMusicStoped");
		isPlaying=false;
		mAdapter.setLyric(null);
		mAdapter.notifyDataSetChanged();
		mMusic=null;
		
		if(imageLoader!=null){
			imageLoader.cancleLoading();
		}
	}

	@Override
	public void onPlayModeChanged(int playMode) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onPlayModeChanged");
		setCurrentPlayModeBg(playMode);
	}

	@Override
	public void onNewSongPlayed(Music music,int position) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMuiscPlayed");
		mMusic = music;
		play_music_title.setText(mMusic.getTitle());
		play_music_artist.setText(mMusic.getArtist());

		play_music_album.setText(mMusic.getAlbum());
		
		play_music_cursong.setText((position+1)+"");
		
		play_music_endtime.setText(TimeHelper.milliSecondsToFormatTimeString(mMusic.getDuration()));
	
//		mAdapter.setLyric(null);
//		mAdapter.notifyDataSetChanged();
		// 歌词秀清空
		play_music_emptylyric.setText("正在加载中...");
		play_music_content.setImageDrawable(getResources().getDrawable(R.drawable.player_bg));
		
		if(imageLoader!=null&&mMusic!=null){
			//加载图片
			imageLoader.setAlphaImageView(play_music_content,mMusic.getArtist());
		}
	}

	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		play_music_curtime.setText(TimeHelper.milliSecondsToFormatTimeString(currenMillis));
		play_music_progressBar.setProgress((int)(currenMillis*1.0/mMusic.getDuration() *play_music_progressBar.getMax()));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(this);
		}
		if(mServiceConn!=null){
			unbindService(mServiceConn);
		}
	
		if(imageLoader!=null){
			imageLoader.cancleLoading();
		}
	}
	
	
	private void setCurrentPlayModeBg(int playMode){
		switch (playMode) {
		case PlayMode.REPEAT:
			play_music_playmode.setImageResource(R.drawable.button_playmode_repeat);
			Toast.makeText(PlayMusic.this, "列表循环", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.REPEAT_SINGLE:
			play_music_playmode.setImageResource(R.drawable.button_playmode_repeat_single);
			Toast.makeText(PlayMusic.this, "单曲循环", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.SEQUENTIAL:
			play_music_playmode.setImageResource(R.drawable.button_playmode_sequential);
			Toast.makeText(PlayMusic.this, "顺序播放", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.SHUFFLE:
			play_music_playmode.setImageResource(R.drawable.button_playmode_shuffle);
			Toast.makeText(PlayMusic.this, "随机播放", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.play_music_pre://播放上一首
			mBinder.playPre();
			break;
		case R.id.play_music_play://播放or暂停
			if(isPlaying){
				mBinder.playPause();
			}else{
				mBinder.playToPlay();
			}
			break;
		case R.id.play_music_next://播放下一首
			mBinder.playNext();
			break;
		default:
			break;
		}
	}

	private LyricListener mLyricListener = new LyricListener() {

		@Override
		public void onLyricLoaded(List<LyricSentence> lyricSentences, int index) {
			Log.i(TAG, "onLyricLoaded");
			if (lyricSentences != null) {
				Log.i(TAG, "onLyricLoaded--->歌词句子数目=" + lyricSentences.size()
						+ ",当前句子索引=" + index);
				mAdapter.setLyric(lyricSentences);
				mAdapter.setCurrentSentenceIndex(index);
				mAdapter.notifyDataSetChanged();
				// 本方法执行时，lyricshow的控件还没有加载完成，所以延迟下再执行相关命令
				mHandler.sendMessageDelayed(
						Message.obtain(null, MSG_SET_LYRIC_INDEX, index, 0),
						100);
			}
			
		}

		@Override
		public void onLyricSentenceChanged(int indexOfCurSentence) {
			Log.i(TAG, "onLyricSentenceChanged--->当前句子索引=" + indexOfCurSentence);
			mAdapter.setCurrentSentenceIndex(indexOfCurSentence);
			mAdapter.notifyDataSetChanged();
			play_music_listview_lyricshow
					.smoothScrollToPositionFromTop(indexOfCurSentence,
							play_music_listview_lyricshow.getHeight() / 2, 500);
		}
	};

}
