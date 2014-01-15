package com.raul.basic.android.ui.basic;

import android.content.Context;
import android.os.Bundle;

import com.raul.basic.android.framework.logic.BaseLogicBuilder;
import com.raul.basic.android.framework.ui.BaseTabActivity;
import com.raul.basic.android.logic.LogicBuilder;
import com.raul.basic.android.util.Log;

public class BasicTabActivity extends BaseTabActivity {

	private static final String TAG = "BasicTabActivity";

	/**
	 * Activity的初始化方法
	 * 
	 * @param savedInstanceState
	 *            传入的Bundle对象
	 */
	protected void onCreate(Bundle savedInstanceState) {
		if (!isInit()) {
			BaseLogicBuilder logicBuilder = createLogicBuilder(this
					.getApplicationContext());
			super.setLogicBuilder(logicBuilder);
			Log.i(TAG, "Load logic builder successful");
		}
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化logic的方法，由子类实现 在该方法里通过getLogicByInterfaceClass获取logic对象
	 * 
	 */
	@Override
	protected void initLogics() {
		// TODO Auto-generated method stub
	}

	/**
	 * Logic建造管理类需要创建的接口 需要子类继承后，指定Logic建造管理类具体实例
	 * 
	 * @param context
	 *            系统的context对象
	 * @return Logic建造管理类具体实例
	 */
	protected BaseLogicBuilder createLogicBuilder(Context context) {
		return LogicBuilder.getInstance(context);
	}

}
