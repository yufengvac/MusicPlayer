package com.vac.musicplayer.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.vac.musicplayer.bean.Album;

public class AlbumLoader extends AsyncTaskLoader<List<Album>> {

	private static final String TAG = AlbumLoader.class.getName();
	
	private List<Album> mAlbumList = null;
	
	// 数据库查询相关参数
	private String mSelection = null;
	private String[] mSelectionArgs = null;
	private String mSortOrder = null;
	
	private ContentResolver mContentResolver=null;
	
	/** 要从MediaStore检索的列 */
	private final String[] mProjection = new String[] { Albums.ALBUM,
			Albums.NUMBER_OF_SONGS, Albums._ID, Albums.ALBUM_ART };
	
	public AlbumLoader(Context context, String selection,
			String[] selectionArgs, String sortOrder) {
		super(context);
		this.mSelection = selection;
		this.mSelectionArgs = selectionArgs;
		if (sortOrder == null) {
			// 默认按歌曲数目倒序排序
			this.mSortOrder = MediaStore.Audio.Artists.NUMBER_OF_TRACKS
					+ " desc";
		}
		this.mSortOrder = sortOrder;
		mContentResolver = context.getContentResolver();
	}

	@Override
	public List<Album> loadInBackground() {
		Log.i(TAG, "loadInBackground");
		Cursor cursor = mContentResolver.query(Albums.EXTERNAL_CONTENT_URI,
				mProjection, mSelection, mSelectionArgs, mSortOrder);

		List<Album> itemsList = new ArrayList<Album>();

		// 将数据库查询结果保存到一个List集合中(存放在RAM)
		if (cursor != null) {
			int index_album_name = cursor.getColumnIndex(Albums.ALBUM);
			int index_album_id = cursor.getColumnIndex(Albums._ID);
			int index_num_of_songs = cursor
					.getColumnIndex(Albums.NUMBER_OF_SONGS);
			int index_art_work = cursor.getColumnIndex(Albums.ALBUM_ART);

			while (cursor.moveToNext()) {
				Album item = new Album();
				item.setAlbum_id(cursor.getInt(index_album_id));
				item.setAlbum_name(cursor.getString(index_album_name));
				item.setAlbum_num(cursor.getInt(index_num_of_songs));
				item.setAlbum_pic(cursor.getString(index_art_work));
				itemsList.add(item);
			}
			cursor.close();
		}
		// 如果没有扫描到媒体文件，itemsList的size为0，因为上面new过了
		return itemsList;
	}
	@Override
	public void deliverResult(List<Album> data) {
		Log.i(TAG, "deliverResult");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		List<Album> oldList = data;
		mAlbumList = data;

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

	protected void onReleaseResources(List<Album> data) {
		Log.i(TAG, "onReleaseResources");
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	@Override
	protected void onStartLoading() {
		Log.i(TAG, "onStartLoading");
		if (mAlbumList != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mAlbumList);
		}
		// If the data has changed since the last time it was loaded
		// or is not currently available, start a load.
		forceLoad();
	}

	@Override
	protected void onStopLoading() {
		Log.i(TAG, "onStartLoading");
		super.onStopLoading();
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(List<Album> data) {
		super.onCanceled(data);
		Log.i(TAG, "onCanceled");
		// At this point we can release the resources associated with 'data'
		// if needed.
		onReleaseResources(data);
	}

	@Override
	protected void onReset() {
		super.onReset();
		Log.i(TAG, "onReset");
		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'data'
		// if needed.
		if (mAlbumList != null) {
			onReleaseResources(mAlbumList);
			mAlbumList = null;
		}
	}

	@Override
	protected void onForceLoad() {
		// TODO Auto-generated method stub
		super.onForceLoad();
	}

}
