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
import com.vac.musicplayer.adapter.SearchAlbumAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.TingAlbum;

public class SearchAlbumFra extends Fragment {

	private int index =1;
	private int colorValue;
	private String searchContent;
	private ZrcListView mZrcListView;
	private TextView totalTextview;
	private SearchAlbumAdapter mAlbumAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_result_album_fra, null);
		searchContent = getArguments().getString("search");
		colorValue = getArguments().getInt("color");
		initView(view);
		searchAlbum(searchContent,index,false);
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
				searchAlbum(searchContent, index, false);
			}
		});
        
        mZrcListView.setOnLoadMoreStartListener(new OnStartListener() {
			
			@Override
			public void onStart() {
				searchAlbum(searchContent, index, true);
			}
		});
	}

	public void searchAlbum(String trim,int page,final boolean isLoadMore){
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("page", page+"");
		params.add("q", trim);
		mClient.get(Constant.TING_ALBUM, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				String result = new String(arg2);
				Log.v("TAG","结果是："+result);
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
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
			
			}
		});
	}
}
