package com.vac.musicplayer.fragment;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.NetSong;
import com.vac.musicplayer.fragment.search.TabSearchFra;

public class SearchFragment extends Fragment {

	private LinearLayout title_content;
	private int colorValue;
	private EditText search_edit;
	private ImageView search_go;
	private FragmentManager fm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, null);
		initView(view);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		title_content.setBackgroundColor(colorValue);
		return view;
	}
	private void initView(View view) {
		title_content = (LinearLayout) view.findViewById(R.id.search_fra_titlebar_content);
		search_edit = (EditText) view.findViewById(R.id.search_fragment_edit);
		search_go = (ImageView) view.findViewById(R.id.tab_main_fra_search);
		search_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (search_edit.getText().toString().trim().isEmpty()) {
					return;
				}
				replace(search_edit.getText().toString().trim());
			}
		});
		
		fm = getChildFragmentManager();
	}
	protected void replace(String trim) {
		TabSearchFra tsf = new TabSearchFra();
		Bundle bundle = new Bundle();
		bundle.putInt("color", colorValue);
		bundle.putString("search", trim);
		tsf.setArguments(bundle);
		fm.beginTransaction().replace(R.id.search_fragment_content, tsf).commit();
	}
	
}
