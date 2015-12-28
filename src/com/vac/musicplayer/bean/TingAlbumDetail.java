package com.vac.musicplayer.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TingAlbumDetail{

	private TingAlbum tingAlbum;
	private ArrayList<TingSingleSong> songList;
	public TingAlbum getTingAlbum() {
		return tingAlbum;
	}
	public void setTingAlbum(TingAlbum tingAlbum) {
		this.tingAlbum = tingAlbum;
	}
	public ArrayList<TingSingleSong> getSongList() {
		return songList;
	}
	public void setSongList(ArrayList<TingSingleSong> songList) {
		this.songList = songList;
	}
	public static TingAlbumDetail jsonToBean(JSONObject obj) throws JSONException{
		TingAlbumDetail tad = new TingAlbumDetail();
		tad.setTingAlbum(TingAlbum.jsonToBean(obj));
		
		if (obj.has("songList")) {
			JSONArray array = obj.getJSONArray("songList");
			ArrayList<TingSingleSong> tssList = new ArrayList<TingSingleSong>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj_ = array.getJSONObject(i);
				tssList.add(TingSingleSong.jsonToBean(obj_));
			}
			tad.setSongList(tssList);
		}
		return tad;
	}
}
