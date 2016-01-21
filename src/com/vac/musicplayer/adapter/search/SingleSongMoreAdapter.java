package com.vac.musicplayer.adapter.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.adapter.search.SingleSongMoreAdapter.SingleSongMore;

public class SingleSongMoreAdapter extends MyBaseAdapter<SingleSongMore> {

	public SingleSongMoreAdapter(Context mContext) {
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
			convertView = mlayoutInflater.inflate(R.layout.item_single_song_more_option, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.item_single_song_more_option_img);
			holder.tv = (TextView) convertView.findViewById(R.id.item_single_song_more_option_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(mData.get(position).getText());
		return convertView;
	}
	public static class ViewHolder{
		private ImageView img;
		private TextView tv;
	}
	public class SingleSongMore{
		
		private String url;
		private String text;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}
	public SingleSongMore getSingleSongMore(){
		return new SingleSongMore();
	}
}
