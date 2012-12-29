package cn.kli.queen.wish;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class WishComposeScreen extends BaseScreen implements OnClickListener {
	public final static int MODE_NEW = 1;
	public final static int MODE_EDIT = 2;
	
	private EditText mContentEdit;
	private EditText mCommitEdit;
	protected DbHelper mDbHelper;
	private Message mCurrentMsg;
	
	public WishComposeScreen(Context context) {
		super(context, R.layout.wish_compose_screen);
	}
	
	@Override
	void onLayoutInflaterFinished(Context context, View root) {
		mDbHelper = DbHelper.getInstance(context);
		mContentEdit = (EditText)root.findViewById(R.id.wish_content);
		mCommitEdit = (EditText)root.findViewById(R.id.wish_comment);
	    root.findViewById(R.id.wish_commit).setOnClickListener(this);
	}
	
	public void start(Message msg){
		mCurrentMsg = msg;
		switch(mCurrentMsg.what){
		case MODE_NEW:
			wishToDisplay(null);
			break;
		case MODE_EDIT:
			Cursor cursor = mDbHelper.getWishCursor(mCurrentMsg.arg1);
			Wish wish = buildWish(cursor);
			wishToDisplay(wish);
			break;
		}
	}
	
	private Wish buildWish(Cursor c){
		c.moveToFirst();
		Wish wish = new Wish();
		wish.content = c.getString(c.getColumnIndex(DbHelper.WISH_CONTENT));
		wish.comment = c.getString(c.getColumnIndex(DbHelper.WISH_COMMENT));
		return wish;
	}
	
	private Wish displayToWish(){
		Wish wish = new Wish();
		wish.content = mContentEdit.getText().toString();
		wish.comment = mCommitEdit.getText().toString();
		if(TextUtils.isEmpty(wish.content)){
			return null;
		}
		return wish;
	}
	
	private void wishToDisplay(Wish wish){
		if(wish == null){
			mContentEdit.getText().clear();
			mCommitEdit.getText().clear();
		}else{
			mContentEdit.setText(wish.content);
			mCommitEdit.setText(wish.comment);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wish_commit:
			commit();
			ScreenTranslate.getInstance().transToWishList();
			break;
		}
	}
	
	private void commit() {
		Wish wish = displayToWish();
		if (wish == null) {
			return;
		}
		switch (mCurrentMsg.what) {
		case MODE_NEW:
			wish.time = System.currentTimeMillis();
			mDbHelper.addWish(wish);
			break;
		case MODE_EDIT:
			wish.id = mCurrentMsg.arg1;
			mDbHelper.updateWish(wish);
			break;
		}
	}
}
