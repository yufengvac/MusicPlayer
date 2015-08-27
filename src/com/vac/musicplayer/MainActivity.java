package com.vac.musicplayer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vac.musicplayer.adapter.MyPagerAdapter;
import com.vac.musicplayer.fragment.Albumfra;
import com.vac.musicplayer.fragment.LocalMusicfra;
import com.vac.musicplayer.fragment.Singerfra;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener,OnPageChangeListener {

	private ViewPager viewPager;
	private RadioGroup rg;
	private RadioButton rb1,rb2,rb3;
	private FragmentManager fm;
	private MyPagerAdapter mAdapter;
	private TextView title_of_top;
	private ImageView more_functions;//更多
	private PopupMenu popupMenu=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    	
    	title_of_top = (TextView) findViewById(R.id.title_of_top);
    	title_of_top.setText("本地音乐(0)");
    	
    	more_functions = (ImageView) findViewById(R.id.more_functions);
    	popupMenu = new PopupMenu(this,more_functions);
    	popupMenu.getMenuInflater().inflate(R.menu.title_more,popupMenu.getMenu());
    	
    	more_functions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				popupMenu.show();
			}
		});
    	
    	popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				switch (arg0.getItemId()) {
				case R.id.title_more_1:
					
					break;
				case R.id.title_more_2:
					
					break;
				case R.id.title_more_3:
					
					break;
				case R.id.title_more_4:
					
					break;

				default:
					break;
				}
				return false;
			}
		});
    }
    private void initFragment(){
    	List<Fragment> list = new ArrayList<Fragment>();
    	list.add(new LocalMusicfra());
    	list.add(new Singerfra());
    	list.add(new Albumfra());
    	mAdapter = new MyPagerAdapter(fm, list);
    	viewPager.setAdapter(mAdapter);
    }

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		setRbCheckFalse();
		switch (arg1) {
		case R.id.activity_rb_localmusic:
			rb1.setChecked(true);
			viewPager.setCurrentItem(0);
			break;
		case R.id.activity_rb_singer:
			rb2.setChecked(true);
			viewPager.setCurrentItem(1);
			break;

		case R.id.activity_rb_alblum:
			rb3.setChecked(true);
			viewPager.setCurrentItem(2);
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
			title_of_top.setText("本地音乐(0)");
			break;
		case 1:
			rb2.setChecked(true);
			title_of_top.setText("歌手");
			break;
		case 2:
			rb3.setChecked(true);
			title_of_top.setText("专辑");
			break;

		default:
			break;
		}
	}
}
