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
import com.vac.musicplayer.adapter.search.SearchMVAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.NetParam;
import com.vac.musicplayer.bean.TingSearchMV;
import com.vac.musicplayer.utils.HttpUtils;

public class SearchMVFra extends Fragment {

	private ZrcListView mZrcListView;
	private TextView total_textiview;
	private int index =1;
	private int colorValue = -1;
	private String searchContent;
	private SearchMVAdapter mMvAdapter;
	private boolean isLoadMore;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==HttpUtils.NETSUCCESS) {
				String result = (String) msg.obj;
				try {
					JSONObject obj = new JSONObject(result);
					int pages = obj.getInt("pages");
					int rows = obj.getInt("rows");
					total_textiview.setText("已经为你找到"+rows+"首相关MV");
						JSONArray array = obj.getJSONArray("data");
						ArrayList<TingSearchMV> tingMVList = new ArrayList<TingSearchMV>();
						tingMVList.clear();
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj1= array.getJSONObject(i);
							tingMVList.add(TingSearchMV.jsonToBean(obj1));
						}
						if (isLoadMore) {
							mMvAdapter.addData(tingMVList);
						}else{
							mMvAdapter.setData(tingMVList);
							mZrcListView.setRefreshSuccess("加载成功");
						}
						if (index>=pages) {
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
		View view = inflater.inflate(R.layout.search_result_mv_fra, null);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		searchContent = bundle.getString("search");
		initView(view);
		index =1;
		searchMV(searchContent, index, true,true);
		return view;
	}
	private void initView(View view) {
		mZrcListView = (ZrcListView) view.findViewById(R.id.search_result_mv_zrclistview);
		total_textiview = (TextView) view.findViewById(R.id.search_result_mv_totalcount);
		mMvAdapter = new SearchMVAdapter(getActivity());
		mZrcListView.setAdapter(mMvAdapter);
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
       
       //下拉刷新
       mZrcListView.setOnRefreshStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				index = 1;
				isLoadMore = false;
				searchMV(searchContent,index,false,true);
			}
		});
       
       //上拉加载
       mZrcListView.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				index++;
				isLoadMore = true;
				searchMV(searchContent, index,false,true);
			}
		});
	}
	protected void searchMV(String searchContent, int page,  boolean isUseCache,boolean isToCache) {
		HttpUtils hu = new HttpUtils(getActivity(), mHandler);
		ArrayList<NetParam> params = new ArrayList<NetParam>();
		params.add(new NetParam("q", searchContent));
		params.add(new NetParam("page", page+""));
		hu.get(Constant.TING_MV,params, isUseCache, isToCache);
	}
}
