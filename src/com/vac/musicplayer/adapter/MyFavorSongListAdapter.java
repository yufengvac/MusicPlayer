package com.vac.musicplayer.adapter;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.MusicGroup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFavorSongListAdapter extends MyBaseAdapter<MusicGroup> {

	private int colorValue=-1;
	public MyFavorSongListAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).getId();
	}
	public void setColorValue(int color){
		this.colorValue =color;
		notifyDataSetChanged();
	}
	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_my_favor_song_list, null);
			holder.content = (LinearLayout) convertView.findViewById(R.id.item_my_favor_content);
			holder.name = (TextView) convertView.findViewById(R.id.item_my_favor_name);
			holder.count = (TextView) convertView.findViewById(R.id.item_my_favor_count);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mData.get(position).getTitle());
		if (colorValue!=-1) {
			holder.content.setBackgroundColor(colorValue);
		}
		holder.count.setText(mData.get(position).getItems().size()+"首");
		return convertView;
	}
	private class ViewHolder{
		private LinearLayout content;
		private TextView name;
		private TextView count;
	}
}
