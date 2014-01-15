package com.raul.basic.android.framework.logic;

import android.content.Context;
import android.os.Handler;

public interface ILogic {
	/**
	 * 初始化方法 在被系统管理的logic在注册到LogicBuilder中后立即被调用的初始化方法。
	 * 
	 * @param context
	 *            系统的context对象
	 */
	public void init(Context context);

	/**
	 * 在logic对象里加入UI的handler
	 * 
	 * @param handler
	 *            UI传入的handler对象
	 */
	public void addHandler(Handler handler);

	/**
	 * 在logic对象里移除UI的handler
	 * 
	 * @param handler
	 *            UI传入的handler对象
	 */
	public void removeHandler(Handler handler);
}
