
package com.vac.musicplayer.fragment.search;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyPagerAdapter;
import com.vac.musicplayer.myview.LazyViewPager;
import com.vac.musicplayer.myview.LazyViewPager.OnPageChangeListener;

public class TabSearchFra extends Fragment {

	private LazyViewPager viewPager;
	private RadioGroup rg;
	private RadioButton rb1,rb2,rb3,rb4;
	private TextView cursor;
	private MyPagerAdapter mAdapter;
	private ArrayList<Fragment> fraList = new ArrayList<Fragment>();
	private int colorValue=-1;
	private String search;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_search_fra, null);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		search = bundle.getString("search");
		initView(view);
		
		return view;
	}
	private void initView(View view) {
		viewPager = (LazyViewPager) view.findViewById(R.id.tab_search_viewPager);
		viewPager.setOffscreenPageLimit(3);
		SearchSingleSongFra sssf = new SearchSingleSongFra();
		Bundle bundle = new Bundle();
		bundle.putInt("color", colorValue);
		bundle.putString("search", search);
		sssf.setArguments(bundle);
		fraList.add(sssf);
		
		SearchAlbumFra saf = new SearchAlbumFra();
		saf.setArguments(bundle);
		fraList.add(saf);
		
		SearchSongListFra sslf =new SearchSongListFra();
		sslf.setArguments(bundle);
		fraList.add(sslf);
		
		SearchMVFra smf = new SearchMVFra();
		smf.setArguments(bundle);
		fraList.add(smf);
		mAdapter = new MyPagerAdapter(getChildFragmentManager(), fraList);
		viewPager.setAdapter(mAdapter);
		
		cursor = (TextView) view.findViewById(R.id.tab_search_fra_cursor);
		cursor.setBackgroundColor(colorValue);
		LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
		params.width = getResources().getDisplayMetrics().widthPixels/4;
		cursor.setLayoutParams(params);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					rb1.setTextColor(colorValue);
					rb2.setTextColor(getResources().getColor(R.color.black));
					rb3.setTextColor(getResources().getColor(R.color.black));
					rb4.setTextColor(getResources().getColor(R.color.black));
					break;
				case 1:
					rb2.setTextColor(colorValue);
					rb1.setTextColor(getResources().getColor(R.color.black));
					rb3.setTextColor(getResources().getColor(R.color.black));
					rb4.setTextColor(getResources().getColor(R.color.black));
					break;
				case 2:
					rb3.setTextColor(colorValue);
					rb2.setTextColor(getResources().getColor(R.color.black));
					rb1.setTextColor(getResources().getColor(R.color.black));
					rb4.setTextColor(getResources().getColor(R.color.black));
					break;
				case 3:
					rb4.setTextColor(colorValue);
					rb2.setTextColor(getResources().getColor(R.color.black));
					rb3.setTextColor(getResources().getColor(R.color.black));
					rb1.setTextColor(getResources().getColor(R.color.black));
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
				params.leftMargin = (int) (cursor.getWidth()*(arg1+arg0));
			
				cursor.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		rb1 = (RadioButton) view.findViewById(R.id.tab_search_fra_rb1);
		rb2 = (RadioButton) view.findViewById(R.id.tab_search_fra_rb2);
		rb3= (RadioButton) view.findViewById(R.id.tab_search_fra_rb3);
		rb4 = (RadioButton) view.findViewById(R.id.tab_search_fra_rb4);
		rb1.setTextColor(colorValue);
		rg = (RadioGroup) view.findViewById(R.id.tab_search_fra_rg);
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.tab_search_fra_rb1:
					viewPager.setCurrentItem(0);
					break;
				case R.id.tab_search_fra_rb2:
					viewPager.setCurrentItem(1);
					break;
				case R.id.tab_search_fra_rb3:
					viewPager.setCurrentItem(2);
					break;
				case R.id.tab_search_fra_rb4:
					viewPager.setCurrentItem(3);
					break;

				default:
					break;
				}
			}
		});
	}
	
}
