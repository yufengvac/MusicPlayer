package com.vac.musicplayer.dialogactivity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ProgressBar;

import com.vac.musicplayer.R;
import com.vac.musicplayer.fragment.SelectSkinFra;
import com.vac.musicplayer.listener.OnSkinChangerListener;

public class ChangeSkinDialogActivity extends FragmentActivity {

	private ViewPager viewPager;
	private List<Fragment> fraList = new ArrayList<Fragment>();
	private ProgressBar progressBar;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==1) {
				progressBar.setVisibility(View.GONE);
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
		new Thread(){
			public void run() {
				for (int j = 1; j < 4; j++) {
					SelectSkinFra selectSkinFra = new SelectSkinFra();
					Bundle bundle = new Bundle();
					bundle.putInt("index", j);
					selectSkinFra.setArguments(bundle);
					fraList.add(selectSkinFra);
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(1);
			};
		}.start();
	}
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.change_skin_viewPager);
		progressBar = (ProgressBar) findViewById(R.id.change_skin_progressBar);
		progressBar.setVisibility(View.VISIBLE);
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
}
