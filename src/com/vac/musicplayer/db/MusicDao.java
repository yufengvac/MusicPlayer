package com.vac.musicplayer.db;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vac.musicplayer.bean.Music;


public class MusicDao {
	private DBOpenhelper helper;
	private SQLiteDatabase db;

	public MusicDao(Context context) {
		helper = new DBOpenhelper(context);
	}

	/**
	 */
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	/**
	 * @param music
	 * @return
	 */
	public long insert(Music music) {
		long rowId = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("_id", music.getId());
		values.put("name", music.getTitle());
		values.put("artist", music.getArtist());
		values.put("path", music.getPath());
		rowId = db.insert(DBOpenhelper.LOADEDMUSIC_TBL, null, values);
		db.close();
		return rowId;
	}

	/**
	 * @param music
	 * @return
	 */
	public int update(Music music) {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", music.getTitle());
		values.put("artist", music.getArtist());
		count = db.update(DBOpenhelper.LOADEDMUSIC_TBL, values,
				"_id=" + music.getId(), null);
		db.close();
		return count;
	}

	/**
	 */
	public void scanDIR() {
		Cursor c = query();
		while (c.moveToNext()) {
			int id = c.getInt(c.getColumnIndex("_id"));
			String path = c.getString(c.getColumnIndex("path"));
			if (!(new File(path).exists())) {
				delete(id);
			}
		}
		c.close();
		close();
	}

	/**
	 * @param id
	 * @return
	 */
	public int delete(int id) {
		int count = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBOpenhelper.LOADEDMUSIC_TBL, "_id=" + id, null);
		db.close();
		return count;
	}

	/**
	 * @return
	 */
	public Cursor query() {
		Cursor cursor = null;
		db = helper.getReadableDatabase();
		cursor = db.rawQuery("select * from " + DBOpenhelper.LOADEDMUSIC_TBL,
				null);
		return cursor;
	}

	/**
	 * @return
	 */
	public int getCount() {
		int count = 0;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "
				+ DBOpenhelper.LOADEDMUSIC_TBL, null);
		if (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean exists(int id) {
		boolean isExists = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from "
				+ DBOpenhelper.LOADEDMUSIC_TBL + " where _id=" + id, null);
		if (cursor.moveToNext()) {
			int count = cursor.getInt(0);
			if (count > 0) {
				isExists = true;
			}
		}
		cursor.close();
		db.close();
		return isExists;
	}

	/**
	 * @return
	 */
	public ArrayList<Music> getPageData() {
		ArrayList<Music> musics = new ArrayList<Music>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ DBOpenhelper.LOADEDMUSIC_TBL, null);
		while (cursor.moveToNext()) {
			Music music = new Music();
			music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			music.setTitle(cursor.getString(cursor
					.getColumnIndex("name")));
			music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
			music.setPath(cursor.getString(cursor.getColumnIndex("path")));
			musics.add(music);
		}
		cursor.close();
		db.close();
		return musics;
	}

	/**
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ArrayList<Music> getPageData(int currentPage, int pageSize) {
		ArrayList<Music> musics = new ArrayList<Music>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from "
				+ DBOpenhelper.LOADEDMUSIC_TBL + " limit ?,?",
				new String[] { String.valueOf((currentPage - 1) * pageSize),
						String.valueOf(pageSize) });
		while (cursor.moveToNext()) {
			Music music = new Music();
			music.setId(cursor.getInt(cursor.getColumnIndex("_id")));
			music.setTitle(cursor.getString(cursor
					.getColumnIndex("name")));
			music.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
			music.setPath(cursor.getString(cursor.getColumnIndex("path")));

			musics.add(music);
		}
		cursor.close();
		db.close();
		return musics;
	}
}
