package com.vac.musicplayer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class NetArtist {

	private long id;
	private String name;
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public static NetArtist jsonToBean(JSONObject obj) throws JSONException{
		NetArtist na = new NetArtist();
		if (obj.has("id")) {
			na.setId(obj.getLong("id"));
		}
		if (obj.has("name")) {
			na.setName(obj.getString("name"));
		}
		if (obj.has("picUrl")) {
			na.setPicUrl(obj.getString("picUrl"));
		}
		return na;
	}
}
