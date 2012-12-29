package cn.kli.queen.wish;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

public class WishActivity extends Activity{
	ScreenTranslate mTranslate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wish_activity);
	    mTranslate = ScreenTranslate.create(this, (ViewGroup)findViewById(R.id.container));
	    mTranslate.transToWishList();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mTranslate.backToHome()){
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
