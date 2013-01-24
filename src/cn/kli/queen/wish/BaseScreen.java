package cn.kli.queen.wish;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

abstract public class BaseScreen extends LinearLayout {
	protected Context mContext;

	public BaseScreen(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BaseScreen(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		int res = loadLayout();
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View root = inflater.inflate(res, this);
		onLayoutInflaterFinished(context, root);
	    initLayoutParams();
	}
	
	private void initLayoutParams(){
		ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(param);
	}
	
	abstract int loadLayout();
	
	abstract void onLayoutInflaterFinished(Context context, View root);

	abstract void start(Message msg);
}
