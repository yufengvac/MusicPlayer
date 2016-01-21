package com.vac.musicplayer.adapter.search;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.application.MyApplication;
import com.vac.musicplayer.bean.TingAlbum;

public class SearchAlbumAdapter extends MyBaseAdapter<TingAlbum> {

	private DisplayImageOptions mOptions;
	public SearchAlbumAdapter(Context mContext) {
		super(mContext);
		 mOptions = new MyApplication().getImageOptions(R.drawable.launcher, 0);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).getAlbumId();
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_search_album_result, null);
			holder.name = (TextView) convertView.findViewById(R.id.item_search_album_name);
			holder.singer = (TextView) convertView.findViewById(R.id.item_search_album_singer);
			holder.publishDate = (TextView) convertView.findViewById(R.id.item_search_album_publishdate);
			holder.pic = (ImageView) convertView.findViewById(R.id.item_search_album_pic);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		TingAlbum ta = mData.get(position);
		holder.name.setText(ta.getName());
		holder.singer.setText(ta.getSingerName());
		holder.publishDate.setText(ta.getPublishDate());
		mImageLoader.displayImage(ta.getPicUrl(), holder.pic, mOptions);
		return convertView;
	}

	private class ViewHolder{
		private TextView name,singer,publishDate;
		private ImageView pic;
	}
}
