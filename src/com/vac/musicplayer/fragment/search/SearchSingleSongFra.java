package com.vac.musicplayer.fragment.search;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnStartListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.SearchSingleSongAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.TingSingleSong;

public class SearchSingleSongFra extends Fragment {

	private static final String TAG = SearchSingleSongFra.class.getSimpleName();
	private ZrcListView zrcListview;
//	private SearchAdapter mAdapter;
	private SearchSingleSongAdapter mSingleSongAdapter;
	private int colorValue=-1;
	private String searchContent;
	private int index=1;
	private int limit =20;
	private TextView total_count_textview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_result_single_song_fra, null);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		searchContent = bundle.getString("search");
		initView(view);
		
//		search(searchContent,0,false);
		index = 1;
		searchTing(searchContent, index, false);
		return view;
	}
	private void initView(View view) {
		
		zrcListview = (ZrcListView) view.findViewById(R.id.search_singsong_zrclistview);
		
		 // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(colorValue);
        header.setCircleColor(colorValue);
        zrcListview.setHeadable(header);
        
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(colorValue);
        zrcListview.setFootable(footer);
        
        //下拉刷新
        zrcListview.setOnRefreshStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
//				refresh();
				index = 1;
				searchTing(searchContent,index,false);
			}
		});
        
        //上拉加载
        zrcListview.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				index++;
				searchTing(searchContent, index,true);
			}
		});
        
//        mAdapter = new SearchAdapter(getActivity());
//        mAdapter.setColor(colorValue);
//        zrcListview.setAdapter(mAdapter);
//        zrcListview.setDivider(null);
        
        mSingleSongAdapter = new SearchSingleSongAdapter(getActivity());
        mSingleSongAdapter.setColor(colorValue);
        zrcListview.setAdapter(mSingleSongAdapter);
        zrcListview.setDivider(null);
        
        total_count_textview = (TextView) view.findViewById(R.id.search_result_totalcount);
	}
	
//	public void setData(ArrayList<NetSong> list,boolean isClear){
//		if (isClear) {
//			mAdapter.setData(list);
//			zrcListview.setRefreshSuccess("加载成功");
//		}else{
//			mAdapter.addData(list);
//			zrcListview.setLoadMoreSuccess();
//		}
//	}
	
	public void setTingData(ArrayList<TingSingleSong> list,boolean isClear){
		if (isClear) {
			mSingleSongAdapter.setData(list);
			zrcListview.setRefreshSuccess("加载成功");
		}else{
			mSingleSongAdapter.addData(list);
			zrcListview.setLoadMoreSuccess();
		}
	}
	
//	private void search(String trim,int offest,final boolean isLoadMore) {
//		AsyncHttpClient mClient = new AsyncHttpClient();
//		RequestParams params = new RequestParams();
//		params.add("type", "1");
//		params.add("filterDj", "true");
//		params.add("s", ""+trim);
//		params.add("limit", ""+limit);
//		params.add("offset", offest+"");
//		mClient.get(Constant.SEARCH_MUSIC,params,new AsyncHttpResponseHandler(){
//			@Override
//			public void onStart() {
//				super.onStart();
//			}
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				super.onSuccess(arg0, arg1, arg2);
//				String result = new String(arg2);
//				Log.d(TAG, result);
//				try {
//					JSONObject obj = new JSONObject(result);
//					int code = obj.getInt("code");
//					if (code==200) {
//						JSONObject obj1 = obj.getJSONObject("result");
//						int songTotal = obj1.getInt("songCount");
//						JSONArray array = obj1.getJSONArray("songs");
//						ArrayList<NetSong> netsongList = new ArrayList<NetSong>();
//						for (int i = 0; i < array.length(); i++) {
//							JSONObject obj2= array.getJSONObject(i);
//							netsongList.add(NetSong.jsonToBean(obj2));
//						}
//						if (isLoadMore) {
//							setData(netsongList,false);
//								if ((songTotal-index*limit<limit)&&(songTotal-index*limit)>=0) {
//									zrcListview.stopLoadMore();
//								}else{
//									index++;	
//								}
//						}else{
//							setData(netsongList, true);
//							 zrcListview.startLoadMore(); // 开启LoadingMore功能
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
//					Throwable arg3) {
//				super.onFailure(arg0, arg1, arg2, arg3);
//			}
//		});
//	}
	
	
	private void searchTing(String trim,int page,final boolean isLoadMore) {
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("q", ""+trim);
		params.add("page",""+page);
		Log.d(TAG, "page="+page);
		mClient.get(Constant.TING_SINGER_SONG,params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				String result = new String(arg2);
				Log.d(TAG, result);
				try {
					JSONObject obj = new JSONObject(result);
					int pageCount = obj.getInt("pageCount");
					int totalCount = obj.getInt("totalCount");
					total_count_textview.setText("已经为你找到"+totalCount+"首相关歌曲");
						JSONArray array = obj.getJSONArray("data");
						ArrayList<TingSingleSong> tingSingleSongList = new ArrayList<TingSingleSong>();
						tingSingleSongList.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj1= array.getJSONObject(i);
							tingSingleSongList.add(TingSingleSong.jsonToBean(obj1));
						}
						if (isLoadMore) {
							setTingData(tingSingleSongList,false);
								if (index>=pageCount) {
									zrcListview.stopLoadMore();
								}else{
									zrcListview.startLoadMore(); // 开启LoadingMore功能
								}
						}else{
							setTingData(tingSingleSongList, true);
							 zrcListview.startLoadMore(); // 开启LoadingMore功能
						}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});
	}
}
