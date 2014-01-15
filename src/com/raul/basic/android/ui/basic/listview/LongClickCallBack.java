package com.raul.basic.android.ui.basic.listview;

import android.view.View;

/**
 * 自定义listview,长按某条记录的回调接口
 */
public interface LongClickCallBack {
	/**
	 * 
	 * 开始长按
	 */
	void startLongClick();

	/**
	 * 
	 * 长按过程中，某条记录的状态发生变化
	 * 
	 * @param position
	 *            Item的位置
	 * @param itemSelected
	 *            Item当前的状态
	 * @param view
	 *            Item当前的View
	 */
	void itemStatusChanged(int position, boolean itemSelected, View view);

	/**
	 * 
	 * 退出长按状态
	 */
	void destroyLongClick();
}
