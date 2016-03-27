/******************************************************************************************************************
* File:ECSMonitor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class monitors the environmental control systems that control museum temperature and humidity. In addition to
* monitoring the temperature and humidity, the ECSMonitor also allows a user to set the humidity and temperature
* ranges to be maintained. If temperatures exceed those limits over/under alarm indicators are triggered.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	static private void Heater(MessageManagerInterface ei, boolean ON )
*	static private void Chiller(MessageManagerInterface ei, boolean ON )
*	static private void Humidifier(MessageManagerInterface ei, boolean ON )
*	static private void Dehumidifier(MessageManagerInterface ei, boolean ON )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class ECSMonitor extends Thread
{
	private MessageManagerInterface em = null;	// Interface object to the message manager
	private String MsgMgrIP = null;				// Message Manager IP address
	private float TempRangeHigh = 100;			// These parameters signify the temperature and humidity ranges in terms
	private float TempRangeLow = 0;				// of high value and low values. The ECSmonitor will attempt to maintain
	private float HumiRangeHigh = 100;			// this temperature and humidity. Temperatures are in degrees Fahrenheit
	private float HumiRangeLow = 0;				// and humidity is in relative humidity percentage.
	boolean Registered = true;					// Signifies that this class is registered with an message manager.
	MessageWindow mw = null;					// This is the message window
	Indicator ti;								// Temperature indicator
	Indicator hi;								// Humidity indicator
	
	// message manager is on the local system
	public ECSMonitor(){
		try{
			em = new MessageManagerInterface();
		}catch (Exception e){
			System.out.println("ECSMonitor::Error instantiating message manager interface: " + e);
			Registered = false;
		}
	}
	// message manager is not on the local system
	public ECSMonitor(String MsgIpAddress){
		MsgMgrIP = MsgIpAddress;
		try{
			em = new MessageManagerInterface( MsgMgrIP );
		} catch (Exception e){
			System.out.println("ECSMonitor::Error instantiating message manager interface: " + e);
			Registered = false;
		}
	}

	public void run(){
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		float CurrentTemperature = 0;	// Current temperature as reported by the temperature sensor
		float CurrentHumidity= 0;		// Current relative humidity as reported by the humidity sensor
		int	Delay = 1000;				// The loop delay (1 second)
		boolean Done = false;			// Loop termination flag
		boolean ON = true;				// Used to turn on heaters, chillers, humidifiers, and dehumidifiers
		boolean OFF = false;			// Used to turn off heaters, chillers, humidifiers, and dehumidifiers

		if (em != null){
			mw = new MessageWindow("ECS Monitoring Console", 0, 0);
			ti = new Indicator ("TEMP UNK", mw.GetX()+ mw.Width(), 0);
			hi = new Indicator ("HUMI UNK", mw.GetX()+ mw.Width(), (int)(mw.Height()/2), 2 );
			mw.WriteMessage( "Registered with the message manager." );

	    	try {
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );
			} catch (Exception e){
				System.out.println("Error:: " + e);
			}

			while ( !Done ){
				try{
					eq = em.GetMessageQueue();
				} catch( Exception e ){
					mw.WriteMessage("Error getting message queue::" + e );
				}

				int qlen = eq.GetSize();
				for ( int i = 0; i < qlen; i++ ){
					Msg = eq.GetMessage();
					if ( Msg.GetMessageId() == MessageConstants.TEMP_DATA){
						try{
							CurrentTemperature = Float.valueOf(Msg.GetMessage()).floatValue();
						} catch( Exception e ) {
							mw.WriteMessage("Error reading temperature: " + e);
						}
					} 
					if ( Msg.GetMessageId() == MessageConstants.HUMI_DATA){
						try {
							CurrentHumidity = Float.valueOf(Msg.GetMessage()).floatValue();
						} catch( Exception e ){
							mw.WriteMessage("Error reading humidity: " + e);
						}
					}
					if ( Msg.GetMessageId() == MessageConstants.DEVICE_STOP){
						Done = true;
						try {
							em.UnRegister();
				    	} catch (Exception e){
							mw.WriteMessage("Error unregistering: " + e);
				    	}
				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");
						hi.dispose();
						ti.dispose();
					}
				}
				mw.WriteMessage("Temperature:: " + CurrentTemperature + "F  Humidity:: " + CurrentHumidity );

				// Check temperature and effect control as necessary
				if (CurrentTemperature < TempRangeLow){
					ti.SetLampColorAndMessage("TEMP LOW", 3);
					Heater(ON);
					Chiller(OFF);
				} else {
					if (CurrentTemperature > TempRangeHigh){
						ti.SetLampColorAndMessage("TEMP HIGH", 3);
						Heater(OFF);
						Chiller(ON);
					} else {
						ti.SetLampColorAndMessage("TEMP OK", 1); // temperature is within threshhold
						Heater(OFF);
						Chiller(OFF);
					}
				}
				// Check humidity and effect control as necessary
				if (CurrentHumidity < HumiRangeLow){
					hi.SetLampColorAndMessage("HUMI LOW", 3); // humidity is below threshhold
					Humidifier(ON);
					Dehumidifier(OFF);
				} else {
					if (CurrentHumidity > HumiRangeHigh){
						hi.SetLampColorAndMessage("HUMI HIGH", 3);
						Humidifier(OFF);
						Dehumidifier(ON);
					} else {
						hi.SetLampColorAndMessage("HUMI OK", 1); // humidity is within threshhold
						Humidifier(OFF);
						Dehumidifier(OFF);
					}
				}
				// This delay slows down the sample rate to Delay milliseconds
				try {
					Thread.sleep( Delay );
				} catch( Exception e ){
					System.out.println( "Sleep error:: " + e );
				}
			}
		} else {
			System.out.println("Unable to register with the message manager.\n\n" );
		}
	}

	public boolean IsRegistered(){
		return( Registered );
	}

	public void SetTemperatureRange(float lowtemp, float hightemp ){
		TempRangeHigh = hightemp;
		TempRangeLow = lowtemp;
		mw.WriteMessage( "***Temperature range changed to::" + TempRangeLow + "F - " + TempRangeHigh +"F***" );
	}

	public void SetHumidityRange(float lowhumi, float highhumi ){
		HumiRangeHigh = highhumi;
		HumiRangeLow = lowhumi;
		mw.WriteMessage( "***Humidity range changed to::" + HumiRangeLow + "% - " + HumiRangeHigh +"%***" );
	}

	public void Halt(){
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );
		Message msg;
		msg = new Message( MessageConstants.DEVICE_STOP, "XXX" );
		try{
			em.SendMessage( msg );
		}catch (Exception e){
			System.out.println("Error sending halt message:: " + e);
		}
	}

	/* Purpose: This method posts messages that will signal the temperature
	*		   controller to turn on/off the heater
	*/
	private void Heater( boolean ON ){
		Message msg;
		if ( ON ){
			msg = new Message(MessageConstants.TEMP_CTRL, MessageConstants.HEATER_ON);
		} else {
			msg = new Message(MessageConstants.TEMP_CTRL, MessageConstants.HEATER_OFF);
		}
		try{
			em.SendMessage( msg );
		}catch (Exception e){
			System.out.println("Error sending heater control message:: " + e);
		}
	}

	private void Chiller( boolean ON ){
		Message msg;
		if ( ON ){
			msg = new Message(MessageConstants.TEMP_CTRL, MessageConstants.CHILLER_ON);
		} else {
			msg = new Message(MessageConstants.TEMP_CTRL, MessageConstants.CHILLER_OFF);
		}
		try{
			em.SendMessage( msg );
		} catch (Exception e){
			System.out.println("Error sending chiller control message:: " + e);
		}
	}

	private void Humidifier( boolean ON ){
		Message msg;
		if ( ON ){
			msg = new Message(MessageConstants.HUMI_CTRL, MessageConstants.HUMI_ON);
		} else {
			msg = new Message(MessageConstants.HUMI_CTRL, MessageConstants.HUMI_OFF);
		}
		try{
			em.SendMessage( msg );
		}catch (Exception e){
			System.out.println("Error sending humidifier control message::  " + e);
		}
	}
	
	private void Dehumidifier( boolean ON ){
		Message msg;
		if ( ON ){
			msg = new Message(MessageConstants.HUMI_CTRL, MessageConstants.DEHUMI_ON);
		} else {
			msg = new Message(MessageConstants.HUMI_CTRL, MessageConstants.DEHUMI_OFF);
		}
		try{
			em.SendMessage( msg );
		}

		catch (Exception e){
			System.out.println("Error sending dehumidifier control message::  " + e);
		}
	}
}