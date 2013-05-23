package com.kein.imageselector;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class YourActivity extends ImageSelectHelperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startSelectImage();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.your, menu);
		return true;
	}

}
