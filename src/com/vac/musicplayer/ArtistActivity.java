package com.vac.musicplayer;

import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.content.Loader;

import com.vac.musicplayer.adapter.MusicListAdapter;
import com.vac.musicplayer.bean.Artist;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.loader.MusicLoader;

public class ArtistActivity extends FragmentActivity implements LoaderCallbacks<List<Music>> {

	private static final String TAG = ArtistActivity.class.getSimpleName();
	private ListView artist_listView;
	private Artist artist;
	private MusicListAdapter mAdapter=null;
	private static final int PRIVATE_LOCAL_MUSIC=100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.artist_activity);
		artist = getIntent().getParcelableExtra(Constant.ARTIST_LISTVIEW_ITEM);
		initView();
	}
	@Override
	public void onResume() {
		super.onResume();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			getSupportLoaderManager().initLoader(PRIVATE_LOCAL_MUSIC, null, this);
		}else{//SD卡不可用
			Toast.makeText(ArtistActivity.this, "SD卡不可用", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initView(){
		artist_listView = (ListView) findViewById(R.id.artist_activity_listview);
		mAdapter  = new MusicListAdapter(ArtistActivity.this);
		artist_listView.setAdapter(mAdapter);
	}
	
	@Override
	public void onLoadFinished(
			android.support.v4.content.Loader<List<Music>> arg0,
			List<Music> arg1) {
		mAdapter.setData(arg1);
	}
	@Override
	public void onLoaderReset(
			android.support.v4.content.Loader<List<Music>> arg0) {
		
	}
	@Override
	public android.support.v4.content.Loader<List<Music>> onCreateLoader(
			int arg0, Bundle arg1) {
		StringBuffer sb = new StringBuffer();
		sb.append("1=1");
		sb.append(" and " +Media.ARTIST + " = '"
				+ artist.getArtistName() + "'");
		return new MusicLoader(ArtistActivity.this,sb.toString(),null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}

}
