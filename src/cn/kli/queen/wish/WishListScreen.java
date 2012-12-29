package cn.kli.queen.wish;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.database.Cursor;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WishListScreen extends BaseScreen implements OnClickListener{
	protected DbHelper mDbHelper;
	private ListView mWishList;

	public WishListScreen(Context context) {
		super(context, R.layout.wish_list_screen);
	}

	@Override
	void onLayoutInflaterFinished(Context context, View root) {
		mDbHelper = DbHelper.getInstance(context);
	    mWishList = (ListView)root.findViewById(R.id.wish_list);
	    mWishList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
	    	
	    });
	    root.findViewById(R.id.wish_add).setOnClickListener(this);
	    freshList();
	}

	@Override
	void start(Message msg) {
		
	}
	
	private void freshList(){
	    Cursor cursor = mDbHelper.getWishCursor();
	    WishAdapter adapter = new WishAdapter(mContext, cursor);
		mWishList.setAdapter(adapter);
	}

	private class WishAdapter extends CursorAdapter{
		LayoutInflater inflater;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public WishAdapter(Context context, Cursor c) {
			super(context, c);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return inflater.inflate(R.layout.wish_item, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView content = (TextView)view.findViewById(R.id.wish_content);
			String content_text = cursor.getString(cursor.getColumnIndex(DbHelper.WISH_CONTENT));
			content.setText(content_text);
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if(visibility == View.VISIBLE){
			freshList();
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.wish_add:
			Message msg = new Message();
			msg.what = WishComposeScreen.MODE_NEW;
			ScreenTranslate.getInstance().transToWishCompose(msg);
			break;
		}
	}



}