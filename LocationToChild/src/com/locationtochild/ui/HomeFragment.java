package com.locationtochild.ui;

import java.util.ArrayList;
import java.util.List;

import com.locationtochild.logic.LocationToChildApplication;
import com.locationtochild.logic.model.LocationBean;
import com.locationtochild.ui.R;
import com.locationtochild.ui.InitSettingActivity.AsyncInitTask;
import com.locationtochild.ui.adapter.AddressAdapter;
import com.locationtochild.ui.setting.SettingActivity;
import com.locationtochild.ui.widget.MyProgressDialog;
import com.locationtochild.ui.widget.TopTitleBar;
import com.locationtochild.ui.widget.TopTitleBar.OnTopTitleClickListener;
import com.locationtochild.utils.MessageUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment{
	
	private View mHomeView;
	private TopTitleBar mHomeTitle;
	private ListView mAddress;
	private Button mGetAddressBtn;
	private TextView mAddressText;
	private TextView mTimeText;
	
	private AddressAdapter mAddAdapter;
	private List<LocationBean> mListData;
	private RelativeLayout mListFooter;
	
	private AsyncHomeTask mTask;
	private static final String GET_LOCATION_BY_MESSAGE = "messagetolocation";//ͨ����������ǰλ����Ϣ
	private static final String GET_ALL_LOCATION_BY_DB = "get all location by db";//�ӱ������ݿ���λ����Ϣ
	private boolean mRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {//�˻ص���ȡ������ѱ���������ᾡ������ش���һ����̨�߳�����ȡ����Ƭ����Ҫ������
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {//savedInstanceStateΪ�����״̬��
		if(container == null)//���������ֵΪNull,��ζ�Ÿ���Ƭ���ɼ�
			return null;
		
		mHomeView = (View)inflater.inflate(R.layout.fragment_home, container, false);
		initView();
		initListView();
		setListener();
		return mHomeView;
	}
	
	private void initView(){
		mHomeTitle = (TopTitleBar)mHomeView.findViewById(R.id.title_home);
		mGetAddressBtn = (Button)mHomeView.findViewById(R.id.start_location);
		mAddress = (ListView)mHomeView.findViewById(R.id.home_list);
		mAddressText = (TextView)mHomeView.findViewById(R.id.recent_location);
		mTimeText = (TextView)mHomeView.findViewById(R.id.recent_time);
		mListFooter = (RelativeLayout)LayoutInflater.from(getActivity()).inflate(R.layout.footer_address_list, null);
		mHomeTitle.setRightLineVisibility(View.VISIBLE);
	}
	
	private void initListView(){
		mListData = new ArrayList<LocationBean>();
		mListData = LocationToChildApplication.mDBUtils.getAllAddressInfo();
		initTextView();
		mAddAdapter = new AddressAdapter(getActivity(), mListData);//getActivity()�õ���ǰ��Ƭ�󶨵�Activity
		if(!mListData.isEmpty())
			mAddress.addFooterView(mListFooter);
		mAddress.setAdapter(mAddAdapter);
	}
	
	private void initTextView(){
		String location = getResources().getString(R.string.recently_location_str);
		String time = getResources().getString(R.string.recently_time_str);
		if(!mListData.isEmpty()){
			mAddressText.setText(String.format(location, mListData.get(0).getAddressStr()));
			mTimeText.setText(String.format(time, mListData.get(0).getTime()));
		}else{
			mAddressText.setText("��û��λ�ü�¼!");
			mTimeText.setText("");
		}
		
	}
	
	private void setListener(){
		mHomeTitle.setTopTitleClickListener(new OnTopTitleClickListener() {
			
			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(getActivity(), SettingActivity.class);
				startActivity(intent);
			}
			
			@Override
			public void onLeftClick() {
				// TODO Auto-generated method stub
				
			}
		});
		
		mGetAddressBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mTask != null && mTask.getStatus() == AsyncInitTask.Status.RUNNING)
					mTask.cancel(true);
				mTask = new AsyncHomeTask();
				mTask.execute(GET_LOCATION_BY_MESSAGE);
			}
		});
	}
	
	public class AsyncHomeTask extends AsyncTask<String, Integer, String>{
		private MyProgressDialog mProgressDialog;
		@Override
		protected String doInBackground(String... params) {
			String type = params[0];
			String result = "";//���ص����̵߳Ľ��
			if(type.equalsIgnoreCase(GET_LOCATION_BY_MESSAGE)){
				mRunning = true;
				MessageUtils.getInstance().getPosition();
				result = waitMessageResult();
			}else if(type.equalsIgnoreCase(GET_ALL_LOCATION_BY_DB)){
				
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopProgressDialog();
			if(result.equalsIgnoreCase("λ�ò�ѯ�ɹ�")){
				mListData = LocationToChildApplication.mDBUtils.getAllAddressInfo();
				initTextView();
				mAddAdapter = new AddressAdapter(getActivity(), mListData);
				if(!mListData.isEmpty())
					mAddress.addFooterView(mListFooter);
				mAddress.setAdapter(mAddAdapter);
			}else if(result.equalsIgnoreCase("λ�ò�ѯ��ʱ�����Ժ�����")){
				Toast.makeText(getActivity(), "λ�ò�ѯ��ʱ�����Ժ�����", Toast.LENGTH_SHORT).show();
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
	        	mProgressDialog = MyProgressDialog.createDialog(getActivity());
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
	    		if(result.equalsIgnoreCase("λ�ò�ѯ�ɹ�")){
	    			LocationToChildApplication.mMessageResult = "";
	    			break;
	    		}else if(result.equalsIgnoreCase("λ�ò�ѯ��ʱ�����Ժ�����")){
	    			LocationToChildApplication.mMessageResult = "";
	    			break;
	    		}	
	    	}
	    	mRunning = false;
	    	return result;
	    }
		
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	
}
