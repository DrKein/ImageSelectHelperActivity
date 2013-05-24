ImageSelectHelperActivity
=========================

Image select helper for Android

안드로이드에서 이미지를 선택할 때
---------------------------------
* 갤러리 선택
* 카메라 촬영  
을 손쉽게 하기 위한 Activity 입니다.

아래의 기능들을 제공합니다.
---------------------------------
1. 크롭기능 가능.
2. 가져올 이미지 크기 지정 가능.
3. ImageView 에 자동으로 이미지 넣어줌.
4. 선택된 이미지를 File 로 접근 가능.

샘플 코드
---------
여러분의 Activity는 이런 모습이 될것입니다.  
네, 이게 답니다.
~~~~~ java
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
~~~~~

사용법
---------------------------------
1. 안드로이드 프로젝트를 준비하세요.
2. AndroidManifest.xml 에 android.permission.WRITE_EXTERNAL_STORAGE 를 추가하세요. 
3. 이미지 가져오기를 수행할 Activity 에 ImageSelectHelerActivity 를 extends 하세요.
4. 해당 Activity 에 ivImageSelected 라는 이름의 ImageView 를 배치하세요.
5. 이미지 선택 후 crop 을 원하면 setCropOption(int aspectX, int aspectY) 를 호출 하세요. (옵션:기본은 크롭하지 않음)
6. Bitmap의 크기를 setImageSizeBoundary(int) 로 설정하세요. (옵션) 
7. 이미지 선택 다이얼로그 (갤러리, 카메라, 취소) 버튼을 원하시는 view로 넣을 수 있습니다.
8. startSelectImage() 를 호출하여 이미지 가져오기를 시작하세요.
9. 선택된 이미지 파일이 필요하면, getSelectedImageFile() 를 호출하세요.

귀찮으시면
------------
1. 그냥 startSelectImage() 를 호출하세요.


