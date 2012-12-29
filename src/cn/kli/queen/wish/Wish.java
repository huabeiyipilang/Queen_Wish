package cn.kli.queen.wish;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Wish {
	public int id;
	public String content;
	public String comment;
	public long time;
	public int status;
	public long close_time;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public String getFormatTime(){
		return format.format(new Date(time));
	}
	
	public String getFormatAchieveTime(){
		return format.format(new Date(close_time));
	}
	
	public int getStatusRes(){
		int res = 0;
		switch(status){
		case DbHelper.STATUS_OPEN:
			res = R.string.status_open;
			break;
		case DbHelper.STATUS_ACHIEVED:
			res = R.string.status_achieve;
			break;
		case DbHelper.STATUS_CANNOT_ACHIEVE:
			res = R.string.status_cannot_achieve;
			break;
		case DbHelper.STATUS_CANCEL:
			res = R.string.status_cancel;
			break;
		}
		return res;
	}
}
