package com.vac.musicplayer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.service.MusicService.MusicServiceBinder;
import com.vac.musicplayer.service.MusicService.PlayState;

public class MusicListQueueAdapter extends BaseAdapter {

	private Context mContext;
	private List<Music> mData;
	private int mActivitedPosition=-1;
	private int mState =-1;
	private MusicServiceBinder mBinder;
	
	public MusicListQueueAdapter(Context mContext, List<Music> mData,MusicServiceBinder binder) {
		this.mContext = mContext;
		this.mData = mData;
		this.mBinder = binder;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return mData.get(arg0).getId();
	}
	
	public void setFlagInPosition(int state,int position){
		this.mState = state;
		this.mActivitedPosition  = position;
		notifyDataSetChanged();
	}
	
	public void removeOne(int position){
		mData.remove(position);
		notifyDataSetChanged();
	}

	ViewHolder holder;
	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music_list_queue, null);
			holder.play_indicator = (ImageView) convertView.findViewById(R.id.play_queue_indicator);
			holder.play_title = (TextView) convertView.findViewById(R.id.play_queue_music_title);
			holder.play_artist = (TextView) convertView.findViewById(R.id.play_queue_music_singer);
			holder.play_delete = (ImageView) convertView.findViewById(R.id.play_queue_delete);
			holder.play_delete_linear  = (LinearLayout) convertView.findViewById(R.id.play_queue_delete_linear);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.play_title.setText(mData.get(position).getTitle());
		holder.play_artist.setText(mData.get(position).getArtist());
		holder.play_delete_linear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(position==mActivitedPosition){
					mBinder.removeMusicFromCurrentList(getItemId(position));
				}
				removeOne(position);
			}
		});
		
		if(position==mActivitedPosition){
			holder.play_indicator.setVisibility(View.VISIBLE);
			holder.play_title.setTextColor(mContext.getResources().getColor(R.color.holo_blue_dark));
			holder.play_artist.setTextColor(mContext.getResources().getColor(R.color.holo_blue_dark));
			if(mState==PlayState.Playing){
				holder.play_indicator.setBackgroundResource(R.drawable.img_media_controller_play);
			}else if(mState==PlayState.Paused){
				holder.play_indicator.setBackgroundResource(R.drawable.img_media_controller_pause);
			}
		}else{
			holder.play_indicator.setVisibility(View.INVISIBLE);
			holder.play_title.setTextColor(mContext.getResources().getColor(R.color.black));
			holder.play_artist.setTextColor(mContext.getResources().getColor(R.color.black));
		}
		
		return convertView;
	}

	private class ViewHolder{
		private ImageView play_indicator;
		private TextView play_title;
		private TextView play_artist;
		private ImageView play_delete;
		private LinearLayout play_delete_linear;
	}
}
