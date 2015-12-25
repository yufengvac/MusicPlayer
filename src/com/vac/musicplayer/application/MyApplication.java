package com.vac.musicplayer.application;

import java.io.File;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.utils.ACache;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
			if (!new File(Constant.IMAGECACHE).exists()) {
				new File(Constant.IMAGECACHE).mkdirs(); 
			}
		   File cacheDir =StorageUtils.getOwnCacheDirectory(this, Constant.IMAGECACHE);
	        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
	    	        .threadPoolSize(3) // default  线程池内加载的数量
	    	        .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
	    	        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
	    	        .denyCacheImageMultipleSizesInMemory()
	    	        .memoryCache(new LruMemoryCache(12 * 1024 * 1024)) //可以通过自己的内存缓存实现
	    	        .memoryCacheSize(12 * 1024 * 1024)  // 内存缓存的最大值
	    	        .memoryCacheSizePercentage(13) // default
	    	        .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径  
	    	        .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
	    	        .diskCacheFileCount(100)  // 可以缓存的文件数量 
	    	        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
	    	        .diskCacheFileNameGenerator(new Md5FileNameGenerator()) 
	    	        .tasksProcessingOrder(QueueProcessingType.LIFO) 
	    	        .imageDownloader(new BaseImageDownloader(this)) // default
	    	        .imageDecoder(new BaseImageDecoder(false)) // default
	    	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
	    	        .imageDownloader(new BaseImageDownloader(this,15 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间   
	    	        .writeDebugLogs() // 打印debug log
	    	        .build(); //开始构建
	    	ImageLoader.getInstance().init(config); 
	    	
	}
	
	public DisplayImageOptions getImageOptions(int resId,int time){
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
	        .showImageOnLoading(resId) //设置图片在下载期间显示的图片  
	        .showImageForEmptyUri(resId)//设置图片Uri为空或是错误的时候显示的图片  
	        .showImageOnFail(resId)  //设置图片加载/解码过程中错误时候显示的图片
	        .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	        .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
	        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示  
	        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	        //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置  
	        .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
	        //设置图片加入缓存前，对bitmap进行设置  
	        //.preProcessor(BitmapProcessor preProcessor)  
	        .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
//	        .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少  
	        .displayer(new FadeInBitmapDisplayer(time))//是否图片加载好后渐入的动画时间，可能会出现闪动
	        .build();//构建完成
		return options;
	}
	public DisplayImageOptions getImageOptionsNoWaittingDrawable(int time){
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
	        .cacheInMemory(true)//设置下载的图片是否缓存在内存中  
	        .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中  
	        .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
	        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示  
	        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
	        //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置  
	        .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
	        //设置图片加入缓存前，对bitmap进行设置  
	        //.preProcessor(BitmapProcessor preProcessor)  
	        .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
//	        .displayer(new RoundedBitmapDisplayer(20))//不推荐用！！！！是否设置为圆角，弧度为多少  
	        .displayer(new FadeInBitmapDisplayer(time))//是否图片加载好后渐入的动画时间，可能会出现闪动
	        .build();//构建完成
		return options;
	}
}
