package com.vac.musicplayer.adapter.search;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.bean.TingSearchMV;

public class SearchMVAdapter extends MyBaseAdapter<TingSearchMV> {

	public SearchMVAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	ViewHolder holder;
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			convertView = mlayoutInflater.inflate(R.layout.item_search_mv_result, null);
			holder = new ViewHolder();
			holder.pic = (ImageView) convertView.findViewById(R.id.item_search_mv_pic);
			holder.name = (TextView) convertView.findViewById(R.id.item_search_mv_name);
			holder.singer = (TextView) convertView.findViewById(R.id.item_search_mv_singer);
			holder.number = (TextView) convertView.findViewById(R.id.item_search_mv_playnum);
			holder.size = (TextView) convertView.findViewById(R.id.item_search_mv_size);
			convertView.setTag(holder);
		}else{
			holder  = (ViewHolder) convertView.getTag();
		}
		TingSearchMV tsm = mData.get(position);
		holder.name.setText(tsm.getVideoName());
		holder.singer.setText(tsm.getSingerName());
		holder.number.setText(tsm.getPickCount()+"");
		mImageLoader.displayImage(tsm.getPicUrl(), holder.pic, mOptions);
		
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		String hms = formatter.format(tsm.getMvList().get(0).getDuration()); 
		holder.size.setText(hms);
		
		return convertView;
	}

	private class ViewHolder{
		private ImageView pic;
		private TextView size,name,singer,number;
	}
}
