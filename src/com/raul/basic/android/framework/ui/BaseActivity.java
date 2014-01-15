package com.raul.basic.android.framework.ui;

import java.util.HashSet;
import java.util.Set;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.raul.basic.android.framework.logic.BaseLogicBuilder;
import com.raul.basic.android.framework.logic.ILogic;
import com.raul.basic.android.util.Log;

/**
 * Activity的抽象基类
 */
abstract public class BaseActivity extends ActivityGroup {
	private static final String TAG = "BaseActivity";

	/**
	 * 系统的所有logic的缓存创建管理类
	 */
	private static BaseLogicBuilder mLogicBuilder = null;

	/**
	 * 该activity持有的handler类
	 */
	private Handler mHandler = null;

	/**
	 * 是否独自控制logic监听
	 */
	private boolean isPrivateHandler = false;

	/**
	 * 缓存持有的logic对象的集合
	 */
	private final Set<ILogic> mLogicSet = new HashSet<ILogic>();

	/**
	 * Acitivity的初始化方法
	 * 
	 * @param savedInstanceState
	 *            Bundle对象
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isInit()) {
			Log.e(TAG,
					"Launched the first should be the LauncheActivity's subclass:"
							+ this.getClass().getName(), new Throwable());
			return;
		}

		if (!isPrivateHandler()) {
			BaseActivity.mLogicBuilder.addHandlerToAllLogics(getHandler());
		}
		try {
			initLogics();
		} catch (Exception e) {
			Toast.makeText(this.getApplicationContext(), "Init logics failed :"
					+ e.getMessage(), Toast.LENGTH_LONG);
			Log.e(TAG, "Init logics failed :" + e.getMessage(), e);
		}
	}

	/**
	 * 获取hander对象
	 * 
	 * @return 返回handler对象
	 */
	protected Handler getHandler() {
		if (mHandler == null) {
			mHandler = new Handler() {
				public void handleMessage(Message msg) {
					BaseActivity.this.handleStateMessage(msg);
				}
			};
		}
		return mHandler;
	}

	/**
	 * activity是否已经初始化，加载了mLogicBuilder对象 判断activiy中是否创建了mLogicBuilder对象
	 * 
	 * @return 是否加载了mLogicBuilder
	 */
	protected final boolean isInit() {
		return BaseActivity.mLogicBuilder != null;
	}

	// /**
	// * 获取全局的LogicBuilder对象
	// * @return
	// * 返回LogicBuilder对象
	// */
	// public static ILogicBuilder getLogicBuilder()
	// {
	// return BaseActivity.mLogicBuilder;
	// }

	/**
	 * 判断UI是否独自管理对logic的handler监听
	 * 
	 * @return 是否是私有监听的handler
	 */
	protected boolean isPrivateHandler() {
		return isPrivateHandler;
	}

	/**
	 * 初始化logic的方法，由子类实现 在该方法里通过getLogicByInterfaceClass获取logic对象
	 */
	protected abstract void initLogics();

	/**
	 * 通过接口类获取logic对象
	 * 
	 * @param interfaceClass
	 *            接口类型
	 * @return logic对象
	 */
	protected final ILogic getLogicByInterfaceClass(Class<?> interfaceClass) {
		ILogic logic = mLogicBuilder.getLogicByInterfaceClass(interfaceClass);
		if (isPrivateHandler() && null != logic && !mLogicSet.contains(logic)) {
			logic.addHandler(getHandler());
			mLogicSet.add(logic);
		}
		if (logic == null) {
			Toast.makeText(this.getApplicationContext(),
					"Not found logic by interface class (" + interfaceClass
							+ ")", Toast.LENGTH_LONG);
			Log.e(TAG, "Not found logic by interface class (" + interfaceClass
					+ ")", new Throwable());
			return null;
		}
		return logic;
	}

	/**
	 * 设置全局的logic建造管理类
	 * 
	 * @param logicBuilder
	 *            logic建造管理类
	 */
	protected static final void setLogicBuilder(BaseLogicBuilder logicBuilder) {
		BaseActivity.mLogicBuilder = logicBuilder;
	}

	/**
	 * logic通过handler回调的方法 通过子类重载可以实现各个logic的sendMessage到handler里的回调方法
	 * 
	 * @param msg
	 *            Message对象
	 */
	protected void handleStateMessage(Message msg) {

	}

	/**
	 * activity的释放的方法 在这里对所有加载到logic中的handler进行释放
	 * 
	 * @see android.app.ActivityGroup#onDestroy()
	 */
	protected void onDestroy() {
		Handler handler = getHandler();
		if (handler != null) {
			if (mLogicSet.size() > 0 && isPrivateHandler()) {
				for (ILogic logic : mLogicSet) {
					logic.removeHandler(handler);
				}
			} else if (mLogicBuilder != null) {
				mLogicBuilder.removeHandlerToAllLogics(handler);
			}

		}
		super.onDestroy();
	}
}
