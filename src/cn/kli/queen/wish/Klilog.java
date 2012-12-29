package cn.kli.queen.wish;

import android.util.Log;

public class Klilog {
	Class mClass;
	public Klilog(Class c){
		mClass = c;
	}
	public void i(String msg){
		Log.i(mClass.getName(), msg);
	}

}
