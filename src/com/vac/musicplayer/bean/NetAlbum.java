package com.vac.musicplayer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class NetAlbum {

	private long id;
	private String name;
	private NetArtist artist;
	private String picUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public NetArtist getArtist() {
		return artist;
	}
	public void setArtist(NetArtist artist) {
		this.artist = artist;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public static NetAlbum jsonToBean(JSONObject obj) throws JSONException{
		NetAlbum na = new NetAlbum();
		if (obj.has("id")) {
			na.setId(obj.getLong("id"));
		}
		if (obj.has("name")) {
			na.setName(obj.getString("name"));
		}
		if (obj.has("artist")) {
			JSONObject obj1 = obj.getJSONObject("artist");
			na.setArtist(NetArtist.jsonToBean(obj1));
		}
		if (obj.has("picUrl")) {
			na.setPicUrl(obj.getString("picUrl"));
		}
		
		return na;
	}
}
