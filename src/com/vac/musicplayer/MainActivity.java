package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vac.musicplayer.adapter.MyPagerAdapter;
import com.vac.musicplayer.fragment.localmusic.Albumfra;
import com.vac.musicplayer.fragment.localmusic.FileDirectoryfra;
import com.vac.musicplayer.fragment.localmusic.LocalMusicfra;
import com.vac.musicplayer.fragment.localmusic.LocalMusicfra.onMusicTotalCountListener;
import com.vac.musicplayer.fragment.localmusic.Singerfra;

public class MainActivity extends FragmentActivity implements 
OnCheckedChangeListener,OnPageChangeListener,onMusicTotalCountListener {

	private static final String TAG = MainActivity.class.getName();
	private int currColorValue = -1;
	private ViewPager viewPager;
	private RadioGroup rg;
	private RadioButton rb1,rb2,rb3,rb4;
	private FragmentManager fm;
	private MyPagerAdapter mAdapter;
//	private ImageView more_functions;//更多
//	private PopupMenu popupMenu=null;
	private int totalMusic=0;
//	private ImageView switch_to_player;//去播放页
	
	private LinearLayout title_bar_content;
	
	private TextView cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currColorValue = getIntent().getIntExtra("color", -1);
        fm = getSupportFragmentManager();
        initView();
        initFragment();
    }
    
    private void initView(){
    	viewPager = (ViewPager) findViewById(R.id.activity_viewPager);
    	viewPager.setOffscreenPageLimit(3);
    	viewPager.setOnPageChangeListener(this);
    	rg = (RadioGroup) findViewById(R.id.activity_rg_navigation);
    	rg.setOnCheckedChangeListener(this);
    	
    	rb1 = (RadioButton) findViewById(R.id.activity_rb_localmusic);
    	rb2 = (RadioButton) findViewById(R.id.activity_rb_singer);
    	rb3 = (RadioButton) findViewById(R.id.activity_rb_alblum);
    	rb4 = (RadioButton) findViewById(R.id.activity_rb_file);
    	
//    	title_of_top = (TextView) findViewById(R.id.title_of_top);
//    	title_of_top.setText("歌曲(0)");
    	
//    	more_functions = (ImageView) findViewById(R.id.more_functions);
//    	popupMenu = new PopupMenu(this,more_functions);
//    	popupMenu.getMenuInflater().inflate(R.menu.title_more,popupMenu.getMenu());
    	
//    	more_functions.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				popupMenu.show();
//			}
//		});
    	
    	
//    	popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			
//			@Override
//			public boolean onMenuItemClick(MenuItem arg0) {
//				switch (arg0.getItemId()) {
//				case R.id.title_more_1:
//					
//					break;
//				case R.id.title_more_2:
//					
//					break;
//				case R.id.title_more_3:
//					
//					break;
//				case R.id.title_more_4:
//					
//					break;
//
//				default:
//					break;
//				}
//				return false;
//			}
//		});
    	
//    	switch_to_player = (ImageView) findViewById(R.id.switch_to_player);
//    	switch_to_player.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(MainActivity.this,PlayMusic.class);
////				intent.putExtra(Constant.PLAYLIST_MUISC, MusicListAdapter.getData());
//				startActivity(intent);
//			}
//		});
    	
    	title_bar_content = (LinearLayout) findViewById(R.id.activity_main_titlebar_content);
    	title_bar_content.setBackgroundColor(currColorValue);
    	
    	setColor(rb1);
    	
    	cursor = (TextView) findViewById(R.id.activity_main_cursor);
    	cursor.setBackgroundColor(currColorValue);
    	LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
    	params.width = getResources().getDisplayMetrics().widthPixels/4;
    	cursor.setLayoutParams(params);
    	
    	viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
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
			public void onPageScrolled(int position, float percent, int width) {
				LinearLayout.LayoutParams params = (LayoutParams) cursor.getLayoutParams();
				params.leftMargin = (int) (cursor.getWidth() * (percent + position));
		    	cursor.setLayoutParams(params);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
    }
    private void initFragment(){
    	List<Fragment> list = new ArrayList<Fragment>();
    	list.add(new LocalMusicfra());
    	list.add(new Singerfra());
    	list.add(new Albumfra());
    	list.add(new FileDirectoryfra());
    	mAdapter = new MyPagerAdapter(fm, list);
    	viewPager.setAdapter(mAdapter);
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
	
	private void setRbCheckFalse(){
		rb1.setChecked(false);
		rb2.setChecked(false);
		rb3.setChecked(false);
	}

	private void setColor(RadioButton rb){
		switch (rb.getId()) {
		case R.id.activity_rb_localmusic:
			rb1.setTextColor(currColorValue);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.activity_rb_singer:
			rb2.setTextColor(currColorValue);
			rb1.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.activity_rb_alblum:
			rb3.setTextColor(currColorValue);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb1.setTextColor(getResources().getColor(R.color.black));
			rb4.setTextColor(getResources().getColor(R.color.black));
			break;
			
		case R.id.activity_rb_file:
			rb4.setTextColor(currColorValue);
			rb2.setTextColor(getResources().getColor(R.color.black));
			rb3.setTextColor(getResources().getColor(R.color.black));
			rb1.setTextColor(getResources().getColor(R.color.black));
			break;


		default:
			break;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setRbCheckFalse();
		switch (arg0) {
		case 0:
			rb1.setChecked(true);
//			title_of_top.setText("歌曲("+totalMusic+")");
			break;
		case 1:
			rb2.setChecked(true);
//			title_of_top.setText("歌手");
			break;
		case 2:
			rb3.setChecked(true);
//			title_of_top.setText("专辑");
			break;

		default:
			break;
		}
	}

	@Override
	public void musicTotalCount(int total) {
		totalMusic = total;
		if (viewPager.getCurrentItem()==0) {
//			title_of_top.setText("歌曲("+totalMusic+")");
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
