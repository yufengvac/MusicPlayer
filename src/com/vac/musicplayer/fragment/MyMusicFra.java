package com.vac.musicplayer.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.Main;
import com.vac.musicplayer.R;
import com.vac.musicplayer.R.drawable;
import com.vac.musicplayer.adapter.MyFavorSongListAdapter;
import com.vac.musicplayer.application.MyApplication;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.MusicGroup;
import com.vac.musicplayer.db.MusicGroupDao;
import com.vac.musicplayer.dialogactivity.ChangeSkinDialogActivity;
import com.vac.musicplayer.listener.OnSkinChangerListener;
import com.vac.musicplayer.myview.ListViewForScrollView;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.utils.PreferHelper;
/**
 * @title MyMusicFra
 * @description 我的音乐Fragment
 * @author vac
 * @date 2015年12月10日16:36:06
 *
 */
public class MyMusicFra extends Fragment implements OnClickListener , OnSkinChangerListener{

	private RelativeLayout content1,content2,content3,content4;
	private LinearLayout favor_content;
	private RelativeLayout changeSkinRela; 
	private ImageView my_music_bg,my_music_bg1;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private Animation appAnim_fade_oute,appAnim_fade_in;
	private ImageView img;
	private String lastUrl ="";
	private OnSkinChangerListener mSkinChangeListener = null;
	private ListViewForScrollView mFavorListview;
	private TextView createlist_textview;
	private MyFavorSongListAdapter mFavorAdapter=null;
	private TextView my_music_songlist_count;
	private ScrollView scrollView;
	
	private MusicServiceBinder mBinder = null;
	private TextView total_music_number_textivew;
	
	private int currColorValue = -1;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof OnSkinChangerListener){
			mSkinChangeListener = (OnSkinChangerListener) activity;
		}
		mBinder = ((Main)activity).getBinder();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_music_fra, container,false);
		initView(view);
		return view;
	}
	private void initView(View view) {
		
		scrollView = (ScrollView) view.findViewById(R.id.my_music_fra_scrollView);
		
		content1 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_1);
		content2 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_2);
		content3 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_3);
		content4 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_4);
		favor_content = (LinearLayout) view.findViewById(R.id.my_music_fra_module_favor);
		
		changeSkinRela = (RelativeLayout) view.findViewById(R.id.my_music_fra_skin);
		changeSkinRela.setOnClickListener(this);
		
		my_music_bg = (ImageView) view.findViewById(R.id.my_music_bg);
		my_music_bg1 = (ImageView) view.findViewById(R.id.my_music_bg1);
		img = my_music_bg1;
		
		mFavorListview = (ListViewForScrollView) view.findViewById(R.id.my_music_fra_favor_listview);
		mFavorAdapter = new MyFavorSongListAdapter(getActivity());
		mFavorListview.setAdapter(mFavorAdapter);
		
		my_music_songlist_count = (TextView) view.findViewById(R.id.my_music_fra_num);
		
		String urlAndColor = PreferHelper.readString(getActivity().getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
		if (urlAndColor!=null) {
			String[] array = urlAndColor.split(",");
			mImageLoader.displayImage(array[0], my_music_bg, new MyApplication().getImageOptionsNoWaittingDrawable(200));
			int colorValue = Integer.parseInt(array[1]);
			content1.setBackgroundColor(colorValue);
			content2.setBackgroundColor(colorValue);
			content2.getBackground().setAlpha(150);
			content3.setBackgroundColor(colorValue);
			content3.getBackground().setAlpha(150);
			content4.setBackgroundColor(colorValue);
			favor_content.setBackgroundColor(colorValue);
			mFavorAdapter.setColorValue(colorValue);
			currColorValue = colorValue;
		}
		
		/**创建一个新的歌单*/
		createlist_textview = (TextView) view.findViewById(R.id.my_music_fra_createlist_textview);
		createlist_textview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				createNewList();
			}
		});
		

		MusicGroupDao mgd = new MusicGroupDao(getActivity());
		ArrayList<MusicGroup> groupList = mgd.getGroups();
		mFavorAdapter.setData(groupList);
		
		scrollView.smoothScrollTo(0	, 0);
		
		total_music_number_textivew = (TextView) view.findViewById(R.id.my_music_fra_totalnumber);
		
		content1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(getActivity(),MainActivity.class);
//				intent.putExtra("color", currColorValue);
//				startActivity(intent);
				TabMusicLocalFra tmlf = new TabMusicLocalFra();
				Bundle bundle = new Bundle();
				bundle.putInt("color", currColorValue);
				tmlf.setArguments(bundle);
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.main_content,tmlf);
				transaction.addToBackStack("tabmusiclocalfra");
//				transaction.add(R.id.main_content, tmlf);
//				transaction.show(tmlf);
//				transaction.hide(getParentFragment());
				transaction.setCustomAnimations(R.anim.push_bottom_in, 0);
				transaction.commit();
			}
		});
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appAnim_fade_oute = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out); 
		appAnim_fade_in = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
		appAnim_fade_in.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				if (img==my_music_bg1) {
					my_music_bg1.setVisibility(View.VISIBLE);
				}else{
					my_music_bg.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
			}
		});
		appAnim_fade_oute.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				if (img==my_music_bg1) {
					my_music_bg.setVisibility(View.GONE);
					img = my_music_bg;
				}else if(img==my_music_bg){
					my_music_bg1.setVisibility(View.GONE);
					img = my_music_bg1;
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.my_music_fra_skin:
			Intent intent = new Intent(getActivity(),ChangeSkinDialogActivity.class);
			ChangeSkinDialogActivity.addOnSkinChangerListener(mSkinChangeListener);
			ChangeSkinDialogActivity.addOnSkinChangerListener(this);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
			break;

		default:
			break;
		}
	}
	@Override
	public void onSkinChange(int coloValue,String url) {
		currColorValue = coloValue;
		content1.setBackgroundColor(coloValue);
		content2.setBackgroundColor(coloValue);
		content2.getBackground().setAlpha(150);
		content3.setBackgroundColor(coloValue);
		content3.getBackground().setAlpha(150);
		content4.setBackgroundColor(coloValue);
		favor_content.setBackgroundColor(coloValue);
		lastUrl = url;
/*		if(img==my_music_bg1){
//			my_music_bg1.setImageResource();
				my_music_bg1.setImageResource(R.drawable.default_3); 
				my_music_bg1.startAnimation(appAnim_fade_in);
				my_music_bg.startAnimation(appAnim_fade_oute);
//				img = my_music_bg;
				Log.i("TAG", "bg1渐显，bg渐隐");
			
		}else if(img == my_music_bg){
			my_music_bg.setImageResource(R.drawable.default_4); 
			my_music_bg.startAnimation(appAnim_fade_in);
			my_music_bg1.startAnimation(appAnim_fade_oute);
//			img = my_music_bg1;
			Log.i("TAG", "bg1渐隐，bg渐显");
		}*/
		
		if (!url.isEmpty()) {
			mImageLoader.displayImage(url, my_music_bg, new MyApplication().getImageOptionsNoWaittingDrawable(200));
		}
		if (url.isEmpty()&&(!lastUrl.isEmpty())) {
			PreferHelper.write(getActivity().getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR, lastUrl+","+coloValue);
		}
		if(!url.isEmpty()){
			PreferHelper.write(getActivity().getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR, url+","+coloValue);
		}
		
		mFavorAdapter.setColorValue(coloValue);
	}
    public int getRes(String name) {
    	int r_id = 0;
    	Class<drawable> drawable  =  R.drawable.class;
        Field field = null;
        try {
             field = drawable.getField(name);
             r_id  = field.getInt(field.getName());
        } catch (Exception e) {
        }
        return r_id;
    }

    AlertDialog dialog=null;
	/***
	 * @author vac
	 * @description 创建一个新的歌单
	 * @date 2015年12月15日18:26:01
	 */
	public void createNewList(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
		View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.editemail, null);
		final EditText edit = (EditText) dialogView.findViewById(R.id.edit_dialog_edittext);
		Button ok = (Button) dialogView.findViewById(R.id.edit_dialog_ok);
		Button cancle = (Button) dialogView.findViewById(R.id.edit_dialog_cancel);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (edit.getText().toString().trim().isEmpty()) {
					Toast.makeText(getActivity(), "歌单名称不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
//				String name = PreferHelper.readString(getActivity(),Constant.MY_CREATE_SONG_LIST, Constant.MY_CREATE_SONG_LIST, "");
//				String songListName = name+","+edit.getText().toString().trim();
//				PreferHelper.write(getActivity(), Constant.MY_CREATE_SONG_LIST, Constant.MY_CREATE_SONG_LIST,songListName);
//				String[] songListNameArray = songListName.split(",");
//				mFavorAdapter.clear();
//				for (int i = 0; i < songListNameArray.length; i++) {
//					if (!songListNameArray[i].isEmpty()) {
//						mFavorAdapter.addOneData(songListNameArray[i]);
//					}
//				}
//				mFavorAdapter.notifyDataSetChanged();
//				my_music_songlist_count.setText(mFavorAdapter.getCount()+"个");
				addMySongList(edit.getText().toString().trim());
				dialog.dismiss();
			}
		});
		cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		builder.setView(dialogView);
		dialog = builder.create();
		dialog.show();
	}
	/***
	 * @author vac
	 * @description 创建一个新的歌单
	 * @param name
	 */
	private void addMySongList(String name){
		MusicGroup mg = new MusicGroup();
		mg.setId(System.currentTimeMillis());
		mg.setTitle(name);
		MusicGroupDao md = new MusicGroupDao(getActivity());
		md.addGroup(mg);
		ArrayList<MusicGroup> groupList = md.getGroups();
		mFavorAdapter.setData(groupList);
	}
}
