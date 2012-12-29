package cn.kli.queen.wish;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

abstract public class BaseScreen extends LinearLayout {
	protected Context mContext;
	
	public BaseScreen(Context context, int res) {
		super(context);
		init(context, res);
	}
	
	private void init(Context context, int res){
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View root = inflater.inflate(res, this);
		onLayoutInflaterFinished(context, root);
	    initLayoutParams();
	    this.setVisibility(View.GONE);
	}
	
	private void initLayoutParams(){
		ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		this.setLayoutParams(param);
	}
	
	abstract void onLayoutInflaterFinished(Context context, View root);

	abstract void start(Message msg);
}
