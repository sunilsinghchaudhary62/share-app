package com.sunshine.shareapk;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.octo.android.robodemo.LabeledPoint;
import com.octo.android.robodemo.RoboDemo;
import com.sunshine.shareapk.adapter.Myadapter;

public class MainActivity extends ActionBarActivity {

	private List<ApplicationInfo> mAppList;
	private Myadapter mAdapter;
	private SwipeMenuListView mListView;
	private ProgressDialog dialog;
	private final static String DEMO_ACTIVITY_ID = "APK";
	private boolean showDemo = true;
	private int pos;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getSharedPreferences("find", MODE_PRIVATE);
		((AdView) findViewById(R.id.main_act_add))
		.loadAd(new AdRequest.Builder().build());
		mAppList = getPackageManager().getInstalledApplications(0);

		mListView = (SwipeMenuListView) findViewById(R.id.listView);

		displayDemoIfNeeded();
		new getapk().execute();
	}

	private void getWorkOnapk() {

		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem uninstall_item = new SwipeMenuItem(
						getApplicationContext());

				uninstall_item.setBackground(new ColorDrawable(Color.parseColor("#A9E2F3")));
				uninstall_item.setWidth(Utils.dp2px(90, MainActivity.this));
				uninstall_item.setIcon(R.drawable.ic_action_discard);
				menu.addMenuItem(uninstall_item);

				// create "delete" item
				SwipeMenuItem share_item = new SwipeMenuItem(
						getApplicationContext());
				share_item.setBackground(new ColorDrawable(Color.parseColor("#CECEF6")));
				share_item.setWidth(Utils.dp2px(90, MainActivity.this));
				share_item.setIcon(R.drawable.ic_action_share);
				menu.addMenuItem(share_item);

				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());

				openItem.setBackground(new ColorDrawable(Color.parseColor("#FA5882")));
				openItem.setWidth(Utils.dp2px(90, MainActivity.this));
				openItem.setTitle("Open");
				openItem.setTitleSize(18);
				openItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(openItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				ApplicationInfo item = mAppList.get(position);

				switch (index) {
				case 0:

					if ((item.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
						Toast.makeText(getApplicationContext(),
								"system File cannot Deleted", 1000).show();
					} else {
						if (item.packageName.equals(getPackageName())) {
							Toast.makeText(getApplicationContext(),
									"Close the application first", 1000).show();
						} else {
							Unistall(item, position);
							mAdapter.notifyDataSetChanged();
						}
					}
					break;
				case 1:
					try {
						Utils.ShareAPK(item, MainActivity.this);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Can't Share",
								1000).show();
					}
					break;

				case 2:
					Utils.open(item, MainActivity.this);
					break;

				}

				return false;
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.Refresh:
			new getapk().execute();
			break;
		case R.id.aboutus:
			try {
				Intent i = new Intent(MainActivity.this, AboutUs.class);
				startActivity(i);
			} catch (Exception e) {
				Log.d("therock", e.toString());
			}
			break;
		case R.id.rateus:
			Intent rateIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + getPackageName()));
			startActivity(rateIntent);
			break;
		default:
			break;
		}

		return true;
	}

	private void displayDemoIfNeeded() {

		boolean neverShowDemoAgain = RoboDemo.isNeverShowAgain(this,
				DEMO_ACTIVITY_ID);

		if (!neverShowDemoAgain && showDemo) {
			showDemo = false;
			ArrayList<LabeledPoint> arrayListPoints = new ArrayList<LabeledPoint>();
			LabeledPoint p = new LabeledPoint(this, 0.99f, 0.45f,
					getString(R.string.swipe));
			arrayListPoints.add(p);

			p = new LabeledPoint(this, 0.95f, 0.05f,
					getString(R.string.text_move_demo_step_2));
			arrayListPoints.add(p);

			// start DemoActivity.
			Intent intent = new Intent(this, MainActivityDemoActivity.class);
			RoboDemo.prepareDemoActivityIntent(intent, DEMO_ACTIVITY_ID,
					arrayListPoints);
			startActivity(intent);
		}
	}

	public void Unistall(ApplicationInfo item, int position) {

		pos = position;
		prefs.edit().putString("packagedeleteornot", item.packageName).commit();
		Log.e("data save is===", item.packageName);
		try {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.fromParts("package", item.packageName, null));

			startActivityForResult(intent, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {

			String requestedPackageName = prefs.getString("packagedeleteornot",
					"");

			boolean isPresent = Utils.isAppPresent(
					requestedPackageName, this);

			if (isPresent) {

				Toast.makeText(getApplicationContext(), "cancel", 1000).show();
			} else {
				mAppList.remove(pos);
				mAdapter.notifyDataSetChanged();

			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public class getapk extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("please wait..");
			dialog.setCancelable(false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				runOnUiThread(new Runnable() {
					public void run() {
						getWorkOnapk();
						
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			mAdapter = new Myadapter(mAppList, MainActivity.this);
			mListView.setAdapter(mAdapter);
			dialog.dismiss();
			super.onPostExecute(result);
		}

	}
}
