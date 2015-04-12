package com.xandy.sharepanel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xandy.expanddialog.ExpandDialog;

public class SharePanel implements OnItemClickListener {
	
	private static final String TAG = "SharePanel";
	
	private Context mContext;
	private ExpandDialog mDialog;
	private String mImagePath;
	private Uri mImageUri;
	private String mMsg;
	
	private String[] mPackages = new String[]{};
	private String[] mActivitys = new String[]{};
	private String[] mLabels = new String[]{};
	private int[] mResIds = {
			R.drawable.wechat_friend,
			R.drawable.wechat_pyq,
			R.drawable.sina_weibo,
			R.drawable.tecent_qq,
			R.drawable.tencent_weibo,
			R.drawable.tecent_qzone
	};
	
	private GridView mPanelContent;
	private ShareAdapter mShareAdapter;
	
	private static final int MODE_IMAGE_PATH = 0;
	private static final int MODE_IMAGE_URI  = 1;
	private static final int MODE_TEXT_MSG   = 2;
	private int mMode = -1;
	
	
	public SharePanel( Context context ) {
		this.mContext = context;
		initSharePanel();
	}
	
	private void initSharePanel() {
		Resources resources = mContext.getResources();
		mPackages = resources.getStringArray(R.array.share_list_package_name);
		mActivitys = resources.getStringArray(R.array.share_list_activity_name);
		mLabels = resources.getStringArray(R.array.share_list_label);
		
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		View panelView = mInflater.inflate(R.layout.share_panel, null);
		mPanelContent = (GridView) panelView.findViewById(R.id.panel_share_gridview);
		mShareAdapter = new ShareAdapter(mContext);
		mPanelContent.setAdapter(mShareAdapter);
		mPanelContent.setOnItemClickListener(this);
		
		mDialog = new ExpandDialog.Builder(mContext)
						.setView(panelView)
						.setCancelable(true)
						.setGravity(Gravity.BOTTOM)
						.setCanceledOnTouchOutside(true)
						.create();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View parent, int position, long id) {
		if( ShareTools.checkInstallation(mContext, mPackages[position]) ) {
			mContext.startActivity( getShareIntent(position) );			
		} else {
			Toast.makeText(mContext, R.string.share_panel_not_install_tip,Toast.LENGTH_SHORT).show();
		}
		dissmis();
	}
	
	private Intent getShareIntent( int position ) {
		ComponentName shareActivity = new ComponentName(mPackages[position], mActivitys[position]);
		if( MODE_IMAGE_PATH == mMode ) {
			return ShareTools.getImageShareIntent(shareActivity, mImagePath);
		} else if( MODE_IMAGE_URI == mMode ) {
			return ShareTools.getImageShareIntent(shareActivity, mImageUri);
		} else {
			return ShareTools.getTextShareIntent(shareActivity, mMsg);
		}
	}
	
	public void shareImage(String imagePath) {
		mMode = MODE_IMAGE_PATH;
		this.mImagePath = imagePath;
		show();
	}
	
	public void shareImage(Uri imageUri) {
		mMode = MODE_IMAGE_URI;
		this.mImageUri = imageUri;
		show();
	}
	
	public void shareText(String msg) {
		mMode = MODE_TEXT_MSG;
		this.mMsg = msg;
		show();
	}
	
	private void show() {
		try {
			if( null != mDialog ) {
				mDialog.show();
			}			
		} catch (Exception e) {
			Log.d(TAG, e.getLocalizedMessage());
		}		
	}
	
	public void dissmis() {
		if( null != mDialog ) {
			mDialog.dismiss();
		}
	}
	
	private class ShareAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mInflater;
		public ShareAdapter( Context context ) {
			this.mContext = context;
			mInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mPackages.length ;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
	        if (convertView == null) {
	        	convertView = mInflater.inflate(R.layout.share_intent_item, parent, false);
	            holder = new ViewHolder();
	            holder.mIcon = (ImageView) convertView.findViewById(R.id.share_intent_item_icon);
	            holder.mLabel = (TextView) convertView.findViewById(R.id.share_intent_item_label);
	            convertView.setTag(holder);
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        }
	        holder.mLabel.setText(mLabels[position]); 
	        holder.mIcon.setImageResource(mResIds[position]);
	        return convertView;
		}
	}
	
	private class ViewHolder {
		public ImageView mIcon;
		public TextView mLabel;
	}
}
