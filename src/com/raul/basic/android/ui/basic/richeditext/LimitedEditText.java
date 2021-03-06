package com.raul.basic.android.ui.basic.richeditext;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import com.raul.basic.android.util.StringUtil;

/**
 * 输入限制EditText 最多输入多少个字符，一个汉字对应两个字符。在xml中通过属性名为"maxCharLength"进行配置
 * 
 * @author xiaomiaomiao
 * 
 */
public class LimitedEditText extends EditText {

	/**
	 * 属性名
	 */
	private static final String ATTRIBUTE_MAX_CHAR_LENGTH = "maxCharLength";

	private static Context mContext;

	/**
	 * 构造方法，限定允许最大输入字符长度
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public LimitedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		int maxCharLen = attrs.getAttributeUnsignedIntValue(null,
				ATTRIBUTE_MAX_CHAR_LENGTH, -1);
		if (maxCharLen > 0) {
			setFilters(new InputFilter[] { new CharLengthFilter(maxCharLen) });
		}
	}

	public LimitedEditText(Context context) {
		super(context);
		mContext = context;
	}

	public LimitedEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	/**
	 * 
	 * 设置最大字符数
	 * 
	 * @param maxCharLength
	 *            maxCharLength
	 */
	public void setMaxCharLength(int maxCharLength) {
		setFilters(new InputFilter[] { new CharLengthFilter(maxCharLength) });
	}

	/**
	 * This filter will constrain edits not to make the length of the text
	 * greater than the specified length.
	 */
	public static class CharLengthFilter implements InputFilter {

		private int mMax;

		public CharLengthFilter(int max) {
			mMax = max;
		}

		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			int destLen = StringUtil.count2BytesChar(dest.toString());
			int keep = mMax - (destLen - (dend - dstart));
			int srcLen = StringUtil.count2BytesChar(source.toString());
			if (keep <= 0) {
				return "";
			} else if (keep >= srcLen) {
				return null; // keep original
			} else {
				StringBuffer buffer = new StringBuffer();
				int cnt = 0;
				for (int i = start; i < end; i++) {
					char c = source.charAt(i);
					cnt++;

					// 如果为汉字
					if (c >= 256) {
						cnt++;
					}
					// 表情符号以 "[" 开头
					else if (c == '[') {
						StringBuffer emotionStrBuf = new StringBuffer();
						int j = i + 1;
						int tempStrLen = 0;
						// 找到"[]"中内容
						for (; j < end; j++) {
							tempStrLen++;
							char temp = source.charAt(j);

							// 如果中括号中文字为汉字，算两个字符
							if (temp >= 256) {
								tempStrLen++;
							} else if (temp == ']') {
								// 找到结束符],退出本次循环
								break;
							}
							emotionStrBuf.append(temp);

						}

						// 若找到了[]中内容是否为表情
						if (j < end
								&& ChatTextParser
										.getInstance(mContext)
										.getEmotionPattern()
										.matcher(
												"[" + emotionStrBuf.toString()
														+ "]").matches()) {
							// 若为表情，总字数需要加上表情所占长度(长度中不包括[)
							cnt = cnt + tempStrLen;
							if (cnt <= keep) {
								// 如果没有超过限制，则把表情加入字符串中，继续进行循环
								buffer.append("[");
								buffer.append(emotionStrBuf.toString());
								buffer.append("]");
								i = j;
								continue;
							} else {
								// 若为表情，而且字符数已经超过限制，跳出循环
								break;
							}
						}
					}

					// 判断字符数如果没有超过，则添加到字符串中，若超过则跳出循环
					if (cnt <= keep) {
						buffer.append(c);
					} else {
						break;
					}
				}
				return buffer.toString();
			}

		}
	}

}
