package com.vac.musicplayer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TingOut {

	private String name;
	private String url;
	private String logo;
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
}
