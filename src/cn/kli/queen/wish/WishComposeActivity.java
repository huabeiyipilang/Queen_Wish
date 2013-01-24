package cn.kli.queen.wish;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WishComposeActivity extends Activity implements OnClickListener {
	private WishComposeScreen mScreen;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wish_compose_activity);
	    mScreen = (WishComposeScreen)findViewById(R.id.wish_compose_screen);
	    Message cmd = getIntent().getParcelableExtra("cmd");
	    mScreen.start(cmd);

		findViewById(R.id.wish_commit).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wish_commit:
			mScreen.commit();
		case R.id.btn_back:
			finish();
			break;
		}
	}

}
