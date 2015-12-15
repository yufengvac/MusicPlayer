package com.vac.musicplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.Main;
import com.vac.musicplayer.R;
import com.vac.musicplayer.application.MyApplication;
import com.vac.musicplayer.dialogactivity.ChangeSkinDialogActivity;
import com.vac.musicplayer.listener.OnSkinChangerListener;

public class SelectSkinFra extends Fragment implements OnClickListener{

	private ImageView skin_1,skin_2,skin_3;
	private ImageView skin_4,skin_5,skin_6;
	private ImageView skin_7,skin_8,skin_9;
	private LinearLayout content1,content2,content3;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options =null;
	private int index=0;
	public static List<OnSkinChangerListener> listenerList = new ArrayList<OnSkinChangerListener>();
	private int[] colorArray = new int[9];
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof ChangeSkinDialogActivity){
			if (!listenerList.contains((OnSkinChangerListener)activity)) {
				listenerList.add((OnSkinChangerListener)activity);
			}
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.select_skin_fra, container,false);
		initView(view);
		
		MyApplication app =new MyApplication();
		options = app.getImageOptions(R.drawable.launcher, 0);
		
		Bundle bundle = getArguments();
		index = bundle.getInt("index");
		switch (index) {
		case 1:
			content1.setVisibility(View.VISIBLE);
			content2.setVisibility(View.GONE);
			content3.setVisibility(View.GONE);
			final String uri1 = "drawable://"+R.drawable.default_1;
			mImageLoader.displayImage(uri1, skin_1, options);
			final String uri2 = "drawable://"+R.drawable.default_2;
			mImageLoader.displayImage(uri2, skin_2, options);
			final String uri3 = "drawable://"+R.drawable.default_3;
			mImageLoader.displayImage(uri3, skin_3, options);
			skin_1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[0],uri1);
				}
			});
			skin_2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[1],uri2);
				}
			});
			skin_3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[2],uri3);
				}
			});
			break;
		case 2:
			content2.setVisibility(View.VISIBLE);
			content1.setVisibility(View.GONE);
			content3.setVisibility(View.GONE);
			final String uri4 = "drawable://"+R.drawable.default_4;
			mImageLoader.displayImage(uri4, skin_4, options);
			final String uri5 =  "drawable://"+R.drawable.default_5;
			mImageLoader.displayImage(uri5, skin_5, options);
			final String uri6 = "drawable://"+R.drawable.default_6;
			mImageLoader.displayImage(uri6, skin_6, options);
			skin_4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[3],uri4);
				}
			});
			skin_5.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[4],uri5);
				}
			});
			skin_6.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[5],uri6);
				}
			});
			break;
		case 3:
			content3.setVisibility(View.VISIBLE);
			content1.setVisibility(View.GONE);
			content2.setVisibility(View.GONE);
			final String uri7 = "drawable://"+R.drawable.default_7;
			mImageLoader.displayImage(uri7, skin_7, options);
			final String uri8 = "drawable://"+R.drawable.default_8;
			mImageLoader.displayImage(uri8, skin_8, options);
			final String uri9 = "drawable://"+R.drawable.default_9;
			mImageLoader.displayImage(uri9, skin_9, options);
			skin_7.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[6],uri7);
				}
			});
			skin_8.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[7],uri8);
				}
			});
			skin_9.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					changeAllskin(colorArray[8],uri9);
				}
			});
			break;
		case 4:
			
			break;
		case 5:
			
			break;
		case 6:
			
			break;
			

		default:
			break;
		}
		return view;
	}
	private void initView(View view) {
		skin_1 = (ImageView) view.findViewById(R.id.select_skin_1);
		skin_2 = (ImageView) view.findViewById(R.id.select_skin_2);
		skin_3 = (ImageView) view.findViewById(R.id.select_skin_3);
		
		skin_4 = (ImageView) view.findViewById(R.id.select_skin_4);
		skin_5 = (ImageView) view.findViewById(R.id.select_skin_5);
		skin_6 = (ImageView) view.findViewById(R.id.select_skin_6);
		
		skin_7 = (ImageView) view.findViewById(R.id.select_skin_7);
		skin_8 = (ImageView) view.findViewById(R.id.select_skin_8);
		skin_9 = (ImageView) view.findViewById(R.id.select_skin_9);
		
		content1 = (LinearLayout) view.findViewById(R.id.select_skin_content1);
		content2 = (LinearLayout) view.findViewById(R.id.select_skin_content2);
		content3 = (LinearLayout) view.findViewById(R.id.select_skin_content3);
		
		colorArray[0]=Color.rgb(255, 25, 0);//#FF1900
		colorArray[1]=Color.rgb(81, 135, 163);//#5187A3
		colorArray[2]=Color.rgb(228, 176, 200);//#E4B0C8
		colorArray[3]=Color.rgb(255, 135, 61);//#FF873D
		colorArray[4]=Color.rgb(120, 178, 190);//#78B2BE
		colorArray[5]=Color.rgb(0, 151, 111);//#00976F
		colorArray[6]=Color.rgb(255, 201, 120);//#FFC978
		colorArray[7]=Color.rgb(130, 146, 36);//#829224
		colorArray[8]=Color.rgb(85, 134, 81);//#558651
	}
	public void changeAllskin(int colorValue,String url){
		for (int i = 0; i < listenerList.size(); i++) {
			if(listenerList.get(i)!=null){
				listenerList.get(i).onSkinChange(colorValue,url);
			}
		}
	}
	@Override
	public void onClick(View v) {
		
	}
	public static void addOnSkinChangerListener(OnSkinChangerListener listener){
		if(listener!=null){
			if (listener instanceof MyMusicFra||listener instanceof Main) {
				if (!listenerList.contains(listener)) {
					Log.v("TAG", "添加listener="+listener);
					listenerList.add(listener);
				}
			}
		}
	}
	@Override
	public void onDestroy() {
		listenerList.clear();
		super.onDestroy();
	}
}
