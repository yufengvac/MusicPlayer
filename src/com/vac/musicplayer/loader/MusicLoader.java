package com.vac.musicplayer.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Playlists;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.vac.musicplayer.bean.Music;
/**
 * 
 * @author vac
 * @title MusicLoader
 * @description 扫描本地音乐的Loader
 * @date 2015年8月27日22:54:01
 *
 */
public class MusicLoader extends AsyncTaskLoader<List<Music>> {

	private static final String TAG = MusicLoader.class.getSimpleName();
	private String selection=null;
	private String[] selectionArgs=null;
	private String sortOrder=null;
	private ContentResolver mResolver=null;
	private List<Music> mData=null;
	private String[] mProjection = null;//从数据库中要检出的列
	
	public MusicLoader(Context context,String selection,String[] selectionArgs,
			String sortOrder) {
		super(context);
		this.selection = selection;
		this.selectionArgs = selectionArgs;
		this.sortOrder= sortOrder;
		mResolver = context.getContentResolver();
//		mProjection = new String[]{Playlists._ID,Playlists.NAME,Playlists.DATA
//				,Playlists.DATE_ADDED,Playlists.DATE_MODIFIED};
		mProjection = new String[]{
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.ALBUM_ID,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DATE_ADDED,
				MediaStore.Audio.Media.DATE_MODIFIED,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.SIZE};
	}

	@Override
	public List<Music> loadInBackground() {
		Log.i(TAG, "loadInBackground-->"+Thread.currentThread().getName());
		Cursor cursor = mResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				mProjection, selection, selectionArgs, sortOrder);
		List<Music> musicList = new ArrayList<Music>();
		if(cursor!=null){
			while (cursor.moveToNext()) {
				Music music = new Music();
				music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
				music.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
				music.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
				music.setData(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
				music.setDate_add(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)));
				music.setData_modify(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)));
				music.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
				music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
				music.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
				musicList.add(music);
			}
			cursor.close();
			cursor =null;
		}
		return musicList;
	}


	@Override
	public void onCanceled(List<Music> data) {
		super.onCanceled(data);
		onReleaseResources(data);
	}

	@Override
	public void deliverResult(List<Music> data) {
		Log.i(TAG, "deliverResult");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		List<Music> oldList = data;
		mData = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldList != null) {
			onReleaseResources(oldList);
		}
	}

	protected void onReleaseResources(List<Music> data) {
		Log.i(TAG, "onReleaseResources");
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}
	
	@Override
	protected void onStartLoading() {
		Log.i(TAG, "onStartLoading");
		super.onStartLoading();
		if(mData!=null){
			deliverResult(mData);
		}
		forceLoad();
	}
	
	@Override
	protected void onStopLoading() {
		Log.i(TAG, "onStopLoading");
		super.onStopLoading();
		cancelLoad();
	}
	
	@Override
	protected void onReset() {
		Log.i(TAG, "onReset");
		onStopLoading();
		super.onReset();
		if(mData!=null){
			onReleaseResources(mData);
			mData=null;
		}
		
	}
	
	@Override
	protected void onForceLoad() {
		Log.i(TAG, "onForceLoad");
		super.onForceLoad();
	}

	
}
