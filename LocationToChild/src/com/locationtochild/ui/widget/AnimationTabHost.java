package com.locationtochild.ui.widget;

import com.locationtochild.ui.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class AnimationTabHost extends TabHost{
	
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	public AnimationTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		slideLeftIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context,
				R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,
				R.anim.slide_right_out);
	}

	public AnimationTabHost(Context context) {
		this(context, null);
	}
	
	@Override
	public void setCurrentTab(int index) {
		// TODO Auto-generated method stub
		int currentTabId = getCurrentTab();
		View currentTabView = getCurrentTabView();
		
		// ���廭����Ļ����
		if (currentTabView != null) {// ��һ�ν���ú���ʱ��Ϊ��
			if (index > currentTabId) {
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < currentTabId) {
				getCurrentView().startAnimation(slideRightOut);
			}
		}
		
		// ���������Ļ����
		if (currentTabView != null) {
			if (index > currentTabId) {
				getCurrentView().startAnimation(slideLeftIn);
			} else if (index < currentTabId) {
				getCurrentView().startAnimation(slideRightIn);
			}
		}

	}
}
