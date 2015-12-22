package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vac.musicplayer.R.color;
import com.vac.musicplayer.adapter.MusicListQueueAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayMode;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.PreferHelper;

public class PlayMusicQueue extends Activity implements OnPlayMusicStateListener{

	private TextView play_queue_name;
	private ImageButton play_queue_playmode;
	private ListView play_queue_listView;
	private MusicServiceBinder mBinder=null;
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	private MusicListQueueAdapter mAdapter =null;
	private String music_list_type;
	private int colorValue=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music_queue);
		if (getIntent().getIntExtra("color", -1)!=-1) {
			colorValue = getIntent().getIntExtra("color", -1);
		}else{
			String urlAndColor = PreferHelper.readString(this.getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
			if (urlAndColor!=null) {
				String[] array = urlAndColor.split(",");
				colorValue= Integer.parseInt(array[1]);
			}else{
				colorValue = Color.rgb(249, 96, 98);
			}
		}
		initView();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent(PlayMusicQueue.this,MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	
	private void initView(){
		play_queue_name = (TextView) findViewById(R.id.play_music_queue_name);
		play_queue_playmode = (ImageButton) findViewById(R.id.play_music_queue_playmode);
		play_queue_listView = (ListView) findViewById(R.id.play_music_queue_listView);
		
		play_queue_playmode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mBinder!=null){
					mBinder.changePlayMode();
				}
			}
		});
		
		play_queue_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(PlayMusicQueue.this,MusicService.class);
				intent.setAction(MusicService.ACTION_PLAY);
				intent.putExtra(Constant.CLICK_MUSIC_LIST, true);
				intent.putExtra(Constant.PLAYLIST_MUSIC_REQUEST_ID, mAdapter.getItemId(position));
				intent.putExtra(Constant.MUSIC_LIST_TYPE, music_list_type);
				startService(intent);
			}
		});
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		View view = getWindow().getDecorView();
		view.setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.gravity = Gravity.BOTTOM|Gravity.RIGHT;
//		lp.x = getResources().getDimensionPixelSize(R.dimen.playqueue_dialog_marginright);
//		lp.y = getResources().getDimensionPixelSize(R.dimen.playqueue_dialog_marginbottom);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = getResources().getDisplayMetrics().heightPixels/2;
		getWindowManager().updateViewLayout(view, lp);
	}
	
	public void close(View view){
		finish();
		this.overridePendingTransition(R.anim.push_bottom_out, 0);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			finish();
			this.overridePendingTransition(R.anim.push_bottom_out, 0);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(PlayMusicQueue.this);
		}
		if(mServiceConnection!=null){
			unbindService(mServiceConnection);
		}
	}
	
	private  ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			mBinder = (MusicServiceBinder) arg1;
			mBinder.registerOnPlayMusicStateListener(PlayMusicQueue.this);
			Bundle bundle = mBinder.getCurrentPlayMusicInfo();
			
			initPlayQueueInfo(bundle);
		}
	};
	
	private void initPlayQueueInfo(Bundle bundle){
		mCurrentMusicList.clear();
		mCurrentMusicList = bundle.getParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST);
		mAdapter = new MusicListQueueAdapter(PlayMusicQueue.this, mCurrentMusicList,mBinder);
		if (colorValue!=-1) {
			mAdapter.setColor(colorValue);
		}
		play_queue_listView.setAdapter(mAdapter);
		
		music_list_type = bundle.getString(Constant.MUSIC_LIST_TYPE);
		play_queue_name.setText(music_list_type);
		
		int currentPlayState = bundle.getInt(Constant.PLAYING_MUSIC_STATE);
		Music music= bundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
		if(currentPlayState!=PlayState.Stopped&&mCurrentMusicList.size()>0&&music!=null){
			int position = MusicService.findPositionByMusicId(mCurrentMusicList, music.getId());
			if(currentPlayState==PlayState.Playing){
				mAdapter.setFlagInPosition(currentPlayState, position);
			}else if(currentPlayState == PlayState.Paused){
				mAdapter.setFlagInPosition(currentPlayState, position);
			}
			
		
			if (position!= -1) {
				play_queue_listView.setSelectionFromTop(position,getResources().getDimensionPixelSize(R.dimen.playqueue_dialog_select_item_from_top));
			}
		}else if(currentPlayState==PlayState.Stopped&&mCurrentMusicList.size()>0){
			int sharePosition = PreferHelper.readInt(PlayMusicQueue.this, Constant.SHARE_NMAE_MUSIC,
					Constant.SHARE_NMAE_MUSIC_POSITION, -1);
			mAdapter.setFlagInPosition( PlayState.Paused, sharePosition);
		}
		
	}

	@Override
	public void onMuiscPlayed(Music music) {
		mAdapter.setFlagInPosition(PlayState.Playing,MusicService.findPositionByMusicId(mCurrentMusicList, music.getId()));
	}

	@Override
	public void onMusicPaused(Music music) {
		mAdapter.setFlagInPosition(PlayState.Paused,MusicService.findPositionByMusicId(mCurrentMusicList, music.getId()));
	}

	@Override
	public void onMusicStoped() {
	}

	@Override
	public void onPlayModeChanged(int playMode) {
		setCurrentPlayModeBg(playMode);
	}

	@Override
	public void onNewSongPlayed(Music music, int position) {
		mAdapter.setFlagInPosition(PlayState.Playing,MusicService.findPositionByMusicId(mCurrentMusicList, music.getId()));
	}

	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		
	}
	
	private void setCurrentPlayModeBg(int playMode){
		switch (playMode) {
		case PlayMode.REPEAT:
			play_queue_playmode.setImageResource(R.drawable.button_playmode_repeat_blue);
//			Toast.makeText(PlayMusic.this, "列表循环", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.REPEAT_SINGLE:
			play_queue_playmode.setImageResource(R.drawable.button_playmode_repeat_single_blue);
//			Toast.makeText(PlayMusic.this, "单曲循环", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.SEQUENTIAL:
			play_queue_playmode.setImageResource(R.drawable.button_playmode_sequential_blue);
//			Toast.makeText(PlayMusic.this, "顺序播放", Toast.LENGTH_SHORT).show();
			break;
		case PlayMode.SHUFFLE:
			play_queue_playmode.setImageResource(R.drawable.button_playmode_shuffle_blue);
//			Toast.makeText(PlayMusic.this, "随机播放", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}




