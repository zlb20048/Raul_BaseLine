package com.raul.basic.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
 * 地址操作封装 包含服务器地址、本地路径
 * 
 * @author xiaomiaomiao
 */
public final class UriUtil {

	private UriUtil() {
	}

	private static final String TAG = "UriUtil";

	private static final String domain = "com.raul";
	private static final String woclient = domain + "/raulclient";
	private static final String group = "group." + domain;

	private static final String FORMAT_JID_COMPLETE = "%s@" + woclient;
	private static final String FORMAT_JID_SIMPLE = "%s@" + domain;
	private static final String FORMAT_GROUP_JID_COMPLETE = "%s@" + group;
	private static final String FORMAT_MY_GROUP_JID_SIMPLE = "%s@" + group
			+ "/%s";
	private static final String FORMAT_SM_JID_SIMPLE = "%s@" + domain
			+ "/msisdn";

	/**
	 * 群发JID
	 */
	private static final String TOGETHER_SEND_JID = "multicast." + domain;

	/**
	 * 获取群发聊天、群发消息的接收者ID
	 * 
	 * @return
	 */
	public static String getTogetherImJid() {
		return TOGETHER_SEND_JID;
	}

	public static String getGroupServiceUri() {
		return group;
	}

	/**
	 * 系统消息类型的发送者，使用域名做为标识
	 * 
	 * @return
	 */
	public static String getSysMsgSender() {
		return domain;
	}

	private static String buildJid(String src, String format) {
		int idx = src.indexOf("@");
		String id = src;
		if (idx >= 0) {
			id = src.substring(0, idx);
		}

		return String.format(format, id);
	}

	public static String buildXmppJid(String woyouAccount) {
		if (StringUtil.isNullOrEmpty(woyouAccount)) {
			Log.w(TAG, "buildXmppJid error: userAccount is null");
			return "";
		}

		return buildJid(woyouAccount, FORMAT_JID_COMPLETE);
	}

	public static String buildXmppJidNoWo(String woyouAccount) {
		if (StringUtil.isNullOrEmpty(woyouAccount)) {
			Log.w(TAG, "buildXmppJidNoWo error: userAccount is null");
			return "";
		}

		return buildJid(woyouAccount, FORMAT_JID_SIMPLE);
	}

	public static String buildSmXmppJid(String woyouAccount) {
		if (StringUtil.isNullOrEmpty(woyouAccount)) {
			Log.w(TAG, "buildSmXmppJid error: userAccount is null");
			return "";
		}

		return buildJid(woyouAccount, FORMAT_SM_JID_SIMPLE);
	}

	public static String buildMyXmppGroupJid(String groupId, String userAccount) {
		if (StringUtil.isNullOrEmpty(groupId)
				|| StringUtil.isNullOrEmpty(userAccount)) {
			Log.w(TAG,
					"buildMyXmppGroupJid error: groupId or userAccount is null");
			return "";
		}

		if (groupId.contains("@")) {
			if (groupId.contains("/")) {
				return groupId;
			} else {
				return groupId + "/" + userAccount;
			}
		}
		return String.format(FORMAT_MY_GROUP_JID_SIMPLE, groupId, userAccount);
	}

	public static String buildXmppGroupJID(String groupId) {
		if (StringUtil.isNullOrEmpty(groupId)) {
			Log.w(TAG, "buildXmppGroupJID error: groupId is null");
			return "";
		}

		return buildJid(groupId, FORMAT_GROUP_JID_COMPLETE);
	}

	public static String getWoYouIdFromJid(String jid) {
		if (jid != null && jid.indexOf("@") != -1) {
			return jid.substring(0, jid.indexOf("@"));
		}
		return jid;
	}

	public static String getGroupJidFromJid(String jid) {
		if (jid != null && jid.indexOf('/') != -1) {
			return jid.substring(0, jid.indexOf('/'));
		}
		return jid;
	}

	public static String getGroupMemberIdFromJid(String jid) {
		if (jid != null && jid.indexOf('/') != -1) {
			return jid.substring(jid.indexOf('/') + 1);
		}
		return jid;
	}

	/**
	 * 本地文件夹类型
	 */
	public enum LocalDirType {
		/**
		 * 图片
		 */
		IMAGE("image"),
		/**
		 * 音频
		 */
		AUDIO("audio"),
		/**
		 * 视频
		 */
		VIDEO("video"),
		/**
		 * 下载
		 */
		DOWNLOAD("download"),
		/**
		 * 缩略图
		 */
		THUMB_NAIL("thumbnail"),

		/**
		 * 系统相册的目录
		 */
		DCIM("DCIM/raul"),

		/**
		 * 新浪微博
		 */
		MBLOG_SINA("mblog/sina");

		private String value;

		private LocalDirType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static String getHomeDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory() + "/raul/";
		}

		return null;
	}

	/**
	 * 获取日志路径，需要有SDCard才可以
	 * 
	 * @return Log文件所在的文件夹路径
	 */
	public static String getMyLogDir() {
		if (getHomeDir() != null) {
			return getHomeDir() + "log/";
		}

		return null;
	}

	/**
	 * 获取日志路径，需要有SDCard才可以
	 * 
	 * @return Log文件所在的文件夹路径
	 */
	public static String getMyImageCacheDir() {
		if (getHomeDir() != null) {
			return getHomeDir() + "cache/.nomedia";
		}

		return null;
	}

	/**
	 * 获取本地存储目录
	 * 
	 * @param userAccount
	 * @param dirType
	 *            目录类型
	 * @return 本地存储目录
	 */
	public static String getLocalStorageDir(String userAccount,
			LocalDirType dirType) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(Environment.getExternalStorageDirectory().getPath());
		buffer.append("/sunivo/");
		buffer.append(String.valueOf(userAccount));
		buffer.append("/");
		buffer.append(dirType.getValue());
		buffer.append("/");
		return buffer.toString();
	}

	public static String getLocalSendWeiboDir(String userAccount,
			LocalDirType dirType) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(Environment.getExternalStorageDirectory().getPath());
		buffer.append("/sunivo/");
		buffer.append(String.valueOf(userAccount));
		buffer.append("/");
		buffer.append(dirType.getValue());
		buffer.append("/");
		buffer.append("pre/sendImg.png");
		return buffer.toString();
	}

	/**
	 * 
	 * @return 本地存储目录
	 */
	public static String getDcimStorgeDir() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(Environment.getExternalStorageDirectory().getPath());
		buffer.append("/");
		buffer.append(LocalDirType.DCIM.getValue());
		buffer.append("/");
		return buffer.toString();
	}

	/**
	 * 获取ip地址和端口
	 * 
	 * @param httpUrl
	 *            标准地址串 格式 http://192.168.9.104:5222
	 * @return strs数组 1.ip 2.port
	 */
	public static String[] resolveHttpUrl(String httpUrl) {
		if (httpUrl == null) {
			Log.e(TAG, "httpUrl is null");
			throw new NullPointerException("httpUrl is null");
		}
		String[] strs = new String[2];

		try {
			String ip = httpUrl.substring(7, httpUrl.lastIndexOf(":"));
			strs[0] = ip;
			String port = httpUrl.substring(httpUrl.lastIndexOf(":") + 1);
			strs[1] = port;
		} catch (Exception e) {
			Log.e(TAG, "wrong http url form");
			Log.e(TAG, "http :" + httpUrl);
		}
		return strs;
	}

	/**
	 * 获得用户手动保存图片的文件名
	 * 
	 * @return 用户手动保存图片的文件名
	 */
	public static String getSavedImageFileName() {
		return "sunivo_" + MyDateFormat.TIMESTAMP_DF.format(new Date())
				+ ".jpg";
	}

	/**
	 * 全局时间日期格式
	 * 
	 */
	public interface MyDateFormat {
		/**
		 * 时间戳格式
		 */
		SimpleDateFormat TIMESTAMP_DF = new SimpleDateFormat("yyyyMMddHHmmss");

		/**
		 * 生日格式
		 */
		SimpleDateFormat BIRTHDAY_DF = new SimpleDateFormat("yyyy-MM-dd");
	}

}
