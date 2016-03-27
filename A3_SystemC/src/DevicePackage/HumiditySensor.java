package DevicePackage;

import MessagePackage.MessageConstants;

public class HumiditySensor extends AbstractDevice{

	public HumiditySensor() {
		this.deviceConfig = new DeviceConfig(
				0.5f,0.6f,
				"Humidity Sensor","id","A sensor of humidity",
				"Relative Humidity", "%",
				MessageConstants.HUMI_DATA,MessageConstants.HUMI_CONF,
				MessageConstants.HUMI_ON, MessageConstants.HUMI_OFF,
				MessageConstants.DEHUMI_ON, MessageConstants.DEHUMI_OFF
		);
	}
	
	public static void main(String[] args) {
		HumiditySensor humiditySensor = new HumiditySensor();
		humiditySensor.run();
	}
}
