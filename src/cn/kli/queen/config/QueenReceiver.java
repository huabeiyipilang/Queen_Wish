package cn.kli.queen.config;

import cn.kli.queen.wish.MainActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class QueenReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if("cn.kli.intent.ENABLE_ENTRY".equals(action)){
			PackageManager pm = context.getPackageManager();
			ComponentName cName = new ComponentName(context.getPackageName(),MainActivity.class.getName());
			int state = intent.getBooleanExtra("enable", true) ? 
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED
				: PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
			pm.setComponentEnabledSetting(cName,state, PackageManager.DONT_KILL_APP);
		}
	}

}
