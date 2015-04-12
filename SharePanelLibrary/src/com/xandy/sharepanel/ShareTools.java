package com.xandy.sharepanel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

public class ShareTools {
	
	private static final String TAG = "ShareTools";
	
	/**
	 * 判断是否安装腾讯、新浪等指定的分享应用
	 * @param context 上下文
	 * @param packageName 应用的包名
	 * @return 安装了则返回true,没安装返回false;
	 */
	public static boolean checkInstallation(Context context , String packageName) {
		boolean isInstall = false;
		try {
			context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			isInstall = true;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isInstall;
	}
	
	/**
	 * 跳转到到应用市场下载指定应用
	 * @param context 上下文
	 * @param packageName 包名
	 */
	public static void goToMarkt( Context context , String packageName ) {
		Uri uri = Uri.parse("market://details?id=" + packageName );
		Intent markt = new Intent(Intent.ACTION_VIEW,uri);
		context.startActivity(markt);
	}
	
	/**
	 * 通过packageName和activtyName过滤,得到指定的Activity,用此ACtivity来分享图片
	 * @param packageName
	 * @param activtyName
	 * @param imagePath
	 * @return
	 */
	public static Intent getImageShareIntent( String packageName , String activtyName , String imagePath ) {
		return getImageShareIntent(new ComponentName(packageName, activtyName), imagePath);
	}
	
	/**
	 * 通过packageName和activtyName过滤,得到指定的Activity,用此ACtivity来分享图片
	 * @param packageName
	 * @param activtyName
	 * @param imageUri
	 * @return
	 */
	public static Intent getImageShareIntent( String packageName , String activtyName , Uri imageUri ) {
		return getImageShareIntent(new ComponentName(packageName, activtyName), imageUri);
	}
	
	/**
	 * 通过ComponentName过滤,得到指定的Activity,用此ACtivity来分享图片
	 * @param activity 指定的Activity
	 * @param imagePath 图片的路径
	 * @return
	 */
	public static Intent getImageShareIntent( ComponentName activity , String imagePath ) {
		return getImageShareIntent( activity, Uri.fromFile( new File(imagePath) ) );
	}
	
	/**
	 * 通过ComponentName过滤,得到指定的Activity,用此ACtivity来分享图片
	 * @param activity 指定的Activity
	 * @param imageUri 图片的Uri
	 * @return
	 */
	public static Intent getImageShareIntent( ComponentName activity , Uri imageUri ) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
		shareIntent.setComponent(activity);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		return shareIntent;
	}
	
	
	/**
	 * 通过packageName和activtyName过滤,得到指定的Activity,用此ACtivity来分享文字
	 * @param packageName
	 * @param activtyName
	 * @param msg
	 * @return
	 */
	public static Intent getTextShareIntent( String packageName , String activtyName , String msg ) {
		return getImageShareIntent(new ComponentName(packageName, activtyName), msg);
	}
	
	
	/**
	 * 通过ComponentName过滤,得到指定的Activity,用此ACtivity来分享文字
	 * @param activity 指定的Activity
	 * @param msg 图片的Uri
	 * @return
	 */
	public static Intent getTextShareIntent( ComponentName activity , String msg ) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addCategory(Intent.CATEGORY_DEFAULT);
		shareIntent.setComponent(activity);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
		return shareIntent;
	}
	
	/**
	 * 获取指定的Intent对应的第一个应用的名称
	 * @param context
	 * @param componentIntent
	 * @return componentIntent对应的第一个应用的名称
	 */
	public static String getShareActivityLabel(Context context , Intent componentIntent) {
		List<ResolveInfo> activitys = new ArrayList<ResolveInfo>();  
		PackageManager pManager = context.getPackageManager();
	    activitys = pManager.queryIntentActivities( componentIntent ,
	    		PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	    if( activitys.isEmpty() ) {
	    	return "";	    	
	    } else {
	    	return activitys.get(0).loadLabel(pManager).toString();
	    }
	}
	
	/**
	 * 获取指定的Intent对应的第一个应用的Icon
	 * @param context
	 * @param componentIntent
	 * @return componentIntent对应的第一个应用的Icon
	 */
	public static Drawable getShareActivityIcon(Context context , Intent componentIntent) {
		List<ResolveInfo> activitys = new ArrayList<ResolveInfo>();  
		PackageManager pManager = context.getPackageManager();
	    activitys = pManager.queryIntentActivities( componentIntent ,
	    		PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
	    if( activitys.isEmpty() ) {
	    	return null;	    	
	    } else {
	    	return activitys.get(0).loadIcon(pManager);
	    }
	}
	
	
	/**
	 * 查询手机内所有支持分享图片的应用，并将其打印出来
	 * @param context 上下文
	 * @return
	 */
	public static void printShareImageAppsList(Context context) {
	    List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();  
	    Intent intent=new Intent(Intent.ACTION_SEND);  
	    intent.addCategory(Intent.CATEGORY_DEFAULT);  
	    intent.setType("image/*");  
	    PackageManager pManager = context.getPackageManager();
	    mApps = pManager.queryIntentActivities(intent,PackageManager.COMPONENT_ENABLED_STATE_DEFAULT); 
	    for( ResolveInfo app : mApps ) {
	    	Log.d(TAG, "packageName : <item>" + app.activityInfo.packageName + "</item>" ); 
	    }
	    for( ResolveInfo app : mApps ) {
	    	Log.d(TAG, "calssName : <item>" + app.activityInfo.name + "</item>" ); 
	    }
	    for( ResolveInfo app : mApps ) {
	    	Log.d(TAG, "activityName : <item>" + app.loadLabel(pManager) + "</item>" ); 
	    }
	}

}
