package MessagePackage;

public enum MessageConstants {
	CSL_TEMP,			// ECSConsole flag: temperature in degrees Fahrenheit
	CSL_HUMI,			// ECSConsole flag: humidity in percentage relative humidity

	TEMP_SENSOR,		// Temperature Sensor
	HEATER_ON_CONF,		// Temperature Sensor: turn on the heater
	HEATER_OFF_CONF,	// Temperature Sensor: turn off the heater
	CHILLER_ON_CONF,	// Temperature Sensor: turn on the chiller
	CHILLER_OFF_CONF,	// Temperature Sensor: turn off the chiller
	
	TEMP_CTRL,			// Temperature Controller
	HEATER_ON,			// Temperature Controller: turn on the heater
	HEATER_OFF,			// Temperature Controller: turn off the heater
	CHILLER_ON,			// Temperature Controller: turn on the chiller
	CHILLER_OFF,		// Temperature Controller: turn off the chiller
	
	HUMI_SENSOR,		// Humidity Sensor
	HUMI_ON_CONF,		// Humidity Sensor: turn on the heater
	HUMI_OFF_CONF,		// Humidity Sensor: turn off the heater
	DEHUMI_ON_CONF,		// Humidity Sensor: turn on the chiller
	DEHUMI_OFF_CONF,	// Humidity Sensor: turn off the chiller
	
	HUMI_CTRL,			// Humidity Controller
	HUMI_ON,			// Humidity Controller: turn on the humidifier
	HUMI_OFF,			// Humidity Controller: turn off the humidifier
	DEHUMI_ON,			// Humidity Controller: turn on the dehumidifier
	DEHUMI_OFF			// Humidity Controller: turn off the dehumidifier
}
