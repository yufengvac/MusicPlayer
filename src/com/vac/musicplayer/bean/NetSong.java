package com.vac.musicplayer.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetSong {
	
	private long id;
	private String name;
	private ArrayList<NetArtist> artists;
	private NetAlbum album;
	private String audio;
	private long djProgramId;
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
	public ArrayList<NetArtist> getArtists() {
		return artists;
	}
	public void setArtists(ArrayList<NetArtist> artists) {
		this.artists = artists;
	}
	public NetAlbum getAlbum() {
		return album;
	}
	public void setAlbum(NetAlbum album) {
		this.album = album;
	}
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
	}
	public long getDjProgramId() {
		return djProgramId;
	}
	public void setDjProgramId(long djProgramId) {
		this.djProgramId = djProgramId;
	}
	
	public static NetSong jsonToBean(JSONObject obj) throws JSONException{
		NetSong ns = new NetSong();
		if (obj.has("id")) {
			ns.setId(obj.getLong("id"));
		}
		if (obj.has("name")) {
			ns.setName(obj.getString("name"));
		}
		
		if (obj.has("artists")) {
			JSONArray array = obj.getJSONArray("artists");
			ArrayList<NetArtist> data = new ArrayList<NetArtist>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj_ = array.getJSONObject(i);
				data.add(NetArtist.jsonToBean(obj_));
			}
			ns.setArtists(data);
		}
		
		if (obj.has("album")) {
			JSONObject obj_ = obj.getJSONObject("album");
			ns.setAlbum(NetAlbum.jsonToBean(obj_));
		}
		if (obj.has("audio")) {
			ns.setAudio(obj.getString("audio"));
		}
		if (obj.has("djProgramId")) {
			ns.setDjProgramId(obj.getLong("djProgramId"));
		}
		return ns;
	}
}
