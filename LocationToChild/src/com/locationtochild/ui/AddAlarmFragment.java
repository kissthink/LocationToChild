package com.locationtochild.ui;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.locationtochild.logic.model.TimeAlarm;
import com.locationtochild.utils.AlarmHelper;

public class AddAlarmFragment extends Fragment {
	private View mAddAlarmView;
	private static int WEEKEND = 2;
	private static int WORKDAY = 1;
	private static int FREE = 0;
	private static int WORKDAYNUM = 31;
	private static int WEEKENDNUM = 96;
	private int mFlag;
	private TimeAlarm mTimeAlarm;
	private int mPosition = -1;
	private boolean[] mArray;
	private static String mFree = "�Զ���";
	private static String mWorkday = "������";
	private static String mWorkend = "��ĩ";
	// ������ʾ������
	private LinearLayout mTimeShow;
	private TextView mTimeText;
	private RadioGroup mRadioAlarmType;
	private TextView mAlarmType;
	// ��������ѡ��
	private TableLayout mTypeTable;
	private ImageView mImageMon;
	private ImageView mImageTus;
	private ImageView mImageWed;
	private ImageView mImageThu;
	private ImageView mImageFri;
	private ImageView mImageSat;
	private ImageView mImageSun;
	// ���ñ����ȡ����ť
	private Button mSubmit;
	private Button mCancel;
	private Button mDelete;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)// ���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		mPosition = getArguments().getInt("position");
		System.out.println("the position is " + mPosition);
		mAddAlarmView = (View) inflater.inflate(R.layout.fragment_add_alarm,
				container, false);
		initView();
		// ��ʼ��������Ϣ
		initData();
		initListener();
		return mAddAlarmView;
	}

	public void initView() {
		mTimeShow = (LinearLayout) mAddAlarmView
				.findViewById(R.id.add_alarm_first);
		mTimeText = (TextView) mAddAlarmView.findViewById(R.id.alarm_time_text);
		mAlarmType = (TextView) mAddAlarmView
				.findViewById(R.id.alarm_type_text);
		mTypeTable = (TableLayout) mAddAlarmView
				.findViewById(R.id.add_alarm_table);
		// ����ѡ��
		mImageMon = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_mon);
		mImageTus = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_tus);
		mImageWed = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_wen);
		mImageThu = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_thu);
		mImageFri = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_fri);
		mImageSat = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_sat);
		mImageSun = (ImageView) mAddAlarmView.findViewById(R.id.add_alarm_sun);
		// ��������
		mRadioAlarmType = (RadioGroup) mAddAlarmView
				.findViewById(R.id.alarm_type_radio);
		// ����ȡ��
		mSubmit = (Button) mAddAlarmView.findViewById(R.id.add_alarm_submit);
		mCancel = (Button) mAddAlarmView.findViewById(R.id.add_alarm_cancel);
		mDelete = (Button) mAddAlarmView.findViewById(R.id.add_alarm_delete);
	}

	public void initListener() {
		mTimeShow.setOnClickListener(choose_time);
		// ����ѡ����
		mImageMon.setOnClickListener(choose_day);
		mImageTus.setOnClickListener(choose_day);
		mImageWed.setOnClickListener(choose_day);
		mImageThu.setOnClickListener(choose_day);
		mImageFri.setOnClickListener(choose_day);
		mImageSat.setOnClickListener(choose_day);
		mImageSun.setOnClickListener(choose_day);
		// ѡ����������
		mRadioAlarmType.setOnCheckedChangeListener(choose_type);
		// �����ȡ���¼�
		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ��ȡ���ѵ���Ŀ��Ϣ����ӵ���Ӧ��Ŀ¼�������������顢�Ƿ�����ʱ�����С�����
				// --------------------------------------�޸�
				// ��ʼ�����ѵ���������
				boolean flag = true;
				int dayOfWeek = computeDayOfWeek();
				if (dayOfWeek == 0) {
					Toast.makeText(getActivity(), "��ѡ����������", Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (mTimeAlarm.getTime().equals(mTimeText.getText().toString())
						&& mTimeAlarm.getDayOfWeek() == dayOfWeek) {
					flag = false;
					System.out.println("no changing the data");
				}
				mTimeAlarm.setDayOfWeek(dayOfWeek);
				mTimeAlarm.setTime(mTimeText.getText().toString());
				if (AlarmHelper.getInstance().hasExist(getActivity(),mTimeAlarm.getTime())) {
					showDialog();
					return;
				}
				// �������ݵ�����
				if (mPosition < 0) {
					mTimeAlarm.setIsOn(true);
					AlarmHelper.getInstance().addAlarm(mTimeAlarm,
							getActivity());
				} else {
					AlarmHelper.getInstance().updateAlarm(mPosition,
							getActivity(), flag);
				}
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		mCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		// ɾ���¼�
		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlarmHelper.getInstance().removeAlarm(mPosition, getActivity());
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}

	// ��ʾ������������ʾ��
	private void showDialog() {
		new AlertDialog.Builder(getActivity())
				.setTitle("�ظ�����")
				.setMessage("�������Ѵ��ڣ�")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								chooseTimeFun();
							}
						}).show();
	}

	// ��ʼ��������Ϣ
	public void initData() {
		mArray = new boolean[7];
		if (mPosition >= 0) {
			// ��ȡ�޸Ķ���
			mTimeAlarm = AlarmHelper.getInstance().getAlarmList(getActivity())
					.get(mPosition);
			mTimeText.setText(mTimeAlarm.getTime());
			// �ж��������Ƿ�Ϊ�Զ���,����ʼ��ͼ��
			dealArray(mTimeAlarm.getDayOfWeek());
		} else {
			// ��ʼ���µĶ���
			mTimeAlarm = new TimeAlarm();
			mTimeAlarm.setTime("08:00");
			mFlag = WORKDAY;
		}
		if (mFlag != FREE) {
			mTypeTable.setVisibility(View.INVISIBLE);
		}
	}

	// �����ʼ����������Ϣ
	private void dealArray(int dayOfWeek) {
		// ��ʼ�����ڵ�ѡ������
		if (dayOfWeek == WORKDAYNUM) {
			mFlag = WORKDAY;
		} else if (dayOfWeek == WEEKENDNUM) {
			mFlag = WEEKEND;
		} else {
			mFlag = FREE;
		}
		if (mFlag == FREE) {
			mRadioAlarmType.check(R.id.radio_free);
			setArrayList(dayOfWeek);
		} else if (mFlag == WEEKEND) {
			mRadioAlarmType.check(R.id.radio_weekend);
		}
	}

	// �����������ݳ�ʼ����������
	public void setArrayList(int dayOfWeek) {
		for (int i = 0; i < mArray.length; i++) {
			if ((dayOfWeek >> i) % 2 == 1) {
				mArray[i] = true;
				setChecked(i, true);
			}
		}
	}

	// ���������ʼ������ѡ��ͼ��
	public void setChecked(int position, boolean flag) {
		int id = 0;
		switch (position) {
		case 0:
			id = R.id.add_alarm_mon;
			break;
		case 1:
			id = R.id.add_alarm_tus;
			break;
		case 2:
			id = R.id.add_alarm_wen;
			break;
		case 3:
			id = R.id.add_alarm_thu;
			break;
		case 4:
			id = R.id.add_alarm_fri;
			break;
		case 5:
			id = R.id.add_alarm_sat;
			break;
		case 6:
			id = R.id.add_alarm_sun;
			break;
		default:
			break;
		}
		if (id > 0) {
			ImageView firstIn = (ImageView) mAddAlarmView.findViewById(id);
			if (flag)
				firstIn.setImageResource(R.drawable.ocheck_type_day);
			else {
				firstIn.setImageResource(R.drawable.check_type_day);
			}
		}
	}

	// ѡ��ʱ��
	private OnClickListener choose_time = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("click time show picker");
			chooseTimeFun();
		}
	};
	// 
	private void chooseTimeFun(){
		int hour = Integer.parseInt(mTimeText.getText().toString()
				.substring(0, 2));
		int mini = Integer.parseInt(mTimeText.getText().toString()
				.substring(3));
		new TimePickerDialog(getActivity(), timePickerListen, hour, mini,
				true).show();
	}

	// ����ѡ����
	TimePickerDialog.OnTimeSetListener timePickerListen = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String hour;
			if (hourOfDay < 10) {
				hour = "0" + hourOfDay + ":";
			} else {
				hour = "" + hourOfDay + ":";
			}
			if (minute < 10) {
				mTimeText.setText(hour + "0" + minute);
			} else {
				mTimeText.setText(hour + minute);
			}
		}
	};

	// ѡ��������ʾ��ѡ��������
	private RadioGroup.OnCheckedChangeListener choose_type = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == R.id.radio_free) {
				mTypeTable.setVisibility(View.VISIBLE);
				mAlarmType.setText(mFree);
				mFlag = FREE;
			} else if (checkedId == R.id.radio_weekend) {
				mTypeTable.setVisibility(View.INVISIBLE);
				mAlarmType.setText(mWorkend);
				mFlag = WEEKEND;
			} else {
				mTypeTable.setVisibility(View.INVISIBLE);
				mAlarmType.setText(mWorkday);
				mFlag = WORKDAY;
			}
		}
	};

	// �Զ���ѡ����������
	private OnClickListener choose_day = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ImageView image = (ImageView) v;
			boolean flag = false;
			int i = -1;
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.add_alarm_mon:
				i = 0;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_tus:
				i = 1;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_wen:
				i = 2;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_thu:
				i = 3;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_fri:
				i = 4;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_sat:
				i = 5;
				if (mArray[i]) {
					flag = true;
				}
				break;
			case R.id.add_alarm_sun:
				i = 6;
				if (mArray[i]) {
					flag = true;
				}
				break;
			default:
				break;
			}
			if (i >= 0) {
				if (flag) {
					image.setImageResource(R.drawable.check_type_day);
					mArray[i] = false;
				} else {
					image.setImageResource(R.drawable.ocheck_type_day);
					mArray[i] = true;
				}
			}
		}
	};

	// ��ȡ��������
	private int computeDayOfWeek() {
		int sum = 0;
		if (mFlag == WEEKEND) {
			sum = WEEKENDNUM;
		} else if (mFlag == WORKDAY) {
			sum = WORKDAYNUM;
		} else {
			for (int i = 0; i < mArray.length; i++) {
				if (mArray[i]) {
					sum = sum + (int) (Math.pow(2, i));
				}
			}
		}
		System.out.println("the day of week is " + sum);
		return sum;
	}

}
