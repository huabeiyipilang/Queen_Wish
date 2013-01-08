package cn.kli.queen.communicationservice;

import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

public class ComMessage implements Parcelable{
	public String from;
	public String title;
	public String content;
	public Message callBack;
	
	public ComMessage(){
	}
	
	private ComMessage(Parcel in){
		from = in.readString();
		title = in.readString();
		content = in.readString();
		try {
			callBack = in.readParcelable(Message.class.getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
			callBack = null;
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(from);
		out.writeString(title);
		out.writeString(content);
		out.writeParcelable(callBack, flags);
	}
	
	public static final Parcelable.Creator<ComMessage> CREATOR = new Parcelable.Creator<ComMessage>(){

		@Override
		public ComMessage createFromParcel(Parcel in) {
			return new ComMessage(in);
		}

		@Override
		public ComMessage[] newArray(int size) {
			return new ComMessage[size];
		}
		
	};
	
	public boolean checkEnable(){
		if(from == null || title == null || content == null || callBack == null){
			return false;
		}
		return true;
	}
}
