package MessagePackage;

public enum MessageConstants {
	CSL_TEMP,			// 1 ECSConsole flag: temperature in degrees Fahrenheit
	CSL_HUMI,			// 2 ECSConsole flag: humidity in percentage relative humidity

	TEMP_SENSOR,		// -5 Temperature Sensor
	HEATER_ON_CONF,		// H1 Temperature Sensor: turn on the heater
	HEATER_OFF_CONF,	// H0 Temperature Sensor: turn off the heater
	CHILLER_ON_CONF,	// C1 Temperature Sensor: turn on the chiller
	CHILLER_OFF_CONF,	// C0 Temperature Sensor: turn off the chiller
	
	TEMP_CTRL,			// 5 Temperature Controller
	HEATER_ON,			// H1 Temperature Controller: turn on the heater
	HEATER_OFF,			// H0 Temperature Controller: turn off the heater
	CHILLER_ON,			// C1 Temperature Controller: turn on the chiller
	CHILLER_OFF,		// C0 Temperature Controller: turn off the chiller
	
	HUMI_SENSOR,		// -4 Humidity Sensor
	HUMI_ON_CONF,		// H1 Humidity Sensor: turn on the humidifier
	HUMI_OFF_CONF,		// H0 Humidity Sensor: turn off the humidifier
	DEHUMI_ON_CONF,		// D1 Humidity Sensor: turn on the dehumidifier
	DEHUMI_OFF_CONF,	// D0 Humidity Sensor: turn off the dehumidifier
	
	HUMI_CTRL,			// 4 Humidity Controller
	HUMI_ON,			// H1 Humidity Controller: turn on the humidifier
	HUMI_OFF,			// H0 Humidity Controller: turn off the humidifier
	DEHUMI_ON,			// D1 Humidity Controller: turn on the dehumidifier
	DEHUMI_OFF,			// D0 Humidity Controller: turn off the dehumidifier
	
	DEVICE_STOP,		// 99 Tells a device to stop (originally it was value 99)
	
	INVALID
}
