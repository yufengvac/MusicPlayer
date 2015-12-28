package com.vac.musicplayer.listener;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public interface OnPageAddListener {
	/**歌曲、歌手、专辑、文件夹的Fragment*/
	public static final int TABMUSICLOCALFRA=100;
	
	/**某一个歌手的所有歌曲的Fragment*/
	public static final int SONGOFSINGERARTIST = 101;
	
	/**搜索页的Fragment*/
	public static final int SEARCHFRAGMENT = 102;
	
	/**搜索专辑详细页的Fragment*/
	public static final int SEARCHALBUMDETAIL= 103;
	public void onPageAddListener(int type,Bundle bundle);
	
}
