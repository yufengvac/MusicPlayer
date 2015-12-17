package com.vac.musicplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenhelper extends SQLiteOpenHelper {
	public static final String DBNAME = "music.db";
	public static final int VERSION  =1;
	public static final String LOADEDMUSIC_TBL = "vacmusic";
	public static final String MUSICGROUP_TBL = "musicgroup";
	public static final String MUSICITEM_TBL = "musicitem";
	public DBOpenhelper (Context context){
		super(context,DBNAME,null,VERSION);
	}
	
	public DBOpenhelper (Context context,int version){
		super(context,DBNAME,null,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + LOADEDMUSIC_TBL
				+"(" +
				"_id integer primary key," +
				"name text not null," +
				"artist text ," +
				"path text not null" +
				")");
		db.execSQL("create table if not exists " + MUSICGROUP_TBL
				+"(" +
				"_id integer primary key autoincrement," +
				"title text not null"  +
				")");
		db.execSQL("create table if not exists " + MUSICITEM_TBL
				+"(" +
				"_id integer primary key autoincrement," +
				"musicid integer not null," +
				"groupid integer references " + 
				MUSICGROUP_TBL + 
				"(_id))" );
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
