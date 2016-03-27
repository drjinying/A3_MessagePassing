package DevicePackage;

import MessagePackage.MessageConstants;

public class DeviceConfig {
	float winPosX;
	float winPosY;
	String deviceName;
	String deviceID;
	String deviceDesc;
	String metricName;
	String metricUnit;
	String valueRaiserName;
	MessageConstants valueRaiserOnCmd;
	MessageConstants valueRaiserOffCmd;
	String valueDropperName;
	MessageConstants valueDropperOnCmd;
	MessageConstants valueDropperOffCmd;
	MessageConstants sendMsgId;
	MessageConstants readMsgId;
	boolean isController;
	
	// for constructing a sensor
	public DeviceConfig (float winPosX, float winPosY, 
			String sensorName, String deviceID, String deviceDesc, String metricName, String metricUnit, 
			MessageConstants sendMsgId, MessageConstants readMsgId) 
	{
		this.winPosX = winPosX;
		this.winPosY = winPosY;
		this.deviceName = sensorName;
		this.deviceID = deviceID;
		this.deviceDesc = deviceDesc;
		this.metricName = metricName;
		this.sendMsgId = sendMsgId;
		this.readMsgId = readMsgId;
		this.isController = false;
	}
	// for constructing a controller
	public DeviceConfig (float winPosX, float winPosY, 
			String deviceName, String deviceID, String deviceDesc, MessageConstants sendMsgId, MessageConstants readMsgId,
			String valueRaiserName, MessageConstants valueRaiserOnCmd, MessageConstants valueRaiserOffCmd,
			String valueDropperName, MessageConstants valueDropperOnCmd, MessageConstants valueDropperOffCmd) 
	{
		this.winPosX = winPosX;
		this.winPosY = winPosY;
		this.deviceName = deviceName;
		this.deviceID = deviceID;
		this.deviceDesc = deviceDesc;
		this.valueRaiserName = valueRaiserName;
		this.valueDropperName = valueDropperName;
		this.valueRaiserOnCmd = valueRaiserOnCmd;
		this.valueRaiserOffCmd = valueRaiserOffCmd;
		this.valueDropperOnCmd = valueDropperOnCmd;
		this.valueDropperOffCmd = valueDropperOffCmd;
		this.sendMsgId = sendMsgId;
		this.readMsgId = readMsgId;
		this.isController = true;
	}
}
