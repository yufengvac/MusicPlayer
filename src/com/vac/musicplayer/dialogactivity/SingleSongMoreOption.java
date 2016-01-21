package com.vac.musicplayer.dialogactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.search.SingleSongMoreAdapter;
import com.vac.musicplayer.adapter.search.SingleSongMoreAdapter.SingleSongMore;
import com.vac.musicplayer.bean.TingAudition;

public class SingleSongMoreOption extends Activity {

	private GridView gridView;
	private SingleSongMoreAdapter mAdapter;
	private int colorValue = -1;
	private LinearLayout content;
	private List<SingleSongMore> list = new ArrayList<SingleSongMore>();
	private String[] title = new String[]{"收藏","加入歌单","下载","分享","插队播放","歌手","专辑","评论","MV"};
	private String musicName;
	private ArrayList<TingAudition> auditionList = new ArrayList<TingAudition>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_song_more_option);
		colorValue = getIntent().getIntExtra("color", -1);
		initView();
	}
	private void initView() {
		gridView = (GridView) findViewById(R.id.single_song_more_option_gridview);
		content = (LinearLayout) findViewById(R.id.single_song_more_otpion_content);
		content.setBackgroundColor(colorValue);
		mAdapter = new SingleSongMoreAdapter(this);
		gridView.setAdapter(mAdapter);
		
		for (int i = 0; i < title.length; i++) {
			SingleSongMore ssm =  mAdapter.getSingleSongMore();
			ssm.setText(title[i]);
			list.add(ssm);
		}
		mAdapter.setData(list);
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
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
        lp.height = getResources().getDisplayMetrics().heightPixels/2;
        
		getWindowManager().updateViewLayout(view, lp);
	}
}
