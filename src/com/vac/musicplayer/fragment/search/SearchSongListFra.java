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
import com.vac.musicplayer.adapter.SearchSongListAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.TingSongList;

public class SearchSongListFra extends Fragment {

	private int page =1;
	private int colorValue;
	private String searchContent;
	private ZrcListView mZrcListView;
	private TextView totalTextview;
	private SearchSongListAdapter mSongListAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_result_songlist_fra, null);
		searchContent = getArguments().getString("search");
		colorValue = getArguments().getInt("color");
		initView(view);
		page = 1;
		searchSongList(searchContent,page,false);
		return view;
	}
	private void initView(View view) {
		mZrcListView = (ZrcListView) view.findViewById(R.id.search_result_songlist_zrclistview);
//		mZrcListView.setDivider(null);
		totalTextview = (TextView) view.findViewById(R.id.search_result_songlist_totalcount);
		mSongListAdapter = new SearchSongListAdapter(getActivity());
		mZrcListView.setAdapter(mSongListAdapter);
		mZrcListView.setDivider(null);
		
		 // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(colorValue);
        header.setCircleColor(colorValue);
        mZrcListView.setHeadable(header);
        
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(colorValue);
        mZrcListView.setFootable(footer);
		
        mZrcListView.setOnRefreshStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				page = 1;
				searchSongList(searchContent, page, false);
			}
		});
        
        mZrcListView.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				searchSongList(searchContent, page, true);
			}
		});
	}
	private void searchSongList(String searchContent, int index,final boolean isLoadMore) {
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("page", index+"");
		params.add("q", searchContent);
		mClient.get(Constant.TING_SONGLIST, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				String result = new String(arg2);
				Log.v("TAG","结果是："+result);
				try {
					JSONObject obj = new JSONObject(result);
					int pageCount = obj.getInt("pages");
					int rows = obj.getInt("rows");
					totalTextview.setText("已经为你找到"+rows+"张相关歌单");
					ArrayList<TingSongList> albumList = new ArrayList<TingSongList>();
					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj_ = array.getJSONObject(i);
						albumList.add(TingSongList.jsonToBean(obj_));
					}
					if (isLoadMore) {
						mSongListAdapter.addData(albumList);
					}else{
						mSongListAdapter.setData(albumList);
						mZrcListView.setRefreshSuccess("加载成功");
					}
					if (page>=pageCount) {
						mZrcListView.stopLoadMore();
					}else{
						mZrcListView.startLoadMore();
						page++;
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
