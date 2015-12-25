package com.vac.musicplayer.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TingSongList {

	private String title;
	private String desc;
	private long _id;
	private long quan_id;
	private long[] song_list;
	private long create_at;
	private int comment_count;
	private int listen_count;
	private String pic_url;
	private String author;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public long getQuan_id() {
		return quan_id;
	}
	public void setQuan_id(long quan_id) {
		this.quan_id = quan_id;
	}
	public long[] getSong_list() {
		return song_list;
	}
	public void setSong_list(long[] song_list) {
		this.song_list = song_list;
	}
	public long getCreate_at() {
		return create_at;
	}
	public void setCreate_at(long create_at) {
		this.create_at = create_at;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getListen_count() {
		return listen_count;
	}
	public void setListen_count(int listen_count) {
		this.listen_count = listen_count;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public static TingSongList jsonToBean(JSONObject obj) throws JSONException{
		TingSongList tsl = new TingSongList();
		if (obj.has("title")) {
			tsl.setTitle(obj.getString("title"));
		}
		if (obj.has("desc")) {
			tsl.setDesc(obj.getString("desc"));
		}
		if (obj.has("_id")) {
			tsl.set_id(obj.getLong("_id"));
		}
		if (obj.has("quan_id")) {
			tsl.setQuan_id(obj.getLong("quan_id"));
		}
		if (obj.has("song_list")) {
			String songlist = obj.getString("song_list");
			String[] songlists = songlist.split(",");
			long[] arr = new long[songlists.length];
			for (int i = 0; i < songlists.length; i++) {
				arr[i] = Long.parseLong(songlists[i]);
			}
			tsl.setSong_list(arr);
		}
		if (obj.has("create_at")) {
			tsl.setCreate_at(obj.getLong("create_at"));
		}
		if (obj.has("comment_count")) {
			tsl.setComment_count(obj.getInt("comment_count"));
		}
		if (obj.has("listen_count")) {
			tsl.setListen_count(obj.getInt("listen_count"));
		}
		if (obj.has("pic_url")) {
			tsl.setPic_url(obj.getString("pic_url"));
		}
		if (obj.has("author")) {
			tsl.setAuthor(obj.getString("author"));
		}
		return tsl;
	}
}
