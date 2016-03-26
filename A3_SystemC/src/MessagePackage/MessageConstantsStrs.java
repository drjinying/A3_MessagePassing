package MessagePackage;

/*
 * The MessageConstants class is used for storing all constant values related to message
 * like message IDs, action code, etc
 * Those variables should be used system wide instead of magic numbers
 * By doing so, we promote maintainability, readability and changeability
 * */
public class MessageConstantsStrs {
	static class ID {
		/*
		 * For a string representation of the floating point ambient room
		 * temperature in degrees Fahrenheit
		 * Used in: ECSConsole
		 */
		public static final String CSL_TEMP = "1";
		/*
		 * For a string representation of the floating point ambient room
		 * humidity in percentage relative humidity
		 * Used in: ECSConsole
		 */
		public static final String CSL_HUMI = "2";
		/*
		 * For the Action Codes of Temperature Sensor
		 */
		public static final String TEMP_SENSOR = "-5";
		/*
		 * FOr the Action Code of Temperature Controller
		 */
		public static final String TEMP_CTRL = "5";
		/*
		 * For the Action Codes of Humidity Sensor
		 */
		public static final String HUMI_SENSOR = "-4";
		/*
		 * For the Action Codes of Humidity Controller
		 */
		public static final String HUMI_CTRL = "4";
	}
	static class ActionCode {
		/*
		 * The confirmation codes of Temperature Sensor
		 */
		
		// Confirmation turn on the heater
		public static final String HEATER_ON_CONF = "H1";
		// Confirmation turn off the heater
		public static final String HEATER_OFF_CONF = "H0";
		// Confirmation turn on the chiller
		public static final String CHILLER_ON_CONF = "C1";
		// Confirmation turn off the chiller
		public static final String CHILLER_OFF_CONF = "C0";
		
		/*
		 * The confirmation codes of Humidity Sensor
		 */
		
		// Confirmation turn on the heater
		public static final String HUMI_ON_CONF = "H1";
		// Confirmation turn off the heater
		public static final String HUMI_OFF_CONF = "H0";
		// Confirmation turn on the chiller
		public static final String DEHUMI_ON_CONF = "D1";
		// Confirmation turn off the chiller
		public static final String DEHUMI_OFF_CONF = "D0";
		
		/*
		 * The action codes of Humidity Controller
		 */
		
		// turn on the humidifier
		public static final String HUMI_ON = "H1";
		// turn off the humidifier
		public static final String HUMI_OFF = "H0";
		// turn on the dehumidifier
		public static final String DEHUMI_ON = "D1";
		// turn off the dehumidifier
		public static final String DEHUMI_OFF = "D0";
		
		/*
		 * The action codes of Temperature Controller
		 */
		
		// turn on the heater
		public static final String HEATER_ON = "H1";
		// turn off the heater
		public static final String HEATER_OFF = "H0";
		// turn on the chiller
		public static final String CHILLER_ON = "C1";
		// turn off the chiller
		public static final String CHILLER_OFF = "C0";
		
	}
}
