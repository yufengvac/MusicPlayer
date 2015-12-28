package com.vac.musicplayer.fragment.search.detail;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.SearchSingleSongAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.TingAlbumDetail;
import com.vac.musicplayer.utils.HttpUtils;

public class SearchAlbumDetailFra extends Fragment {

	private TextView titlebar_title,titlebar_singer,titlebar_date,titlebar_produce;
	private ListView songListview;
	private ImageView pic;
	private ImageLoader mImageLoader ;
	private SearchSingleSongAdapter mSongAdapter;
	private long albumId= 0l;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==HttpUtils.NETSUCCESS) {
				try {
					String result = (String) msg.obj;
					JSONObject obj = new JSONObject(result);
					if (obj.getString("msg").equals("ok")) {
						JSONObject obj_ = obj.getJSONObject("data");
						TingAlbumDetail tad = TingAlbumDetail.jsonToBean(obj_);
						titlebar_title.setText(tad.getTingAlbum().getName());
						titlebar_singer.setText(tad.getTingAlbum().getSingerName());
						titlebar_date.setText(tad.getTingAlbum().getPublishDate()+"发行");
						titlebar_produce.setText(tad.getTingAlbum().getDescription());
						mSongAdapter.setData(tad.getSongList());
						mImageLoader.displayImage(tad.getTingAlbum().getPicUrl(), pic);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_album_detail_fra, null);
		albumId = getArguments().getLong("albumId");
		initView(view);
		return view;
	}
	private void initView(View view) {
		titlebar_title = (TextView) view.findViewById(R.id.search_album_detail_fra_title);
		titlebar_singer = (TextView) view.findViewById(R.id.search_album_detail_fra_singer);
		titlebar_date = (TextView) view.findViewById(R.id.search_album_detail_fra_publish_date);
		titlebar_produce = (TextView) view.findViewById(R.id.search_album_detail_fra_produce);
		songListview = (ListView) view.findViewById(R.id.search_album_detail_fra_listview);
		pic = (ImageView) view.findViewById(R.id.search_album_detail_fra_pic);
		mImageLoader = ImageLoader.getInstance();
		mSongAdapter = new SearchSingleSongAdapter(getActivity());
		songListview.setAdapter(mSongAdapter);
		
		HttpUtils httpUtils = new HttpUtils(getActivity(), mHandler);
		httpUtils.get(Constant.SEARCH_ALBUM_PATE1+albumId, true, true);
	}
}
