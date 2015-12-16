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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.fragment.MyMusicFra;
import com.vac.musicplayer.fragment.NetMusicFra;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.listener.OnSkinChangerListener;
import com.vac.musicplayer.loader.MusicLoader;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.PreferHelper;

public class Main extends FragmentActivity implements OnSkinChangerListener,OnPlayMusicStateListener{
	private final static String TAG = Main.class.getSimpleName();
	private static final int PRIVATE_LOCAL_MUSIC=100;
	private ViewPager viewPager;
	private MyFragmentPagerAdapter mAdapter;
	private List<Fragment> fraList = new ArrayList<Fragment>();
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	private RadioButton rb_localmusic,rb_netmusic;
	private RadioGroup rg_navigation;
	private LinearLayout content;
	private ProgressBar main_progressbar;
	
	private int SharePosition = -2;
	private Music mMusic;
	private TextView song_name_textview,artist_name_textview;
	private ImageView main_play_imageview;
	
	private MusicServiceBinder mBinder=null;
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
			
			mBinder.registerOnPlayMusicStateListener(Main.this);
			toLoadMusic();
			/*if(currentMusicBundle!=null){
				Log.d(TAG, "在此onServiceConnected设置currentMusicBundle");
				Music mMusic = null;
				
				int currentPlayState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
				Log.v(TAG, "LocalMusicFragment.class====+currentPlayState==="+currentPlayState);
				if(currentPlayState==PlayState.Stopped){
					int SharePosition = PreferHelper.readInt(Main.this, Constant.SHARE_NMAE_MUSIC,
							Constant.SHARE_NMAE_MUSIC_POSITION, -1);
//					mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,SharePosition);
//					int offest = listView.getHeight();
//					listView.setSelectionFromTop(SharePosition,offest/2);
				}
				
				
				mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
				
//				if(mMusic!=null){
//					int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
//					Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
//					if(currentPlayState==PlayState.Playing){
//						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_START,currentPlayPosition);
//					}else{
//						mAdapter.setSpecifiedIndicator(MusicListAdapter.ANIMATION_PAUSE,currentPlayPosition);
//					}
//					int offest = listView.getHeight();
//					listView.setSelectionFromTop(currentPlayPosition,offest/2);
//				}
			}*/
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.main_viewPager);
		
		MyMusicFra musicFra = new MyMusicFra();
		fraList.add(musicFra);
		NetMusicFra netMusicFra = new NetMusicFra();
		fraList.add(netMusicFra);
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		
		rb_localmusic = (RadioButton) findViewById(R.id.main_rb_localmusic);
		rb_netmusic = (RadioButton) findViewById(R.id.main_rb_netmusic);
		
		rg_navigation = (RadioGroup) findViewById(R.id.main_rg_navigation);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				rb_localmusic.setChecked(false);
				rb_netmusic.setChecked(false);
				if (arg0==0) {
					rb_localmusic.setChecked(true);
				}else if(arg0==1){
					rb_netmusic.setChecked(true);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		rg_navigation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				rb_localmusic.setChecked(false);
				rb_netmusic.setChecked(false);
					if (arg1==R.id.main_rb_localmusic) {
						rb_localmusic.setChecked(true);
						viewPager.setCurrentItem(0);
					}else if(arg1==R.id.main_rb_netmusic){
						rb_netmusic.setChecked(true);
						viewPager.setCurrentItem(1);
					}
			}
		});
		content = (LinearLayout) findViewById(R.id.main_titlebar_content);
		String urlAndColor = PreferHelper.readString(Main.this, Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
		if (urlAndColor!=null) {
			int colorValue = Integer.parseInt(urlAndColor.split(",")[1]);
			content.setBackgroundColor(colorValue);
		}
		
		main_progressbar = (ProgressBar) findViewById(R.id.main_progressbar);
		main_progressbar.setMax(300);
		
		song_name_textview = (TextView) findViewById(R.id.main_song_name);
		artist_name_textview = (TextView) findViewById(R.id.main_song_artist);
		
		main_play_imageview = (ImageView) findViewById(R.id.main_play_imageview);
	}
	
	private class MyFragmentPagerAdapter extends FragmentPagerAdapter{

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fraList.get(arg0);
		}

		@Override
		public int getCount() {
			return fraList.size();
		}
		
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
			bindService(new Intent(this,MusicService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
		}
	}
	@Override
	public void onSkinChange(int coloValue,String url) {
		content.setBackgroundColor(coloValue);
	}
	@Override
	protected void onStop() {
		super.onStop();
		if(mBinder!=null){
			mBinder.unRegisterPlayMusicStateListener(this);
		}
		if(mServiceConnection!=null){
			Log.v(TAG, "LocalMusicFragment中解绑服务");
			unbindService(mServiceConnection);
		}
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
//						mAdapter.setData(data);
//						musicTotalListener.musicTotalCount(data.size());//设置音乐的总数，接口回调给宿主MainActivity.class
						mBinder.setCurrentPlayList(data);
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
	public void playSong(View view){
		if (SharePosition==-1) {
			playSongInPosition(mCurrentMusicList.get(0).getId());
		}else{
			playSongInPosition(mCurrentMusicList.get(SharePosition).getId());
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
		if (mMusic==null) {
			Log.e(TAG, "onPlayProgressUpdate-->mMusic是空的");
		}
		Log.e(TAG, "currenMillis="+currenMillis+",mMusic.getDuration()="+mMusic.getDuration()+""
				+ ","+(int)(currenMillis*1.0/mMusic.getDuration() *main_progressbar.getMax()));
		main_progressbar.setProgress((int)(currenMillis*1.0/mMusic.getDuration() *main_progressbar.getMax()));
	}
}
