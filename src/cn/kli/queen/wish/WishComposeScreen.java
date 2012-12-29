package cn.kli.queen.wish;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class WishComposeScreen extends BaseScreen implements OnClickListener {
	public final static int MODE_NEW = 1;
	public final static int MODE_EDIT = 2;
	
	//views
	private EditText mContentEdit;
	private EditText mCommitEdit;
	private TextView mWishTime;
	private TextView mAchieveTime;
	private Spinner mStatus;
	private LinearLayout mWishDetails;
	private LinearLayout mAchieveInfo;
	private Button mCommit;
	private Button mBack;
	
	protected DbHelper mDbHelper;
	private Message mCurrentMsg;
	private Wish mWish;
	private Klilog klilog = new Klilog(this.getClass());
	
	public WishComposeScreen(Context context) {
		super(context, R.layout.wish_compose_screen);
	}
	
	@Override
	void onLayoutInflaterFinished(Context context, View root) {
		mDbHelper = DbHelper.getInstance(context);
		mContentEdit = (EditText)root.findViewById(R.id.wish_content);
		mCommitEdit = (EditText)root.findViewById(R.id.wish_comment);
		mWishTime = (TextView)root.findViewById(R.id.wish_time);
		mAchieveTime = (TextView)root.findViewById(R.id.achieve_time);
		mStatus = (Spinner)root.findViewById(R.id.wish_status);
		String[] status = getResources().getStringArray(R.array.status_items);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, status);
		mStatus.setAdapter(adapter);
		mWishDetails = (LinearLayout)root.findViewById(R.id.details);
		mAchieveInfo = (LinearLayout)root.findViewById(R.id.achieve_info);
		mCommit = (Button)root.findViewById(R.id.wish_commit);
		mCommit.setOnClickListener(this);
		mBack = (Button)root.findViewById(R.id.btn_back);
		mBack.setOnClickListener(this);
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
		wish.id = c.getInt(c.getColumnIndex(DbHelper.WISH_ID));
		wish.content = c.getString(c.getColumnIndex(DbHelper.WISH_CONTENT));
		wish.comment = c.getString(c.getColumnIndex(DbHelper.WISH_COMMENT));
		wish.status = c.getInt(c.getColumnIndex(DbHelper.WISH_STATUS));
		wish.time = c.getLong(c.getColumnIndex(DbHelper.WISH_TIME));
		wish.close_time = c.getLong(c.getColumnIndex(DbHelper.WISH_CLOSE_TIME));
		return wish;
	}
	
	private Wish displayToWish(){
		Wish wish = new Wish();
		wish.content = mContentEdit.getText().toString();
		wish.comment = mCommitEdit.getText().toString();
		wish.status = mStatus.getSelectedItemPosition();
		klilog.i("[displayToWish] wish.status = "+wish.status);
		
		if(TextUtils.isEmpty(wish.content)){
			return null;
		}
		return wish;
	}
	
	private void wishToDisplay(Wish wish){
		showButtons(wish);
		if(wish == null){
			mWishDetails.setVisibility(View.GONE);
			mContentEdit.getText().clear();
			mCommitEdit.getText().clear();
			mStatus.setSelection(DbHelper.STATUS_OPEN);
		}else{
			mWishDetails.setVisibility(View.VISIBLE);
			mAchieveInfo.setVisibility(wish.status == DbHelper.STATUS_ACHIEVED ? View.VISIBLE:View.GONE);
			mContentEdit.setText(wish.content);
			mCommitEdit.setText(wish.comment);
			mWishTime.setText(wish.getFormatTime());
			mAchieveTime.setText(wish.getFormatAchieveTime());
			mStatus.setSelection(wish.status);
		}
	}
	
	private void showButtons(Wish wish){
		if(wish == null || wish.status == DbHelper.STATUS_OPEN){
			mCommit.setVisibility(View.VISIBLE);
			mBack.setVisibility(View.GONE);
			mContentEdit.setEnabled(true);
			mCommitEdit.setEnabled(true);
			mStatus.setEnabled(true);
		}else{
			mCommit.setVisibility(View.GONE);
			mBack.setVisibility(View.VISIBLE);
			mContentEdit.setEnabled(false);
			mCommitEdit.setEnabled(false);
			mStatus.setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.wish_commit:
			commit();
		case R.id.btn_back:
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
			if(wish.status == DbHelper.STATUS_ACHIEVED && wish.close_time == 0){
				wish.close_time = System.currentTimeMillis();
			}
			mDbHelper.updateWish(wish);
			break;
		}
	}
}
