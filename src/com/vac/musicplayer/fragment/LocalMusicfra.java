package com.vac.musicplayer.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Playlists;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.vac.musicplayer.MainActivity;
import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.loader.MusicLoader;

public class LocalMusicfra extends Fragment {

	private static final int PRIVATE_LOCAL_MUSIC=0;
	private static final String TAG = LocalMusicfra.class.getSimpleName();
	private ListView listView;
	private MusicListAdapter mAdapter=null;
	
	private onMusicTotalCountListener musicTotalListener=null;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof MainActivity){
			musicTotalListener = (onMusicTotalCountListener) activity;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.locamusic_fra, null);
		
		initView(view);
		
		return view;
	}
	private void initView(View v){
		listView = (ListView) v.findViewById(R.id.localmusic_lv);
		mAdapter = new MusicListAdapter(getActivity());
		listView.setAdapter(mAdapter);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			getLoaderManager().initLoader(PRIVATE_LOCAL_MUSIC, null, new LoaderCallbacks<List<Music>>() {

				@Override
				public Loader<List<Music>> onCreateLoader(int id, Bundle bundle) {
					Log.i(TAG, "onCreateLoader");
					return new MusicLoader(getActivity(),null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
				}

				@Override
				public void onLoadFinished(Loader<List<Music>> loader,
						List<Music> data) {
					Log.i(TAG, "onLoadFinished");
					if(data.size()>0){
						mAdapter.setData(data);
						musicTotalListener.musicTotalCount(data.size());//设置音乐的总数，接口回调给宿主MainActivity.class
					}
					
				}

				@Override
				public void onLoaderReset(Loader<List<Music>> arg0) {
					Log.i(TAG, "onLoaderReset");
					mAdapter.setData(null);
				}

				
			});
		}else{//SD卡不可用
			Toast.makeText(getActivity(), "SD卡不可用", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	public interface onMusicTotalCountListener{
		public abstract void musicTotalCount(int total);
	}
}
