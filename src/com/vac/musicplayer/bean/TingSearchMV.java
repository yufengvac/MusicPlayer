package com.vac.musicplayer.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TingSearchMV {

	private long id;
	private long songId;
	private String videoName;
	private String singerName;
	private String picUrl;
	private int pickCount;
	private int bulletCount;
	private ArrayList<TingMV> mvList;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSongId() {
		return songId;
	}
	public void setSongId(long songId) {
		this.songId = songId;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
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
	public int getPickCount() {
		return pickCount;
	}
	public void setPickCount(int pickCount) {
		this.pickCount = pickCount;
	}
	public int getBulletCount() {
		return bulletCount;
	}
	public void setBulletCount(int bulletCount) {
		this.bulletCount = bulletCount;
	}
	public ArrayList<TingMV> getMvList() {
		return mvList;
	}
	public void setMvList(ArrayList<TingMV> mvList) {
		this.mvList = mvList;
	}
	
	public static TingSearchMV jsonToBean(JSONObject obj) throws JSONException{
		TingSearchMV tsm = new TingSearchMV();
		if (obj.has("id")) {
			tsm.setId(obj.getLong("id"));
		}
		if (obj.has("songId")) {
			tsm.setSongId(obj.getLong("songId"));
		}
		if (obj.has("videoName")) {
			tsm.setVideoName(obj.getString("videoName"));
		}
		if (obj.has("singerName")) {
			tsm.setSingerName(obj.getString("singerName"));
		}
		if (obj.has("picUrl")) {
			tsm.setPicUrl(obj.getString("picUrl"));
		}
		if (obj.has("pickCount")) {
			tsm.setPickCount(obj.getInt("pickCount"));
		}
		if (obj.has("bulletCount")) {
			tsm.setBulletCount(obj.getInt("bulletCount"));
		}
		if (obj.has("mvList")) {
			JSONArray array = obj.getJSONArray("mvList");
			ArrayList<TingMV> mvList = new ArrayList<TingMV>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj_ = array.getJSONObject(i);
				mvList.add(TingMV.jsonToBean(obj_));
			}
			tsm.setMvList(mvList);
		}
		
		return tsm;
	}
}
