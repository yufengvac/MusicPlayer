package com.vac.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.R;
import com.vac.musicplayer.application.MyApplication;
import com.vac.musicplayer.dialogactivity.ChangeSkinDialogActivity;
import com.vac.musicplayer.listener.OnSkinChangerListener;
/**
 * @title MyMusicFra
 * @description 我的音乐Fragment
 * @author vac
 * @date 2015年12月10日16:36:06
 *
 */
public class MyMusicFra extends Fragment implements OnClickListener , OnSkinChangerListener{

	private RelativeLayout content1,content2,content3,content4;
	private LinearLayout favor_content;
	private RelativeLayout changeSkinRela; 
	private ImageView my_music_bg,my_music_bg1;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private Animation appAnim;
	private ImageView img;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_music_fra, container,false);
		initView(view);
		return view;
	}
	private void initView(View view) {
		content1 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_1);
		content2 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_2);
		content3 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_3);
		content4 = (RelativeLayout) view.findViewById(R.id.my_music_fra_module_4);
		favor_content = (LinearLayout) view.findViewById(R.id.my_music_fra_module_favor);
		
		changeSkinRela = (RelativeLayout) view.findViewById(R.id.my_music_fra_skin);
		changeSkinRela.setOnClickListener(this);
		
		my_music_bg = (ImageView) view.findViewById(R.id.my_music_bg);
		my_music_bg1 = (ImageView) view.findViewById(R.id.my_music_bg1);
		img = my_music_bg1;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		appAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);  
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.my_music_fra_skin:
			Intent intent = new Intent(getActivity(),ChangeSkinDialogActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
			break;

		default:
			break;
		}
	}
	@Override
	public void onSkinChange(int coloValue,String url) {
		content1.setBackgroundColor(coloValue);
		content2.setBackgroundColor(coloValue);
		content2.getBackground().setAlpha(150);
		content3.setBackgroundColor(coloValue);
		content3.getBackground().setAlpha(150);
		content4.setBackgroundColor(coloValue);
		favor_content.setBackgroundColor(coloValue);
		if(img==my_music_bg1){
			mImageLoader.displayImage(url, my_music_bg1, new MyApplication().getImageOptionsNoWaittingDrawable(500));
			my_music_bg.startAnimation(appAnim);
			img = my_music_bg;
			Log.i("TAG", "bg1渐显，bg渐隐");
		}else if(img == my_music_bg){
			mImageLoader.displayImage(url, my_music_bg, new MyApplication().getImageOptionsNoWaittingDrawable(500));
			my_music_bg1.startAnimation(appAnim);
			img = my_music_bg1;
			Log.i("TAG", "bg1渐隐，bg渐显");
		}
		appAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
//				if (img==my_music_bg1) {
//					my_music_bg.setVisibility(View.GONE);
//				}else{
//					my_music_bg1.setVisibility(View.GONE);
//				}
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
//				if (img==my_music_bg1) {
//					my_music_bg1.setVisibility(View.GONE);
//				}else{
//					my_music_bg.setVisibility(View.GONE);
//				}
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		SelectSkinFra.setOnSkinChangerListener(this);
	}
}
