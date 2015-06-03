package com.locationtochild.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.locationtochild.ui.R;
import com.locationtochild.ui.setting.SettingFragment.OnSettingItemSelect;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.Constants;
import com.locationtochild.utils.Constants.SettingsConstants;

public class TeleCenterFragment extends Fragment {
	private final int TEXTNORMAL = 0xff585b5b;
	private final int TEXTSELECT = 0xff36B9AF;
	private View mTeleCenterView;
	private TextView mTeleQQOne;
	private TextView mTeleQQTwo;
	private TextView mTeleQQThree;
	private Button mTeleCenterSubmit;

	private OnSettingItemSelect mListener;
	private TopTitleBar mTeleCenterTitle;
	// ͨ�������ȡ��ǰ���ĺ�����ʾ
	private TextView mOldTeleCenterText;
	private String mOldTeleCenterNum;
	private String mQQOneNum;
	private String mQQTwoNum;
	private String mQQThreeNum;
	private int mCheckId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// savedInstanceStateΪ�����״̬��
		// ��ȡ��ʼֵ
		Bundle bundle = this.getArguments();
		mOldTeleCenterNum = bundle.getString("tele_center");
		mQQOneNum = bundle.getString("qq_one");
		mQQTwoNum = bundle.getString("qq_two");
		mQQThreeNum = bundle.getString("qq_three");

		if (container == null)
			return null;
		mTeleCenterView = (View) inflater.inflate(
				R.layout.fragment_setting_telecenter, container, false);
		initView();
		setListener();
		initData();
		return mTeleCenterView;
	}

	private void initView() {
		mOldTeleCenterText = (TextView) mTeleCenterView
				.findViewById(R.id.setting_telecenter_old);
		mTeleQQOne = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_one);
		mTeleQQTwo = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_two);
		mTeleQQThree = (TextView) mTeleCenterView
				.findViewById(R.id.telecenter_qq_three);
		mTeleCenterSubmit = (Button) mTeleCenterView
				.findViewById(R.id.setting_telecenter_submit);
		mTeleCenterTitle = (TopTitleBar) mTeleCenterView
				.findViewById(R.id.title_setting_telecenter);
	}

	private void setListener() {
		// ���ú��뵥���¼�
		mTeleQQOne.setOnClickListener(choose_tele);
		mTeleQQTwo.setOnClickListener(choose_tele);
		mTeleQQThree.setOnClickListener(choose_tele);

		mTeleCenterSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mCheckId < 1) {
					Toast.makeText(getActivity(), "��ѡ��Ҫ�л������ĺ���",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String centerTele = null;
				switch (mCheckId) {
				case 1:
					centerTele = mQQOneNum;
					break;
				case 2:
					centerTele = mQQTwoNum;
					break;
				case 3:
					centerTele = mQQThreeNum;
					break;
				default:
					break;
				}
				if (mOldTeleCenterNum.equals(centerTele)) {
					Toast.makeText(getActivity(), "��ѡ��ĺ����Ѿ������ĺ��룡",
							Toast.LENGTH_LONG).show();
					return;
				}
				mListener.settingMessage(SettingsConstants.CENTRE_NUMBER,centerTele);
				// ���ó����������ĺ���
				System.out.println("submit center_tele"+centerTele);
			}
		});
		// ������һҳ
		mTeleCenterTitle
				.setTopTitleClickListener(new OnTopTitleClickListener() {
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

	// ���õ����¼�
	private View.OnClickListener choose_tele = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.telecenter_qq_one && mCheckId != 1) {
				mTeleQQOne.setTextColor(TEXTSELECT);
				mTeleQQTwo.setTextColor(TEXTNORMAL);
				mTeleQQThree.setTextColor(TEXTNORMAL);
				mCheckId = 1;
			} else if (v.getId() == R.id.telecenter_qq_two && mCheckId != 2) {
				mTeleQQOne.setTextColor(TEXTNORMAL);
				mTeleQQTwo.setTextColor(TEXTSELECT);
				mTeleQQThree.setTextColor(TEXTNORMAL);
				mCheckId = 2;
			} else if (v.getId() == R.id.telecenter_qq_three && mCheckId != 3) {
				mTeleQQOne.setTextColor(TEXTNORMAL);
				mTeleQQTwo.setTextColor(TEXTNORMAL);
				mTeleQQThree.setTextColor(TEXTSELECT);
				mCheckId = 3;
			}
		}
	};

	// ͨ�������ȡ���ĺ���ĳ�ʼ��Ϣ
	public void initData() {
		mCheckId = 0;
		int flag = 0;
		if (mQQOneNum.equals("")) {
			mTeleQQOne.setVisibility(View.GONE);
		} else {
			if (mQQOneNum.equals(mOldTeleCenterNum)) {
				mCheckId = 1;
				mTeleQQOne.setTextColor(TEXTSELECT);
			}
			mTeleQQOne.setText(mQQOneNum);
			flag = flag + 1;
		}
		if (mQQTwoNum.equals("")) {
			mTeleQQTwo.setVisibility(View.GONE);
		} else {
			if (mQQTwoNum.equals(mOldTeleCenterNum)) {
				mTeleQQTwo.setTextColor(TEXTSELECT);
				mCheckId = 2;
			}
			mTeleQQTwo.setText(mQQTwoNum);
			flag = flag + 1;
		}
		if (mQQThreeNum.equals("")) {
			mTeleQQThree.setVisibility(View.GONE);
		} else {
			if (mQQThreeNum.equals(mOldTeleCenterNum)) {
				mTeleQQThree.setTextColor(TEXTSELECT);
				mCheckId = 3;
			}
			mTeleQQThree.setText(mQQThreeNum);
			flag = flag + 1;
		}
		if (flag < 1 || (flag == 1 && mCheckId > 0)) {
			// ���ð�ť����
			mTeleCenterSubmit.setBackgroundColor(getResources().getColor(
					R.color.theme_color));
			mTeleCenterSubmit.setOnClickListener(null);
			mTeleCenterSubmit.setText("���ö������ż��ɸ������ĺ���");
		}
		// ��ȡ��ǰ���ĺ��룬����ʾ��������
		if (mCheckId < 1) {
			mOldTeleCenterText.setText("δ�������ĺ���");
			mOldTeleCenterText.setGravity(Gravity.CENTER_HORIZONTAL);
		} else {
			mOldTeleCenterText.setText("��ǰ���ĺ��룺" + mOldTeleCenterNum);
		}
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
