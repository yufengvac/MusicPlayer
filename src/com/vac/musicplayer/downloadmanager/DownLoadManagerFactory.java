package com.vac.musicplayer.downloadmanager;

import android.content.Context;
/***
 * @author vac
 * @description 创建一个DownLoadManager对象
 * @date 2016年1月23日16:35:27
 */
public class DownLoadManagerFactory {
	
	private static DownLoadManager mDownLoadM = null;
	private DownLoadManagerFactory(){};
	public static DownLoadManager getInstance(Context context){
		if (mDownLoadM==null) {
			mDownLoadM = new DownLoadManager(context);
		}
		return mDownLoadM;
	}
}
