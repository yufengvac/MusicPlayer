package com.vac.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyPagerAdapter;
import com.vac.musicplayer.fragment.localmusic.Albumfra;
import com.vac.musicplayer.fragment.localmusic.FileDirectoryfra;
import com.vac.musicplayer.fragment.localmusic.LocalMusicfra;
import com.vac.musicplayer.fragment.localmusic.Singerfra;

public class TabMusicLocalFra extends Fragment implements 
OnCheckedChangeListener,OnPageChangeListener{
	private final static String TAG = TabMusicLocalFra.class.getSimpleName();
	private int valueColor = -1;
	private ViewPager viewPager;
	private RadioGroup rg;
	private RadioButton rb1,rb2,rb3,rb4;
	private FragmentManager fm;
	private MyPagerAdapter mAdapter;
	private LinearLayout title_bar_content;
	
	private TextView cursor;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "TabMusicLocalFra->onAttach");
	}
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "TabMusicLocalFra->onDetach");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, null);
		Bundle bundle  = getArguments();
		valueColor = bundle.getInt("color");
		   fm = getChildFragmentManager();
	        initView(view);
	        initFragment();
		return view;
	}
	private void initFragment() {
		List<Fragment> list = new ArrayList<Fragment>();
    	list.add(new LocalMusicfra());
    	list.add(new Singerfra());
    	list.add(new Albumfra());
    	list.add(new FileDirectoryfra());
    	mAdapter = new MyPagerAdapter(fm, list);
    	viewPager.setAdapter(mAdapter);
	}
	private void initView(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.activity_viewPager);
    	viewPager.setOffscreenPageLimit(3);
    	viewPager.setOnPageChangeListener(this);
    	rg = (RadioGroup)view. findViewById(R.id.activity_rg_navigation);
    	rg.setOnCheckedChangeListener(this);
    	
    	rb1 = (RadioButton)view. findViewById(R.id.activity_rb_localmusic);
    	rb2 = (RadioButton)view. findViewById(R.id.activity_rb_singer);
    	rb3 = (RadioButton)view. findViewById(R.id.activity_rb_alblum);
    	rb4 = (RadioButton)view. findViewById(R.id.activity_rb_file);
    	
    	title_bar_content = (LinearLayout)view.findViewById(R.id.activity_main_titlebar_content);
    	title_bar_content.setBackgroundColor(valueColor);
    	
    	setColor(rb1);
    	
    	cursor = (TextView)view.findViewById(R.id.activity_main_cursor);
    	cursor.setBackgroundColor(valueColor);
    	LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
    	params.width = getResources().getDisplayMetrics().widthPixels/4;
    	cursor.setLayoutParams(params);
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int position, float percent, int width) {
		LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
		params.leftMargin = (int) (cursor.getWidth() * (percent + position));
    	cursor.setLayoutParams(params);
	}
	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			setColor(rb1);
			break;
		case 1:
			setColor(rb2);
			break;
		case 2:
			setColor(rb3);
			break;
		case 3:
			setColor(rb4);
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		switch (arg1) {
		case R.id.activity_rb_localmusic:
			setColor(rb1);
			viewPager.setCurrentItem(0);
			break;
		case R.id.activity_rb_singer:
			setColor(rb2);
			viewPager.setCurrentItem(1);
			break;

		case R.id.activity_rb_alblum:
			setColor(rb3);
			viewPager.setCurrentItem(2);
			break;
		case R.id.activity_rb_file:
			setColor(rb4);
			viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
	
	private void setColor(RadioButton rb){
		switch (rb.getId()) {
		case R.id.activity_rb_localmusic:
			rb1.setTextColor(valueColor);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.activity_rb_singer:
			rb2.setTextColor(valueColor);
			rb1.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.activity_rb_alblum:
			rb3.setTextColor(valueColor);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb1.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
			
		case R.id.activity_rb_file:
			rb4.setTextColor(valueColor);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb1.setTextColor(getResources().getColor(R.color.black));
			break;


		default:
			break;
		}
	}
}
