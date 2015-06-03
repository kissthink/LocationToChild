package com.locationtochild.ui.setting;

import static com.locationtochild.utils.Constants.SETTING_DEVICEKEY;
import static com.locationtochild.utils.Constants.SETTING_TELECENTER;
import static com.locationtochild.utils.Constants.SETTING_TELEDEVICE;
import static com.locationtochild.utils.Constants.SETTING_TELEQQ;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.MainActivity;
import com.locationtochild.ui.R;
import com.locationtochild.ui.widget.MyProgressDialog;
import com.locationtochild.utils.MessageUtils;
import com.locationtochild.utils.Constants.SettingsConstants;

public class SettingActivity extends FragmentActivity implements
		SettingFragment.OnSettingItemSelect {
	public Intent mIntentSI;
	// ������Ƭ����
	private SettingFragment mSetFragment;
	private Fragment mTailFragment;
	// ����Ƭ�ι�����
	private FragmentManager mSettingFrgManager;
	// ����Ƭ��ת����
	private FragmentTransaction mSettingFrgTran;
	// ��ȡ���ò���
	private boolean mGpsOn;
	private boolean mWallOn;
	private String mTeleCenter;
	private String mTeleQQOne;
	private String mTeleQQTwo;
	private String mTeleQQThree;
	private String mTeleDevice;
	private String mDeviceKye;
	// ��ȡ����Χ����״̬
	private String mScale;
	private String mTimeSpan;
	private String mLongitude;
	private String mLatitude;
	// ���ݲ���
	private Bundle mBundle;
	// �����첽����
	private AsyncSettingTask mSettingTask;
	private boolean mRunning = false;
	private SharedPreferences mWatchInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting_main);
		// ��ȡ������Ϣ
		getSettingParm();
		System.out.println("the setting param is get !");
		mBundle = new Bundle();
		// ��ʼ��Ƭ��Ϊ����������
		if (findViewById(R.id.setting_fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}
			initSettingFragment();
		}
	}

	public void initSettingFragment() {
		mSetFragment = new SettingFragment();
		mBundle.putBoolean("gps", mGpsOn);
		mBundle.putBoolean("wall", mWallOn);
		mSetFragment.setArguments(mBundle);
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		mSettingFrgTran.add(R.id.setting_fragment_container, mSetFragment)
				.commit();
	}

	// ��ȡ���ò���
	public void getSettingParm() {
		SharedPreferences userInfo = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		mTeleDevice = userInfo.getString("watch", "");
		System.out.println("the device num is " + mTeleDevice);
		if (mTeleDevice.equals("")) {
			Toast.makeText(getApplicationContext(), "�޷���ȡ������Ϣ",
					Toast.LENGTH_LONG).show();
			// ��ת����ҳ��������ò�������
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
		mWatchInfo = getSharedPreferences(mTeleDevice, MODE_PRIVATE);
		mGpsOn = mWatchInfo.getBoolean("GpsIsOpen", true);
		mWallOn = mWatchInfo.getBoolean("WallIsOpen", false);
		mTeleCenter = mWatchInfo.getString("Centre", "18710726977");
		mTeleQQOne = mWatchInfo.getString("QQOne", "18710726977");
		mTeleQQTwo = mWatchInfo.getString("QQTwo", "");
		mTeleQQThree = mWatchInfo.getString("QQThree", "");
		mDeviceKye = mWatchInfo.getString("password", "0000");
		// ��ȡ����Χ������
		mScale = mWatchInfo.getString("Scale", "500");
		mTimeSpan = mWatchInfo.getString("TimeSpan", "5");
		mLatitude = mWatchInfo.getString("Latitude", "116.302251");
		mLongitude = mWatchInfo.getString("Longitude", "39.9890077");
	}

	// fragment�л�
	public void changeFragment(int TAG, boolean flag) {
		// ��תҳ�����
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		// ��flagΪtrue�򷵻ص������ý���
		if (flag) {
			// ���Խ���ϸ��ҳ֮���Ƿ������
			mSettingFrgTran.setCustomAnimations(R.anim.slide_right_in,
					R.anim.slide_right_out);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mSetFragment);
		} else {
			// �����ǩ����fragment�½�
			dealTag(TAG);
			mSettingFrgTran.setCustomAnimations(R.anim.slide_left_in,
					R.anim.slide_left_out);
			mSettingFrgTran.addToBackStack(null);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mTailFragment);
		}
		mSettingFrgTran.commit();
	}

	// fragment����
	public void updateFragment(int TAG) {
		// ��תҳ�����
		mSettingFrgManager = getSupportFragmentManager();
		mSettingFrgTran = mSettingFrgManager.beginTransaction();
		if (TAG < 0) {
			mSettingFrgManager.popBackStack();
		}
		// ����������ҳ��
		mSetFragment = new SettingFragment();
		mBundle.putBoolean("gps", mGpsOn);
		mBundle.putBoolean("wall", mWallOn);
		mSetFragment.setArguments(mBundle);
		mSettingFrgTran.replace(R.id.setting_fragment_container, mSetFragment);
		if (TAG > 0) {
			dealTag(TAG);
			mSettingFrgTran.addToBackStack(null);
			mSettingFrgTran.replace(R.id.setting_fragment_container,
					mTailFragment);
		}
		mSettingFrgTran.commit();
	}

	public void dealTag(int TAG) {
		// ������ת��ϸ�ڽ���
		switch (TAG) {
		case SETTING_DEVICEKEY:
			mTailFragment = new DeviceKeyFragment();
			mBundle.putString("device_key", mDeviceKye);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELEQQ:
			mTailFragment = new TeleQQFragment();
			mBundle.putString("qq_one", mTeleQQOne);
			mBundle.putString("qq_two", mTeleQQTwo);
			mBundle.putString("qq_three", mTeleQQThree);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELECENTER:
			mTailFragment = new TeleCenterFragment();
			mBundle.putString("tele_center", mTeleCenter);
			mBundle.putString("qq_one", mTeleQQOne);
			mBundle.putString("qq_two", mTeleQQTwo);
			mBundle.putString("qq_three", mTeleQQThree);
			mTailFragment.setArguments(mBundle);
			break;
		case SETTING_TELEDEVICE:
			mTailFragment = new TeleDeviceFragment();
			mBundle.putString("tele_device", mTeleDevice);
			mTailFragment.setArguments(mBundle);
			break;
		}
	}

	// ʵ�ֽӿڵĵ���
	@Override
	public void onItemSelected(int TAG, boolean flag) {
		// TODO Auto-generated method stub
		changeFragment(TAG, flag);
	}

	@Override
	public void settingMessage(String... params) {
		// TODO Auto-generated method stub
		System.out.println("setting message ");
		mSettingTask = new AsyncSettingTask();
		mSettingTask.execute(params);
	}

	// �첽������
	public class AsyncSettingTask extends AsyncTask<String, Integer, String> {

		private MyProgressDialog mProgressDialog;

		@Override
		protected String doInBackground(String... params) {
			String type = params[0];
			String result = ""; // ���ص����̵߳Ľ��
			if (type.equalsIgnoreCase(SettingsConstants.GPS_STATE)) {// ����GPS����
				mRunning = true;
				boolean flag = false;
				System.out.println("click gps in setting ");
				mGpsOn = flag;
				if (params[1].equalsIgnoreCase("true")) {
					flag = true;
				}
				MessageUtils.getInstance().setLocationOn(flag);
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.WALL_STATE)) {// ����WALL����
				mRunning = true;
				System.out.println("click wall in setting ");
				if (params[1].equalsIgnoreCase("true")) {
					mWallOn = true;
					MessageUtils.getInstance().setWallOn(mScale, mTimeSpan,
							mLongitude, mLatitude);
				} else {
					mWallOn = false;
					MessageUtils.getInstance().setWallOff();
				}
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.FAMILY_NUMBER)) {// �������ֱ�󶨵������
				mRunning = true;
				mTeleQQOne = params[2];
				if (params[1].equalsIgnoreCase("one")) {
					mTeleQQTwo = "";
					mTeleQQThree = "";
					MessageUtils.getInstance().setQQPhoneNum(params[2]);
				} else if (params[1].equalsIgnoreCase("two")) {
					mTeleQQTwo = params[3];
					mTeleQQThree = "";
					MessageUtils.getInstance().setQQPhoneNum(params[2],
							params[3]);
				} else if (params[1].equalsIgnoreCase("three")) {
					mTeleQQTwo = params[3];
					mTeleQQThree = params[4];
					MessageUtils.getInstance().setQQPhoneNum(params[2],
							params[3], params[4]);
				}
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.CENTRE_NUMBER)) {// �������ֱ�󶨵����ĺ���
				mRunning = true;
				mTeleCenter = params[1];
				MessageUtils.getInstance().setCenterPhoneNum(params[1]);
				result = waitMessageResult();
			} else if (type.equalsIgnoreCase(SettingsConstants.DEVICE_KEY)) {// �����ֱ�����
				System.out.println(params[1] + " new pass " + params[2]);
				mDeviceKye = params[2];
				mRunning = true;
				MessageUtils.getInstance().setKeyWord(params[1], params[2]);
				result = waitMessageResult();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			stopProgressDialog();
			boolean success = false;
			int tag = -1;
			if (result.contains("�����")) {
				if (result.contains("���óɹ�")) {
					Toast.makeText(SettingActivity.this, "��������óɹ�",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("QQOne", mTeleQQOne).commit();
					mWatchInfo.edit().putString("QQTwo", mTeleQQTwo).commit();
					mWatchInfo.edit().putString("QQThree", mTeleQQThree)
							.commit();
					success = true;
					tag = SETTING_TELEQQ;
					// �ϴ�������

				} else {
					Toast.makeText(SettingActivity.this, "���������ʧ�ܣ�������!",
							Toast.LENGTH_SHORT).show();
					mTeleQQOne = mWatchInfo.getString("QQOne", "");
					mTeleQQTwo = mWatchInfo.getString("QQTwo", "");
					mTeleQQThree = mWatchInfo.getString("QQThree", "");
				}
			} else if (result.contains("���ĺ���")) {
				if (result.contains("���óɹ�")) {
					Toast.makeText(SettingActivity.this, "���ĺ������óɹ�",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("Centre", mTeleCenter).commit();
					success = true;
					tag = SETTING_TELECENTER;
					// �ϴ�������
				} else {
					Toast.makeText(SettingActivity.this, "���ĺ�������ʧ�ܣ�������!",
							Toast.LENGTH_SHORT).show();
					mTeleCenter = mWatchInfo.getString("Centre", "");
				}
			} else if (result.contains("GPS")) {
				if (result.contains("���óɹ�")) {
					Toast.makeText(SettingActivity.this, "GPS�л��ɹ�",
							Toast.LENGTH_SHORT).show();
					System.out
							.println("the state gps------------------------------>"
									+ mGpsOn);
					mWatchInfo.edit().putBoolean("GpsIsOpen", mGpsOn).commit();
					success = true;
				} else {
					Toast.makeText(SettingActivity.this, "GPS�л�ʧ�ܣ�������!",
							Toast.LENGTH_SHORT).show();
					mGpsOn = mWatchInfo.getBoolean("GpsIsOpen", true);
				}
			} else if (result.contains("����Χ��")) {
				if (result.contains("���óɹ�")) {
					Toast.makeText(SettingActivity.this, "����Χ���л��ɹ�",
							Toast.LENGTH_SHORT).show();
					System.out
							.println("the state wall----------------------------->"
									+ mWallOn);
					mWatchInfo.edit().putBoolean("WallIsOpen", mWallOn)
							.commit();
					success = true;
				} else {
					Toast.makeText(SettingActivity.this, "����Χ���л�ʧ�ܣ�������!",
							Toast.LENGTH_SHORT).show();
					mWallOn = mWatchInfo.getBoolean("WallIsOpen", false);
				}
			} else if (result.contains("�����޸�")) {
				if (result.contains("���óɹ�")) {
					Toast.makeText(SettingActivity.this, "�����޸ĳɹ�",
							Toast.LENGTH_SHORT).show();
					mWatchInfo.edit().putString("password", mDeviceKye)
							.commit();
					success = true;
					tag = SETTING_DEVICEKEY;
				} else {
					Toast.makeText(SettingActivity.this, "�����޸�ʧ�ܣ�������!",
							Toast.LENGTH_SHORT).show();
					mDeviceKye = mWatchInfo.getString("password", "0000");
				}
			} else {
				Toast.makeText(SettingActivity.this, "wait����Ϊ��",
						Toast.LENGTH_SHORT).show();
			}
			if (success) {
				updateFragment(tag);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			startProgressDialog();
		}

		private void startProgressDialog() {
			if (mProgressDialog == null) {
				mProgressDialog = MyProgressDialog
						.createDialog(SettingActivity.this);
				mProgressDialog.setMessage("���ڷ��ͣ������ĵȴ�...");
			}
			mProgressDialog.show();
		}

		private void stopProgressDialog() {
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}

		private String waitMessageResult() {
			String result = "";
			while (mRunning) {
				result = LocationToChildApplication.mMessageResult;
				if (result.contains("���óɹ�") || result.contains("����ʧ��")) {
					LocationToChildApplication.mMessageResult = "";
					break;
				}
			}
			mRunning = false;
			return result;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
