package DevicePackage;

import java.util.Random;

import InstrumentationPackage.*;
import MessagePackage.*;

public abstract class ECSDevice {
	// fields for sensor
	float metricValue;			// Current simulated metric value(like humidity)
	float driftValue;			// The amount of metric value gained or lost
	
	private static final boolean ON  = true;
	private static final boolean OFF  = false;
	
	// valueRaiser: the device which raises the monitored value
	// like a heater
	boolean valueRaiserState = OFF;
	Indicator valueRaiserIndc;
	
	// valueDropper: the device which decreases the monitored value
	// like a chiller
	boolean valueDropperState = OFF;
	Indicator valueDropperIndc;

	private String msgMgrIP = null;
	private ECSDeviceConfig deviceConfig;
	
	// for initializing a sensor or a controller
	public ECSDevice(ECSDeviceConfig config) {
		deviceConfig = config;
	}
	
	public ECSDevice(String msgMgrIP, ECSDeviceConfig config) {
		this.msgMgrIP = msgMgrIP;
		deviceConfig = config;
	}
	
	public void main() {
		Message msg = null;							// Message object
		MessageQueue msgQue = null;					// Message Queue
		MessageManagerInterface msgMgrIntf = null;	// Interface object to the message manager
		final int	Delay = 2500;							// The loop delay (2.5 seconds)
		boolean Done = false;						// Loop termination flag

		// message manager is on the local system
 		if (msgMgrIP == null) {
			System.out.println("\n\nAttempting to register on the local machine..." );
			try {
				msgMgrIntf = new MessageManagerInterface();
			} catch (Exception e) {
				System.out.println("Error instantiating message manager interface: " + e);
			}
		} else {
			System.out.println("\n\nAttempting to register on the machine:: " + msgMgrIP);
			try {
				msgMgrIntf = new MessageManagerInterface(msgMgrIP);
			} catch (Exception e) {
				System.out.println("Error instantiating message manager interface: " + e);
			}
		}
		// check registration
		if (msgMgrIntf != null) {
			System.out.println("Registered with the message manager." );
			MessageWindow mw = new MessageWindow(deviceConfig.deviceName + " Status Console", deviceConfig.winPosX, deviceConfig.winPosY);

			if (deviceConfig.isController) {
				valueRaiserIndc = new Indicator (deviceConfig.valueDropperName + " OFF", mw.GetX(), mw.GetY()+mw.Height());
				valueDropperIndc = new Indicator (deviceConfig.valueRaiserName + " OFF", mw.GetX()+(valueRaiserIndc.Width()*2), mw.GetY()+mw.Height());
			}
			
			mw.WriteMessage("Registered with the message manager." );
	    	try {
				mw.WriteMessage("   Participant id: " + msgMgrIntf.GetMyId() );
				mw.WriteMessage("   Registration Time: " + msgMgrIntf.GetRegistrationTime() );
			} catch (Exception e) {
				System.out.println("Error:: " + e);
			}

	    	if (!deviceConfig.isController) {
				mw.WriteMessage("\nInitializing Humidity Simulation::" );
				metricValue = GetRandomNumber() * (float) 100.00;
				if (CoinToss()) {
					driftValue = GetRandomNumber() * (float) -1.0;
				} else {
					driftValue = GetRandomNumber();
				}
				mw.WriteMessage("   Initial " + deviceConfig.metricName + " Set:: " + metricValue );
	    	}

			while (!Done) {
				
				if (!deviceConfig.isController) {
					PostFloat(msgMgrIntf, metricValue);
					mw.WriteMessage("Current " + deviceConfig.metricName + ":: " + metricValue + deviceConfig.metricUnit);
				}
				
				try {
					msgQue = msgMgrIntf.GetMessageQueue();
				} catch(Exception e) {
					mw.WriteMessage("Error getting message queue::" + e);
				}

				int qlen = msgQue.GetSize();
				for (int i = 0; i < qlen; i++) {
					msg = msgQue.GetMessage();
					if (msg.GetMessageId() == deviceConfig.readMsgId) {
						String printContent = "";
						if (msg.GetMessage().equals(deviceConfig.valueRaiserOnCmd)) {
							printContent = "Received " + deviceConfig.valueRaiserName + " on message";
							valueRaiserState = ON;
						} else if (msg.GetMessage().equals(deviceConfig.valueRaiserOffCmd)) {
							printContent = "Received " + deviceConfig.valueRaiserName + " off message";
							valueRaiserState = OFF;
						} else if (msg.GetMessage().equals(deviceConfig.valueDropperOnCmd)) {
							printContent = "Received " + deviceConfig.valueDropperName + " on message"; 
							valueDropperState = ON;
						} else if (msg.GetMessage().equals(deviceConfig.valueDropperOffCmd)) {
							printContent = "Received " + deviceConfig.valueDropperName + " off message";
							valueDropperState = OFF;
						}
						
						if (deviceConfig.isController) {
							mw.WriteMessage(printContent);
							ConfirmMessage(msgMgrIntf, msg.GetMessage());
						}						
					}

					if (msg.GetMessageId() == MessageConstants.DEVICE_STOP){
						Done = true;
						try {
							msgMgrIntf.UnRegister();
				    	} catch (Exception e) {
							mw.WriteMessage("Error unregistering: " + e);
				    	}
				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");		
				    	if (deviceConfig.isController) {
				    		valueRaiserIndc.dispose();
				    		valueDropperIndc.dispose();
				    	}						
					}
				}
				
				if (deviceConfig.isController) {
					valueRaiserIndc.SetLampColorAndMessage(deviceConfig.valueRaiserName + " " + (valueRaiserState ? "ON" : "OFF"), (valueRaiserState ? 1 : 0));
					valueDropperIndc.SetLampColorAndMessage(deviceConfig.valueDropperName + " " + (valueDropperState ? "ON" : "OFF"), (valueDropperState ? 1 : 0));
				} else {
					if (valueRaiserState) {
						metricValue += GetRandomNumber();
					}
					if (!valueRaiserState && !valueDropperState) {
						metricValue += driftValue;
					}
					if (valueDropperState){
						metricValue -= GetRandomNumber();
					}
				}

				try {
					Thread.sleep(Delay);
				} catch(Exception e) {
					System.out.println( "Sleep error:: " + e );
				}
			}
		} else {
			System.out.println("Unable to register with the message manager.\n\n" );
		}
	}
	
	private void ConfirmMessage(MessageManagerInterface ei, String m){
		Message msg = new Message(deviceConfig.sendMsgId, m);
		try {
			ei.SendMessage(msg);
		} catch (Exception e) {
			System.out.println("Error Confirming Message:: " + e);
		}
	}
	
	private float GetRandomNumber(){
		Random r = new Random();
		float val = r.nextFloat();
		// map [0.0 - 1] to [0.1 - 1]
		return (float) (val * 0.9 + 0.1);
	}

	private boolean CoinToss(){
		return new Random().nextBoolean();
	}

	private void PostFloat(MessageManagerInterface ei, float metricValue ){
		Message msg = new Message( deviceConfig.sendMsgId, String.valueOf(metricValue) );
		try {
			ei.SendMessage(msg);
		} catch (Exception e) {
			System.out.println( "Error Posting " + deviceConfig.metricName + ":: " + e );
		}
	}
}
