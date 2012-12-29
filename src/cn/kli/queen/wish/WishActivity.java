package cn.kli.queen.wish;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class WishActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wish_activity);
	    ScreenTranslate translate = ScreenTranslate.create(this, (ViewGroup)findViewById(R.id.container));
	    translate.transToWishList();
	}
	
}
