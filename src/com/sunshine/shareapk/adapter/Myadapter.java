package com.sunshine.shareapk.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunshine.shareapk.R;

public class Myadapter extends BaseAdapter {
	List<ApplicationInfo> mApplist;
	Context ctx;


	public Myadapter(List<ApplicationInfo> mApplist, Context ctx) {
		this.ctx = ctx;
		this.mApplist = mApplist;
	}

	@Override
	public int getCount() {
		return mApplist.size();
	}

	@Override
	public ApplicationInfo getItem(int position) {
		return mApplist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(ctx, R.layout.item_list_app, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		ApplicationInfo item = getItem(position);
		holder.iv_icon.setImageDrawable(item.loadIcon(ctx.getPackageManager()));
		holder.tv_name.setText(item.loadLabel(ctx.getPackageManager()));
		return convertView;
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;

		public ViewHolder(View view) {
			iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			tv_name = (TextView) view.findViewById(R.id.tv_name);

			view.setTag(this);
		}

	}

}