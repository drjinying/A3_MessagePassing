package DevicePackage;

import MessagePackage.MessageConstants;

public class HumidityController extends AbstractDevice{

	public HumidityController() {
		this.deviceConfig = new DeviceConfig(
				0.0f,0.6f,
				"Humidity Controller","id","A device which can turns the humidity device on and off",
				MessageConstants.HUMI_CTRL,MessageConstants.HUMI_SENSOR,
				"Humidifier",MessageConstants.HUMI_ON,MessageConstants.HUMI_OFF,
				"Dehumidifier",MessageConstants.DEHUMI_ON,MessageConstants.DEHUMI_OFF
		);
	}
	
	public static void main(String[] args) {
		HumidityController humiController = new HumidityController();
		humiController.run();
	}
}
