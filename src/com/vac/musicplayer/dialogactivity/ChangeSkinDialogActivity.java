package com.vac.musicplayer.dialogactivity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vac.musicplayer.Main;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.ChangeSkinAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.fragment.MyMusicFra;
import com.vac.musicplayer.fragment.SelectSkinFra;
import com.vac.musicplayer.listener.OnSkinChangerListener;
import com.vac.musicplayer.myview.ColorPickerView;
import com.vac.musicplayer.myview.HorizonListView;
import com.vac.musicplayer.utils.PreferHelper;

public class ChangeSkinDialogActivity extends FragmentActivity implements OnSkinChangerListener{

	private ViewPager viewPager;
	private List<Fragment> fraList = new ArrayList<Fragment>();
	private List<String> urlList = new ArrayList<String>();
	private ProgressBar progressBar;
	private int currColorValue=0;
	private TextView text_1,text_2,line_1,line_2;
	private String type ="pic";
	
	private ColorPickerView colorPickerView;
	private HorizonListView horizonListview;
	private ChangeSkinAdapter mSkinAdapter;
	
	public static List<OnSkinChangerListener> listenerList = new ArrayList<OnSkinChangerListener>();
	
	private int[] colorArray = new int[9];
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				progressBar.setVisibility(View.GONE);
				viewPager.setOffscreenPageLimit(1);
				MyFragmentPagerAdapter mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
				viewPager.setAdapter(mAdapter);
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.change_skin_dialog_activity);
		initView();
		initDefaultSkin();
	}
	private void initDefaultSkin() {
//		new Thread(){
//			public void run() {
//				for (int j = 1; j < 4; j++) {
//					SelectSkinFra selectSkinFra = new SelectSkinFra();
//					Bundle bundle = new Bundle();
//					bundle.putInt("index", j);
//					selectSkinFra.setArguments(bundle);
//					fraList.add(selectSkinFra);
//				}
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				mHandler.sendEmptyMessage(1);
//			};
//		}.start();
	}
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.change_skin_viewPager);
		progressBar = (ProgressBar) findViewById(R.id.change_skin_progressBar);
		progressBar.setVisibility(View.GONE);
		
		text_1 = (TextView) findViewById(R.id.skin_indicator_text_1);
		text_2 = (TextView) findViewById(R.id.skin_indicator_text_2);
		
		line_1 = (TextView) findViewById(R.id.skin_indicator_line_1);
		line_2 = (TextView) findViewById(R.id.skin_indicator_line_2);
		String value = PreferHelper.readString(ChangeSkinDialogActivity.this, Constant.MAIN_BG_COLOR,Constant.MAIN_BG_COLOR);
		if (value!=null) {
			currColorValue = Integer.parseInt(value.split(",")[1]);
			text_1.setTextColor(currColorValue);
			line_1.setBackgroundColor(currColorValue);
			text_2.setTextColor(getResources().getColor(R.color.grey_white_));
			line_2.setBackgroundColor(getResources().getColor(R.color.grey_white_));
		}
		
		colorPickerView = (ColorPickerView) findViewById(R.id.change_skin_colorpicker);
		 colorPickerView.setOnColorChangeListenrer(new ColorPickerView.OnColorChangedListener() {
				
				@Override
				public void colorChanged(int color){
					currColorValue = color;
					for (int i = 0; i < listenerList.size(); i++) {
						if(listenerList.get(i)!=null){
							listenerList.get(i).onSkinChange(currColorValue,"");
						}
					}
					if (type.equals("pic")) {
						text_1.setTextColor(currColorValue);
						line_1.setBackgroundColor(currColorValue);
						text_2.setTextColor(getResources().getColor(R.color.grey_white_));
						line_2.setBackgroundColor(getResources().getColor(R.color.grey_white_));
					}else{
						text_2.setTextColor(currColorValue);
						line_2.setBackgroundColor(currColorValue);
						text_1.setTextColor(getResources().getColor(R.color.grey_white_));
						line_1.setBackgroundColor(getResources().getColor(R.color.grey_white_));
					}
				}
			});
		 
		 horizonListview = (HorizonListView) findViewById(R.id.change_skin_horizonListview);
		 
		 mSkinAdapter = new ChangeSkinAdapter(this);
		 horizonListview.setAdapter(mSkinAdapter);
		 initUrlAndColor();
		 mSkinAdapter.setData(urlList);
		 horizonListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				changeAllskin(colorArray[arg2],(String)(mSkinAdapter.getItem(arg2)));
				currColorValue = colorArray[arg2];
			}
		});
	}
	private void initUrlAndColor() {
		urlList.add( "drawable://"+R.drawable.default_1);
		 urlList.add( "drawable://"+R.drawable.default_2);
		 urlList.add( "drawable://"+R.drawable.default_3);
		 urlList.add( "drawable://"+R.drawable.default_4);
		 urlList.add( "drawable://"+R.drawable.default_5);
		 urlList.add( "drawable://"+R.drawable.default_6);
		 urlList.add( "drawable://"+R.drawable.default_7);
		 urlList.add( "drawable://"+R.drawable.default_8);
		 urlList.add( "drawable://"+R.drawable.default_9);
		 
		 colorArray[0]=Color.rgb(255, 25, 0);//#FF1900
			colorArray[1]=Color.rgb(81, 135, 163);//#5187A3
			colorArray[2]=Color.rgb(228, 176, 200);//#E4B0C8
			colorArray[3]=Color.rgb(255, 135, 61);//#FF873D
			colorArray[4]=Color.rgb(120, 178, 190);//#78B2BE
			colorArray[5]=Color.rgb(0, 151, 111);//#00976F
			colorArray[6]=Color.rgb(255, 201, 120);//#FFC978
			colorArray[7]=Color.rgb(130, 146, 36);//#829224
			colorArray[8]=Color.rgb(85, 134, 81);//#558651
	}
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		View view = getWindow().getDecorView();
		view.setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = (LayoutParams) view.getLayoutParams();
		lp.gravity = Gravity.BOTTOM;
		lp.x = 0;
		lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        
		getWindowManager().updateViewLayout(view, lp);
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
	public void onSkinChange(int coloValue, String url) {
		currColorValue = coloValue;
		if (type.equals("pic")) {
			text_1.setTextColor(currColorValue);
			line_1.setBackgroundColor(currColorValue);
			text_2.setTextColor(getResources().getColor(R.color.grey_white_));
			line_2.setBackgroundColor(getResources().getColor(R.color.grey_white_));
		}else{
			text_2.setTextColor(currColorValue);
			line_2.setBackgroundColor(currColorValue);
			text_1.setTextColor(getResources().getColor(R.color.grey_white_));
			line_1.setBackgroundColor(getResources().getColor(R.color.grey_white_));
		}
		
	}
	public void selectPic(View view){
		if (type.equals("color")) {
			type ="pic";
			text_1.setTextColor(currColorValue);
			line_1.setBackgroundColor(currColorValue);
			text_2.setTextColor(getResources().getColor(R.color.grey_white_));
			line_2.setBackgroundColor(getResources().getColor(R.color.grey_white_));
			
			line_1.setVisibility(View.VISIBLE);
			line_2.setVisibility(View.GONE);
			colorPickerView.setVisibility(View.GONE);
			viewPager.setVisibility(View.VISIBLE);
		}
	}
	public void selectColor(View view){
		if (type.equals("pic")) {
			type ="color";
			text_2.setTextColor(currColorValue);
			line_2.setBackgroundColor(currColorValue);
			text_1.setTextColor(getResources().getColor(R.color.grey_white_));
			line_1.setBackgroundColor(getResources().getColor(R.color.grey_white_));
			
			line_2.setVisibility(View.VISIBLE);
			line_1.setVisibility(View.GONE);
			colorPickerView.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		String result = PreferHelper.readString(ChangeSkinDialogActivity.this.getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
		String[] array = result.split(",");
		PreferHelper.write(ChangeSkinDialogActivity.this.getApplicationContext(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR,array[0]+","+currColorValue);
	}
	public static void addOnSkinChangerListener(OnSkinChangerListener listener){
		if(listener!=null){
			if (listener instanceof MyMusicFra||listener instanceof Main) {
				if (!listenerList.contains(listener)) {
					Log.v("TAG", "添加listener="+listener);
					listenerList.add(listener);
				}
			}
		}
	}
	public void changeAllskin(int colorValue,String url){
		for (int i = 0; i < listenerList.size(); i++) {
			if(listenerList.get(i)!=null){
				listenerList.get(i).onSkinChange(colorValue,url);
			}
		}
	}
	@Override
	public void onDestroy() {
		listenerList.clear();
		super.onDestroy();
	}
}
