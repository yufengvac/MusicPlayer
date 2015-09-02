package com.vac.musicplayer.bean;

public class LyricInfo {

	private long aid;
	private long artist_id;
	private String song;
	private String lrc;
	private long sid;
	public long getAid() {
		return aid;
	}
	public void setAid(long aid) {
		this.aid = aid;
	}
	public long getArtist_id() {
		return artist_id;
	}
	public void setArtist_id(long artist_id) {
		this.artist_id = artist_id;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}

	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	public String getLrc() {
		return lrc;
	}
	public void setLrc(String lrc) {
		this.lrc = lrc;
	}
	@Override
	public String toString() {
		return "LyricInfo [aid=" + aid + ", artist_id=" + artist_id + ", song="
				+ song + ", lrc=" + lrc + ", sid=" + sid + "]";
	}
	
}
