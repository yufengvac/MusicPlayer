package com.vac.musicplayer.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vac.musicplayer.R;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.fragment.search.SearchHotFra;
import com.vac.musicplayer.fragment.search.SearchHotFra.OnHotClickListener;
import com.vac.musicplayer.fragment.search.TabSearchFra;
import com.vac.musicplayer.listener.OnPageAddListener;
import com.vac.musicplayer.utils.PreferHelper;

public class SearchFragment extends Fragment implements OnHotClickListener{

	private LinearLayout title_content;
	private int colorValue;
	private EditText search_edit;
	private ImageView search_go;
	private FragmentManager fm;
	private ImageView clearEditImageView;
	private boolean afterSearch  =false;
	private OnPageAddListener mPageListener;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnPageAddListener) {
			mPageListener = (OnPageAddListener) activity;
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_fragment, null);
		initView(view);
		Bundle bundle = getArguments();
		colorValue = bundle.getInt("color");
		title_content.setBackgroundColor(colorValue);
		
		SearchHotFra shf = new SearchHotFra();
		Bundle b = new Bundle();
		b.putInt("color", colorValue);
		shf.setArguments(b);
		shf.setOnHotListener(this);
		fm.beginTransaction().replace(R.id.search_fragment_content, shf).addToBackStack(null).commit();
		afterSearch =false;
		return view;
	}
	private void initView(View view) {
		title_content = (LinearLayout) view.findViewById(R.id.search_fra_titlebar_content);
		clearEditImageView = (ImageView) view.findViewById(R.id.search_fragment_clearedit_imageview);
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
		
		search_edit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if (search_edit.getText().toString().trim().isEmpty()) {
					clearEditImageView.setVisibility(View.GONE);
				}else{
					clearEditImageView.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		
		fm = getChildFragmentManager();
		
		
		clearEditImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				search_edit.setText("");
				if (afterSearch) {
					SearchHotFra shf = new SearchHotFra();
					Bundle b = new Bundle();
					b.putInt("color", colorValue);
					shf.setArguments(b);
					shf.setOnHotListener(SearchFragment.this);
					fm.beginTransaction().replace(R.id.search_fragment_content, shf).commit();
					afterSearch =false;
				}
			}
		});
	}
	protected void replace(String trim) {
		TabSearchFra tsf = new TabSearchFra();
		Bundle bundle = new Bundle();
		bundle.putInt("color", colorValue);
		bundle.putString("search", trim);
		tsf.setArguments(bundle);
		tsf.setOnPageListener(mPageListener);
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.search_fragment_content, tsf);
		ft.commit();
		String history = PreferHelper.readString(getActivity(), Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY);
		if (history!=null) {
			if (!history.contains(trim)) {
				history  +=(trim+",");
				PreferHelper.write(getActivity(), Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY,history);
			}
		}else{
			history +=(trim+",");
			PreferHelper.write(getActivity(), Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY,history);
		}
		afterSearch = true;
	}
	@Override
	public void onHotClick(String searchContent) {
		search_edit.setText(searchContent);
		replace(searchContent);
	}
	
}
