package com.vac.musicplayer.fragment.search;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.SearchHistoryAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.listener.OnPageAddListener;
import com.vac.musicplayer.myview.WordWrapView;
import com.vac.musicplayer.utils.HttpUtils;
import com.vac.musicplayer.utils.PreferHelper;

/***
 * 热门搜索
 * @author vac
 *
 */
public class SearchHotFra extends Fragment {

	private WordWrapView mWordWrapView;
	private TextView search_hot_title,search_hot_title_history;
	private ListView search_history_listview;
	private SearchHistoryAdapter mHistoryAdapter;
	private ArrayList<String> historyList  = new ArrayList<String>();
	private int color = -1;
	private OnHotClickListener mHotListener;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==HttpUtils.NETSUCCESS) {
				try {
					String result = (String) msg.obj;
					JSONObject obj = new JSONObject(result);
					if (obj.getInt("totalCount")>0) {
						JSONArray array = obj.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj_ = array.getJSONObject(i);
							final String val = obj_.getString("val");
							 TextView textview = new TextView(getActivity());
						      textview.setText(val);
						      mWordWrapView.addView(textview);
						      textview.setClickable(true);
						      textview.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									mHotListener.onHotClick(val);
								}
							});
						}
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
		View view = inflater.inflate(R.layout.search_hot_fra, null);
		color = getArguments().getInt("color");
		initView(view);
		return view;
	}
	
	public void setOnHotListener(OnHotClickListener listener){
		this.mHotListener = listener;
	}
	
	private void initView(View view) {
		mWordWrapView = (WordWrapView) view.findViewById(R.id.search_hot_wordwrapview);
		HttpUtils httpUtils = new HttpUtils(getActivity(), mHandler);
		httpUtils.get(Constant.SEARCH_HOT, false, true);
		
		search_hot_title = (TextView) view.findViewById(R.id.search_hot_fra_title);
		search_hot_title.setTextColor(color);
		
		search_hot_title_history = (TextView) view.findViewById(R.id.search_hot_fra_title_history);
		search_hot_title_history.setTextColor(color);
		search_history_listview = (ListView) view.findViewById(R.id.search_hot_fra_history_listview);
		mHistoryAdapter = new SearchHistoryAdapter(getActivity());
		search_history_listview.setAdapter(mHistoryAdapter);
		search_history_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mHotListener.onHotClick((String)mHistoryAdapter.getItem(arg2));
			}
		});
		
		String history = PreferHelper.readString(getActivity(), Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY);
		if (history!=null) {
			search_hot_title_history.setVisibility(View.VISIBLE);
			search_history_listview.setVisibility(View.VISIBLE);
			String[] historys = history.split(",");
			int len = historys.length;
			if (historys.length>=10) {
				len = 10;
			}
			for (int i = 0; i < len; i++) {
				if (historys[len-i-1]!=null&&(!historys[len-i-1].isEmpty())) {
					historyList.add(historys[len-i-1]);
				}
			}
			mHistoryAdapter.setData(historyList);
		}else{
			search_hot_title_history.setVisibility(View.GONE);
			search_history_listview.setVisibility(View.GONE);
		}
	}
	
	public interface OnHotClickListener{
		public void onHotClick(String searchContent);
	}
}
