package com.locationtochild.utils;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.locationtochild.db.DBUtils;
import com.locationtochild.logic.model.TimeAlarm;

public class AlarmHelper {
	private static AlarmHelper mAlarmHelper;
	private DBUtils mDbUtil;
	private List<TimeAlarm> mAlarmList;
	private static String[] mDay = { "����һ ", " ���ڶ� ", " ������ ", " ������ ",
			" ������ ", " ������ ", " ������ " };

	public static final String ALARM_ALERT_ACTION = "com.locationtochild.ALARM_ALERT";

	private AlarmHelper() {

	}

	public synchronized static AlarmHelper getInstance() {
		if (mAlarmHelper == null)
			mAlarmHelper = new AlarmHelper();
		return mAlarmHelper;
	}

	// ��ȡ��ʼ�������б�
	public List<TimeAlarm> getAlarmList(Context context) {
		if (mAlarmList == null) {
			mDbUtil = new DBUtils(context);
			mAlarmList = mDbUtil.getAlarmData();
			// ��ʼ�����ӵ�������Ϣ
			for (int i = 0; i < mAlarmList.size(); i++) {
				mAlarmList.get(i).setDescription(
						initDescription(mAlarmList.get(i).getDayOfWeek()));
			}
		}
		return mAlarmList;
	}

	// ��ʼ����������
	public void initAlarm(Context context) {
		System.out.println("init the alarm when launched phone ");
		// ���������������
		if (mAlarmList == null) {
			mAlarmList = getAlarmList(context);
		}
		for (int i = 0; i < mAlarmList.size(); i++) {
			if (mAlarmList.get(i).getIsOn()) {
				openAlarm(i, context);
			}
		}
	}

	// �ж������Ƿ��ظ�
	public boolean hasExist(Context context,String alarmTime){
		System.out.println("coming hasexist ");
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		// �������ݵ����ݿ��У�ͬ���������б�
		int num = mDbUtil.hasExistAlarm(alarmTime);
		if (num > 0) {
			return true;
		}
		else 
			return false;
	}
	
	// �������
	public void addAlarm(TimeAlarm alarm, Context context) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		// �������ݵ����ݿ��У�ͬ���������б�
		int id = mDbUtil.insertIntoAlarm(alarm);
		if (id < 0) {
			// ��������ʧ��
			System.out.println("insert error");
			return;
		}
		alarm.setId(id);
		alarm.setDescription(initDescription(alarm.getDayOfWeek()));
		mAlarmList.add(alarm);
		System.out.println("call the add alarm id is " + id);
		// �����´����ѵ�����
		long timeInMillis = calculateAlarm(alarm);
		// �������
		enableAlert(context, id, timeInMillis);
	}

	// �޸���������
	public void updateAlarm(int position, Context context, boolean flag) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		mAlarmList.get(position).setDescription(
				initDescription(mAlarmList.get(position).getDayOfWeek()));
		mDbUtil.updateAlarm(mAlarmList.get(position));
		if (flag && mAlarmList.get(position).getIsOn()) {
			System.out.println("change the alarm on ");
			// ��������
			openAlarm(position, context);
		}
	}

	// ɾ������
	public void removeAlarm(int position, Context context) {
		if (mAlarmList.get(position).getIsOn()) {
			System.out.println("delete alarm--------->disable the alarm ");
			// disableAlarm(context, position);
		}
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		int id = mAlarmList.get(position).getId();
		mDbUtil.deleteFromAlarm(id);
		mAlarmList.remove(position);
		System.out.println("remove alarm id" + id + " the position is "
				+ position);
	}

	// ������������
	public void openAlarm(int position, Context context) {
		// �����´����ѵ�����
		long timeInMillis = calculateAlarm(mAlarmList.get(position));
		// �������
		enableAlert(context, mAlarmList.get(position).getId(), timeInMillis);
	}

	// �л���������
	public void changeAlarm(int position, Context context) {
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		int id = mAlarmList.get(position).getId();
		System.out.println("the before alarm is "
				+ mAlarmList.get(position).getIsOn());
		mDbUtil.manageAlarmOn(id, mAlarmList.get(position).getIsOn());

		if (mAlarmList.get(position).getIsOn()) {
			System.out.println("cancel alarm--------->disable the alarm ");
			disableAlarm(context, id);
			mAlarmList.get(position).setIsOn(false);
		} else {
			System.out.println("add alarm--------->add the alarm ");
			// �����´����ѵ�����
			long timeInMillis = calculateAlarm(mAlarmList.get(position));
			// �������
			enableAlert(context, id, timeInMillis);
			mAlarmList.get(position).setIsOn(true);
		}

	}

	// �������ѹ���ȡ������------------------------------------------>test
	public void disableAlarm(Context context, int id) {
		System.out.println("cancel alarm meme !");
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ALERT_ACTION);
		PendingIntent cancel = PendingIntent.getBroadcast(context, id, intent,
				0);
		am.cancel(cancel);
	}

	// �������ѹ����������----------------------------------------->test
	private void enableAlert(Context context, int id, long atTimeInMillis) {
		System.out.println("add alarm meme !");
		// �������
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ALERT_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, id, intent,
				0);
		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
	}

	// �����´����ѵ�ʱ��
	static long calculateAlarm(TimeAlarm alarm) {
		// ��ȡ��ǰʱ��
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);
		// ������ǰ���õ�����ʱ��
		int alarmHour = Integer.parseInt(alarm.getTime().substring(0, 2));
		int alarmMin = Integer.parseInt(alarm.getTime().substring(3));
		// ������õ�ʱ����ڵ�ǰʱ�䣬��ʱ����к���
		if (alarmHour < nowHour || alarmHour == nowHour
				&& alarmMin <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
			System.out.println("the time is < now time !");
		}
		c.set(Calendar.HOUR_OF_DAY, alarmHour);
		c.set(Calendar.MINUTE, alarmMin);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		// ��ȡ��һ������ʱ��
		int addDays = getNextAlarm(c, alarm.getDayOfWeek());
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c.getTimeInMillis();
	}

	// ��ȡ��һ�ε�����ʱ�� ----------------------------�����
	public static int getNextAlarm(Calendar c, int dayOfWeek) {
		// �л�ʱ�䵽���������ڣ�����calendar�õ�������һ��������Ϊ2
		int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
		int day = 0;
		int dayCount = 0;
		// �����ݽ�����λ����
		int yi;
		for (; dayCount < 7; dayCount++) {
			day = (today + dayCount) % 7;
			// �����ݽ�����λ������֮���2ȡ���жϵ�ǰ�����Ƿ�ѡ��
			yi = dayOfWeek >> day;
			if (yi % 2 > 0) {
				break;
			}
		}
		System.out.println("the daycount is " + dayCount);
		return dayCount;
	}

	public void setNextAlarm(Context context,String time){
		if (mDbUtil == null) {
			mDbUtil = new DBUtils(context);
		}
		TimeAlarm alarm=mDbUtil.getIdByTime(time);
		if(alarm!=null){
			System.out.println("set next alarm meme !Helper !");
			// �����´����ѵ�����
			long timeInMillis = calculateAlarm(alarm);
			// �������
			enableAlert(context, alarm.getId(), timeInMillis);
		}
	}
	
	/**
	 * ��ʼ�����ѵ���������ת��Ϊ�ַ���
	 * 
	 * @param dayOfWeek
	 */
	public String initDescription(int dayOfWeek) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 7; i++) {
			if ((dayOfWeek >> i) % 2 == 1) {
				sb.append(mDay[i]);
			}
		}
		return sb.toString();
	}
}
