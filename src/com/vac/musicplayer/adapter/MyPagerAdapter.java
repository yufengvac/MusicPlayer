package com.vac.musicplayer.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> data;
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public MyPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.data = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public int getCount() {
		return data.size();
	}

}
