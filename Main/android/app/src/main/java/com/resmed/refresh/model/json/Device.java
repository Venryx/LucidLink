package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class Device {
	@SerializedName("AppVersion")
	private String appVersion;
	@SerializedName("DeviceId")
	private String deviceId;
	@SerializedName("DeviceModel")
	private String deviceModel;
	@SerializedName("DeviceName")
	private String deviceName;
	@SerializedName("DeviceOS")
	private String deviceOS;
	@SerializedName("DeviceOSVersion")
	private String deviceOSVersion;

	public String getAppVersion() {
		return this.appVersion;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public String getDeviceModel() {
		return this.deviceModel;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public String getDeviceOS() {
		return this.deviceOS;
	}

	public String getDeviceOSVersion() {
		return this.deviceOSVersion;
	}
}