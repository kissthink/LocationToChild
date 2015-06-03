package com.locationtochild.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.widget.ExitDialog;
import com.locationtochild.ui.widget.MyProgressDialog;
import com.locationtochild.utils.Constants.SettingsConstants;
import com.locationtochild.utils.HttpUtils;
import com.locationtochild.utils.MessageUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class InitSettingActivity extends Activity{

	private ViewFlipper mContent;
	private Button mNextBtn;
	private OneSettings mOne;
	private TwoSettings mTwo;
	private ThreeSettings mThree;
	private int mDisplay = 0;
	private AsyncInitTask mTask;
	private boolean mRunning = false ;
	private SharedPreferences mWatchSP;//���ڱ��ش洢�ֱ����������Ϣ
	private String mQQOne;//��һ�������
	private String mQQTwo;//�ڶ��������
	private String mQQThree;//�����������
	private String mCentre;//���ĺ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_init);
		
		initView();
		setListener();
		
		mOne = new OneSettings(this);
		mTwo = new TwoSettings(this);
		mThree = new ThreeSettings(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		mContent.addView(mOne.getView(),0, params);
		mContent.addView(mTwo.getView(), 1, params);
		mContent.addView(mThree.getView(), 2, params);
		mContent.setDisplayedChild(0);
		
		LocationToChildApplication.getInstance().addActivity(this);
	}
	
	private void initView(){
		mContent = (ViewFlipper)findViewById(R.id.init_flipper);
		mNextBtn = (Button)findViewById(R.id.next_btn);
	}
	
	private void setListener(){
		mNextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mDisplay==0){//��ʼ�����ֱ�������
					String watchNum = mOne.getEditText();
					if(checkInput(watchNum)){
						LocationToChildApplication.mWatchNumber = watchNum ;
						Intent intent = getIntent();
						String username = intent.getStringExtra("username");
						mTask = new AsyncInitTask();
						mTask.execute(SettingsConstants.WATCH_NUMBER, username, watchNum);
						mDisplay++;
					}else{
						Toast.makeText(InitSettingActivity.this, "�����ֻ�������������ٴ����룡", Toast.LENGTH_SHORT).show();
					}
				}else if(mDisplay==1){//��ʼ��������Ž���
					Log.i("tag", "��ʼ���������");
					String[] number = new String[3];
					number[0] = mTwo.getFamilyOne();
					number[1] = mTwo.getFamilyTwo();
					number[2] = mTwo.getFamilyThree();
					if(mTask != null && mTask.getStatus() == AsyncInitTask.Status.RUNNING)
						mTask.cancel(true);
					sendNotNull(number);
					mDisplay++;
				}else{//��ʼ�������ĺ������
					Log.i("tag", "��ʼ�������ĺ���");
					mCentre = mThree.getCentre();
					if(checkInput(mCentre)){
						if(mCentre.equalsIgnoreCase(mQQOne)||mCentre.equalsIgnoreCase(mQQTwo)||mCentre.equalsIgnoreCase(mQQThree)){
							if(mTask != null && mTask.getStatus() == AsyncInitTask.Status.RUNNING)
								mTask.cancel(true);
							mTask = new AsyncInitTask();
							mTask.execute(SettingsConstants.CENTRE_NUMBER, mCentre);
						}else
							Toast.makeText(InitSettingActivity.this, "�������ĺ��룬���������֮һ��", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(InitSettingActivity.this, "�����ֻ�������������ٴ����룡", Toast.LENGTH_SHORT).show();
					}
					
				}
				
				
			}
		});
	}
	
	private void sendNotNull(String[] number){
		int count = 0;
		String[] temp = new String[3];
		for(int i = 0 ; i < number.length ; i++){
			if(!"".equalsIgnoreCase(number[i])){
				count++;
				temp[count] = number[i];
			}
		}
		mTask = new AsyncInitTask();
		if(count==0){
			Toast.makeText(this, "������������룡", Toast.LENGTH_SHORT).show();
		}else if(count==1){
			if(checkInput(temp[1])){
				mQQOne = temp[1]; mQQTwo = ""; mQQThree = "";
				mTask.execute(SettingsConstants.FAMILY_NUMBER, "one", temp[1]);
			}else
				Toast.makeText(this, "�ֻ����벻��11λ��", Toast.LENGTH_SHORT).show();
		}else if(count==2){
			if(checkInput(temp[1])&&checkInput(temp[2])){
				mQQOne = temp[1]; mQQTwo = temp[2]; mQQThree = "";
				mTask.execute(SettingsConstants.FAMILY_NUMBER, "two", temp[1], temp[2]);
			}else{
				Toast.makeText(this, "�����ֻ����벻��11λ��", Toast.LENGTH_SHORT).show();
			}
		}else if(count==3){
			if(checkInput(temp[1])&&checkInput(temp[2])&&checkInput(temp[3])){
				mQQOne = temp[1]; mQQTwo = temp[2]; mQQThree = temp[3];
				mTask.execute(SettingsConstants.FAMILY_NUMBER, "three", temp[1], temp[2],temp[3]);
			}else
				Toast.makeText(this, "�����ֻ����벻��11λ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean checkInput(String number){
		if(number.trim().length()==11){
			return true;
		}else{
			return false;
		}
	}
	
	public class AsyncInitTask extends AsyncTask<String, Integer, String>{
		
		private MyProgressDialog mProgressDialog;
		
		@Override
		protected String doInBackground(String... params) {
			String type = params[0];
			String result = "";//���ص����̵߳Ľ��
			if(type.equalsIgnoreCase(SettingsConstants.WATCH_NUMBER)){//�����ֱ�ĺ���
				SharedPreferences userInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
				userInfo.edit().putString("watch", params[2]).commit();//���ֱ�������û��ֻ��Ű�
				try {
					String resultHttp = HttpUtils.getInstance().postWatchPhone(params[1], params[2]);
					JSONObject jo = new JSONObject(resultHttp);
					String code = jo.getString("code");
					if(code.equalsIgnoreCase("20000")){
						JSONObject temp = jo.getJSONObject("result");
						writeSharedPreferences(temp);
						result = "success";
					}else if(code.equalsIgnoreCase("20010")){
						result = "failure";
					}else{
						result = "timeout";
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}else if(type.equalsIgnoreCase(SettingsConstants.FAMILY_NUMBER)){//�������ֱ�󶨵������
				mRunning = true;
				if(params[1].equalsIgnoreCase("one")){
					MessageUtils.getInstance().setQQPhoneNum(params[1]);
				}else if(params[1].equalsIgnoreCase("two")){
					MessageUtils.getInstance().setQQPhoneNum(params[1], params[2]);
				}else if(params[1].equalsIgnoreCase("three")){
					MessageUtils.getInstance().setQQPhoneNum(params[1], params[2], params[3]);
				}
				result = waitMessageResult();
			}else if(type.equalsIgnoreCase(SettingsConstants.CENTRE_NUMBER)){//�������ֱ�󶨵����ĺ���
				mRunning = true;
				MessageUtils.getInstance().setCenterPhoneNum(params[1]);
				result = waitMessageResult();
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			stopProgressDialog();
			if(result.equals("success")){
				Toast.makeText(InitSettingActivity.this, "��������ȡ�ֱ���Ϣ�ɹ���", Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(InitSettingActivity.this, MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
			}else if(result.contains("�����")){
			
				if(result.contains("���óɹ�")){
					Toast.makeText(InitSettingActivity.this, "��������óɹ�", Toast.LENGTH_SHORT).show();
					writeQQToSharedPreference();
					mContent.setDisplayedChild(mDisplay);
				}
				else{
					Toast.makeText(InitSettingActivity.this, "���������ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
				}
			}else if(result.contains("���ĺ���")){
				if(result.contains("���óɹ�")){
					Toast.makeText(InitSettingActivity.this, "���ĺ������óɹ�", Toast.LENGTH_SHORT).show();
					SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
					sp.edit().putBoolean("isSetting", true).commit();
					SharedPreferences num = getSharedPreferences(LocationToChildApplication.mWatchNumber, MODE_PRIVATE);
					num.edit().putString("Centre",mCentre).commit();
					finish();
					Log.i("tag", "��ת���ϴ����ݣ�");
					Intent intent = new Intent(InitSettingActivity.this, SynchronousActivity.class);
					intent.putExtra("where", "InitSetting");
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
				}
				else{
					Toast.makeText(InitSettingActivity.this, "���ĺ�������ʧ�ܣ�������!", Toast.LENGTH_SHORT).show();
				}
			}else if(result.equals("failure")){
				Log.i("tag", "������û�д��ֱ���Ϣ");
				mContent.setDisplayedChild(mDisplay);
			}else if(result.equalsIgnoreCase("timeout")){
				Toast.makeText(InitSettingActivity.this, "�����쳣����鿴�������ԣ�", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(InitSettingActivity.this, "wait����Ϊ��", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			startProgressDialog();
		}

		private void startProgressDialog(){  
	        if (mProgressDialog == null){  
	        	mProgressDialog = MyProgressDialog.createDialog(InitSettingActivity.this);
	            mProgressDialog.setMessage("���ڷ��ͣ������ĵȴ�...");  
	        }   
	        mProgressDialog.show();  
	    }  
	      
	    private void stopProgressDialog(){  
	        if (mProgressDialog != null){  
	            mProgressDialog.dismiss();  
	            mProgressDialog = null;  
	        }  
	    }
	    
	    private String waitMessageResult(){
	    	String result = ""; 
	    	while(mRunning){
	    		result = LocationToChildApplication.mMessageResult;
	    		if(result.contains("���óɹ�")||result.contains("����ʧ��")){
	    			LocationToChildApplication.mMessageResult = "";
	    			break;
	    		}
	    			
	    	}
	    	mRunning = false;
	    	return result;
	    }
	}
	
	/**
	 * ���ӷ��������ص��ֻ�����д�ڱ������ݿ�
	 * @param watchphone
	 * @throws JSONException
	 */
	private void writeSharedPreferences(JSONObject watchphone) throws JSONException{
		String watchNum = getSharedPreferences("UserInfo", MODE_PRIVATE).getString("watch", "");
		mWatchSP = getSharedPreferences(watchNum, MODE_PRIVATE);
		Editor ed = mWatchSP.edit();
		ed.putString("QQOne", watchphone.getString("qqOne"));
		ed.putString("QQTwo", watchphone.getString("qqTwo"));
		ed.putString("QQThree", watchphone.getString("qqThree"));
		ed.putString("Centre", watchphone.getString("centre"));
		ed.putBoolean("GpsIsOpen", trueOrFalse(watchphone.getString("gpsStatus")));
		ed.putBoolean("WallIsOpen", trueOrFalse(watchphone.getString("electFenceStatus")));
		ed.putString("Password", watchphone.getString("watchPwd"));
		ed.commit();
		
		SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
		sp.edit().putBoolean("isSetting", true).commit();
	}
	
	/***
	 * ���������û�и��ֱ��������Ϣ�����û�ͨ�����ý������õ�����ű��浽����
	 */
	private void writeQQToSharedPreference(){
		mWatchSP = getSharedPreferences(LocationToChildApplication.mWatchNumber, MODE_PRIVATE);
		Editor ed = mWatchSP.edit();
		ed.putString("QQOne", mQQOne);
		ed.putString("QQTwo", mQQTwo);
		ed.putString("QQThree", mQQThree);
		ed.commit();
	}
	
	private boolean trueOrFalse(String number){
		if(number.equalsIgnoreCase("1")){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialog mExit = new ExitDialog(this);
		mExit.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mRunning = false;
	}
	
	
}
