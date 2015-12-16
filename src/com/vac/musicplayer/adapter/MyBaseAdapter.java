package com.vac.musicplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
/***
 * @title MyBaseAdapter<T>
 * @author vac
 *@description 这是所有适配器的父类，懒得写相同的方法了，封装了一下
 * @param <T> 模型javabean
 * @date 2015年12月16日10:57:45
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mData = new ArrayList<T>();
	protected LayoutInflater mlayoutInflater;
	protected ImageLoader mImageLoader ;
	
	public MyBaseAdapter(Context mContext) {
		this.mContext = mContext;
		mlayoutInflater = LayoutInflater.from(mContext);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public abstract long getItemId(int position);
	
	/**
	 * @description 清空数据源，并设置数据，刷新适配器
	 * @param list
	 */
	public void setData(List<T> list){
		mData.clear();
		if(list!=null){
			mData.addAll(list);
		}
		notifyDataSetChanged();
	}
	
	/**
	 * @description 在原有数据的基础上，添加数据，刷新适配器
	 * @param list
	 */
	public void addData(List<T> list){
		mData.addAll(list);
		notifyDataSetChanged();
	}
	
	/**
	 * @description 添加一条数据，并不刷新适配器，需要手动调用刷新的方法
	 * @param t
	 */
	public void addOneData(T t){
		if(t!=null){
			mData.add(t);
		}
	}
	
	/**
	 * @description 清空数据源，添加一条数据，并不刷新适配器，需要手动调用刷新的方法
	 * @param t
	 */
	public void clearAndAddOneData(T t){
		mData.clear();
		if(t!=null){
			mData.add(t);
		}
	}
	
	/**
	 * @description 清空数据源，并不刷新适配器，需要手动调用刷新的方法
	 */
	public void clear(){
		mData.clear();
	}
	
	/**
	 * @description 清空数据源，刷新适配器
	 */
	public void clearDataAndNotify(){
		mData.clear();
		notifyDataSetChanged();
	}
	
	/**
	 * @description 删除第position位置上的数据，刷新适配器
	 * @param position
	 */
	public void removeOneData(int position){
		mData.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup viewGroup);

}
