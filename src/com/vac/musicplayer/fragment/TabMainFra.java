package com.vac.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vac.musicplayer.Main;
import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.utils.PreferHelper;

public class TabMainFra extends Fragment {

	private final static String TAG = TabMainFra.class.getSimpleName();
	private ViewPager viewPager;
	private MyFragmentPagerAdapter mAdapter;
	private List<Fragment> fraList = new ArrayList<Fragment>();
	private RadioButton rb_localmusic,rb_netmusic;
	private RadioGroup rg_navigation;
	private LinearLayout content;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "TabMainFra->onAttach");
	}
	@Override
	public void onDetach() {
		super.onDetach();
		Log.i(TAG, "TabMainFra->onDetach");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_main_fra, null);
		initView(view);
		return view;
	}
	private void initView(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.main_viewPager);
		fraList.clear();
		MyMusicFra musicFra = new MyMusicFra();
		fraList.add(musicFra);
		NetMusicFra netMusicFra = new NetMusicFra();
		fraList.add(netMusicFra);
		mAdapter = new MyFragmentPagerAdapter(getFragmentManager());
		
		viewPager.setAdapter(mAdapter);
		
		rb_localmusic = (RadioButton) view.findViewById(R.id.main_rb_localmusic);
		rb_netmusic = (RadioButton) view.findViewById(R.id.main_rb_netmusic);
		
		rg_navigation = (RadioGroup)view. findViewById(R.id.main_rg_navigation);
		
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
		content = (LinearLayout) view.findViewById(R.id.main_titlebar_content);
		String urlAndColor = PreferHelper.readString(getActivity(), Constant.MAIN_BG_COLOR, Constant.MAIN_BG_COLOR);
		if (urlAndColor!=null) {
			int colorValue = Integer.parseInt(urlAndColor.split(",")[1]);
			content.setBackgroundColor(colorValue);
		}
		
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
}
