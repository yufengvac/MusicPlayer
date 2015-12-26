package com.vac.musicplayer.fragment.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zrc.widget.SimpleFooter;
import zrc.widget.SimpleHeader;
import zrc.widget.ZrcListView;
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
import com.vac.musicplayer.adapter.SearchAlbumAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.NetParam;
import com.vac.musicplayer.bean.TingAlbum;
import com.vac.musicplayer.utils.HttpUtils;

public class SearchAlbumFra extends Fragment {

	private int index =1;
	private int colorValue;
	private String searchContent;
	private ZrcListView mZrcListView;
	private TextView totalTextview;
	private SearchAlbumAdapter mAlbumAdapter;
	private boolean isLoadMore;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==HttpUtils.NETSUCCESS) {
				String result  = (String) msg.obj;
				try {
					JSONObject obj = new JSONObject(result);
					int pageCount = obj.getInt("pageCount");
					int totalCount = obj.getInt("totalCount");
					totalTextview.setText("已经为你找到"+totalCount+"张相关专辑");
					ArrayList<TingAlbum> albumList = new ArrayList<TingAlbum>();
					JSONArray array = obj.getJSONArray("data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj_ = array.getJSONObject(i);
						albumList.add(TingAlbum.jsonToBean(obj_));
					}
					if (isLoadMore) {
						mAlbumAdapter.addData(albumList);
					}else{
						mAlbumAdapter.setData(albumList);
						mZrcListView.setRefreshSuccess("加载成功");
					}
					if (index>=pageCount) {
						mZrcListView.stopLoadMore();
					}else{
						mZrcListView.startLoadMore();
						index++;
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
		View view = inflater.inflate(R.layout.search_result_album_fra, null);
		searchContent = getArguments().getString("search");
		colorValue = getArguments().getInt("color");
		initView(view);
		searchAlbum(searchContent,index,true,true);
		return view;
	}
	
	private void initView(View view) {
		mZrcListView = (ZrcListView) view.findViewById(R.id.search_result_aibum_zrclistview);
		mZrcListView.setDivider(null);
		totalTextview = (TextView) view.findViewById(R.id.search_result_album_totalcount);
		mAlbumAdapter = new SearchAlbumAdapter(getActivity());
		mZrcListView.setAdapter(mAlbumAdapter);
		
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
				index = 1;
				isLoadMore = false;
				searchAlbum(searchContent, index, false,true);
			}
		});
        
        mZrcListView.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				isLoadMore = true;
				searchAlbum(searchContent, index, false,true);
			}
		});
	}

	public void searchAlbum(String trim,int page,boolean isUseCache,boolean isToCache){
		HttpUtils hu = new HttpUtils(getActivity(),mHandler);
		ArrayList<NetParam> paramsList = new ArrayList<NetParam>();
		paramsList.add(new NetParam("q", trim));
		paramsList.add(new NetParam("page", ""+page));
		hu.get(Constant.TING_ALBUM,paramsList, isUseCache, isToCache);
	}
}
