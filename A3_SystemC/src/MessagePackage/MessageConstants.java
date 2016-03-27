package MessagePackage;
/*
 * XXX Sensor--> (XXX_DATA+float value)
 * 				 -->Monitor-->(XXX_CTRL+XXX_ON/XXX_OFF)
 * 					-->XXX Controller-->(XXX_CONF+XXX_ON/XXX_OFF)
 * XXX Sensor<--
 */
public enum MessageConstants {	
	TEMP_DATA,			// 1 Message ID of a temperature float value in degrees Fahrenheit
						// Sent by Temperature Sensor, Read by ECSMonitor
	
	HUMI_DATA,			// 2 Message ID of a Humidity float value in percentage
						// Sent by Humidity Sensor, Read by ECSMonitor
	
	HUMI_CTRL,			// 4 Message ID of a command to turn ON or OFF the Humidifier/Dehumidifier
						// Sent by ECSMonitor, Read by Humidity Controller
	
	TEMP_CTRL,			// 5 Message ID of a command to turn ON or OFF the Heater/Chiller
						// Sent by ECSMonitor, Read by Humidity Controller
	
	HUMI_CONF,			// -4 Message ID of a confirmation of turning ON or OFF the Humidifier/Dehumidifier
						// Sent by Humidity Controller, Read by Humidity Sensor
	
	TEMP_CONF,			// -5 Message ID of a confirmation of turning ON or OFF the Heater/Chiller
						// Sent by Temperature Controller, Read by Temperature Sensor
	
	HUMI_ON,			// H1 Message body: turn on the humidifier
	HUMI_OFF,			// H0 Message body: turn off the humidifier
	DEHUMI_ON,			// D1 Message body: turn on the dehumidifier
	DEHUMI_OFF,			// D0 Message body: turn off the dehumidifier
	
	HEATER_ON,			// H1 Message body: turn on the heater
	HEATER_OFF,			// H0 Message body: turn off the heater
	CHILLER_ON,			// C1 Message body: turn on the chiller
	CHILLER_OFF,		// C0 Message body: turn off the chiller

	DEVICE_STOP,		// 99 Tells a device to stop
}
