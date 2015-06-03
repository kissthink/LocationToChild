package com.locationtochild.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.ui.widget.AnimationTabHost;
import com.locationtochild.ui.widget.ExitDialog;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	// private static final String TAG_MAIN = "main_debug";
	private static final String TAB_HOME = "tab_home";
//	private static final String TAB_MAP = "tab_map";
	private static final String TAB_ALARM = "tab_alarm";
	private static final String TAB_MORE = "tab_more";
	
	private SharedPreferences mUserInfo;// �洢�û��ĸ�����Ϣ�������Ƿ��һ�δ�APP����¼���Լ��û���¼�����˻���Ϣ����

	private AnimationTabHost mTabHost;
	private RadioGroup mRadioGroup;
	private RadioButton mDefaultBtn;

	private FragmentManager mFrgManager;
	private FragmentTransaction mFrgTran;
	private Fragment mTabFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// if (isFirstOpen()) {
		// finish();
		// Intent intent = new Intent(this, LoginActivity.class);
		// startActivity(intent);
		// } else {
		// }
		if (isLogin()) {
			if (isSetting()) {
				initTabHost();
				initRadioGroup();
			} else {
				finish();
				Intent intent = new Intent(this, InitSettingActivity.class);
				startActivity(intent);
			}
		} else {
			finish();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}

		LocationToChildApplication.getInstance().addActivity(this);
	}

	private void initTabHost() {
		mTabHost = (AnimationTabHost) findViewById(R.id.anim_tabhost);
		mTabHost.setup();

		TabSpec ts1 = mTabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME);
		ts1.setContent(new Intent(this, HomeFragment.class));
		mTabHost.addTab(ts1);

//		TabSpec ts2 = mTabHost.newTabSpec(TAB_MAP).setIndicator(TAB_MAP);
//		ts2.setContent(new Intent(this, MapFragment.class));
//		mTabHost.addTab(ts2);

		// 2013-12-28�޸ģ�������Ϣ�����б�
		TabSpec ts3 = mTabHost.newTabSpec(TAB_ALARM).setIndicator(TAB_ALARM);
		ts3.setContent(new Intent(this, AlarmFragment.class));
		mTabHost.addTab(ts3);

		TabSpec ts4 = mTabHost.newTabSpec(TAB_MORE).setIndicator(TAB_MORE);
		ts4.setContent(new Intent(this, MoreFragment.class));
		mTabHost.addTab(ts4);

		mTabHost.setCurrentTab(0);// ����Ĭ�ϱ�ǩ
	}

	private void initRadioGroup() {
		mRadioGroup = (RadioGroup) findViewById(R.id.main_group);
		mRadioGroup.setOnCheckedChangeListener(this);
		mDefaultBtn = (RadioButton) findViewById(R.id.rb_home);
		mDefaultBtn.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		mFrgManager = getSupportFragmentManager();
		mFrgTran = mFrgManager.beginTransaction();

		if (mTabFragment != null) {
			// �Ƿ���Ҫ����mTabFragment�ǿյ��жϣ���ÿ���л�ʱ������fragment���߻���Щ���ݣ�--����
			mTabFragment.onDetach();
		}

		switch (checkedId) {
		case R.id.rb_home:
			mTabFragment = new HomeFragment();
			mTabHost.setCurrentTab(0);
			break;
		case R.id.rb_map:
			mTabFragment = new MapFragment();
			mTabHost.setCurrentTab(1);
			break;
		case R.id.rb_alarm:
			mTabFragment = new AlarmFragment();
			mTabHost.setCurrentTab(2);
			break;
		case R.id.rb_more:
			mTabFragment = new MoreFragment();
			mTabHost.setCurrentTab(3);
			break;
		}
		mFrgTran.replace(android.R.id.tabcontent, mTabFragment).commit();
	}

	/**
	 * �ж�Ӧ���ǲ��ǵ�һ�δ�
	 * 
	 * @return isFirstOpen��boolean
	 */
	// private boolean isFirstOpen() {
	// mUserInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
	// if (!mUserInfo.contains("isFirstOpen")){
	// mUserInfo.edit().putBoolean("isFirstOpen", true).commit();
	// }
	// return mUserInfo.getBoolean("isFirstOpen", true);
	// }

	/**
	 * �ж�Ӧ���Ƿ�ɹ���½�� �ɹ���½�����Խ��к�̨��¼��
	 * 
	 * @return
	 */
	private boolean isLogin() {
		mUserInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
		if (!mUserInfo.contains("isLogin"))
			mUserInfo.edit().putBoolean("isLogin", false).commit();
		return mUserInfo.getBoolean("isLogin", false);
	}

	/**
	 * �жϳ�ʼ���ã��ֻ����롢����š����ĺ��룩�Ƿ��Ѿ����óɹ�
	 * 
	 * @return boolean
	 */
	private boolean isSetting() {
		mUserInfo = getSharedPreferences("UserInfo", MODE_PRIVATE);
		if (!mUserInfo.contains("isSetting"))
			mUserInfo.edit().putBoolean("isSetting", false);
		return mUserInfo.getBoolean("isSetting", false);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		ExitDialog mExit = new ExitDialog(this);
		mExit.show();
	}

}
