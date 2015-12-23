package com.vac.musicplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vac.musicplayer.R;

public class SearchFragment extends Fragment {

	private LinearLayout title_content;
	private int colorValue;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, null);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		initView(view);
		return view;
	}
	private void initView(View view) {
		title_content = (LinearLayout) view.findViewById(R.id.search_fra_titlebar_content);
		title_content.setBackgroundColor(colorValue);
	}
}
