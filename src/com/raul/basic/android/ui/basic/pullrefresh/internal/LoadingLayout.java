package com.raul.basic.android.ui.basic.pullrefresh.internal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raul.basic.android.R;

/**
 * 加载布局类
 * 
 * @author xiaomiaomiao
 * 
 */
public class LoadingLayout extends FrameLayout {
	/**
	 * 默认的旋转动画间隔
	 */
	private static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	/**
	 * 头部的图片
	 */
	private final ImageView mHeaderImage;

	/**
	 * 加载的progress
	 */
	private final ProgressBar mHeaderProgress;

	/**
	 * 头部的text,显示pull or release
	 */
	private final TextView mHeaderText;

	/**
	 * 显示上次更新的时间
	 */
	private TextView mHeaderTimeText;

	/**
	 * 显示正在刷新的提示
	 */
	private TextView mHeaderRefreshingText;

	/**
	 * 下拉的标签
	 */
	private String mPullLabel;

	/**
	 * 刷新的标签文字
	 */
	private String mRefreshingLabel;

	/**
	 * 释放的标签文字
	 */
	private String mReleaseLabel;

	/**
	 * 箭头翻转动画
	 */
	private final Animation mRotateAnimation;

	/**
	 * 重置箭头动画
	 */
	private final Animation mResetRotateAnimation;

	/**
	 * 加载布局的构造函数
	 * 
	 * @param context
	 *            Context
	 * @param releaseLabel
	 *            释放时文字
	 * @param pullLabel
	 *            下拉时文字
	 * @param refreshingLabel
	 *            刷新时文字
	 */
	public LoadingLayout(Context context, String releaseLabel,
			String pullLabel, String refreshingLabel) {
		super(context);
		// 初始化view
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header, this);

		mHeaderText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
		mHeaderTimeText = (TextView) header
				.findViewById(R.id.pull_to_refresh_time);
		mHeaderRefreshingText = (TextView) header
				.findViewById(R.id.pull_to_refreshing);
		mHeaderImage = (ImageView) header
				.findViewById(R.id.pull_to_refresh_image);
		mHeaderProgress = (ProgressBar) header
				.findViewById(R.id.pull_to_refresh_progress);

		// 初始化下拉箭头的动画
		final Interpolator interpolator = new LinearInterpolator();
		mRotateAnimation = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(interpolator);
		mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mResetRotateAnimation.setInterpolator(interpolator);
		mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

		mReleaseLabel = releaseLabel;
		mPullLabel = pullLabel;
		mRefreshingLabel = refreshingLabel;
		reset();

	}

	/**
	 * 重置下拉刷新header
	 */
	public void reset() {
		mHeaderText.setText(mPullLabel);
		mHeaderText.setVisibility(View.VISIBLE);
		mHeaderTimeText.setVisibility(View.VISIBLE);
		mHeaderRefreshingText.setVisibility(View.GONE);
		mHeaderImage.setVisibility(View.VISIBLE);
		mHeaderProgress.setVisibility(View.GONE);
	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		mHeaderText.setText(mPullLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mResetRotateAnimation);
	}

	/**
	 * 
	 * 释放刷新
	 */
	public void releaseToRefresh() {
		mHeaderText.setText(mReleaseLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	/**
	 * 重新刷新
	 */
	public void refreshing() {
		mHeaderText.setVisibility(View.GONE);
		mHeaderTimeText.setVisibility(View.GONE);
		mHeaderRefreshingText.setVisibility(View.VISIBLE);
		mHeaderRefreshingText.setText(mRefreshingLabel);

		mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置重刷新的列头文字
	 * 
	 * @param refreshingLabel
	 *            列头文字
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		mHeaderRefreshingText.setText(refreshingLabel);
	}

	/**
	 * 
	 * 设置上次更新时间 这个方法需要在初始化以及结束刷新的时候调用一下
	 * 
	 * @param time
	 *            上次更新的时间
	 */
	public void setHeaderTime(String time) {
		mHeaderTimeText.setText(time);
	}

}
