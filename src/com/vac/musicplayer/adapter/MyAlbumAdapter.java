package com.vac.musicplayer.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Album;

public class MyAlbumAdapter extends BaseAdapter {

	private Context mContext;
	private List<Album> mData;
	
	public MyAlbumAdapter(Context mContext, List<Album> mData) {
		super();
		this.mContext = mContext;
		this.mData = mData;
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
		return mData.get(arg0).getAlbum_id();
	}

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if(convertView==null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ablum, null);
			holder.pic = (ImageView) convertView.findViewById(R.id.item_album_pic);
			holder.name = (TextView) convertView.findViewById(R.id.item_blum_name);
			holder.artist = (TextView) convertView.findViewById(R.id.item_blum_artist);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Log.v("TAG", mData.get(position).getAlbum_pic()+","+mData.get(position).getAlbum_name()+","+mData.get(position).getAlbum_num());
		String url = mData.get(position).getAlbum_pic();
		if (url != null) {
			if (url.endsWith(".jpg") || url.endsWith(".png")) {
				Bitmap bitmap = BitmapFactory.decodeFile(url);
				if (bitmap != null) {
					holder.pic.setImageBitmap(bitmap);
					bitmap.recycle();
				}
			} else if (url != null) {
				url = url + ".jpg";
				Bitmap bitmap = BitmapFactory.decodeFile(url);
				if (bitmap != null) {
					holder.pic.setImageBitmap(bitmap);
					bitmap.recycle();
				}
			}
		}
		holder.name.setText(mData.get(position).getAlbum_name());
		holder.artist.setText(mData.get(position).getAlbum_num()+"");
		return convertView;
	}

	private class ViewHolder{
		private ImageView pic;
		private TextView name,artist;
	}
}
