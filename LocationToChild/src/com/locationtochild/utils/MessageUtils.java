package com.locationtochild.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.logic.model.LocationBean;
import com.locationtochild.utils.Constants.MessageType;
import android.telephony.SmsManager;
import android.util.Log;

public class MessageUtils {
	// ���ŷ�������ö��Žӿڷ��Ͷ���
	private SmsManager mSmsManager;
	private static MessageUtils mMessageUtil;
	// ��ʼ��mPhoneNum
	private String mWatchNum = LocationToChildApplication.mWatchNumber;
	private String mWatchPwd = LocationToChildApplication.mWatchPassword;

	public synchronized static MessageUtils getInstance() {
		if (mMessageUtil == null)
			mMessageUtil = new MessageUtils();
		return mMessageUtil;
	}

	private MessageUtils(){
		mSmsManager = SmsManager.getDefault();
	}
	
	/**
	 * ��ȡ��ǰ��λ����Ϣ
	 */
	public void getPosition() {
		sendMessage(mWatchNum, "988"+mWatchPwd);
	}

	/**
	 * ����һ������� 
	 * @param qQPhoneNum
	 */
	public void setQQPhoneNum(String... qQPhoneNum) {
		String temp = null ;
		String flag="";
		for(int i = 0 ; i < qQPhoneNum.length ; i++){
			temp += qQPhoneNum[i]+"#";
		}
		// 2014-1-7lichen�޸ģ�������������޸���bug
		switch (qQPhoneNum.length) {
		case 1:
			flag="##";
			break;
		case 2:
			flag="#";
			break;
		case 3:
			flag="";
			break;
		default:
			break;
		}
		String nQQPhone = "#711#" + temp + flag + mWatchPwd + "##";
		sendMessage(mWatchNum, nQQPhone);
	}

	/**
	 * ����һ�����ĺ�
	 * 
	 * @param centerPhoneNum ���ĺ���            
	 * */
	public void setCenterPhoneNum(String centerPhoneNum) {
		String nCPhone = "#710#" + centerPhoneNum + "#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nCPhone);
	}

	/**
	 * ���ö�λ���ϴ�ģʽ��Ϣ
	 * @param int timeSpan ��λʱ����
	 * */
	public void setLocationModel(String timeSpan, String sendTimes) {
		String nLocation = "#730#" + timeSpan + "#" + sendTimes + "#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nLocation);
	}

	/**
	 * ����GPS�Ƿ���
	 * @param boolean isOn GPS�Ƿ���
	 * */
	public void setLocationOn(boolean isOn) {
		if (isOn) {
			setLocationModel(180+"", 1+"");
		} else {
			setLocationModel(0+"", 0+"");
		}
	}

	/**
	 * ȡ������Χ��
	 * 
	 * @throws ����
	 * */
	public void setWallOff() {
		sendMessage(mWatchNum, "#760#"+mWatchPwd+"##");
	}

	/**
	 * ���õ���Χ��
	 *@param scaleΪ�뾶  timeSpanΪʱ���� 
	 * */
	public void setWallOn(String scale, String timeSpan, String longitude,
			String latitude) {
		String nWall = "#751#" + scale + "#" + timeSpan + "#" + longitude
				+ "N#" + latitude + "E#"+mWatchPwd+"##";
		sendMessage(mWatchNum, nWall);
	}

	/**
	 * �����޸�
	 * @param  nkey ������  okey ������
	 * @param msgText
	 */
	public void setKeyWord(String okey,String nkey){
		String changeKey="#770#"+nkey+"#"+okey+"##";
		sendMessage(mWatchNum,changeKey);
	}
	
	// ������Ϣ
	public void sendMessage(String phoneNum, String msgText) {
		// ���ý��ջ�ִ
		mSmsManager.sendTextMessage(mWatchNum, null, msgText,
				LocationToChildApplication.mSendPI,
				LocationToChildApplication.mDeliverPI);
	}
	
	/**
	 * ���ݲ�ָͬ���������
	 * @param message ͨ���㲥���յ��Ķ�������
	 */
	public String startParse(String message){
		String result = "";
		if(!message.equalsIgnoreCase("")){
			if((message.charAt(0)+"").equalsIgnoreCase("&")){
				result = parseSettingMessage(message);
			}else{
				Log.i("tag", "�յ���ѯλ�ö���");
				result = parseLocationMessage(message);
			}
		}else{
			Log.i("tag", "��ʼ����------->��������Ϊ��");//����ר��
		}
		
		return result;
	}
	
	/**
	 * ������������ָ����������ݵó��Ƿ����óɹ���
	 * @param message ���յ��Ķ����ַ���
	 * @return result ת��Ϊ���ý��˵���ַ���
	 */
	private String parseSettingMessage(String message){
		String result = "";
		int type = Integer.parseInt(message.substring(1, 4));
		switch(type){
		case MessageType.MESSAGE_QQ:
			result = "�����";
			break;
		case MessageType.MESSAGE_CENTER:
			result = "���ĺ���";
			break;
		case MessageType.MESSAGE_WALL:
			result = "����Χ��";
			break;
		case MessageType.MESSAGE_WALL_CANCEL:
			result = "ȡ������Χ��";
			break;
		case MessageType.MESSAGE_TIME_SAPN:
			result = "GPS";
			break;
		case MessageType.MESSAGE_DEVICE_KEY:
			result = "�����޸�";
			break;
		default:
			result = "δ֪��ָ��";
		}
		result += getSettingResult(message);
		return result;
	}
	
	/**
	 * ����λ�ò�ѯ������Χ����SOS��ȶ��š�
	 * @param messageBody �յ���������
	 * @return result չʾ���;
	 */
	private String parseLocationMessage(String messageBody){
		String result = "";
		LocationBean location = new LocationBean();
		int i = messageBody.length() - 3;
		Log.i("tag", "i��λ��----->"+i);
		for(int j = 0; j < messageBody.length(); j++){
			Log.i("tag", "�ַ�---->"+j+":"+messageBody.charAt(j));
		}
		
		Log.i("tag", "��ʼ����λ��----"+messageBody.charAt(i));
		Log.i("tag", "boolean------"+Character.isDigit(messageBody.charAt(i)));
		if (Character.isDigit(messageBody.charAt(i))) {
			// ���������ո���ж��ŵĽ���
			Log.i("tag", "���һλ������");
			int count = 2;
			int[] device = new int[3];
			while (i >= 0 && count >= 0) {
				if ((messageBody.charAt(i)+"").equals(" ")) {
					Log.i("tag", "i��λ��---->"+i);
					device[count] = i;
					count--;
				}
				i--;
			}
			Log.i("tag", messageBody.substring(device[2] + 1)+"-"+messageBody.substring(device[1] + 1,device[2])+"----"+messageBody.substring(0, device[0]));
			location.setLongtitudeStr(Double.parseDouble(messageBody.substring(device[2] + 1)));
			location.setLatitudeStr(Double.parseDouble(messageBody.substring(device[1] + 1,device[2])));
			location.setAddressStr(messageBody.substring(0, device[0]));
			location.setTime(getNowtime());
			LocationToChildApplication.mDBUtils.insertAddresstoDB(location);//��������Ĳ��뵽�������ݿ�
			result = "λ�ò�ѯ�ɹ�";
		}else if(messageBody.equalsIgnoreCase("λ�ò�ѯ��ʱ�����Ժ�����")){
			result = "λ�ò�ѯ��ʱ�����Ժ�����";
		}
		return result;
	}
	
	/**
	 * ͨ��ƥ�䣬�õ����ú�Ľ��
	 * @param message ��������
	 * @return �������óɹ�����ʧ��
	 */
	private String getSettingResult(String message){
		String success="���óɹ�";
		String failure="����ʧ��";
		Matcher matcher = Pattern.compile(success).matcher(message);
		if(matcher.find())
			return success;
		else
			return failure+",�����ԣ�";
	}
	
	/**
	 * �õ���ǰ��ʱ�䣬��ʽyyyy-MM-dd HH:mm:ss
	 * 
	 * @return ��ǰʱ���ַ���
	 */
	private String getNowtime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String date = formatter.format(curDate);
		return date;
	}
}
