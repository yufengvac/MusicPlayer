package com.vac.musicplayer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.bean.LyricInfo;

/**
 * 
 * */
public class LyricDownloadManager {
	private static final String TAG = LyricDownloadManager.class
			.getSimpleName();
	public static final String GB2312 = "GB2312";
	public static final String UTF_8 = "utf-8";
	private final int mTimeOut = 10 * 1000;
	private LyricXMLParser mLyricXMLParser = new LyricXMLParser();
	private URL mUrl = null;
	private int mDownloadLyricId = -1;

	private Context mContext = null;

	public LyricDownloadManager(Context c) {
		mContext = c;
	}

	/*
	 * 根据歌曲名和歌手名取得该歌的XML信息文件 返回歌词保存路径
	 */
	public String searchLyricFromWeb(String musicName, String singerName) {
		Log.i(TAG, "下载前，歌曲名:" + musicName + ",歌手名:" + singerName);

		// 传进来的如果是汉字，那么就要进行编码转化
		try {
			musicName = URLEncoder.encode(musicName, UTF_8);
			singerName = URLEncoder.encode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		// 百度音乐盒的API
		String strUrl = "http://box.zhangmen.baidu.com/x?op=12&count=1&title="
				+ musicName + "$$" + singerName + "$$$$";

		// 生成URL
		try {
			mUrl = new URL(strUrl);
			Log.i(TAG, "请求获取歌词信息的URL：" + mUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) mUrl
					.openConnection();
			httpConn.setReadTimeout(mTimeOut);
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.i(TAG, "http连接失败");
				return null;
			}
			httpConn.connect();
			Log.i(TAG, "http连接成功");

			// 将百度音乐盒的返回的输入流传递给自定义的XML解析器，解析出歌词的下载ID
			mDownloadLyricId = mLyricXMLParser.parseLyricId(httpConn
					.getInputStream());
			httpConn.disconnect();
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.i(TAG, "http连接连接IO异常");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "XML解析错误");
			return null;
		}
		return fetchLyricContent(musicName, singerName);
	}

	/** 根据歌词下载ID，获取网络上的歌词文本内容 */
	private String fetchLyricContent(String musicName, String singerName) {
		if (mDownloadLyricId == -1) {
			Log.i(TAG, "未指定歌词下载ID");
			return null;
		}
		BufferedReader br = null;
		StringBuilder content = null;
		String temp = null;
		String lyricURL = "http://box.zhangmen.baidu.com/bdlrc/"
				+ mDownloadLyricId / 100 + "/" + mDownloadLyricId + ".lrc";
		Log.i(TAG, "歌词的真实下载地址:" + lyricURL);

		try {
			mUrl = new URL(lyricURL);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}

		// 获取歌词文本，存在字符串类中
		try {
			// 建立网络连接
			br = new BufferedReader(new InputStreamReader(mUrl.openStream(),
					GB2312));
			if (br != null) {
				content = new StringBuilder();
				// 逐行获取歌词文本
				while ((temp = br.readLine()) != null) {
					content.append(temp);
					Log.i(TAG, "<Lyric>" + temp);
				}
				br.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.i(TAG, "歌词获取失败");
		}

		try {
			musicName = URLDecoder.decode(musicName, UTF_8);
			singerName = URLDecoder.decode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (content != null) {
			// 检查保存的目录是否已经创建

			String folderPath = PreferenceManager.getDefaultSharedPreferences(
					mContext).getString(Constant.KEY_LYRIC_SAVE_PATH,
					Constant.LYRIC_SAVE_FOLDER_PATH);
			File file = new File(Constant.ROOT_PATH);
			if(!file.exists()){
				file.mkdir();
			}
			
			File savefolder = new File(folderPath);
			if (!savefolder.exists()) {
				savefolder.mkdirs();
			}
			String savePath = folderPath + musicName + "_" + singerName
					+ ".lrc";
			Log.i(TAG, "歌词保存路径:" + savePath);

			saveLyric(content.toString(), savePath);

			return savePath;
		} else {
			return null;
		}

	}

	/** 将歌词保存到本地，写入外存中 */
	private void saveLyric(String content, String filePath) {
		// 保存到本地
		File file = new File(filePath);
		
		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
			Log.i(TAG, "很遗憾，将歌词写入外存时发生了IO错误");
		}
		Log.i(TAG, "歌词保存成功");
	}
	
	public String downloadLyricJson(String name,String artist){
		String savePath =null;
		Log.i(TAG, "downloadLyric下载方式二------ 下载前，歌曲名:" + name + ",歌手名:" + artist);

		// 传进来的如果是汉字，那么就要进行编码转化
		try {
			name = URLEncoder.encode(name, UTF_8);
			artist = URLEncoder.encode(artist, UTF_8);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			String lyricUrl = "http://geci.me/api/lyric/"+name+"/"+artist;
			HttpGet get = new HttpGet(lyricUrl);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = null;
			response = client.execute(get);
			if(response.getStatusLine().getStatusCode()==200){
				String result = EntityUtils.toString(response.getEntity());
				JSONObject obj = new JSONObject(result);
				if(obj.getInt("count")==0){
					return null;
				}
				if(obj.getInt("code")!=0){
					return null;
				}
				JSONArray array = obj.getJSONArray("result");
				List<LyricInfo> list = JSON.parseArray(array.toString(), LyricInfo.class);
				return downloadLyricContent(list,name,artist);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return savePath;
	}
	
	private String downloadLyricContent(List<LyricInfo> mList,String name,String artist){
		String lyricPath =null;
		if(mList==null){
			Log.i(TAG, "mList 为空");
			return null;
		}
		for(int i=0;i<mList.size();i++){
			try {
				String lyricUrl = mList.get(i).getLrc();
				Log.i(TAG, "歌词下载地址是："+lyricUrl);
				HttpGet get = new HttpGet(lyricUrl);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = null;
				response = client.execute(get);
				if(response.getStatusLine().getStatusCode()==200){
					String result = EntityUtils.toString(response.getEntity(),UTF_8);
					Log.i(TAG, "歌词下载成功---"+result);
					
					try {
						name = URLDecoder.decode(name, UTF_8);
						artist = URLDecoder.decode(artist, UTF_8);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (result != null) {
						// 检查保存的目录是否已经创建

						String folderPath = PreferenceManager.getDefaultSharedPreferences(
								mContext).getString(Constant.KEY_LYRIC_SAVE_PATH,
								Constant.LYRIC_SAVE_FOLDER_PATH);
						File file = new File(Constant.ROOT_PATH);
						if(!file.exists()){
							file.mkdir();
						}
						
						File savefolder = new File(folderPath);
						if (!savefolder.exists()) {
							savefolder.mkdirs();
						}
						String savePath = folderPath + name + "_" + artist
								+ ".lrc";
						Log.i(TAG, "歌词保存路径:" + savePath);

						saveLyric(result, savePath);
						lyricPath = savePath;
						return lyricPath;
					} else {
						return null;
					}
					
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lyricPath;
	}
	
}
