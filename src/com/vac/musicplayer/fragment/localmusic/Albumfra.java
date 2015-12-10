package com.vac.musicplayer.fragment.localmusic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyAlbumAdapter;
import com.vac.musicplayer.bean.Album;
import com.vac.musicplayer.loader.AlbumLoader;

public class Albumfra extends Fragment {

	private ListView album_listView;
	private static final int PRIVATE_LOCAL_ALBUM=1;
	private List<Album> mAlbumList = new ArrayList<Album>();
	private MyAlbumAdapter mAdapter=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.album_fra, null);
		album_listView = (ListView) view.findViewById(R.id.album_listView);
		mAdapter = new MyAlbumAdapter(getActivity(), mAlbumList);
		album_listView.setAdapter(mAdapter);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(PRIVATE_LOCAL_ALBUM, null, new LoaderCallbacks<List<Album>>() {

			@Override
			public Loader<List<Album>> onCreateLoader(int arg0, Bundle arg1) {
				

				StringBuilder where = new StringBuilder(Albums._ID
						+ " in (select distinct " + Media.ALBUM_ID
						+ " from audio_meta where (1=1 ");
				where.append("))");
				return new AlbumLoader(getActivity(),where.toString(),null, Media.ARTIST_KEY);
			}

			@Override
			public void onLoadFinished(Loader<List<Album>> arg0,
					List<Album> data) {
				mAlbumList.clear();
				mAlbumList.addAll(data);
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onLoaderReset(Loader<List<Album>> arg0) {
				
			}});
	}
}
