package com.locationtochild.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsSendReceiver extends BroadcastReceiver{
	
	private static final String mSmsIsSend = "SMS_SEND_ACTIOIN";
	private boolean isSuccess;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(mSmsIsSend)) {
			try {
				switch (getResultCode()) {
				case Activity.RESULT_OK://���Ͷ��ųɹ�
					isSuccess = true;
					break;
				default://���Ͷ���ʧ��
					isSuccess = false;
					break;
				
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
	
	public boolean getResultState(){
		return isSuccess;
	}

}
