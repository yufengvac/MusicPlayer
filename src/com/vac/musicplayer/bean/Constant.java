package com.vac.musicplayer.bean;

public class Constant {

	public static final String PLAYLIST_MUISC ="playlist_music";
	
	/**是从播放列表点击过去   value ->True*/
	public static final String CLICK_MUSIC_LIST = "click_music_list";
	
	public static final String DEFAULT_MUISC_LIST= "default_music_list";
	
	/**从播放列表点击过去的歌曲ID key  value->_ID*/
	public static final String PLAYLIST_MUSIC_REQUEST_ID = "playlist_music_request_id";
	
	/**返回的Music信息 value->Music*/
	public static final String PLAYING_MUSIC_ITEM = "playing_music_item";
	
	/**返回的Music播放状态信息 value->Music
	 * 					public static final int Stopped =0;
	 *					public static final int Prepraing=1;
	 *					public static final int Playing=2;
	 *					public static final int Paused =3;
	 *value->mState
	 * */
	public static final String PLAYING_MUSIC_STATE = "playing_music_state";
	
	/**音乐播放时或者暂停时的 进度  value->progress*/
	public static final String PLAYING_MUSIC_PROGRESS="playing_music_progress";
	
	/**当前播放的音乐在播放列表中的位置 value -> mPlayingPosition*/
	public static final String  PLAYING_MUSIC_POSITION_IN_LIST = "playing_music_position_in_list";

	/**当前服务中的播放列表  value->currentPlayList*/
	public static final String PLAYING_MUSIC_CURRENT_LIST = "playing_music_current_list";

	/**记录下当前播放的音乐的 名称*/
	public static final String SHARE_NMAE_MUSIC = "share_name_music";
	
	/**记录下当前播放的音乐的位置*/
	public static final String SHARE_NMAE_MUSIC_POSITION = "share_name_music_position";
}
