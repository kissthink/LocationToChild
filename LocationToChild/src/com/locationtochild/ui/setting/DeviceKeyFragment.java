package com.locationtochild.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.locationtochild.ui.R;
import com.locationtochild.ui.setting.SettingFragment.OnSettingItemSelect;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants;
import com.locationtochild.utils.Constants.SettingsConstants;

public class DeviceKeyFragment extends Fragment {
	private View mKeyView;
	// �ؼ�
	private EditText mOriginPass;
	private EditText mNewPass;
	private EditText mNewPassCon;
	private Button mKeySubmit;
	private String mOriginPwd;
	// ���ز���
	private OnSettingItemSelect mListener;
	private TopTitleBar mKeyTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��ȡ��ʼֵ
		Bundle bundle = this.getArguments();
		mOriginPwd = bundle.getString("device_key");

		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mKeyView = (View) inflater.inflate(R.layout.fragment_setting_passwd,
				container, false);
		initView();
		initListener();
		return mKeyView;
	}

	private void initView() {
		mOriginPass = (EditText) mKeyView.findViewById(R.id.setting_key_opass);
		mNewPass = (EditText) mKeyView.findViewById(R.id.setting_key_npass);
		mNewPassCon = (EditText) mKeyView.findViewById(R.id.setting_key_npass1);
		mKeySubmit = (Button) mKeyView.findViewById(R.id.setting_key_submit);
		mKeyTitle = (TopTitleBar) mKeyView.findViewById(R.id.title_setting_key);
	}

	private void initListener() {
		mKeySubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ���������޸ĺ���
				if(checkInput()){
					mListener.settingMessage(SettingsConstants.DEVICE_KEY,mOriginPass.getText().toString(),mNewPass.getText().toString());
				}
			}
		});
		// ������һҳ
		mKeyTitle.setTopTitleClickListener(new OnTopTitleClickListener() {

			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub
				mListener.onItemSelected(Constants.SETTING_MAIN, true);
			}
		});
	}

	// �ж������Ƿ����Ҫ��
	public boolean checkInput() {
		if (mOriginPass.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "��ǰ���벻��Ϊ�գ�", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (mNewPass.getText().toString().trim().equals("")) {
			Toast.makeText(getActivity(), "�����벻��Ϊ�գ�", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (!mNewPass.getText().toString().trim()
				.equals(mNewPassCon.getText().toString().trim())) {
			Toast.makeText(getActivity(), "�����������������һ�£�", Toast.LENGTH_SHORT)
					.show();
			return false;
		} else if (mNewPass.getText().toString().trim().length() != 4) {
			Toast.makeText(getActivity(), "�����õ���������Ϊ4λ��ĸ�����ֵ���ϣ�",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!mOriginPass.getText().toString().trim().equals(mOriginPwd)) {
			Toast.makeText(getActivity(), "�������", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (OnSettingItemSelect) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement onSettingItemClick!");
		}
	}
}
