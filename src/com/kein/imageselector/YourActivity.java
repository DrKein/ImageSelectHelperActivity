package com.kein.imageselector;

import android.os.Bundle;
import android.view.View;

public class YourActivity extends ImageSelectHelperActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_your);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//setImageSizeBoundary(400); // optional. default is 500.
				//setCropOption(1, 1);  // optional. default is no crop.
				//setCustomButtons(btnGallery, btnCamera, btnCancel); // you can set these buttons.
				startSelectImage();
			}
		});
		
		getSelectedImageFile(); // extract selected & saved image file.
	}

}
