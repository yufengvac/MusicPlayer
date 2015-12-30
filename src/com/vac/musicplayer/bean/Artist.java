package com.vac.musicplayer.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lq 2013-6-1 lq2625304@gmail.com
 * */
public class Artist implements Parcelable {
	private String artist_name;
	private int number_of_tracks;
	private int number_of_albums;
	private String url;
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Artist() {

	}

	public int getNumberOfAlbums() {
		return number_of_albums;
	}

	public void setNumberOfAlbums(int number_of_albums) {
		this.number_of_albums = number_of_albums;
	}

	public int getNumberOfTracks() {
		return number_of_tracks;
	}

	public void setNumberOfTracks(int number_of_tracks) {
		this.number_of_tracks = number_of_tracks;
	}

	public String getArtistName() {
		return artist_name;
	}

	public void setArtistName(String artist_name) {
		this.artist_name = artist_name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 写数据进行保存
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putString("artist_name", artist_name);
		bundle.putInt("number_of_albums", number_of_albums);
		bundle.putInt("number_of_tracks", number_of_tracks);
		bundle.putString("url", url);
		dest.writeBundle(bundle);

	}

	// 用来创建自定义的Parcelable的对象
	public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
		public Artist createFromParcel(Parcel in) {
			return new Artist(in);
		}

		public Artist[] newArray(int size) {
			return new Artist[size];
		}
	};

	// 读数据进行恢复
	private Artist(Parcel in) {
		Bundle bundle = in.readBundle();
		artist_name = bundle.getString("artist_name");
		number_of_albums = bundle.getInt("number_of_albums");
		number_of_tracks = bundle.getInt("number_of_tracks");
		url = bundle.getString("url");
	}
}
