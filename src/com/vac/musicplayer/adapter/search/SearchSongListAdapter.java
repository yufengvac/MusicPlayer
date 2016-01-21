package com.vac.musicplayer.adapter.search;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.bean.TingSongList;

public class SearchSongListAdapter extends MyBaseAdapter<TingSongList>{

	public SearchSongListAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_search_songlist_result, null);
			holder.songlistpic = (ImageView) convertView.findViewById(R.id.item_search_songlist_pic);
			holder.name = (TextView) convertView.findViewById(R.id.item_search_songlist_name);
			holder.num = (TextView) convertView.findViewById(R.id.item_search_songlist_number);
			holder.play_count = (TextView) convertView.findViewById(R.id.item_search_songlist_playcount);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		TingSongList songList = mData.get(position);
		holder.name.setText(songList.getTitle());
		holder.num.setText(songList.getSong_list().length+"首");
		mImageLoader.displayImage(songList.getPic_url(), holder.songlistpic, mOptions);
		
		int count = songList.getListen_count();
		if (count<10000) {
			holder.play_count.setText(count+"人播放");
		}else{
			DecimalFormat df = new DecimalFormat("0.0");
			holder.play_count.setText(df.format(count*1.0/10000)+"万人播放");
		}
		return convertView;
	}

	private class ViewHolder{
		private ImageView songlistpic;
		private TextView name,num,play_count;
	}
}
