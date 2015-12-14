package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vac.musicplayer.fragment.MyMusicFra;
import com.vac.musicplayer.fragment.NetMusicFra;
import com.vac.musicplayer.fragment.SelectSkinFra;
import com.vac.musicplayer.listener.OnSkinChangerListener;

public class Main extends FragmentActivity implements OnSkinChangerListener{
	private ViewPager viewPager;
	private MyFragmentPagerAdapter mAdapter;
	private List<Fragment> fraList = new ArrayList<Fragment>();
	private RadioButton rb_localmusic,rb_netmusic;
	private RadioGroup rg_navigation;
	private LinearLayout content;
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
	protected void onResume() {
		super.onResume();
		SelectSkinFra.setOnSkinChangerListener(this);
	}
	@Override
	public void onSkinChange(int coloValue,String url) {
		content.setBackgroundColor(coloValue);
	}
}
