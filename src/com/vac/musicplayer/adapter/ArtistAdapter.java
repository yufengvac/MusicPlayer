package com.vac.musicplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.R;
import com.vac.musicplayer.application.MyApplication;
import com.vac.musicplayer.bean.Artist;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.utils.ACache;
import com.vac.musicplayer.utils.HttpUtils;

public class ArtistAdapter extends BaseAdapter {
	private List<Artist> mData = null;
	private Context mContext = null;
	private ImageLoader mImageLoader ;
	private DisplayImageOptions mOptions;

	/** 默认初始化构造一个长度为0的数据列表 */
	public ArtistAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<Artist>();
		mImageLoader = ImageLoader.getInstance();
		mOptions = new MyApplication().getImageOptions(R.drawable.avatar, 0);
	}

	public void setData(List<Artist> data) {
		mData.clear();
		if (data != null) {
			mData.addAll(data);
		}
		notifyDataSetChanged();
	}

	public List<Artist> getData() {
		return mData;
	}

	@Override
	public boolean isEmpty() {
		return mData.size() == 0;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Artist getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_artist, null);
			holder.artist_name = (TextView) convertView
					.findViewById(R.id.artist_name);
			holder.num_of_tracks = (TextView) convertView
					.findViewById(R.id.num_of_tracks);
			holder.headpotriate = (ImageView) convertView.findViewById(R.id.item_artist_head);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (getItem(position).getArtistName().equals("<unknown>")) {
			holder.artist_name.setText(mContext.getResources().getString(
					R.string.unknown_artist));
		} else {
			holder.artist_name.setText(mData.get(position).getArtistName());
		}
		holder.num_of_tracks.setText(mData.get(position).getNumberOfTracks()+"首");
		
		if (!mData.get(position).getArtistName().equals(R.string.unknown_artist)) {
			holder.headpotriate.setTag(mData.get(position).getArtistName());
			toGetSingerPic(position,mData.get(position).getArtistName(),holder.headpotriate);
		}
		return convertView;
	}

	private void toGetSingerPic(final int position,final String artistName, final ImageView headpotriate) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		final ACache aCache = ACache.get(Constant.FILECACHE,"file");
		String json = aCache.getAsString(Constant.SEARCH_SINGER+"&q="+HttpUtils.encode(artistName));
		if (json!=null) {
			try {
				JSONObject obj = new JSONObject(json);
				if (obj.getInt("rows")>=1) {
					JSONArray array = obj.getJSONArray("data");
					if (array.length()>=1) {
						JSONObject obj_ = array.getJSONObject(0);
						String picUrl = obj_.getString("pic_url");
						String singerName = obj_.getString("singer_name");
						String tag = (String) headpotriate.getTag();
						if (tag.equals(singerName)) {
							mImageLoader.displayImage(picUrl, headpotriate,mOptions);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return;
		}
		httpClient.get(Constant.SEARCH_SINGER+"&q="+HttpUtils.encode(artistName), new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				try {
					String result = new String(arg2);
					JSONObject obj = new JSONObject(result);
					if (obj.getInt("rows")>=1) {
						JSONArray array = obj.getJSONArray("data");
						if (array.length()>=1) {
							JSONObject obj_ = array.getJSONObject(0);
							String picUrl = obj_.getString("pic_url");
							String singerName = obj_.getString("singer_name");
							String tag = (String) headpotriate.getTag();
							if (tag.equals(singerName)) {
								mImageLoader.displayImage(picUrl, headpotriate,mOptions);
							}
						}
						aCache.put(Constant.SEARCH_SINGER+"&q="+HttpUtils.encode(artistName), result);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	static class ViewHolder {
		TextView artist_name;
		TextView num_of_tracks;
		ImageView headpotriate;
	}
}
