package com.raul.basic.android.ui.basic.actionbar;

public class ActionBarMenuItem {

	/**
	 * memu的id
	 */
	private int id;

	/**
	 * menu背景的resid
	 */
	private int drawableId;

	/**
	 * menu标题的resid
	 */
	private int menuTitle;

	/**
	 * menu的可用性
	 */
	private boolean menuEnabled;

	/**
	 * 构造方法
	 * 
	 * @param menuItemId
	 *            memu的id
	 * @param iconId
	 *            menu背景的resid
	 */
	public ActionBarMenuItem(int menuItemId, int iconId) {
		this(menuItemId, iconId, -1);
	}

	/**
	 * 构造方法
	 * 
	 * @param menuItemId
	 *            memu的id
	 * @param iconId
	 *            menu背景的resid
	 * @param titleId
	 *            menu标题的resid
	 */
	public ActionBarMenuItem(int menuItemId, int iconId, int titleId) {
		this(menuItemId, iconId, titleId, true);
	}

	/**
	 * 构造方法
	 * 
	 * @param menuItemId
	 *            memu的id
	 * @param iconId
	 *            menu背景的resid
	 * @param titleId
	 *            menu标题的resid
	 * @param enabled
	 *            menu标题的可用性
	 */
	public ActionBarMenuItem(int menuItemId, int iconId, int titleId,
			boolean enabled) {
		id = menuItemId;
		drawableId = iconId;
		menuTitle = titleId;
		menuEnabled = enabled;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 获取drawable资源id
	 * 
	 * @return the drawableId
	 */
	public int getDrawableId() {
		return drawableId;
	}

	/**
	 * 设置drawable资源id
	 * 
	 * @param drawableId
	 *            the drawableId to set
	 */
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	/**
	 * 获取title资源id
	 * 
	 * @return the menuTitle
	 */
	public int getMenuTitle() {
		return menuTitle;
	}

	/**
	 * 设置title资源id
	 * 
	 * @param menuTitle
	 *            the menuTitle to set
	 */
	public void setMenuTitle(int menuTitle) {
		this.menuTitle = menuTitle;
	}

	/**
	 * 获取menu标题的可用性
	 * 
	 * @return menu标题的可用性
	 */
	public boolean isEnabled() {
		return menuEnabled;
	}

	/**
	 * 设置menu标题的可用性
	 * 
	 * @param rnabled
	 *            menu标题的可用性
	 */
	public void setEnabled(boolean rnabled) {
		menuEnabled = rnabled;
	}

}
