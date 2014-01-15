package com.raul.basic.android.ui.basic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.raul.basic.android.AppManager;
import com.raul.basic.android.R;
import com.raul.basic.android.common.FusionCode.Common;
import com.raul.basic.android.framework.logic.BaseLogicBuilder;
import com.raul.basic.android.framework.ui.LauncheActivity;
import com.raul.basic.android.logic.LogicBuilder;
import com.raul.basic.android.logic.login.ILoginLogic;
import com.raul.basic.android.logic.login.LoginLogic;
import com.raul.basic.android.ui.basic.dialog.BasicDialog;
import com.raul.basic.android.ui.basic.dialog.ProgressDialog;
import com.raul.basic.android.util.Log;

public abstract class BasicActivity extends LauncheActivity {

	private static final String TAG = "BasicActivity";

	/**
	 * 退出菜单
	 */
	private static final int MENU_QUIT = 1;

	/**
	 * 当前的activity的对象实例
	 */
	public static BasicActivity currentActivtiy;

	/**
	 * 页面是否进入pause状态
	 */
	private boolean isPaused;

	/**
	 * 页面是否需要刷新
	 */
	private boolean needUpdate;

	/**
	 * 进度显示框
	 */
	private ProgressDialog mProDialog;

	/**
	 * BasicDialog对话框
	 */
	private BasicDialog mBasicDialog;

	/**
	 * Toast对象
	 */
	private Toast mToast;

	/**
	 * 是否展示进度条标志
	 */
	public static boolean isProgressDialogShow = false;

	/**
	 * 页面是否进入pause状态
	 */
	private boolean mIsPaused;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}

	/**
	 * Activity销毁前，先关闭dialog（不然会出现WindowLeaked）
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeProgressDialog();
		closeNormalDialog();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	/**
	 * 创建Logic建造管理类 指定Logic建造管理类具体实例
	 * 
	 * @param context
	 *            系统的context对象
	 * @return Logic建造管理类具体实例
	 */
	@Override
	protected BaseLogicBuilder createLogicBuilder(Context context) {
		BaseLogicBuilder builder = LogicBuilder.getInstance(context);
		LoginLogic loginLogic = (LoginLogic) builder
				.getLogicByInterfaceClass(ILoginLogic.class);
		loginLogic.addHandler(new Handler() {
			public void handleMessage(Message msg) {
				if (null != currentActivtiy) {
					currentActivtiy.handleStatusMessage(msg);
				}
			};
		});
		return builder;
	}

	@Override
	protected void initLogics() {
		// 实现父类的抽象方法.子类就可以选择性实现此方法了
	}

	/**
	 * 系统的初始化方法
	 * 
	 * @param context
	 *            系统的context对象
	 */
	private void initSystem() {

	}

	private void handleStatusMessage(Message msg) {
	}

    
    /**
     * 当焦点停留在view上时，隐藏输入法栏
     * 
     * @param view
     *            view
     */
    protected void hideInputWindow(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        if (null != imm && null != view)
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 当焦点停留在view上时，显示输入法栏
     * 
     * @param view
     *            view
     */
    protected void showInputWindow(View view)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        if (null != imm && null != view)
        {
            imm.showSoftInput(view, 0);
        }
    }
    
    /**
     * 当页面软件盘存在时，隐藏输入法栏
     * @param context 上下文
     */
    protected void hideInputWindow(Context context)
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        
        if ((null != imm) && (null != context) && (context instanceof Activity))
        {
            Activity ac = (Activity) context;
            if (null != ac.getCurrentFocus()
                    && null != ac.getCurrentFocus().getWindowToken())
            {
                imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                        .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

	/**
	 * 
	 * 构建弹出提示框
	 * 
	 * @return AlertDialog.Builder
	 */
	public BasicDialog.Builder getBuilder() {
		BasicDialog.Builder builder = new BasicDialog.Builder(getContext());
		return builder;
	}

	/**
	 * 获取安全的context，防止badtoken异常<BR>
	 * 
	 * @return context
	 */
	protected Context getContext() {
		Context context = null;
		if (null != getParent()) {
			context = getParent();
		} else {
			context = this;
		}
		return context;
	}

	/**
	 * 
	 * 确认提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，左边显示“确定”按钮，右边显示“取消”按钮
	 * 
	 * @param msgResId
	 *            字符串id
	 * @param okClickListener
	 *            确认按钮的点击事件处理
	 */
	public void showConfirmDialog(int msgResId,
			android.content.DialogInterface.OnClickListener okClickListener) {
		mBasicDialog = getBuilder().setTitle(R.string.prompt)
				.setMessage(getResources().getString(msgResId))
				.setPositiveButton(R.string.dialog_btn_cancel, null)
				.setNegativeButton(R.string.dialog_btn_ok, okClickListener)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 确认提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，左边显示“确定”按钮，右边显示“取消”按钮
	 * 
	 * @param titleResId
	 *            标题字符串id
	 * @param msgResId
	 *            字符串id
	 * @param okClickListener
	 *            确认按钮的点击事件处理
	 */
	public void showConfirmDialog(int titleResId, int msgResId,
			android.content.DialogInterface.OnClickListener okClickListener) {
		mBasicDialog = getBuilder().setTitle(titleResId)
				.setMessage(getResources().getString(msgResId))
				.setPositiveButton(R.string.dialog_btn_cancel, null)
				.setNegativeButton(R.string.dialog_btn_ok, okClickListener)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 确认提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，左边显示“确定”按钮，右边显示“取消”按钮
	 * 
	 * @param msgRes
	 *            字符串
	 * @param onClickListener
	 *            确认按钮的点击事件处理
	 */
	public void showConfirmDialog(String msgRes,
			android.content.DialogInterface.OnClickListener onClickListener) {
		mBasicDialog = getBuilder()
				.setTitle(R.string.prompt)
				.setMessage(msgRes)
				.setPositiveButton(R.string.dialog_btn_cancel, null)
				.setNegativeButton(R.string.dialog_btn_ok,
						(DialogInterface.OnClickListener) onClickListener)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 确认提示框 标题栏显示“提示”，显示内容由msgResId提供， 只显示“确定”按钮
	 * 
	 * @param msgResId
	 *            资源id
	 */
	public void showOnlyConfirmDialog(int msgResId) {
		showOnlyConfirmDialog(msgResId, null);
	}

	/**
	 * 确认提示框 标题栏显示“提示”，显示内容由msgResId提供， 只显示“确定”按钮
	 * 
	 * @param msgResId
	 *            资源id
	 * @param okClickListener
	 *            onClickListener
	 */
	public void showOnlyConfirmDialog(int msgResId,
			android.content.DialogInterface.OnClickListener okClickListener) {
		mBasicDialog = getBuilder()
				.setTitle(R.string.prompt)
				.setMessage(getResources().getString(msgResId))
				.setPositiveButton(R.string.dialog_btn_ok,
						(DialogInterface.OnClickListener) okClickListener)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 确认提示框 标题栏显示“提示”，显示内容由msgResId提供， 只显示“确定”按钮
	 * 
	 * @param msgResId
	 *            资源id
	 * @param okClickListener
	 *            onClickListener
	 * @param cancelListener
	 *            OnCancelListener
	 */
	public void showOnlyConfirmDialog(int msgResId,
			DialogInterface.OnClickListener okClickListener,
			DialogInterface.OnCancelListener cancelListener) {
		mBasicDialog = getBuilder()
				.setTitle(R.string.prompt)
				.setMessage(getResources().getString(msgResId))
				.setPositiveButton(R.string.dialog_btn_ok,
						(DialogInterface.OnClickListener) okClickListener)
				.create();
		mBasicDialog.setOnCancelListener(cancelListener);
		mBasicDialog.show();
	}

	/**
	 * 
	 * 确认提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，只显示“确定”按钮
	 * 
	 * @param titleId
	 *            标题
	 * @param messageId
	 *            提示内容
	 * @param okClickListener
	 *            确认按钮的点击事件处理
	 */
	public void showOnlyConfirmDialog(int titleId, int messageId,
			DialogInterface.OnClickListener okClickListener) {
		mBasicDialog = getBuilder()
				.setTitle(titleId)
				.setMessage(getResources().getString(messageId))
				.setPositiveButton(R.string.dialog_btn_ok,
						(DialogInterface.OnClickListener) okClickListener)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 带自画View的Dialog
	 * 
	 * @param textId
	 *            textId
	 * @param listener
	 *            listener
	 * @param view
	 *            view
	 */
	protected void showViewDialog(int textId,
			final android.content.DialogInterface.OnClickListener listener,
			final View view) {
		mBasicDialog = getBuilder().setTitle(textId).setContentView(view)
				.setPositiveButton(R.string.dialog_btn_cancel, null)
				.setNegativeButton(R.string.dialog_btn_ok, listener).create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 带自画View的Dialog
	 * 
	 * @param textId
	 *            textId
	 * @param listener
	 *            listener
	 * @param view
	 *            view
	 */
	protected void showViewDialog(int textId, final View view) {
		mBasicDialog = getBuilder().setTitle(textId).setContentView(view)
				.create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 显示进度框<BR>
	 * 
	 * @param message
	 *            对话框显示信息
	 */
	protected void showProgressDialog(String message) {
		// 默认可取消当前对话框
		showProgressDialog(message, true);
	}

	/**
	 * 显示进度框<BR>
	 * 
	 * @param message
	 *            对话框显示信息
	 * @param cancelable
	 *            对话框可取消标志
	 */
	private void showProgressDialog(String message, boolean cancelable) {
		if (mProDialog == null) {
			mProDialog = new ProgressDialog(getContext(), cancelable);
		}
		mProDialog.setMessage(message);
		showProgressDialog(mProDialog);
	}

	/**
	 * 弹出进度框<BR>
	 * 
	 * @param proDialog
	 *            对话框显示信息
	 */
	protected void showProgressDialog(ProgressDialog proDialog) {
		if (!mIsPaused) {
			proDialog.show();
			isProgressDialogShow = true;
		}
	}

	/**
	 * 
	 * 显示进度框<BR>
	 * 
	 * @param msgResId
	 *            对话框显示信息
	 */
	protected void showProgressDialog(int msgResId) {
		showProgressDialog(getResources().getString(msgResId), true);
	}

	/**
	 * 显示进度框<BR>
	 * 
	 * @param msgResId
	 *            对话框显示信息
	 * @param cancelable
	 *            是否可取消的标志
	 */
	protected void showProgressDialog(int msgResId, boolean cancelable) {
		showProgressDialog(getResources().getString(msgResId), cancelable);
	}

	/**
	 * 
	 * 关闭进度框<BR>
	 */
	protected void closeProgressDialog() {
		// 关闭ProgressDialog
		if (null != mProDialog) {
			mProDialog.dismiss();
			mProDialog = null;
			isProgressDialogShow = false;
		}
	}

	/**
	 * 关闭对话框<BR>
	 */
	protected void closeNormalDialog() {
		// 关闭BasicDialog
		if (null != mBasicDialog) {
			mBasicDialog.dismiss();
			mBasicDialog = null;
		}
	}

	/**
	 * 显示提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，居中显示“确定”按钮，用户点击后关闭对话框。
	 * 
	 * @param msgResId
	 *            提示信息
	 */
	protected void showPromptDialog(int msgResId) {
		showPromptDialog(getResources().getString(msgResId));
	}

	/**
	 * 
	 * 显示提示框<BR>
	 * 标题栏显示“提示”，显示内容由msgResId提供，居中显示“确定”按钮，用户点击后关闭对话框。 [功能详细描述]
	 * 
	 * @param message
	 *            提示信息
	 */
	protected void showPromptDialog(String message) {
		showPromptDialog(message, null, true);
	}

	/**
	 * 
	 * 显示提示框<BR>
	 * 
	 * @param message
	 *            message
	 * @param okClickListener
	 *            onClickListener
	 * @param cancelable
	 *            cancelable
	 */
	protected void showPromptDialog(String message,
			DialogInterface.OnClickListener okClickListener, boolean cancelable) {
		mBasicDialog = getBuilder().setTitle(R.string.prompt)
				.setMessage(message)
				.setPositiveButton(R.string.dialog_btn_cancel, null)
				.setNegativeButton(R.string.dialog_btn_ok, okClickListener)
				.create();
		mBasicDialog.setCancelable(cancelable);
		mBasicDialog.show();
	}

	/**
	 * 显示提示框
	 * 
	 * @param message
	 *            提示内容
	 * @param titleStr
	 *            标题
	 * @param leftStr
	 *            左按钮
	 * @param rightStr
	 *            右按钮
	 * @param okClickListener
	 *            监听
	 * @param cancelable
	 *            cancelable
	 */
	protected void showPromptDialog(String message, int titleStr, int leftStr,
			int rightStr, DialogInterface.OnClickListener okClickListener,
			boolean cancelable) {
		mBasicDialog = getBuilder().setTitle(titleStr).setMessage(message)
				.setPositiveButton(leftStr, null)
				.setNegativeButton(rightStr, okClickListener).create();
		mBasicDialog.setCancelable(cancelable);
		mBasicDialog.show();
	}

	/**
	 * 显示提示框
	 * 
	 * @param message
	 *            提示内容
	 * @param titleStr
	 *            标题
	 * @param leftStr
	 *            左按钮
	 * @param rightStr
	 *            右按钮
	 * @param okClickListener
	 *            监听
	 * @param cancelClickListener
	 *            取消监听
	 * @param cancelable
	 *            cancelable
	 */
	protected void showPromptDialog(String message, int titleStr, int leftStr,
			int rightStr, DialogInterface.OnClickListener okClickListener,
			DialogInterface.OnClickListener cancelClickListener,
			boolean cancelable) {
		mBasicDialog = getBuilder().setTitle(titleStr).setMessage(message)
				.setPositiveButton(leftStr, cancelClickListener)
				.setNegativeButton(rightStr, okClickListener).create();
		mBasicDialog.setCancelable(cancelable);
		mBasicDialog.show();
	}

	/**
	 * 
	 * 显示编辑Dialog
	 * 
	 * @param textId
	 *            textId
	 * @param listener
	 *            listener
	 * @param editText
	 *            editText
	 */
	protected void showTextEditDialog(int textId,
			final android.content.DialogInterface.OnClickListener listener,
			final EditText editText) {
		editText.setSelection(editText.getText().toString().length());
		mBasicDialog = getBuilder().setTitle(textId).setContentView(editText)
				.setPositiveButton(R.string.dialog_btn_ok, listener)
				.setNegativeButton(R.string.dialog_btn_cancel, null).create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 显示信息Dialog
	 * 
	 * @param titleId
	 *            标题
	 * @param iConId
	 *            标题图片
	 * @param messageID
	 *            展示信息
	 * @param leftButton
	 *            左按钮名称
	 * @param leftListener
	 *            监听
	 * @param rightButton
	 *            右按钮名称
	 * @param rightListener
	 * 
	 *            监听
	 * @return BasicDialog
	 */
	protected void showMessageDialog(int titleId, int iConId, int messageID,
			int leftButton,
			final android.content.DialogInterface.OnClickListener leftListener,
			int rightButton,
			final android.content.DialogInterface.OnClickListener rightListener) {
		mBasicDialog = getBuilder().setTitle(titleId).setMessage(messageID)
				.setPositiveButton(leftButton, leftListener)
				.setNegativeButton(rightButton, rightListener).create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 确认提示框 标题栏显示“提示”，显示内容由msgResId提供，左边显示“确定”按钮，右边显示“取消”按钮
	 * 
	 * @param titleId
	 *            标题
	 * @param iconId
	 *            图片
	 * @param messageId
	 *            提示内容
	 * @param onClickListener
	 *            确认按钮的点击事件处理
	 */
	public void showIconDialog(int titleId, int messageId,
			android.content.DialogInterface.OnClickListener onClickListener) {
		mBasicDialog = getBuilder()
				.setTitle(titleId)
				.setMessage(getResources().getString(messageId))
				.setPositiveButton(
						R.string.dialog_btn_ok,
						(android.content.DialogInterface.OnClickListener) onClickListener)
				.setNegativeButton(R.string.dialog_btn_cancel, null).create();
		mBasicDialog.show();
	}

	/**
	 * 
	 * 根据资源id show toast
	 * 
	 * @param msgId
	 *            字符串id
	 */
	protected void showToast(int msgId) {
		showToast(getResources().getString(msgId));
	}

	/**
	 * 
	 * 根据字符串 show toast
	 * 
	 * @param message
	 *            字符串
	 */
	protected void showToast(CharSequence message) {
		if (null == mToast) {
			mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(message);
		}
		mToast.show();
	}

	/**
	 * 
	 * 是否需要MENU
	 * 
	 * @return 默认为否
	 */

	protected boolean isNeedMenu() {
		return false;
	}

	/**
	 * 菜单
	 * 
	 * @param menu
	 *            Menu
	 * @return true
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (isNeedMenu()) {
			menu.add(Menu.NONE, MENU_QUIT, Menu.NONE, R.string.exit);
			menu.getItem(0).setIcon(R.drawable.menu_exit_icon);
		}
		return true;
	}

	/**
	 * 
	 * 退出菜单
	 * 
	 * @param item
	 *            MenuItem
	 * @return true
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_QUIT) {
			showIconDialog(R.string.wake, R.string.cancel_app,
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// 本地保存登录信息
							// Editor edit =
							// getSharedPreferences(Common.SHARED_PREFERENCE_NAME,
							// Context.MODE_PRIVATE).edit();
							// edit.putBoolean(Common.KEY_ISLOGIN, false);
							// edit.commit();
							dialog.dismiss();
							finish();
							// 结束Activity&从堆栈中移除
							AppManager.getAppManager().finishActivity(
									BasicActivity.this);
						}
					});
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 检查网络
	 * 
	 * @return 是否连接
	 */
	protected boolean checkNet() {
		// 检查网络
		ConnectivityManager manger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manger.getActiveNetworkInfo();
		if (!(info != null && info.isConnected())) {
			showToast(R.string.check_network);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 是否是paused状态
	 * 
	 * @return boolean
	 */
	protected boolean isPaused() {
		return isPaused;
	}

	/**
	 * 是否需要更新
	 * 
	 * @return boolean
	 */
	protected boolean isNeedUpdate() {
		return needUpdate;
	}

	/**
	 * 
	 * 设置是否要更新
	 * 
	 * @param needUpdate
	 *            needUpdate
	 */
	protected void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isPaused = false;
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "BasictActivity " + this + " onPause");
		mIsPaused = true;
		super.onPause();
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		if (null != imm && imm.isActive()) {
			if (null != this.getCurrentFocus()
					&& null != this.getCurrentFocus().getWindowToken()) {
				imm.hideSoftInputFromWindow(this.getCurrentFocus()
						.getApplicationWindowToken(), 0);
			}
		}
	}

	/**
	 * 
	 * 获取shared preferences
	 * 
	 * @return SharedPreferences
	 */
	public SharedPreferences getSharedPreferences() {
		return getSharedPreferences(Common.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
	}

	/**
	 * 
	 * 返回当前程序版本名
	 * 
	 * @return 版本号
	 */
	protected String getAppVersionName() {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.i(TAG, "Exception>>>" + e);
		}
		return versionName;
	}

	/**
	 * 
	 * 是否为已经登录
	 * 
	 * @return 是否为已经登录
	 */
	protected boolean hasLogined() {
		return getSharedPreferences().getBoolean(Common.KEY_ISLOGIN, false);
	};

	/**
	 * 
	 * 设置已经成功登录
	 * 
	 * @param logined
	 *            boolean 是否为已经登录
	 */
	protected void setLogined(boolean logined) {
		getSharedPreferences().edit().putBoolean(Common.KEY_ISLOGIN, logined)
				.commit();
	}

}
