package com.vac.musicplayer.listener;

import com.vac.musicplayer.bean.Music;

/**
 *
 * @author vac
 *@description 定义了音乐播放一系列状态改变的接口
 *@date 2015年8月28日18:38:14
 */
public interface OnPlayMusicStateListener {

	/**
	 * 音乐播放
	 */
	public abstract void onMuiscPlayed(Music music);
	
	/**
	 * 音乐暂停
	 */
	public abstract void onMusicPaused(Music music);
	
	/**
	 * 音乐停止
	 */
	public abstract void onMusicStoped();
	
	/**
	 * 播放模式改变时调用此方法
	 * 
	 * @param playMode
	 *            播放模式
	 * */
	public abstract void onPlayModeChanged(int playMode);
	
	/**
	 * 播放新的音乐
	 * @param music 
	 */
	public abstract void onNewSongPlayed(Music music,int position);
	
	/**
	 * 播放进度
	 * @param currenMillis
	 * 					当前播放进度，已播放的毫秒数（单位：毫秒）
	 */
	public abstract  void onPlayProgressUpdate(long currenMillis);
}
