package com.raul.basic.android.component.net.http;

/**
 * 业务逻辑层监听器,一般由UI层进行实现.
 * UI层调用逻辑层进行数据处理，逻辑层通过此接口回调UI.
 * 
 * @author xiaomiaomiao
 * 
 */
public interface IHttpListener {
	/**
	 * 
	 * UI请求数据后回调函数 返回给调用者Response数据。UI层在处理回调时按如下逻辑进行处理。
	 * 
	 * @param action
	 *            请求id
	 * @param response
	 *            Response
	 */
	void onResult(int action, Response response);

	/**
	 * 
	 * 更新进度实现方法 此方法仅仅用于决定UI层是否显示正在联网的进度框
	 * 
	 * @param isInProgress
	 *            是否正在进行联网
	 */
	void onProgress(boolean isInProgress);
}
