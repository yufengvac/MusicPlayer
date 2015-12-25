package com.vac.musicplayer.utils;

import java.io.File;

import com.vac.musicplayer.bean.Constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtils {

	private static ACache aCache;
	public static void get(Context mContext,String url,boolean isUseCache,boolean isToCache){
		if (!new File(Constant.FILECACHE).exists()) {
			new File(Constant.FILECACHE).mkdirs();
			aCache = ACache.get(mContext, Constant.FILECACHE);
		}
		if (!isNetworkAvailable(mContext)) {
			
		}
	}
	
	public static boolean isNetworkAvailable(Context mContext) {
		Context context = mContext.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					System.out.println(i + "===状态==="
							+ networkInfo[i].getState());
					System.out.println(i + "===类型==="
							+ networkInfo[i].getTypeName());
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
