package com.kein.imageselector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author kein
 * 
 */
public class ImageSelectHelperActivity extends Activity {
	/** Buttons for selector dialog */
	private View mBtnGallery = null, mBtnCamera = null, mBtnCancel = null;

	private final int REQ_CODE_PICK_GALLERY = 900001;
	private final int REQ_CODE_PICK_CAMERA = 900002;

	/**
	 * Call this to start!
	 */
	public void startSelectImage() {
		if (!checkWriteExternalPermission()) {
			showAlert("we need android.permission.WRITE_EXTERNAL_STORAGE");
			return;
		}
		if (mBtnGallery == null) {
			setDefaultButtons();
		}
		setButtonsClick();
		showSelectDialog();
	}

	private boolean checkWriteExternalPermission() {
		String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
		int res = checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);
	}

	/**
	 * Set your own button views for selector dialog.
	 */
	public void setCustomButtons(View btnGallery, View btnCamera, View btnCancel) {
		if (btnGallery == null || btnCamera == null || btnCancel == null) {
			showAlert("All buttons should not null.");
		} else {
			mBtnGallery = btnGallery;
			mBtnCamera = btnCamera;
			mBtnCancel = btnCancel;
		}
	}

	/**
	 * Set default buttons for selector dialog, unless you set.
	 */
	private void setDefaultButtons() {
		Button btn1 = new Button(this);
		Button btn2 = new Button(this);
		Button btn3 = new Button(this);
		btn1.setText("From Gallery");
		btn2.setText("From Camera");
		btn3.setText("Cancel");
		mBtnGallery = btn1;
		mBtnCamera = btn2;
		mBtnCancel = btn3;
	}

	private File getTempImageFile() {
		File path = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/temp/");
		if (!path.exists()) {
			path.mkdirs();
		}
		File file = new File(path, "tempimage.png");
		return file;
	}

	private void setButtonsClick() {
		mBtnGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectDialog.dismiss();
				Intent i = new Intent(Intent.ACTION_PICK);
				i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, REQ_CODE_PICK_GALLERY);
			}
		});
		mBtnCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSelectDialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempImageFile()));
				intent.putExtra("return-data", true);
				startActivityForResult(intent, REQ_CODE_PICK_CAMERA);
			}
		});
		mBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSelectDialog != null && mSelectDialog.isShowing()) {
					mSelectDialog.dismiss();
				}
			}
		});
	}

	private Dialog mSelectDialog;

	private void showSelectDialog() {
		if (mSelectDialog == null) {
			mSelectDialog = new Dialog(this);
			mSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			LinearLayout linear = new LinearLayout(this);
			linear.setOrientation(LinearLayout.VERTICAL);
			linear.addView(mBtnGallery);
			linear.addView(mBtnCamera);
			linear.addView(mBtnCancel);
			int dialogWidth = getResources().getDisplayMetrics().widthPixels / 2;
			if (dialogWidth / 2 > 700) {
				dialogWidth = 700;
			}
			mSelectDialog.setContentView(linear, new LayoutParams(dialogWidth, LayoutParams.WRAP_CONTENT));
		}
		mSelectDialog.show();
	}

	private void showAlert(String msg) {
		new AlertDialog.Builder(this).setTitle("Error").setMessage(msg).setPositiveButton(android.R.string.ok, null).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_PICK_GALLERY && resultCode == Activity.RESULT_OK) {
			// 갤러리의 경우 곧바로 data 에 uri가 넘어옴.
			Uri uri = data.getData();
			copyUriToFile(uri, getTempImageFile()); 
			sourceImageSelected(getTempImageFile());
		} else if (requestCode == REQ_CODE_PICK_CAMERA && resultCode == Activity.RESULT_OK) {
			// 카메라의 경우 file 로 결과물이 돌아옴.
			sourceImageSelected(getTempImageFile());
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void sourceImageSelected(File img) {
		Bitmap bm = convertFileToBitmap(img);
		((ImageView)findViewById(R.id.ivImageSelected)).setImageBitmap(bm);
	}
	
	// TODO 소스 이미지 선택 후 이미지 사이즈 요청이 있으면 bitmap options 적용하여 비트맵 로드함.
	
	// TODO 이미지 사이즈 수정 후, 카메라 rotation 정보가 있으면 회전 보정함. 

	// TODO 이미지 사이즈 수정 요청이 있으면 resize 함.

	// TODO crop 옵션이 켜져 있으면 이미지 crop 수행함. crop size 입력가능.
	
	// TODO 결과 file 을 얻어갈 수 있는 메서드 제공.
	
	
	private Bitmap convertFileToBitmap(File file) {
		// TODO 원하는 크기의 이미지로 options 설정.
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		return bitmap;
	}

	private Bitmap convertUriToBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			InputStream is = getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private void copyUriToFile(Uri srcUri, File target) {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			// 스트림 생성
			inputStream = (FileInputStream)getContentResolver().openInputStream(srcUri);
			outputStream = new FileOutputStream(target);

			// 채널 생성
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			// 채널을 통한 스트림 전송
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}

}
