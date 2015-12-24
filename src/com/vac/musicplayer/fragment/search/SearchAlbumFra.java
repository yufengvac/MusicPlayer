package com.vac.musicplayer.fragment.search;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vac.musicplayer.R;

public class SearchAlbumFra extends Fragment {

	private int index =2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_result_fra, null);
		
		new Thread(){
			public void run() {
//					http://s.music.163.com/search/get?filterDj=true&s=%E8%AE%B8%E5%B5%A9&limit=10&type=
//					search(index);
			};
		}.start();
		return view;
	}
	
	public void search(int type){
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("type", ""+type);
		params.add("s", "许嵩");
		params.add("limit", "20");
		params.add("offset", "0");
		params.add("filterDj", "true");
		mClient.get("http://s.music.163.com/search/get", params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				String result = new String(arg2);
				Log.v("TAG","第 "+index +" 次搜索的结果是："+result);
				try {
					JSONObject obj = new JSONObject(result);
					int code = obj.getInt("code");
					if (code!=200) {
						if (index<1001) {
							search(index++);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
				Log.v("TAG", "搜索的结果是：失败");
				if (index<1001) {
					search(index++);
				}
			}
		});
	}
}
