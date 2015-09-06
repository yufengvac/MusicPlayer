package com.vac.musicplayer.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vac.musicplayer.ArtistActivity;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.ArtistAdapter;
import com.vac.musicplayer.bean.Artist;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.loader.ArtistLoader;

public class Singerfra extends Fragment implements LoaderCallbacks<List<Artist>> {
	private static final String TAG = Singerfra.class.getSimpleName();
	private ListView artist_listView;
	private ArtistAdapter mAdapter;
	private final int ARTIST_RETRIEVE_LOADER = 0;
	private String mSortOrder = MediaStore.Audio.Artists.NUMBER_OF_TRACKS
			+ " desc";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.singer_fra, null);
		artist_listView = (ListView) view.findViewById(R.id.artist_listView);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new ArtistAdapter(getActivity());
		artist_listView.setAdapter(mAdapter);
		getLoaderManager().initLoader(ARTIST_RETRIEVE_LOADER, null, this);
		
		artist_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(),ArtistActivity.class);
				intent.putExtra(Constant.ARTIST_LISTVIEW_ITEM, mAdapter.getItem(arg2));
				startActivity(intent);
			}
		});
	}

	@Override
	public Loader<List<Artist>> onCreateLoader(int arg0, Bundle arg1) {

		// 创建并返回一个Loader
		return new ArtistLoader(getActivity(), null, null,mSortOrder);
	}

	@Override
	public void onLoadFinished(Loader<List<Artist>> arg0, List<Artist> data) {
		// 载入完成，更新列表数据
		mAdapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Artist>> arg0) {
		Log.i(TAG, "onLoaderReset");
		mAdapter.setData(null);
	}
}
