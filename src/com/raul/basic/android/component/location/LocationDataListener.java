package com.raul.basic.android.component.location;

/**
 * 
 * 定位信息数据结果监听
 * 
 * @author raulxiao
 */
public interface LocationDataListener {
	/**
	 * 
	 * 定位结果监听
	 * 
	 * @param result
	 *            位置信息
	 */
	void onLocationResult(LocationInfo result);

	/**
	 * 
	 * 进度条监听
	 * 
	 * @param show
	 *            boolean
	 */
	void onLocationProgress(boolean show);

}
