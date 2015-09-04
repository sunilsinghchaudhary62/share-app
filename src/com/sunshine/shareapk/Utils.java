package com.sunshine.shareapk;

import java.io.File;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

public class Utils 
{
	static void open(ApplicationInfo item,Context ctx) {
		// open app
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(item.packageName);
		List<ResolveInfo> resolveInfoList = ctx.getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			ResolveInfo resolveInfo = resolveInfoList.get(0);
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName componentName = new ComponentName(
					activityPackageName, className);

			intent.setComponent(componentName);
			ctx.startActivity(intent);
		}
	}
	

	public static void ShareAPK(ApplicationInfo item,Context ctx) {
		try {
			File srcFile = new File(item.publicSourceDir);
			Intent share = new Intent();
			share.setAction(Intent.ACTION_SEND);
			share.setType("application/vnd.android.package-archive");
			share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
			ctx.startActivity(Intent.createChooser(share, "Sharing"));
		} catch (Exception e) {
			Log.e("ShareApp", e.getMessage());
		}

	}
	


	static int dp2px(int dp,Context ctx) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				ctx.getResources().getDisplayMetrics());
	}
	public static boolean  isAppPresent(String packageName,Context context) {


		  try{
		    ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, 0 );
		    return true;

		} catch( PackageManager.NameNotFoundException e ){

		   return false;
		}

		 }
}
