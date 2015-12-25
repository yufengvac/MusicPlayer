package com.vac.musicplayer.bean;

import org.json.JSONException;
import org.json.JSONObject;

/***
 * 天天动听MV
 * @author vac
 *
 */
public class TingMV {

	private long id;
	private long songId;
	private long videoId;
	private String picUrl;
	private long durationMilliSecond;
	private long duration;
	private int bitRate;
	private String path;
	private long size;
	private String suffix;
	private int horizontal;
	private int vertical;
	private String url;
	private int type;
	private String typeDescription;
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
	public long getVideoId() {
		return videoId;
	}
	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public long getDurationMilliSecond() {
		return durationMilliSecond;
	}
	public void setDurationMilliSecond(long durationMilliSecond) {
		this.durationMilliSecond = durationMilliSecond;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public int getBitRate() {
		return bitRate;
	}
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public int getHorizontal() {
		return horizontal;
	}
	public void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}
	public int getVertical() {
		return vertical;
	}
	public void setVertical(int vertical) {
		this.vertical = vertical;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public static TingMV jsonToBean(JSONObject obj) throws JSONException{
		TingMV tmv = new TingMV();
		if (obj.has("id")) {
			tmv.setId(obj.getLong("id"));
		}
		if (obj.has("songId")) {
			tmv.setSongId(obj.getLong("songId"));
		}
		if (obj.has("videoId")) {
			tmv.setVideoId(obj.getLong("videoId"));
		}
		if (obj.has("picUrl")) {
			tmv.setPicUrl(obj.getString("picUrl"));
		}
		if (obj.has("durationMilliSecond")) {
			tmv.setDurationMilliSecond(obj.getLong("durationMilliSecond"));
		}
		if (obj.has("duration")) {
			tmv.setDuration(obj.getLong("duration"));
		}
		if (obj.has("bitRate")) {
			tmv.setBitRate(obj.getInt("bitRate"));
		}
		if (obj.has("path")) {
			tmv.setPath(obj.getString("path"));
		}
		if (obj.has("size")) {
			tmv.setSize(obj.getLong("size"));
		}
		if (obj.has("suffix")) {
			tmv.setSuffix(obj.getString("suffix"));
		}
		if (obj.has("horizontal")) {
			tmv.setHorizontal(obj.getInt("horizontal"));
		}
		if (obj.has("vertical")) {
			tmv.setVertical(obj.getInt("vertical"));
		}
		if (obj.has("url")) {
			tmv.setUrl(obj.getString("url"));
		}
		if (obj.has("type")) {
			tmv.setType(obj.getInt("type"));
		}
		if (obj.has("typeDescription")) {
			tmv.setTypeDescription("typeDescription");
		}
		return tmv;
	}
}
