package com.vac.musicplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Music;

/**
 * 
 * */
public class MusicListAdapter extends BaseAdapter implements OnClickListener {
	private Context mContext = null;
	/** 数据源 */
	private ArrayList<Music> mData = null;

	/** 播放时为相应播放条目显示一个播放标记 */
	private int mActivateItemPos = -1;
	
	private AnimationDrawable animaiton=null;

	public static final int ANIMATION_START=0;
	public static final int ANIMATION_PAUSE=1;
	private int musicAnimation =-1;
	
	private int color;
	public void setColor(int color){
		this.color = color;
	}

	public MusicListAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<Music>();
	}

	
	public void setData(List<Music> data) {
		mData.clear();
		if (data != null) {
			mData.addAll(data);
		}
		mActivateItemPos = -1;
		notifyDataSetChanged();
	}

//	public static ArrayList<Music> getData() {
//		return mData;
//	}

	/** 让指定位置的条目显示一个正在播放标记（活动状态标记） */
	public void setSpecifiedIndicator(int state,int position) {
		musicAnimation = state;
		mActivateItemPos = position;
		notifyDataSetChanged();
	}
	

	@Override
	public boolean isEmpty() {
		return mData.size() == 0;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Music getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_music_list, parent, false);
			holder = new ViewHolder();
			holder.indicator = convertView.findViewById(R.id.play_indicator);
			holder.title = (TextView) convertView.findViewById(R.id.textview_music_title);
			holder.artist = (TextView) convertView.findViewById(R.id.textview_music_singer);
			holder.popup_menu = (ImageView) convertView.findViewById(R.id.track_popup_menu);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mActivateItemPos == position) {
			holder.popup_menu.setVisibility(View.VISIBLE);
			if(musicAnimation==ANIMATION_START){		
				animaiton = (AnimationDrawable) holder.popup_menu.getBackground();
				animaiton.start();
				Log.d("TAG", "适配器中已经开始播放了动画~~~~~~~~~~");
			}else if(musicAnimation==ANIMATION_PAUSE){
				animaiton = (AnimationDrawable) holder.popup_menu.getBackground();
				animaiton.stop();
				Log.d("TAG", "适配器中已经开始停止了动画~~~~~~~~~~");
			}
			
			holder.title.setTextColor(color);
			holder.artist.setTextColor(color);
		} else {
			holder.popup_menu.setVisibility(View.GONE);
			holder.title.setTextColor(mContext.getResources().getColor(R.color.black));
			holder.artist.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
		}
		holder.title.setText((position+1)+"."+getItem(position).getTitle());

		if (getItem(position).getArtist().equals("<unknown>")) {
			holder.artist.setText(mContext.getResources().getString(
					R.string.unknown_artist));
		} else {
			holder.artist.setText(getItem(position).getArtist());
		}
		holder.popup_menu.setOnClickListener(MusicListAdapter.this);
		return convertView;
	}

	public static class ViewHolder {
		TextView title;
		TextView artist;
		View indicator;
		ImageView popup_menu;
	}

	@Override
	public void onClick(View v) {
		v.showContextMenu();
	}
	
//	private void startMusicAnimation(int animationState,ImageView imageview){
//
//		if(animationState==ANIMATION_START){
//			startImageAni(imageview);
//		}else if(animationState==ANIMATION_PAUSE){
//			stopMusicAnimation(imageview);
//		}
//		
//	}
	
//	private void stopMusicAnimation(ImageView imageView){
//		Log.i("TAG", "停止了动画");
//		if(animaiton==null){
//			animaiton = (AnimationDrawable) imageView.getBackground();  
//		}
//		animaiton.stop();  
//	}
	
//	private void startImageAni(ImageView imageView){
//		Log.i("TAG", "启动了动画");
//		if(animaiton==null){
//			animaiton = (AnimationDrawable) imageView.getBackground();
//		}
//		animaiton.start();
//	}
}
