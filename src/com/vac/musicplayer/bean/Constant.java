package com.vac.musicplayer.bean;

import java.io.File;

import android.os.Environment;

public class Constant {

	public static final String ROOT_PATH =Environment.getExternalStorageDirectory()
			+ File.separator+ "yufengvac"+ File.separator ;
	public static final String LYRIC_SAVE_FOLDER_PATH = Environment.getExternalStorageDirectory()
			+ File.separator+ "yufengvac"+ File.separator + "lyric" + File.separator;
	public static final String ARTIST_PICTURE_PATH = Environment.getExternalStorageDirectory()
			+File.separator +"yufengvac" +File.separator +"Images"+File.separator;
	
	public static final String KEY_LYRIC_SAVE_PATH = "key_lyric_save_path";
	
	public static final String PLAYLIST_MUISC ="playlist_music";
	
	/**音乐来源 网络、本地*/
	public static final String PLAYING_MUSIC_TYPE = "playing_music_type";
	
	/**音乐来源 网络*/
	public static final String PLAYING_MUSIC_TYPE_NET = "playing_music_type_net";
	
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

	/**当前服务中的音乐播放模式*/
	public static final String PLAYING_MUSIC_PLAYMODE = "playing_music_playmode";
	
	/**记录下当前播放的音乐的 名称*/
	public static final String SHARE_NMAE_MUSIC = "share_name_music";
	
	/**记录下当前播放的音乐的位置*/
	public static final String SHARE_NMAE_MUSIC_POSITION = "share_name_music_position";

	/**获取歌手的写真图片集*/
	public static final String GET_SINGER_XIEZHEN_PIC = "http://artistpicserver.kuwo.cn/pic.web?type=big_artist_pic&pictype=url&content=list&&id=0&name=%E5%91%A8%E6%9D%B0%E4%BC%A6&rid=&from=pc&json=1&version=1&width=1366&height=768";
	
	/**获取歌词   http://geci.me/api/lyric/向往/李健*/
	public static final String GET_LYRIC = "http://geci.me/api/lyric/%E5%90%91%E5%BE%80/%E6%9D%8E%E5%81%A5";

	/**点击歌手进入详细页*/
	public static final String ARTIST_LISTVIEW_ITEM = "artist_listview_item";
	
	/**列表类型  默认 / 许嵩 / 周杰伦.... */
	public static final String MUSIC_LIST_TYPE = "music_list_type";
	
	public static final String DEFAULT_MUSIC_LIST_TYPE = "默认列表";
	
	/**首页选择的图片地址和颜色值*/
	public static final String MAIN_BG_COLOR = "main_bg_color";
	
	/**图片缓存地址*/
	public static final String IMAGECACHE = Environment.getExternalStorageDirectory()+ File.separator+"yufengvac"+File.separator+"cache"+File.separator+"image";
	
	/**网络临时文件缓存地址*/
	public static final String FILECACHE = Environment.getExternalStorageDirectory()+ File.separator+"yufengvac"+File.separator+"cache";
	
	/**json文件缓存地址*/
	public static final String JSONFILECACHE = Environment.getExternalStorageDirectory()+ File.separator+"yufengvac"+File.separator+"cache"+File.separator+"jsonfile";
	
	/***我创建的歌单*/
	public static final String MY_CREATE_SONG_LIST ="my_create_song_list";
	
	/**搜索历史记录*/
	public static final String SEARCH_HISTORY = "search_history";
	
	/**搜索音乐*/
//	http://s.music.163.com/search/get/
//		获取方式：GET
//		参数：
//		src: lofter //可为空
//		type: 1
//		filterDj: true|false //可为空
//		s: //关键词
//		limit: 10 //限制返回结果数
//		offset: 0 //偏移
//		callback: //为空时返回json，反之返回jsonp callback
//	http://s.music.163.com/search/get?type=1&filterDj=false&s=山水之间/许嵩
//  http://s.music.163.com/search/get?type=1&filterDj=false&s=许嵩&limit=20&offset=0
	public static final String SEARCH_MUSIC = "http://s.music.163.com/search/get";
	
	/**
	 * 天天动听接口 搜索单曲
	 * http://search.dongting.com/song/search?uid=000000000000000
	 * & page=1&id=4241599212142040&from=android&resolution=720x1280
	 * &net=2&api_version=1.0&utdid=VVwoC4pkXHwDAKnEMyIDH0ka
	 * &longitude=0.0&user_id=0&splus=4.4.4%252F19&tid=0
	 * &client_id=a4b1447f2bf664a4640c5af030e9fed1&f=f0&os=4.4.4
	 * &app=ttpod
	 * &rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys
	 * &alf=alf10001791&imei=000000000000000&agent=none&size=50&v=v8.2.0.2015091720
	 * &s=s200&ram=1028940+kB
	 * &q=%E8%AE%B8%E5%B5%A9
	 * &active=0&latitude=0.0
	 * &language=zh&imsi=310260000000000
	 * &mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920
	 */
	public static final String TING_SINGER_SONG = "http://search.dongting.com/song/search?uid=000000000000000&&id=4241599212142040&from=android&resolution=720x1280&net=2&api_version=1.0&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&longitude=0.0&user_id=0&splus=4.4.4%252F19&tid=0&client_id=a4b1447f2bf664a4640c5af030e9fed1&f=f0&os=4.4.4&app=ttpod&rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys&alf=alf10001791&imei=000000000000000&agent=none&size=50&v=v8.2.0.2015091720&s=s200&ram=1028940+kB&active=0&latitude=0.0&language=zh&imsi=310260000000000&mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920";
	
	/**搜索专辑
	 * http://search.dongting.com/album/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys&alf=alf10001791&resolution=720x1280&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&ram=1028940+kB&page=1&q=%E8%AE%B8%E5%B5%A9&active=0&tid=0&mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920&imsi=310260000000000&splus=4.4.4%252F19
	 * page = 1
	 * q = 许嵩
	 * */
	public static final String TING_ALBUM ="http://search.dongting.com/album/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys&alf=alf10001791&resolution=720x1280&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&ram=1028940+kB&active=0&tid=0&mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920&imsi=310260000000000&splus=4.4.4%252F19";
	
	
	/**
	 * 搜索歌单
	 * http://search.dongting.com/songlist/search?uid=000000000000000&f=f0&app=ttpod&rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys&hid=4241599212142040&alf=alf10001791&resolution=720x1280&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&ram=1028940+kB&page=1&q=xusong&active=0&mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920&splus=4.4.4%252F19&imsi=310260000000000&tid=0
	 * page = 1
	 * q = 许嵩
	 */
	public static final String TING_SONGLIST = "http://search.dongting.com/songlist/search?uid=000000000000000&f=f0&app=ttpod&rom=generic%252Fvbox86p%252Fvbox86p%253A4.4.4%252FKTU84P%252Feng.buildbot.20150216.120346%253Auserdebug%252Ftest-keys&hid=4241599212142040&alf=alf10001791&resolution=720x1280&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&ram=1028940+kB&active=0&mid=HTC%2BOne%2B-%2B4.4.4%2B-%2BAPI%2B19%2B-%2B1080x1920&splus=4.4.4%252F19&imsi=310260000000000&tid=0";
	
	/**
	 * 搜索MV
	 * http://search.dongting.com/mv/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&alf=alf10001791&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&page=1&q=%E8%AE%B8%E5%B5%A9&imsi=310260000000000&tid=0
	 * page = 1
	 * q = 许嵩
	 */
	public static final String TING_MV = "http://search.dongting.com/mv/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&alf=alf10001791&net=2&size=50&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&imsi=310260000000000&tid=0";
	/**歌曲详情*/
//	http://music.163.com/api/song/detail/?id=33419619&ids=[33419619]
	
	/***
	 * 搜索歌手
	 * http://search.dongting.com/artist/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&alf=alf10001791&net=2&size=1&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&page=1&q=%E5%91%A8%E6%9D%B0%E4%BC%A6&imsi=310260000000000&tid=0
	 * q=周杰伦
	 */
	public static final String SEARCH_SINGER = "http://search.dongting.com/artist/search?uid=000000000000000&f=f0&app=ttpod&hid=4241599212142040&alf=alf10001791&net=2&size=1&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&s=s200&page=1&imsi=310260000000000&tid=0";

	
	/***
	 * 热门搜索
	 * http://api.dongting.com/misc/sug/billboard?uid=000000000000000&f=f0&v=v8.2.0.2015091720&app=ttpod&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&hid=4241599212142040&s=s200&alf=alf10001791&imsi=310260000000000&tid=0&net=2&size=20
	 */
	public static final String SEARCH_HOT = "http://api.dongting.com/misc/sug/billboard?uid=000000000000000&f=f0&v=v8.2.0.2015091720&app=ttpod&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&hid=4241599212142040&s=s200&alf=alf10001791&imsi=310260000000000&tid=0&net=2&size=20";
	
	/***
	 * 根据AlbumId搜索专辑详细
	 * 
	 */
	public static final String SEARCH_ALBUM_PATE1= "http://api.dongting.com/song/album/";
	
	public static final String SEARCH_ALBUM_PATE2= "http://api.dongting.com/song/album/306233?f=0&os=4.4.4&alf=10001791&imei=000000000000000&from=android&resolution=720x1280&net=2&api_version=1.0&agent=none&v=v8.2.0.2015091720&utdid=VVwoC4pkXHwDAKnEMyIDH0ka&userId=0&longitude=0.0&user_id=0&latitude=0.0&language=zh";
}
