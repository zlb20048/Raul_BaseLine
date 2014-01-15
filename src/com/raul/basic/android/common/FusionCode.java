package com.raul.basic.android.common;

public class FusionCode {

	/**
	 * 一些公共信息
	 */
	public interface Common {
		/**
		 * 程序保存shared preferences的名字
		 */
		String SHARED_PREFERENCE_NAME = "raul_preference";

		/**
		 * Account
		 */
		String ACCOUNT = "account";

		/**
		 * Password
		 */
		String ACCOUNT_PWD = "account_pwd";

		/**
		 * 对应服务器返回的loginId
		 */
		String ACCOUNT_ID = "account_id";
		/**
		 * 对应服务器返回的userName
		 */
		String ACCOUNT_NAME = "account_name";

		/**
		 * 对应服务器返回的userCompany
		 */
		String COMPANY = "company";

		/**
		 * 对应服务器返回的loginId
		 */
		String ACCOUNT_FACE = "face_url";

		/**
		 * 是否成功登录
		 */
		String KEY_ISLOGIN = "isLogin";

	}


}
