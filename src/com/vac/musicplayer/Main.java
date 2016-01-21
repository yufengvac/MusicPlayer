package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.bean.NetParam;
import com.vac.musicplayer.fragment.TabMainFra;
import com.vac.musicplayer.fragment.localmusic.ArtistFragment;
import com.vac.musicplayer.fragment.localmusic.TabMusicLocalFra;
import com.vac.musicplayer.fragment.recentmusicplay.RecentMusicPlayFra;
import com.vac.musicplayer.fragment.search.SearchFragment;
import com.vac.musicplayer.fragment.search.detail.SearchAlbumDetailFra;
import com.vac.musicplayer.listener.OnPageAddListener;
import com.vac.musicplayer.listener.OnPlayMusicStateListener;
import com.vac.musicplayer.listener.OnSkinChangerListener;
import com.vac.musicplayer.loader.MusicLoader;
import com.vac.musicplayer.myview.MyMenuButton;
import com.vac.musicplayer.myview.MyPauseButton;
import com.vac.musicplayer.myview.MyProgressbar;
import com.vac.musicplayer.myview.MyTriangle;
import com.vac.musicplayer.service.MusicService;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;
import com.vac.musicplayer.utils.HttpUtils;
import com.vac.musicplayer.utils.PreferHelper;

public class Main extends FragmentActivity implements OnPlayMusicStateListener,OnPageAddListener,OnSkinChangerListener{
	private final static String TAG = Main.class.getSimpleName();
	private static final int PRIVATE_LOCAL_MUSIC=100;
	
	private List<Music> mCurrentMusicList = new ArrayList<Music>();
	
	private MyProgressbar main_progress;
	
	private int SharePosition = -2;
	private Music mMusic;
	private TextView song_name_textview,artist_name_textview;
//	private ImageView main_play_imageview;
	
	/**播放按钮*/
	private MyTriangle myTriangle;
	
	/**暂停按钮*/
	private MyPauseButton myPausebtn;
	
	/**菜单*/
	private MyMenuButton myMenubtn;
	
	private MusicServiceBinder mBinder=null;
	private Bundle currentMusicBundle = null;
	
	private TabMainFra tmf ;
	
	private int currentColor;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==HttpUtils.NETSUCCESS) {
				try {
					String result = (String) msg.obj;
					JSONObject obj = new JSONObject(result);
					if (obj.getInt("rows")>=1) {
						JSONArray array = obj.getJSONArray("data");
						if (array.length()>=1) {
							JSONObject obj_ = array.getJSONObject(0);
							String picUrl = obj_.getString("pic_url");
							mImageLoader.displayImage(picUrl, singerLogoImageView);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		};
	};
	
	
	private FragmentManager fm;
	
	private ImageView singerLogoImageView;
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
//			toLoadMusic();
			Music mMusic = null;
			currentMusicBundle = mBinder.getCurrentPlayMusicInfo();
			if (currentMusicBundle!=null) {
				int playState = currentMusicBundle.getInt(Constant.PLAYING_MUSIC_STATE);
				if (playState==MusicService.PlayState.Playing||playState == MusicService.PlayState.Paused) {
					mMusic = currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
					song_name_textview.setText(mMusic.getTitle());
					artist_name_textview.setText(mMusic.getArtist());
					ArrayList<Music> list = currentMusicBundle.getParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST);
					mCurrentMusicList.clear();
					mCurrentMusicList.addAll(list);
				}else{
					toLoadMusic();
				}
			}
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
		fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		tmf = new TabMainFra();
//		ft.add(R.id.main_content, tmf);
//		ft.show(tmf);
//		ft.commit();
//		mContent = tmf;
		ft.replace(R.id.main_content, tmf).commit();
		
		String urlAndColor = PreferHelper.readString(this.getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
		if (urlAndColor!=null) {
			String[] array = urlAndColor.split(",");
			currentColor= Integer.parseInt(array[1]);
		}else{
			currentColor = Color.rgb(249, 96, 98);
		}
		myTriangle.setColor(currentColor);
		myPausebtn.setColor(currentColor);
		myMenubtn.setColor(currentColor);
		main_progress.setProgressColor(currentColor);
		
	}
	private void initView() {
	
		main_progress= (MyProgressbar) findViewById(R.id.main_progressbar);
		
		song_name_textview = (TextView) findViewById(R.id.main_song_name);
		artist_name_textview = (TextView) findViewById(R.id.main_song_artist);
		
//		main_play_imageview = (ImageView) findViewById(R.id.main_play_imageview);
		myTriangle = (MyTriangle) findViewById(R.id.main_play_mytriangle);
		
		myPausebtn = (MyPauseButton) findViewById(R.id.main_play_pause_mypausebtn);
		
		myMenubtn = (MyMenuButton) findViewById(R.id.main_menu_mymenubtn);
		
		singerLogoImageView = (ImageView) findViewById(R.id.main_singer_logo);
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
	public void onSkinChange(int coloValue,String url) {
		Log.i(TAG, "onSkinChange-->"+coloValue);
		currentColor = coloValue;
		myTriangle.setColor(coloValue);
		myPausebtn.setColor(coloValue);
		myMenubtn.setColor(coloValue);
		main_progress.setProgressColor(coloValue);
	}
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
						if (currentMusicBundle==null||currentMusicBundle.getParcelableArrayList(Constant.PLAYING_MUSIC_CURRENT_LIST).size()==0) {
							mBinder.setCurrentPlayList(data);
							mCurrentMusicList.addAll(data);
							Log.i(TAG, "main中的音乐列表数是："+mCurrentMusicList.size());
						}
//						((TextView)(fraList.get(0).getView().findViewById(R.id.my_music_fra_totalnumber))).setText(data.size()+"首");
//						List<Fragment> FraList = getSupportFragmentManager().getFragments();
//						for (int i = 0; i < FraList.size(); i++) {
//							
//						}
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
							if (currentPlayState==PlayState.Playing||currentPlayState==PlayState.Paused) {
								mMusic =currentMusicBundle.getParcelable(Constant.PLAYING_MUSIC_ITEM);
								song_name_textview.setText(mMusic.getTitle());
								artist_name_textview.setText(mMusic.getArtist());
								searchSingerPic(mMusic.getArtist());
							}else{
								try {
									song_name_textview.setText(mCurrentMusicList.get(SharePosition).getTitle());
									artist_name_textview.setText(mCurrentMusicList.get(SharePosition).getArtist());
									searchSingerPic(mCurrentMusicList.get(SharePosition).getArtist());
								} catch (IndexOutOfBoundsException e) {
									song_name_textview.setText(mCurrentMusicList.get(mCurrentMusicList.size()-1).getTitle());
									artist_name_textview.setText(mCurrentMusicList.get(mCurrentMusicList.size()-1).getArtist());
									searchSingerPic(mCurrentMusicList.get(mCurrentMusicList.size()-1).getArtist());
								}
								
							}
						}
						
						
						if(mMusic!=null){
							int currentPlayPosition = MusicService.findPositionByMusicId(mCurrentMusicList,mMusic.getId());
							Log.v(TAG, "currentPlayState="+currentPlayState+",Position="+currentPlayPosition);
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
	 * @description 搜索歌手图片
	 * @param artist
	 */
	protected void searchSingerPic(String artist) {
		HttpUtils httpUtils = new HttpUtils(Main.this, mHandler);
		ArrayList<NetParam> params = new ArrayList<NetParam>();
		params.add(new NetParam("q", ""+artist));
		httpUtils.get(Constant.SEARCH_SINGER,params, true, true);
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
			try {
				int share = PreferHelper.readInt(Main.this, Constant.SHARE_NMAE_MUSIC,
						Constant.SHARE_NMAE_MUSIC_POSITION, -1);
				playSongInPosition(mCurrentMusicList.get(share).getId());
			} catch (IndexOutOfBoundsException e) {
				playSongInPosition(mCurrentMusicList.get(mCurrentMusicList.size()-1).getId());
			}
			
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
		intent.putExtra("color", currentColor);
		startActivity(intent);
	}
	
	@Override
	public void onMuiscPlayed(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMuiscPlayed");
		mMusic = music;
		song_name_textview.setText(mMusic.getTitle());
		artist_name_textview.setText(mMusic.getArtist());
//		main_play_imageview.setImageResource(R.drawable.img_media_controller_play);
		myTriangle.setVisibility(View.GONE);
		myPausebtn.setVisibility(View.VISIBLE);
		 searchSingerPic(mMusic.getArtist());
	}
	@Override
	public void onMusicPaused(Music music) {
		Log.v(TAG, "PlayMusic-onPlayMusicStateListener--onMusicPaused");
//		main_play_imageview.setImageResource(R.drawable.img_media_controller_pause);
		myTriangle.setVisibility(View.VISIBLE);
		myPausebtn.setVisibility(View.GONE);
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
		searchSingerPic(mMusic.getArtist());
	}
	@Override
	public void onPlayProgressUpdate(long currenMillis) {
		if (main_progress!=null&&mMusic!=null) {
			main_progress.setProgress((int)(currenMillis*1.0/mMusic.getDuration() *main_progress.getMax()));
		}
	}
	@Override
	public void onPageAddListener(int type,Bundle b) {
		switch (type) {
		case OnPageAddListener.TABMUSICLOCALFRA:
			Log.d(TAG, "onPageAddListener-->"+OnPageAddListener.TABMUSICLOCALFRA);
			TabMusicLocalFra tmlf = new TabMusicLocalFra();
			Bundle bundle = new Bundle();
			bundle.putInt("color", currentColor);
			tmlf.setArguments(bundle);
			tmlf.setPageAddListener(this);
			FragmentTransaction transaction = fm.beginTransaction();
				transaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_in, 
					R.anim.push_right_out, R.anim.push_right_out);
			transaction.replace(R.id.main_content, tmlf).addToBackStack(null).commit();
			break;
		case OnPageAddListener.SONGOFSINGERARTIST:
			Log.d(TAG, "onPageAddListener-->"+OnPageAddListener.SONGOFSINGERARTIST);
			ArtistFragment af = new ArtistFragment();
			af.setArguments(b);
			FragmentTransaction transaction1 = fm.beginTransaction();
			transaction1.replace(R.id.main_content, af).addToBackStack(null).commit();
			break;
		case OnPageAddListener.SEARCHFRAGMENT:
			Log.d(TAG, "onPageAddListener-->"+OnPageAddListener.SEARCHFRAGMENT);
			SearchFragment sf = new SearchFragment();
			Bundle sf_b = new Bundle();
			sf_b.putInt("color", currentColor);
			sf.setArguments(sf_b);
			FragmentTransaction transaction2 = fm.beginTransaction();
			transaction2.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_in, 
					R.anim.push_right_out, R.anim.push_right_out);
			transaction2.replace(R.id.main_content, sf).addToBackStack(null).commit();
			break;
		case OnPageAddListener.SEARCHALBUMDETAIL:
			SearchAlbumDetailFra sadf = new SearchAlbumDetailFra();
			b.putInt("color", currentColor);
			sadf.setArguments(b);
			fm.beginTransaction().replace(R.id.main_content, sadf)
			.addToBackStack(null).commit();
			break;
		case OnPageAddListener.RECENTPLAYMUSIC:
			RecentMusicPlayFra rmpf = new RecentMusicPlayFra();
			Bundle bundle_recent = new Bundle();
			bundle_recent.putInt("color", currentColor);
			rmpf.setBinder(mBinder);
			rmpf.setArguments(bundle_recent);
			fm.beginTransaction().replace(R.id.main_content, rmpf)
			.addToBackStack(null).commit();
			break;
		default:
			break;
		}
	}
/*	private void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(
                    R.anim.push_right_in, R.anim.push_right_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.main_content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			int count= fm.getBackStackEntryCount();
			Log.v(TAG, "回退栈中已经有了"+count+"个Fragment");
		}
		return super.onKeyDown(keyCode, event);
	}
}
