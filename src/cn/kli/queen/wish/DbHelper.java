package cn.kli.queen.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "wish";
	private static final int DATABASE_VERSION = 1;
	
	public final static String TABLE_WISH = "wish";
	public final static String WISH_ID = "_id";
	public final static String WISH_CONTENT = "content";
	public final static String WISH_COMMENT = "comment";
	public final static String WISH_TIME = "time";
	public final static String WISH_ACHIEVE_TIME = "achieve_time";
	public final static String WISH_ACHIEVE = "achieve";
	
	final static String[] QUERY_COLUM_WISH = {
		WISH_CONTENT, WISH_COMMENT, WISH_TIME, WISH_ACHIEVE, WISH_ACHIEVE_TIME, WISH_ID
	};
	
	private static DbHelper sSingleton = null;
	
	public static DbHelper getInstance(Context context){
		Log.i("klilog","DbHelper getInstance");
		if(sSingleton == null){
			sSingleton = new DbHelper(context, DB_NAME, null, DATABASE_VERSION);
		}
		return sSingleton;
	}

	private DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("klilog","DbHelper onCreate()");
		
		final String create_table_wish = "CREATE TABLE "+TABLE_WISH+"("
				+WISH_ID+" INTEGER primary key, "
				+WISH_CONTENT+" TEXT, "
				+WISH_COMMENT+" TEXT, "
				+WISH_TIME+" timestamp, "
				+WISH_ACHIEVE_TIME+" timestamp, "
				+WISH_ACHIEVE+ " INTEGER)";
		db.execSQL(create_table_wish);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	public Cursor getWishCursor(){
		return getReadableDatabase().query(TABLE_WISH, QUERY_COLUM_WISH, 
				null, null, null, null, WISH_TIME+" desc");
	}
	
	public Cursor getWishCursor(int id){
		return getReadableDatabase().query(TABLE_WISH, QUERY_COLUM_WISH, 
				WISH_ID+" = "+id, null, null, null, WISH_TIME+" desc");
	}
	
	public long updateWish(Wish wish){
		if(wish.id == 0){
			return 0;
		}
		return  this.getWritableDatabase().update(TABLE_WISH, 
					wishToCv(wish), WISH_ID + " = "+wish.id, null);
	}
	
	public long addWish(Wish wish){
		return this.getWritableDatabase().insert(DbHelper.TABLE_WISH, "", wishToCv(wish));
	}
	
	private ContentValues wishToCv(Wish wish){
		ContentValues cv = new ContentValues();
		if(!TextUtils.isEmpty(wish.content)){
			cv.put(WISH_CONTENT, wish.content);
		}
		if(!TextUtils.isEmpty(wish.comment)){
			cv.put(WISH_COMMENT, wish.comment);
		}
		if(wish.time != 0){
			cv.put(WISH_TIME, wish.time);
		}
		cv.put(WISH_ACHIEVE, wish.achieve);
		if(wish.time != 0){
			cv.put(WISH_ACHIEVE_TIME, wish.achieve_time);
		}
		return cv;
	}

}
