package com.vac.musicplayer.bean;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable{

	/**专辑名称*/
	private String album_name;
	
	/**专辑的id*/
	private int album_id;
	
	/**专辑的歌曲数量*/
	private int album_num;
	
	/**专辑的图片路径*/
	private String album_pic;
	
	
	
	public String getAlbum_name() {
		return album_name;
	}

	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}

	public int getAlbum_id() {
		return album_id;
	}

	public void setAlbum_id(int album_id) {
		this.album_id = album_id;
	}

	public int getAlbum_num() {
		return album_num;
	}

	public void setAlbum_num(int album_num) {
		this.album_num = album_num;
	}

	public String getAlbum_pic() {
		return album_pic;
	}

	public void setAlbum_pic(String album_pic) {
		this.album_pic = album_pic;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// 写数据进行保存
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putString("album_name", album_name);
		bundle.putString("album_pic", album_pic);
		bundle.putInt("album_num", album_num);
		bundle.putInt("album_id", album_id);
		dest.writeBundle(bundle);
	}


	// 用来创建自定义的Parcelable的对象
	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
		public Album createFromParcel(Parcel in) {
			return new Album(in);
		}

		public Album[] newArray(int size) {
			return new Album[size];
		}
	};
	
	// 读数据进行恢复
	private Album(Parcel in) {
		Bundle bundle = in.readBundle();
		album_name = bundle.getString("album_name");
		album_pic = bundle.getString("album_pic");
		album_num = bundle.getInt("album_num");
		album_id = bundle.getInt("album_id");
	}

	public Album() {
	}
}
