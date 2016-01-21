package com.vac.musicplayer.adapter.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.bean.NetSong;

public class SearchAdapter extends MyBaseAdapter<NetSong> {

	private int colorValue;
	public SearchAdapter(Context mContext) {
		super(mContext);
	}

	public void setColor(int color){
		this.colorValue = color;
	}
	@Override
	public long getItemId(int position) {
		return mData.get(position).getId();
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder= new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_search_result, null);
			holder.name = (TextView) convertView.findViewById(R.id.item_search_result_name);
			holder.artist = (TextView) convertView.findViewById(R.id.item_search_result_artist);
			holder.album = (TextView) convertView.findViewById(R.id.item_search_result_album);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mData.get(position).getName());
		holder.artist.setText(mData.get(position).getArtists().get(0).getName());
		holder.artist.setTextColor(colorValue);
		holder.album.setText("·"+mData.get(position).getAlbum().getName());
		return convertView;
	}

	private class ViewHolder{
		private TextView name,artist,album;
	}
}
