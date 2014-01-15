package com.raul.basic.android.framework.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.raul.basic.android.framework.logic.BaseLogicBuilder;

/**
 * 系统的Activity的启动类 第一个启动的Activity必须继承，其他Activity不要继承
 */
abstract public class LauncheActivity extends BaseActivity {
	final private String TAG = "LauncheActivity";

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
	 * Logic建造管理类需要创建的接口 需要子类继承后，指定Logic建造管理类具体实例
	 * 
	 * @param context
	 *            系统的context对象
	 * @return Logic建造管理类具体实例
	 */
	abstract protected BaseLogicBuilder createLogicBuilder(Context context);

}