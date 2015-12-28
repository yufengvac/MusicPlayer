package com.vac.musicplayer.fragment.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
import zrc.widget.ZrcListView.OnItemClickListener;
import zrc.widget.ZrcListView.OnStartListener;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.SearchSongListAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.NetParam;
import com.vac.musicplayer.bean.TingSongList;
import com.vac.musicplayer.utils.HttpUtils;

public class SearchSongListFra extends Fragment {

	private int page =1;
	private int colorValue;
	private String searchContent;
	private ZrcListView mZrcListView;
	private TextView totalTextview;
	private SearchSongListAdapter mSongListAdapter;
	private boolean isLoadMore;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == HttpUtils.NETSUCCESS) {
				String result = (String) msg.obj;
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
		};
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_result_songlist_fra, null);
		searchContent = getArguments().getString("search");
		colorValue = getArguments().getInt("color");
		initView(view);
		page = 1;
		searchSongList(searchContent,page,true,false);
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
				isLoadMore = false;
				searchSongList(searchContent, page, false,true);
			}
		});
        
        mZrcListView.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				isLoadMore = true;
				searchSongList(searchContent, page, false,true);
			}
		});
        mZrcListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ZrcListView parent, View view, int position, long id) {
				
			}
		});
	}
	private void searchSongList(String searchContent, int index, boolean isUseCache,boolean isToCache) {
		
		HttpUtils hu = new HttpUtils(getActivity(), mHandler);
		ArrayList<NetParam> params = new ArrayList<NetParam>();
		params.add(new NetParam("q", searchContent));
		params.add(new NetParam("page", ""+page));
		hu.get(Constant.TING_SONGLIST,params, isUseCache, isToCache);
	}
}
