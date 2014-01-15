package com.raul.basic.android.component.location;

import android.location.Location;

/**
 * 
 * 定位信息实体类
 * 
 * @author raulxiao
 */
public class LocationInfo {
	/**
	 * 位置经纬度
	 */
	private Location location;

	/**
	 * 具体的位置信息
	 */
	private String addressInfo;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}

}
