package com.locationtochild.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.locationtochild.logic.model.TimeAlarm;
import com.locationtochild.ui.adapter.AlarmSettingAdapter;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.AlarmHelper;
public class AlarmFragment extends Fragment{
	private View mAlarmView;
	private TopTitleBar mAlarmTitle;
	private int mPosition;
	// �����б�
	private ListView mSettingList;
	private AlarmSettingAdapter mSettingAdapter;
	private List<TimeAlarm> mDataList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mAlarmView = (View) inflater.inflate(R.layout.fragment_alarm,
				container, false);
		initView();
		initData();
 		initListener();
		return mAlarmView;
	}
	
	public void initView() {
		mSettingList = (ListView) mAlarmView.findViewById(R.id.alarm_setting_list);
		mSettingList.setCacheColorHint(0);  
		
		mAlarmTitle =(TopTitleBar)mAlarmView.findViewById(R.id.title_setting_alarm);
		mAlarmTitle.setRightLineVisibility(View.VISIBLE);
	}

	// ��ʼ�����ݣ������ȫ�ֱ���
	public void initData() {
		// ��ʼ��������Ϣ
		mDataList = AlarmHelper.getInstance().getAlarmList(getActivity());
		mSettingAdapter = new AlarmSettingAdapter(getActivity(), mDataList);
		mSettingList.setAdapter(mSettingAdapter);
	}

	public void initListener() {
		mSettingList.setOnItemClickListener(change_setting);
		setListener();
	}

	// �������
	private void setListener(){
		mAlarmTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
				// ��ת��������ӽ���
				comingToAdd(false);
			}
			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub
			}
		});
	}

	// �����б��¼�����
	private AdapterView.OnItemClickListener change_setting = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			// ��ת����Ӧ����
			mPosition = arg2;
			comingToAdd(true);
		}
	};

	// ������ӻ��޸Ľ���
	public void comingToAdd(boolean flag) {
		// ���û���Ч��
		if (!flag) {
			mPosition = -1;
		}
		// ��ת��������ӵ�Ƭ����
		android.support.v4.app.FragmentManager mFraManager=getActivity().getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction mFrgTran = mFraManager.beginTransaction();
		AddAlarmFragment af=new AddAlarmFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("position", mPosition);
		af.setArguments(bundle);
		mFrgTran.addToBackStack(null);  
		mFrgTran.replace(android.R.id.tabcontent, af).commit();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
}
