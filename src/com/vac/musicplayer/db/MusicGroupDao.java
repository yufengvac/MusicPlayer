package com.vac.musicplayer.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vac.musicplayer.bean.Music;
import com.vac.musicplayer.bean.MusicGroup;


public class MusicGroupDao {
	private DBOpenhelper helper;
	private Context context;
	public MusicGroupDao(Context context){
		this.helper = new DBOpenhelper(context);
		this.context = context;
	}
	/**
	 * @param group
	 * @return
	 */
	public long addGroup(MusicGroup group) {
		long rowId = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", group.getTitle());
		rowId = db.insert(DBOpenhelper.MUSICGROUP_TBL, null, values);
		db.close();
		return rowId;
	}
	
	public long updateGroup(String title,int id) {
		long rowId = 0;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", title);
		rowId=db.update(DBOpenhelper.MUSICGROUP_TBL, values, "_id=?", new String[]{String.valueOf(id)});
		db.close();
		return rowId;
	}
	
	
	public int deleteGroup(int groupId){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		db.delete(DBOpenhelper.MUSICITEM_TBL, "groupid=" + groupId, null);
		db.delete(DBOpenhelper.MUSICGROUP_TBL, "_id=" + groupId, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return -1;
	}
	
	
	
	
	public ArrayList<MusicGroup> getGroups(){
		ArrayList<MusicGroup> groups = new ArrayList<MusicGroup>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DBOpenhelper.MUSICGROUP_TBL, null);
		while(c.moveToNext()){
			MusicGroup group = new MusicGroup();
			group.setTitle(c.getString(c.getColumnIndex("title")));
			group.setId(c.getInt(c.getColumnIndex("_id")));
			group.setItems(new MusicItemDao(context).getMusicsByGroup(group.getId()));
			groups.add(group);
		}
		c.close();
		db.close();
		return groups;
	}
	
	public int getDataCount(){
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery("select * from " + DBOpenhelper.MUSICGROUP_TBL, null);
		return c.getCount();
	}
	
}
