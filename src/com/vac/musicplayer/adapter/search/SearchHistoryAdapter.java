package com.vac.musicplayer.adapter.search;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vac.musicplayer.R;
import com.vac.musicplayer.adapter.MyBaseAdapter;
import com.vac.musicplayer.bean.Constant;
import com.vac.musicplayer.utils.PreferHelper;

public class SearchHistoryAdapter extends MyBaseAdapter<String> {

	public SearchHistoryAdapter(Context mContext) {
		super(mContext);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	ViewHolder holder;
	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = mlayoutInflater.inflate(R.layout.item_search_history, null);
			holder.title = (TextView) convertView.findViewById(R.id.item_search_history_title);
			holder.delete = (ImageView) convertView.findViewById(R.id.item_search_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(mData.get(position));
		holder.delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String history = PreferHelper.readString(mContext, Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY);
				String[] historys = history.split(",");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < historys.length; i++) {
					if (!historys[i].isEmpty()) {
						if (!historys[i].equals(mData.get(position))) {
							sb.append(historys[i]).append(",");
						}
					}
				}
				PreferHelper.write(mContext, Constant.SEARCH_HISTORY, Constant.SEARCH_HISTORY, sb.toString());
				
				String[] historys_ = sb.toString().split(",");
				int len = historys_.length;
				if (historys_.length>=10) {
					len = 10;
				}
				clear();
				for (int i = 0; i < len; i++) {
					if (!historys_[len-i-1].isEmpty()) {
						addOneData(historys_[len-i-1]);
					}
				}
				notifyDataSetChanged();
				
			}
		});
		return convertView;
	}

	private class ViewHolder{
		private TextView title;
		private ImageView delete;
	}
}
