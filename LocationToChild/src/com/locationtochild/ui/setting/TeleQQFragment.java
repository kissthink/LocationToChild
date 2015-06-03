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

public class TeleQQFragment extends Fragment {
	private View mTelephoneView;
	// ����ؼ�
	private EditText mTelephone1Text;
	private EditText mTelephone2Text;
	private EditText mTelephone3Text;
	private Button mTeleSubmit;
	// back����
	private OnSettingItemSelect mListener;
	private TopTitleBar mTeleQQTitle;
	// ��ȡ�������
	private String mQQOne;
	private String mQQTwo;
	private String mQQThree;
	private int mQQnum;
	private String []mNum=new String[3];;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��ȡ��ʼֵ
		Bundle bundle = this.getArguments();
		mQQOne = bundle.getString("qq_one");
		mQQTwo = bundle.getString("qq_two");
		mQQThree = bundle.getString("qq_three");

		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mTelephoneView = (View) inflater.inflate(
				R.layout.fragment_setting_telepqq, container, false);
		initView();
		initListener();
		initData();
		return mTelephoneView;
	}

	private void initView() {
		mTelephone1Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_one);
		mTelephone2Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_two);
		mTelephone3Text = (EditText) mTelephoneView
				.findViewById(R.id.setting_teleqq_three);
		mTeleSubmit = (Button) mTelephoneView
				.findViewById(R.id.setting_teleqq_submit);
		mTeleQQTitle = (TopTitleBar) mTelephoneView
				.findViewById(R.id.title_setting_teleqq);
	}

	private void initListener() {
		mTeleSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ���浱ǰ�޸Ĳ���
				if(!checkInput()){
					return;
				}
				if(mQQnum==1){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "one", mNum[0]);
				}else if(mQQnum==2){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "two", mNum[0], mNum[1]);
				}else if(mQQnum==3){
					mListener.settingMessage(SettingsConstants.FAMILY_NUMBER, "three", mNum[0], mNum[1],mNum[2]);
				}
			}
		});
		// ������һҳ
		mTeleQQTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
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

	// ��ʼ�����������Ϣ,ʹ������������ͬ������
	private void initData() {
		mTelephone1Text.setText(mQQOne);
		mTelephone2Text.setText(mQQTwo);
		mTelephone3Text.setText(mQQThree);
	}

	// �ж���������
	private boolean checkInput() {
		mQQnum=0;
		if (mTelephone1Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone1Text.getText().toString();
			mQQnum++;
		} else if (mTelephone1Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "����ȫ�������1", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mTelephone2Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone2Text.getText().toString();
			if(mQQnum<1){
				Toast.makeText(this.getActivity(), "�밴˳�������������", Toast.LENGTH_SHORT)
				.show();
			}
			mQQnum++;
		} else if (mTelephone2Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "����ȫ�������2", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mTelephone3Text.getText().toString().trim().length() == 11) {
			mNum[mQQnum]=mTelephone3Text.getText().toString();
			if(mQQnum<2){
				Toast.makeText(this.getActivity(), "�밴˳�������������", Toast.LENGTH_SHORT)
				.show();
			}
			mQQnum++;
		} else if (mTelephone3Text.getText().toString().trim().length() > 0) {
			Toast.makeText(this.getActivity(), "����ȫ�������3", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (mQQnum<1) {
			Toast.makeText(this.getActivity(), "������Ҫ����һ���������",
					Toast.LENGTH_SHORT).show();
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
