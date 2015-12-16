package com.vac.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.vac.musicplayer.R;
import com.vac.musicplayer.application.MyApplication;
/**
 * @author vac
 * @description 选择皮肤适配器，里面填充的是ImageView
 * @date 2015年12月16日10:59:37
 *
 */
public class ChangeSkinAdapter extends MyBaseAdapter<String> {

	private DisplayImageOptions options = null;
	public ChangeSkinAdapter(Context mContext) {
		super(mContext);
		MyApplication app =new MyApplication();
		options = app.getImageOptions(R.drawable.launcher, 0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	public Object getItem(int arg0) {
		return mData.get(arg0);
	};

	ViewHolder holder;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_change_skin_imageview, null);
			holder.img = (ImageView) convertView.findViewById(R.id.item_skin_imageview);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		mImageLoader.displayImage(mData.get(position), holder.img, options);
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
	}
}
