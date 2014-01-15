package com.raul.basic.android.logic;

import android.content.Context;

import com.raul.basic.android.framework.logic.BaseLogicBuilder;
import com.raul.basic.android.logic.login.ILoginLogic;
import com.raul.basic.android.logic.login.LoginLogic;
import com.raul.basic.android.logic.main.IMainLogic;
import com.raul.basic.android.logic.main.MainLogic;

public class LogicBuilder extends BaseLogicBuilder {

	private static BaseLogicBuilder instance;

	/**
	 * 构造方法，继承BaseLogicBuilder的构造方法，由父类BaseLogicBuilder对所有logic进行初始化。
	 * 
	 * @param context
	 *            系统的context对象
	 */
	private LogicBuilder(Context context) {
		super(context);
	}

	/**
	 * 
	 * 获取BaseLogicBuilder单例
	 * 单例模式
	 * 
	 * @param context
	 *            系统的context对象
	 * @return BaseLogicBuilder 单例对象
	 */
	public static synchronized BaseLogicBuilder getInstance(Context context) {
		if (null == instance) {
			instance = new LogicBuilder(context);
		}
		return instance;
	}

	/**
	 * LogicBuidler的初始化方法，系统初始化的时候执行<BR>
	 * 
	 * @param context
	 *            系统的context对象
	 * @see com.huawei.basic.android.im.framework.logic.BaseLogicBuilder#init(android.content.Context)
	 */
	protected void init(Context context) {
		registerAllLogics(context);
	}

	/**
	 * 所有logic对象初始化及注册的方法
	 */
	private void registerAllLogics(Context context) {
		super.registerLogic(ILoginLogic.class, new LoginLogic());
		super.registerLogic(IMainLogic.class, new MainLogic());
	}
}
