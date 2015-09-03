package com.vac.musicplayer.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.vac.musicplayer.bean.Constant;

public class MyImageLoader {

	private static final String TAG = MyImageLoader.class.getSimpleName();
	private int height ;
	private int width;
	private File artistF;
	private ImageView rl_content;
	private boolean isStartLoading=true;
	private String artistFile =null;
	private Handler mHandler = new Handler();
	
	private String[] picList ;
	private int count=0;//计数
	private List<String> downloadpicList;//下载图片的地址
	
	public MyImageLoader(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public Bitmap downloadBitmap(String url){
		Bitmap bitmap =null;
		AndroidHttpClient client  = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpResponse response = null;
		HttpGet httpGet =null;
		try { 
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				if(entity!=null){
					bitmap = BitmapFactory.decodeStream(entity.getContent());
					return bitmap;
				}else{
					return null;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (client != null && client.getConnectionManager() != null) {
            	client.getConnectionManager().shutdown();
            	client.close();
            }
        }
		return bitmap;
	}
	
	/**
	 * 根据歌手名字设置背景
	 * @param artist
	 */
	public void setAlphaImageView(ImageView rl,String artist){
		try {
			this.rl_content = rl;
			if(artist.equals("未知艺术家")){
				return;
			}
			
			artist = URLEncoder.encode(artist, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String url = "http://artistpicserver.kuwo.cn/pic.web?type=big_artist_pic&pictype=url&content=list&&id=0&name="
				+artist+"&rid=&from=pc&json=1&version=1&width=1200&height=800";
		//判断是否存在该歌手的图片
		String rootFile = Constant.ARTIST_PICTURE_PATH;
		File file = new File(rootFile);
		if(!file.exists()){
			Log.d(TAG, "根路径不存在，创建一个");
			file.mkdirs();
		}
		
		try {
			String saveAritst = URLDecoder.decode(artist, "UTF-8");
			artistFile = Constant.ARTIST_PICTURE_PATH+saveAritst+File.separator;
			artistF = new File(artistFile);
			if(!artistF.exists()){
				Log.d(TAG, "歌手图片文件夹不存在，创建一个");
				artistF.mkdirs();
				//去网络获取吧
				new getAritstPicAsyncTask().execute(url);
				isStartLoading =false;
			}else{
				
//				Timer time = new Timer();
//				TimerTask task = new TimerTask() {
//					
//					@Override
//					public void run() {
//						Bitmap bitmap =BitmapFactory.decodeFile(picList[]);
//						Drawable drawable = new BitmapDrawable(result);
//						drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
//						rl_content.setImageDrawable(drawable);
//					}
//				};
//				time.schedule(task, 5000);
				//直接从本地获取
				picList = artistF.list();
				if(picList.length==0){
					new getAritstPicAsyncTask().execute(url);
					isStartLoading =false;
				}else{
					toLoadingLocalPic();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private class getAritstPicAsyncTask extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			HttpGet get = new HttpGet(arg0[0]);
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			try {
				HttpResponse response = client.execute(get);
				Log.d(TAG, "获取歌手图片---返回码"+response.getStatusLine().getStatusCode());
				if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
					String result = EntityUtils.toString(response.getEntity());
					Log.d(TAG, "result="+result);
					return result;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
			    if (client != null && client.getConnectionManager() != null) {
	            	client.getConnectionManager().shutdown();
	            	client.close();
	            }
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			downloadpicList = new ArrayList<String>();
			count=0;
			if(result!=null){
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray array = obj.getJSONArray("array");
					for(int i=0;i<array.length();i++){
						JSONObject obj1 = array.getJSONObject(i);
						if(obj1.has("bkurl")){
							downloadpicList.add(obj1.getString("bkurl"));
						}else if(obj1.has("wpurl")){
							downloadpicList.add(obj1.getString("wpurl"));
						}
					}
					Log.d(TAG, "图片URL已经获取完毕--大小是："+downloadpicList.size());
					if(downloadpicList.size()>20){
						for(int i=0;i<20;i++){
							new downAllPicAsyncTask().execute(downloadpicList.get(i));
						}
					}else{
						for(int i=0;i<downloadpicList.size();i++){
							new downAllPicAsyncTask().execute(downloadpicList.get(i));
							isStartLoading=false;
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private class  downAllPicAsyncTask extends AsyncTask<String,Void,Bitmap>{

		private String url=null;
		@Override
		protected Bitmap doInBackground(String... arg0) {
			url = arg0[0];
		    Bitmap bitmap = null;
	        HttpClient httpClient = new DefaultHttpClient();
	        try {
	            httpClient.getParams().setParameter(
	                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	            HttpGet httpGet = new HttpGet(url);
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	            	Log.d(TAG, "图片下载成功！！！");
	                HttpEntity entity = httpResponse.getEntity();
	                //解决缩放大图时出现SkImageDecoder::Factory returned null错误
	                byte[] byteIn = EntityUtils.toByteArray(entity);
	                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	                bmpFactoryOptions.inJustDecodeBounds = true;
	                
	                BitmapFactory.decodeByteArray(byteIn, 0, byteIn.length, bmpFactoryOptions);
	                
	                Log.d(TAG, "bmpFactoryOptions.outHeight="+bmpFactoryOptions.outHeight+
	                		",bmpFactoryOptions.outWidth="+bmpFactoryOptions.outWidth+",height"
	                		+height+",width="+width);
	                int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / height);
	                int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth/ width);
	                
	                if (heightRatio > 1 && widthRatio > 1) {
	                    bmpFactoryOptions.inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
	                }else{
//	                	Matrix matrix = new Matrix();
//	                	int width = widthRatio;//获取资源位图的宽
//	                	int height = heightRatio;//获取资源位图的高
//	                	float w = scalX / viewBg.getWidth();
//	                	float h = scalY / viewBg.getHeight();
//	                	 float sx=(float)width/widthRatio;//要强制转换，不转换我的在这总是死掉。
//	                	 float sy=(float)height/heightRatio;
//	                	
//	                	matrix.postScale(sx, sy);//获取缩放比例
//	                	Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, true); //根据缩放比例获取新的位图
	                }
	                bmpFactoryOptions.inJustDecodeBounds = false;
	                bitmap = BitmapFactory.decodeByteArray(byteIn, 0, byteIn.length, bmpFactoryOptions);
	                return bitmap;
	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (ConnectTimeoutException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            if (httpClient != null && httpClient.getConnectionManager() != null) {
	                httpClient.getConnectionManager().shutdown();
	            }
	        }
	        return null;
		}
		
		@SuppressLint("NewApi") @Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			isStartLoading=false;
			if(result!=null){
				synchronized (result) {
					saveBitmap(artistFile, getFileName(url), result);
//					Drawable drawable = new BitmapDrawable(result);
//					drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
//					rl_content.setImageDrawable(drawable);
					count= count+1;
				
					if(downloadpicList.size()<20){
						if(count==downloadpicList.size()){
							toLoadingLocalPic();
						}
					}else{
						if(count==20){
							toLoadingLocalPic();
						}
					}
					
					url =null;
				}
			}
		}
		
	}
	
	 /**
     * 保存Bitmap到指定目录
     * 
     * @param dir
     *            目录
     * @param fileName
     *            文件名
     * @param bitmap
     * @throws IOException
     */
    public void saveBitmap(String dir, String fileName, Bitmap bitmap) {
        if (bitmap == null||dir==null) {
            return;
        }
        File file = new File(dir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, "图片存储成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
//        	if(bitmap!=null){
//            	bitmap.recycle();
//            }
        }
    }
    
    public String getFileName(String url){
    	String newStr = url.replace("http://topmusic.kuwo.cn/today_recommend/images/", "");
    	Log.d(TAG, "文件名是："+newStr.replaceAll("/", "_"));
    	return newStr.replaceAll("/", "_");
    }
    
    public void cancleLoading(){
    	isStartLoading =false;
    }
    
    public void toLoadingLocalPic(){
    	isStartLoading =true;
    	picList = artistF.list();
    	new Thread(){
			public void run() {
				int i=0;
				for(;i<picList.length;i++){
					if(isStartLoading){
						Log.d(TAG, "本地图片路径是："+picList[i]);
						Bitmap bitmap = BitmapFactory.decodeFile(artistFile+picList[i]);
						final Drawable drawable = new BitmapDrawable(bitmap);
						drawable.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								rl_content.setImageDrawable(drawable);
							}
						});
						if(i==picList.length-1){
							i=0;
						}
						try {
							Thread.sleep(4000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else{
						break;
					}
				}
			};
		}.start();
    }
	
}
