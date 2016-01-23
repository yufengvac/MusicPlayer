package com.vac.musicplayer.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TingOut implements Parcelable{

	private String name;
	private String url;
	private String logo;
	
	public TingOut() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public static TingOut jsonToBean(JSONObject obj) throws JSONException{
		TingOut to = new TingOut();
		if (obj.has("name")) {
			to.setName(obj.getString("name"));
		}
		if (obj.has("url")) {
			to.setName(obj.getString("url"));
		}
		if (obj.has("logo")) {
			to.setName(obj.getString("logo"));
		}
		return to;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	public TingOut(Parcel arg0) {
		name = arg0.readString();
		url = arg0.readString();
		logo = arg0.readString();
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(name);
		arg0.writeString(url);
		arg0.writeString(logo);
	}
	public static final Parcelable.Creator<TingOut> CREATOR = new Creator<TingOut>() {

		@Override
		public TingOut createFromParcel(Parcel arg0) {
			return new TingOut(arg0);
		}

		@Override
		public TingOut[] newArray(int arg0) {
			return new TingOut[arg0];
		}
	}; 
}
