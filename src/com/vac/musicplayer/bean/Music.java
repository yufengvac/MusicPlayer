package com.vac.musicplayer.bean;

import com.google.gson.Gson;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Music implements Parcelable{

	public static final int TYPE_LOCAL = 1;
	public static final int TYPE_NET = 2;
	/**
	 * 
	 */
	private long id ;//歌曲编号
	private String title;//歌曲标题
	private String album;//歌曲专辑
	private String artist;//歌手
	private String data;//歌曲的本地路径
	private int duration;//歌曲的播放时长
	private long size;//歌曲文件的总大小
	private long date_add;
	private long data_modify;
	private String path;
	private int type;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Music(){}
	public long getDate_add() {
		return date_add;
	}
	public void setDate_add(long date_add) {
		this.date_add = date_add;
	}
	public long getData_modify() {
		return data_modify;
	}
	public void setData_modify(long data_modify) {
		this.data_modify = data_modify;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		if(title.equals("<unknown>")){
			return "未知";
		}
		return title;
	}
	public void setTitle(String title) {
		
		this.title = title;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getArtist() {
		if(artist.equals("<unknown>")){
			return "未知艺术家";
		}
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "Music [id=" + id + ", title=" + title + ", album=" + album
				+ ", artist=" + artist + ", data=" + data + ", duration="
				+ duration + ", size=" + size + ", date_add=" + date_add
				+ ", data_modify=" + data_modify + ", path=" + path + ", type="
				+ type + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putLong("id", id);
		bundle.putString("title", title);
		bundle.putString("album", album);
		bundle.putString("artist", artist);
		bundle.putString("data", data);
		bundle.putLong("size", size);
		bundle.putInt("duration", duration);
		bundle.putLong("date_add", date_add);
		bundle.putLong("data_modify", data_modify);
		bundle.putString("path", path);
		dest.writeBundle(bundle);
	}
	
	// 用来创建自定义的Parcelable的对象
	public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
		public Music createFromParcel(Parcel in) {
			return new Music(in);
		}

		public Music[] newArray(int size) {
			return new Music[size];
		}
	};
	
	// 读数据进行恢复
	private Music(Parcel in) {
		Bundle bundle = in.readBundle();
		id = bundle.getLong("id");
		title = bundle.getString("title");
		album = bundle.getString("album");
		artist = bundle.getString("artist");
		data = bundle.getString("data");
		size = bundle.getLong("size");
		duration = bundle.getInt("duration");
		date_add = bundle.getLong("date_add");
		data_modify = bundle.getLong("data_modify");
		path = bundle.getString("path");
	}
	
	public static String beanToJson(Music m){
		String json = "";
		Gson gson = new Gson();
		json = gson.toJson(m);
		Log.v("TAG", "转成的json="+json);
		return json;
	}
}
