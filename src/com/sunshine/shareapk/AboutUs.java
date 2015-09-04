package com.sunshine.shareapk;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.plus.PlusOneButton;

public class AboutUs extends Activity {
	TextView linktext;
	PlusOneButton pButton;
	String url = "http://play.google.com/store/apps/details?id=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus);
		((AdView) findViewById(R.id.about_main_act))
				.loadAd(new AdRequest.Builder().build());
		url = url + getPackageName();
		pButton = (PlusOneButton) findViewById(R.id.plus_one_button);
		linktext = (TextView) findViewById(R.id.linktext);
		linktext.setPaintFlags(linktext.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		linktext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://www.amnixapps.com")));
				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		super.onPause();
	}

	@Override
	protected void onResume() {
		pButton.initialize(url, 7);
		super.onResume();
	}
}
