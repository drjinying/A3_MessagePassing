package DevicePackage;

import MessagePackage.MessageConstants;

public class TemperatureSensor extends AbstractDevice{

	public TemperatureSensor() {
		this.deviceConfig = new DeviceConfig(
				0.5f,0.3f,
				"Temperature Sensor","id","A sensor of temperature",
				"Temperature", "F", 
				MessageConstants.TEMP_DATA,MessageConstants.TEMP_CONF,
				MessageConstants.HEATER_ON, MessageConstants.HEATER_OFF,
				MessageConstants.CHILLER_ON, MessageConstants.CHILLER_OFF
		);
	}
	
	public static void main(String[] args) {
		TemperatureSensor temperatureSensor = new TemperatureSensor();
		temperatureSensor.run();
	}
}
