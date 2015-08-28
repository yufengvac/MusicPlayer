package com.vac.musicplayer;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;

public class PlayMusic extends Activity implements OnPlayMusicStateListener{


	private static final String TAG = PlayMusic.class.getSimpleName();
	private MusicServiceBinder mBinder = null;
	private List<Music> playMusicList=null;

	private ServiceConnection mServiceConn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.i(TAG, "serviceConnected");
			mBinder = (MusicServiceBinder) binder;
		}
	};
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		
		playMusicList = (List<Music>) getIntent().getSerializableExtra(Constant.PLAYLIST_MUISC);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//绑定音乐播放的服务
		bindService(new Intent(PlayMusic.this, MusicService.class), mServiceConn, Context.BIND_AUTO_CREATE);
		if(mBinder!=null){
			mBinder.setCurrentPlayList(playMusicList);
		}
	}
	
	
	@Override
	public void onMuiscPlayed() {
		
	}

	@Override
	public void onMusicPaused() {
		
	}

	@Override
	public void onMusicStoped() {
		
	}

	@Override
	public void onPlayModeChanged(int playMode) {
		
	}

	@Override
	public void onNewSongPlayed(Music music) {
		
	}

	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		
	}
}
