package cn.kli.queen.wish;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class WishComposeScreen extends BaseScreen implements OnClickListener {
	public final static int MODE_NEW = 1;
	public final static int MODE_EDIT = 2;
	
	private EditText mWishEdit;
	protected DbHelper mDbHelper;
	private int mCurrentMode = MODE_NEW;
	
	public WishComposeScreen(Context context) {
		super(context, R.layout.wish_compose_screen);
	}
	
	@Override
	void onLayoutInflaterFinished(Context context, View root) {
		mDbHelper = DbHelper.getInstance(context);
	    mWishEdit = (EditText)root.findViewById(R.id.wish_content);
	    root.findViewById(R.id.wish_commit).setOnClickListener(this);
	}
	
	public void start(Message msg){
		switch(msg.what){
		case MODE_NEW:
			mWishEdit.getText().clear();
			break;
		case MODE_EDIT:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wish_commit:
			String wishContent = mWishEdit.getText().toString();
			if(!TextUtils.isEmpty(wishContent)){
				Wish wish = new Wish();
				wish.content = wishContent;
				mDbHelper.addWish(wish);
			}
			ScreenTranslate.getInstance().transToWishList();
			break;
		}
	}
}
