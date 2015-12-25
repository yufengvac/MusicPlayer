package com.vac.musicplayer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TingAlbum {

	private long albumId;
	private String name;
	private String description;
	private String singerName;
	private String picUrl;
	private String publishDate;
	private int publishYear;
	public int getPublishYear() {
		return publishYear;
	}
	public void setPublishYear(int publishYear) {
		this.publishYear = publishYear;
	}
	private String lang;
	private long[] songs;
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public long[] getSongs() {
		return songs;
	}
	public void setSongs(long[] songs) {
		this.songs = songs;
	}
	public static TingAlbum jsonToBean(JSONObject obj) throws JSONException{
		TingAlbum ta = new TingAlbum();
		if (obj.has("albumId")) {
			ta.setAlbumId(obj.getLong("albumId"));
		}
		if (obj.has("name")) {
			ta.setName(obj.getString("name"));
		}
		if (obj.has("description")) {
			ta.setDescription(obj.getString("description"));
		}
		if (obj.has("singerName")) {
			ta.setSingerName(obj.getString("singerName"));
		}
		if (obj.has("picUrl")) {
			ta.setPicUrl(obj.getString("picUrl"));
		}
		if (obj.has("publishYear")) {
			ta.setPublishYear(obj.getInt("publishYear"));
		}
		if (obj.has("publishDate")) {
			ta.setPublishDate(obj.getString("publishDate"));
		}
		if (obj.has("lang")) {
			ta.setLang(obj.getString("lang"));
		}
		if (obj.has("songs")) {
			JSONArray array = obj.getJSONArray("songs");
			long[] ar = new long[array.length()];
			for (int i = 0; i < array.length(); i++) {
				ar[i] = array.getLong(i);
			}
			ta.setSongs(ar);
		}
		
		return ta;
	}
}
