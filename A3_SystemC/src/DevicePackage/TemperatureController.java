package DevicePackage;

import MessagePackage.MessageConstants;

public class TemperatureController extends AbstractDevice{

	public TemperatureController() {
		this.deviceConfig = new DeviceConfig(
				0.0f,0.3f,
				"Temperature Controller","id","Temperature Controller: controlls heater and chiller",
				"Temperature","F",
				MessageConstants.TEMP_CONF,MessageConstants.TEMP_CTRL,
				"Heater",MessageConstants.HEATER_ON,MessageConstants.HEATER_OFF,
				"Chiller",MessageConstants.CHILLER_ON,MessageConstants.CHILLER_OFF
		);
	}
	
	public static void main(String[] args) {
		TemperatureController temperatureController = new TemperatureController();
		temperatureController.run();
	}
}
