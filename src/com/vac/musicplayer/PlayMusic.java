package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.TimeHelper;

public class PlayMusic extends Activity implements OnPlayMusicStateListener,OnClickListener{


	private static final String TAG = PlayMusic.class.getSimpleName();
	private MusicServiceBinder mBinder = null;
	private List<Music> playMusicList=null;
	
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

	private ServiceConnection mServiceConn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i(TAG, "serviceConnected");
			mBinder = (MusicServiceBinder) binder;
			mBinder.registerOnPlayMusicStateListener(PlayMusic.this);
			
			/**初始化当前的播放信息*/
			initCurrentPlayMusicInfo(mBinder.getCurrentPlayMusicInfo());
		}
	};
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		initView();
		playMusicList = (List<Music>) getIntent().getSerializableExtra(Constant.PLAYLIST_MUISC);
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

		mMusic = bundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
		
		int currentPlayProgress = bundle.getInt(Constant.PLAYING_MUSIC_PROGRESS);
		int currentPlayPosition = bundle.getInt(Constant.PLAYING_MUSIC_POSITION_IN_LIST);
		
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
			if(mMusic.getArtist().equals("<unknown>")){
				play_music_artist.setText("未知艺术家");
			}else{
				play_music_artist.setText(mMusic.getArtist());
			}
			play_music_album.setText(mMusic.getAlbum());
			
			play_music_endtime.setText(TimeHelper.milliSecondsToFormatTimeString(mMusic.getDuration()));
			play_music_curtime.setText(TimeHelper.milliSecondsToFormatTimeString(currentPlayProgress));
		}
		
		play_music_cursong.setText(currentPlayPosition+"");
		
		ArrayList<Music> currentPlayMusicList = bundle.getParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST);
		play_music_totalsong.setText(currentPlayMusicList.size()+"");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//绑定音乐播放的服务
		boolean isbind =bindService(new Intent(PlayMusic.this, MusicService.class), mServiceConn, Context.BIND_AUTO_CREATE);
		if(mBinder!=null){
			mBinder.setCurrentPlayList(playMusicList);
		}
		
		Log.v(TAG, "绑定"+isbind);
	}
	
	
	@Override
	public void onMuiscPlayed(Music music) {
		Log.v(TAG, "onPlayMusicStateListener--onMuiscPlayed");
		isPlaying =true;
		play_music_pause.setBackgroundResource(R.drawable.play_music_pause_sele);
	}


	@Override
	public void onMusicPaused(Music music) {
		Log.v(TAG, "onPlayMusicStateListener--onMusicPaused");
		isPlaying =false;
		play_music_pause.setBackgroundResource(R.drawable.play_music_play_sele);
	}

	@Override
	public void onMusicStoped() {
		Log.v(TAG, "onPlayMusicStateListener--onMusicStoped");
		isPlaying=false;
	}

	@Override
	public void onPlayModeChanged(int playMode) {
		Log.v(TAG, "onPlayMusicStateListener--onPlayModeChanged");
	}

	@Override
	public void onNewSongPlayed(Music music) {
		Log.v(TAG, "onPlayMusicStateListener--onMuiscPlayed");
		mMusic = music;
		play_music_title.setText(mMusic.getTitle());
		if(mMusic.getArtist().equals("<unknown>")){
			play_music_artist.setText("未知艺术家");
		}else{
			play_music_artist.setText(mMusic.getArtist());
		}
		play_music_album.setText(mMusic.getAlbum());
		
		play_music_endtime.setText(TimeHelper.milliSecondsToFormatTimeString(mMusic.getDuration()));
	}

	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		play_music_curtime.setText(TimeHelper.milliSecondsToFormatTimeString(currenMillis));
		play_music_progressBar.setProgress((int)(currenMillis*1.0/mMusic.getDuration() *play_music_progressBar.getMax()));
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConn);
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


}
