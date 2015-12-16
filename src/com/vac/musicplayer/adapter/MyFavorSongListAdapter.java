package com.vac.musicplayer.adapter;

import com.vac.musicplayer.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFavorSongListAdapter extends MyBaseAdapter<String> {

	private int colorValue=-1;
	public MyFavorSongListAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public long getItemId(int position) {
		return 0;
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
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mData.get(position));
		if (colorValue!=-1) {
			holder.content.setBackgroundColor(colorValue);
		}
		return convertView;
	}
	private class ViewHolder{
		private LinearLayout content;
		private TextView name;
	}
}
