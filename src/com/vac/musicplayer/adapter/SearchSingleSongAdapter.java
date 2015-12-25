package com.vac.musicplayer.adapter;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.TingSingleSong;

public class SearchSingleSongAdapter extends MyBaseAdapter<TingSingleSong> {

	private int colorValue;
	public SearchSingleSongAdapter(Context mContext) {
		super(mContext);
	}

	public void setColor(int color){
		this.colorValue = color;
	}
	@Override
	public long getItemId(int position) {
		return mData.get(position).getSongId();
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder= new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_search_singlesong_result, null);
			holder.name = (TextView) convertView.findViewById(R.id.item_search_result_name);
			holder.artist = (TextView) convertView.findViewById(R.id.item_search_result_artist);
			holder.album = (TextView) convertView.findViewById(R.id.item_search_result_album);
			holder.hq = (TextView) convertView.findViewById(R.id.item_search_result_hq);
			holder.mv = (TextView) convertView.findViewById(R.id.item_search_result_mv);
			holder.num = (TextView) convertView.findViewById(R.id.item_search_result_num);
			holder.favor = (TextView) convertView.findViewById(R.id.item_search_result_favor);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(mData.get(position).getName());
		holder.artist.setText(mData.get(position).getSingerName());
		holder.album.setText("·"+mData.get(position).getAlbumName());
		if (mData.get(position).getAuditionList().size()>2) {
			holder.hq.setVisibility(View.VISIBLE);
		}else{
			holder.hq.setVisibility(View.GONE);
		}
		if (mData.get(position).getMvList().size()>0) {
			holder.mv.setVisibility(View.VISIBLE);
		}else{
			holder.mv.setVisibility(View.GONE);
		}
		int favorites = mData.get(position).getFavorites();
		DecimalFormat df = new DecimalFormat("0.0");
		if (favorites>=10000) {
			holder.favor.setText(df.format(favorites*1.0/10000)+"万");
		}else{
			holder.favor.setText(""+favorites);
		}
		holder.num.setText(""+(position+1));
		
		if (mData.get(position).getAuditionList().size()==0) {
			holder.name.setTextColor(mContext.getResources().getColor(R.color.grey_white_));
			holder.artist.setTextColor(mContext.getResources().getColor(R.color.grey_white_));
		}else{
			holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
			holder.artist.setTextColor(colorValue);
		}
		return convertView;
	}

	private class ViewHolder{
		private TextView name,artist,album;
		private TextView hq,mv,num,favor;
	}
}
