package com.raul.basic.android.framework.logic;

/**
 * LogicBuilder的接口
 */
public interface ILogicBuilder {
	/**
	 * 根据logic接口类返回logic对象,如果缓存没有则返回null。
	 * 
	 * @param interfaceClass
	 *            logic接口类
	 * @return logic对象
	 */
	public ILogic getLogicByInterfaceClass(Class<?> interfaceClass);
}
