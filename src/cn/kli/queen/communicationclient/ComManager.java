package cn.kli.queen.communicationclient;

import java.util.LinkedList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Toast;
import cn.kli.queen.communicationservice.ComMessage;
import cn.kli.queen.communicationservice.IComCallback;
import cn.kli.queen.communicationservice.ICommunication;
import cn.kli.queen.wish.Klilog;

public class ComManager {
	enum State{
		DISCONNECTED, CONNECTING, CONNECTED, DISCONNECTING
	}
	public final static String APP_NAME = "Wish";
	public final static String APP_FUNCTION = "New wish";
	private final static int MSG_BIND_SERVICE = 1;
	private final static int MSG_UNBIND_SERVICE = 2;
	private final static int MSG_SERVICE_CONNECTED = 3;
	private final static int MSG_SERVICE_DISCONNECTED = 4;
	private final static int MSG_MESSAGE_SEND = 5;
	private final static int MSG_MESSAGE_SENT = 6;
	private static ComManager sInstance;
	private ICommunication mService;
	private ServiceConnection mConnection;
	private Context mContext;
	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private LinkedList<ComMessage> mMessageQueen = new LinkedList<ComMessage>();
	private State mState = State.DISCONNECTED;
	private Klilog klilog = new Klilog(ComManager.class);
	private IComCallback mCallback;
	
	private ComManager(Context context){
		mContext = context;
		mHandlerThread = new HandlerThread("commanager_thread");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				klilog.i(msgToString(msg.what));
				switch(msg.what){
				case MSG_BIND_SERVICE:
					mState = State.CONNECTING;
					bindService();
					break;
				case MSG_UNBIND_SERVICE:
					mState = State.DISCONNECTING;
					unBindService();
					break;
				case MSG_SERVICE_CONNECTED:
					mState = State.CONNECTED;
					try {
						mService.registerForComState(mCallback);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					sendMessageFromQueen();
					break;
				case MSG_SERVICE_DISCONNECTED:
					mState = State.DISCONNECTED;
					try {
						mService.unRegisterForComState(mCallback);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					break;
				case MSG_MESSAGE_SEND:
					try {
						ComMessage cmsg = (ComMessage)msg.obj;
						mService.sendMessage(cmsg);
//						sendEmptyMessage(MSG_MESSAGE_SENT);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
				case MSG_MESSAGE_SENT:
					ComMessage cmsg = (ComMessage)msg.obj;
					Toast.makeText(mContext, "result = "+cmsg.getCause(), Toast.LENGTH_SHORT).show();
					sendMessageFromQueen();
					break;
				}
			}
		};

		mCallback = new IComCallback.Stub() {
			@Override
			public void onSendComplete(ComMessage cmsg) throws RemoteException {
				klilog.i("CMessage send completed , cause:"+cmsg.getCause());
				Message msg = mHandler.obtainMessage(MSG_MESSAGE_SENT);
				msg.obj = cmsg;
				msg.sendToTarget();
			}
		};
		
	}
	
	public static ComManager getInstance(Context context){
		if(sInstance == null){
			sInstance = new ComManager(context);
		}
		return sInstance;
	}
	
	public void sendMessage(ComMessage msg) {
		mMessageQueen.push(msg);
		switch (mState) {
		case DISCONNECTED:
			mHandler.sendEmptyMessage(MSG_BIND_SERVICE);
			break;
		case CONNECTING:
			break;
		case CONNECTED:
			break;
		case DISCONNECTING:
			mHandler.sendEmptyMessage(MSG_BIND_SERVICE);
			break;
		}
	}
	
	public void sendMessageFromQueen(){
		ComMessage next = mMessageQueen.pollLast();
		try {
			if(next != null){
				mMessageQueen.remove(next);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message msg = null;
		if(next != null){
			msg = mHandler.obtainMessage(MSG_MESSAGE_SEND);
			msg.obj = next;
		}else{
			msg = mHandler.obtainMessage(MSG_UNBIND_SERVICE);
		}
		if(msg != null){
			msg.sendToTarget();
		}
	}
	
	private void bindService() {
		mConnection = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName arg0, IBinder service) {
				mService = ICommunication.Stub.asInterface(service);
				mHandler.sendEmptyMessage(MSG_SERVICE_CONNECTED);
			}
			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				mHandler.sendEmptyMessage(MSG_SERVICE_DISCONNECTED);
			}
		};
		
		mContext.bindService(new Intent("cn.kli.service.communicationservice"),
				mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void unBindService(){
		mContext.unbindService(mConnection);
		mConnection = null;
	}

	private String msgToString(int msg){
		String res = "";
		switch(msg){
		case MSG_BIND_SERVICE:
			res = "MSG_BIND_SERVICE";
			break;
		case MSG_UNBIND_SERVICE:
			res = "MSG_UNBIND_SERVICE";
			break;
		case MSG_SERVICE_CONNECTED:
			res = "MSG_SERVICE_CONNECTED";
			break;
		case MSG_SERVICE_DISCONNECTED:
			res = "MSG_SERVICE_DISCONNECTED";
			break;
		case MSG_MESSAGE_SEND:
			res = "MSG_MESSAGE_SEND";
			break;
		case MSG_MESSAGE_SENT:
			res = "MSG_MESSAGE_SENT";
			break;
		}
		return res;
	}
	
	public static ComMessage getComMessage(){
		ComMessage cmsg = new ComMessage();
		cmsg.from = APP_NAME;
		cmsg.title = APP_FUNCTION;
		return cmsg;
	}
}
