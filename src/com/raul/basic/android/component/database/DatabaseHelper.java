package com.raul.basic.android.component.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库操作帮助类
 * 
 * @author xiaomiaomiao
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * 数据库操作异常控制开关。开发调试阶段打开该开关,正式上线须关闭。
	 */
	public static final boolean IS_PRINT_EXCEPTION = true;

	/**
	 * 打印log信息时传入的标志
	 */
	private static final String TAG = "DatabaseHelper";

	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME_STR = "raul_base_line";

	private static final String DATABASE_NAME_SUFFIX = ".db";

	/**
	 * 数据库的版本号
	 */
	private static final int DATABASE_VERSION = 1;

	/**
	 * 数据库表操作对象
	 */
	private static DatabaseHelper sSingleton = null;

	/**
	 * 
	 * 构造器创建数据库
	 * 
	 * @param context
	 *            上下文
	 */
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME_STR + DATABASE_NAME_SUFFIX, null,
				DATABASE_VERSION);
		Log.i(TAG, "init DatabaseHelper()");
	}

	/**
	 * 获取DatabaseHelper对象
	 * 
	 * @param context
	 *            上下文
	 * @return DatabaseHelper对象
	 */
	public static synchronized DatabaseHelper getInstance(Context context) {
		if (sSingleton == null) {
			sSingleton = new DatabaseHelper(context);
		}
		return sSingleton;
	}

	/**
	 * 创建DB
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "DatabaseHelper on Create()");
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	/**
	 * 创建数据库表
	 * 
	 * @param db
	 *            SQLiteDatabase对象
	 */
	private void createTable(SQLiteDatabase db) {
		// TODO
	}

}
