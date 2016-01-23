package com.vac.musicplayer.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.google.gson.Gson;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.Music;

/***
 * 缓存文件工具类（json缓存）
 * @author vac
 *
 */
public class JsonCacheFileUtils {

	private static final String TAG = JsonCacheFileUtils.class.getSimpleName();
	private final static String path = Constant.JSONFILECACHE;
	private final static String recentPlayMusicPath =path+File.separator+ "recentplay";
	public static void writeRecentPlayMusic(Music music){
		String json = Music.beanToJson(music);
		if (!new File(recentPlayMusicPath).exists()) {
			new File(recentPlayMusicPath).mkdirs();
		}
		File file = new File(recentPlayMusicPath+File.separator+music.getId());
		writeJson(file, json);
	}
	public static ArrayList<Music> readRecentPlayMusic(){
		ArrayList<Music> musicList = new ArrayList<Music>();
		File file = new File(recentPlayMusicPath);
		String[] files = file.list();
		for (int i = files.length-1; i >=0; i--) {
			File file_ = new File(recentPlayMusicPath+File.separator+files[i]);
			String json = readJson(file_);
			Gson gson = new Gson();
			Music music = gson.fromJson(json, Music.class);
			musicList.add(music);
			Log.i(TAG, "最近播放过的文件是："+music.getTitle()+"类型："+music.getType());
		}
		return musicList;
	}
	public static int getCountOfRecentPlayMusic(){
		int count = 0;
		File file = new File(recentPlayMusicPath);
		if (file.exists()) {
			String[] files = file.list();
			count = files.length;
		}
		return count;
	}
	
	private  static void writeJson(File file, String json) {
		BufferedWriter out = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}else{
				Log.i(TAG, "文件已存在");
				return;
			}
			out = new BufferedWriter(new FileWriter(file), 1024);
			out.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String readJson(File file) {
		/**
		 * 读取 String数据
		 * 
		 * @param key
		 * @return String 数据
		 */
		if (!file.exists())
			return null;
		boolean removeFile = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			String readString = "";
			String currentLine;
			while ((currentLine = in.readLine()) != null) {
				readString += currentLine;
			}
			return readString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
