package com.raul.basic.android.ui.basic.actionbar;

/**
 * 有多种返回的接口
 * 
 * @author xiaomiaomiao
 * 
 */
public interface MutiBack {
	/**
	 * 执行返回
	 * 
	 * @return boolean 是否返回
	 */
	boolean onMutiBack();

	/**
	 * 清除状态
	 */
	void clean();
}
