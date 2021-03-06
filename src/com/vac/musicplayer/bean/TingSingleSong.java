package com.vac.musicplayer.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 天天动听单曲
 * @author vac
 *
 */
public class TingSingleSong implements Parcelable{

	private long songId;
	private String name;
	private String alias;
	private String remarks;
	private boolean firstHit;
	private long librettistId;//作词者id
	private String librettistName;//作词者姓名
	private long composerId;//作曲者id
	private String composerName;//作曲者姓名
	private long singerId;//歌手id
	private String singerName;//歌手姓名
	private int singerSFlag;
	private long albumId;//专辑id
	private String albumName;//专辑名称
	private int favorites;//喜欢数
	private long originalId;
	private int type;
	private String tag;
	private int status;
	private ArrayList<TingAudition> auditionList;
	private ArrayList<TingMV> mvList;
	private ArrayList<TingOut > outList;
	public ArrayList<TingOut> getOutList() {
		return outList;
	}
	public void setOutList(ArrayList<TingOut> outList) {
		this.outList = outList;
	}
	public long getSongId() {
		return songId;
	}
	public void setSongId(long songId) {
		this.songId = songId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public boolean isFirstHit() {
		return firstHit;
	}
	public void setFirstHit(boolean firstHit) {
		this.firstHit = firstHit;
	}
	public long getLibrettistId() {
		return librettistId;
	}
	public void setLibrettistId(long librettistId) {
		this.librettistId = librettistId;
	}
	public String getLibrettistName() {
		return librettistName;
	}
	public void setLibrettistName(String librettistName) {
		this.librettistName = librettistName;
	}
	public long getComposerId() {
		return composerId;
	}
	public void setComposerId(long composerId) {
		this.composerId = composerId;
	}
	public String getComposerName() {
		return composerName;
	}
	public void setComposerName(String composerName) {
		this.composerName = composerName;
	}
	public long getSingerId() {
		return singerId;
	}
	public void setSingerId(long singerId) {
		this.singerId = singerId;
	}
	public String getSingerName() {
		return singerName;
	}
	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	public int getSingerSFlag() {
		return singerSFlag;
	}
	public void setSingerSFlag(int singerSFlag) {
		this.singerSFlag = singerSFlag;
	}
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public int getFavorites() {
		return favorites;
	}
	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	public long getOriginalId() {
		return originalId;
	}
	public void setOriginalId(long originalId) {
		this.originalId = originalId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ArrayList<TingAudition> getAuditionList() {
		return auditionList;
	}
	public void setAuditionList(ArrayList<TingAudition> auditionList) {
		this.auditionList = auditionList;
	}
	public ArrayList<TingMV> getMvList() {
		return mvList;
	}
	public void setMvList(ArrayList<TingMV> mvList) {
		this.mvList = mvList;
	}
	
	public static TingSingleSong jsonToBean(JSONObject obj) throws JSONException{
		TingSingleSong tss = new TingSingleSong();
		if (obj.has("songId")) {
			tss.setSongId(obj.getLong("songId"));
		}
		if (obj.has("name")) {
			tss.setName(obj.getString("name"));
		}
		if (obj.has("alias")) {
			tss.setAlias(obj.getString("alias"));
		}
		if (obj.has("remarks")) {
			tss.setRemarks(obj.getString("remarks"));
		}
		if (obj.has("firstHit")) {
			tss.setFirstHit(obj.getBoolean("firstHit"));
		}
		if (obj.has("librettistId")) {
			tss.setLibrettistId(obj.getLong("librettistId"));
		}
		if (obj.has("librettistName")) {
			tss.setLibrettistName(obj.getString("librettistName"));
		}
		if (obj.has("composerId")) {
			tss.setComposerId(obj.getLong("composerId"));
		}
		if (obj.has("composerName")) {
			tss.setComposerName(obj.getString("composerName"));
		}
		if (obj.has("singerId")) {
			tss.setSingerId(obj.getLong("singerId"));
		}
		if (obj.has("singerName")) {
			tss.setSingerName(obj.getString("singerName"));
		}
		if (obj.has("singerSFlag")) {
			tss.setSingerSFlag(obj.getInt("singerSFlag"));
		}
		if (obj.has("albumId")) {
			tss.setAlbumId(obj.getLong("albumId"));
		}
		if (obj.has("albumName")) {
			tss.setAlbumName(obj.getString("albumName"));
		}
		if (obj.has("favorites")) {
			tss.setFavorites(obj.getInt("favorites"));
		}
		if (obj.has("originalId")) {
			tss.setOriginalId(obj.getLong("originalId"));
		}
		if (obj.has("type")) {
			tss.setType(obj.getInt("type"));
		}
		if (obj.has("tags")) {
			tss.setTag(obj.getString("tags"));
		}
		if (obj.has("status")) {
			tss.setStatus(obj.getInt("status"));
		}
		if (obj.has("auditionList")) {
			JSONArray array = obj.getJSONArray("auditionList");
			ArrayList<TingAudition> tingAudList  = new ArrayList<TingAudition>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj_ = array.getJSONObject(i);
				tingAudList.add(TingAudition.jsonToBean(obj_));
			}
			tss.setAuditionList(tingAudList);
		}
		
		if (obj.has("mvList")) {
			JSONArray array = obj.getJSONArray("mvList");
			ArrayList<TingMV> tingMvList  = new ArrayList<TingMV>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj_ = array.getJSONObject(i);
				tingMvList.add(TingMV.jsonToBean(obj_));
			}
			tss.setMvList(tingMvList);
		}
		
		if (obj.has("outList")) {
			try {
				JSONArray array = obj.getJSONArray("outList");
				ArrayList<TingOut> tingOutList  = new ArrayList<TingOut>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj_ = array.getJSONObject(i);
					tingOutList.add(TingOut.jsonToBean(obj_));
				}
				tss.setOutList(tingOutList);
			} catch (Exception e) {
				ArrayList<TingOut> tingOutList  = new ArrayList<TingOut>();
				tss.setOutList(tingOutList);
			}
			
		}
		return tss;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	public TingSingleSong(){
	}
	
	public TingSingleSong(Parcel arg0){
		songId = arg0.readLong();
		name = arg0.readString();
		alias = arg0.readString();
		remarks= arg0.readString();
		librettistId = arg0.readLong();
		librettistName = arg0.readString();
		composerId = arg0.readLong();
		composerName = arg0.readString();
		singerId = arg0.readLong();
		singerName = arg0.readString();
		singerSFlag = arg0.readInt();
		albumId = arg0.readLong();
		albumName = arg0.readString();
		favorites =arg0.readInt();
		originalId = arg0.readLong();
		type = arg0.readInt();
		tag = arg0.readString();
		status = arg0.readInt();
		Parcelable[] tingAudition = arg0.readParcelableArray(TingAudition.class.getClassLoader());
		for (int i = 0; i < tingAudition.length; i++) {
			auditionList.add((TingAudition)tingAudition[i]);
		}
	
		Parcelable[] tingMv = arg0.readParcelableArray(TingMV.class.getClassLoader());
		for (int i = 0; i < tingMv.length; i++) {
			mvList.add((TingMV)tingMv[i]);
		}
		Parcelable[] tingOut_ = arg0.readParcelableArray(TingOut.class.getClassLoader());
		for (int i = 0; i < tingOut_.length; i++) {
			outList.add((TingOut)tingOut_[i]);
		}
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(songId);
		arg0.writeString(name);
		arg0.writeString(alias);
		arg0.writeString(remarks);
		arg0.writeLong(librettistId);
		arg0.writeString(librettistName);
		arg0.writeLong(composerId);
		arg0.writeString(composerName);
		arg0.writeLong(singerId);
		arg0.writeString(singerName);
		arg0.writeInt(singerSFlag);
		arg0.writeLong(albumId);
		arg0.writeString(albumName);
		arg0.writeInt(favorites);
		arg0.writeLong(originalId);
		arg0.writeInt(type);
		arg0.writeString(tag);
		arg0.writeInt(status);
//		arg0.writeList(auditionList);
//		arg0.writeList(mvList);
//		arg0.writeList(outList);
		
		Parcelable[] par1 = new Parcelable[auditionList.size()];
		for (int i = 0; i < par1.length; i++) {
			par1[i]  = auditionList.get(i);
		}
		
		Parcelable[] par2 = new Parcelable[mvList.size()];
		for (int i = 0; i < par2.length; i++) {
			par2[i]  = mvList.get(i);
		}
		
		Parcelable[] par3 = new Parcelable[outList.size()];
		for (int i = 0; i < par3.length; i++) {
			par3[i]  = outList.get(i);
		}
	}
	public static final Parcelable.Creator<TingSingleSong> CREATOR = new Parcelable.Creator<TingSingleSong>() {

		@Override
		public TingSingleSong createFromParcel(Parcel arg0) {
			return new TingSingleSong(arg0);
		}

		@Override
		public TingSingleSong[] newArray(int arg0) {
			return new TingSingleSong[arg0];
		}
	};

}
