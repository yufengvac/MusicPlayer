package com.vac.musicplayer.bean;

import java.io.Serializable;

public class Music implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id ;//歌曲编号
	private String title;//歌曲标题
	private String album;//歌曲专辑
	private String artist;//歌手
	private String data;//歌曲的本地路径
	private int duration;//歌曲的播放时长
	private long size;//歌曲文件的总大小
	private long date_add;
	private long data_modify;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
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
				+ ", data_modify=" + data_modify + "]";
	}
}
