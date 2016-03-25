
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class SecurityMonitor extends Thread
{
	private MessageManagerInterface em = null;	
	private String MsgMgrIP = null;				
	private int WindowState = 0;
	private int DoorState  = 0;
	private int MotionState  = 0;
	private int FireState  = 0;
	boolean Registered = true;					
	MessageWindow mw = null;					// This is the message window
	Indicator wi;								// Window break indicator
	Indicator di;								// Door break indicator
	Indicator mi;								// Motion Detection indicator
	Indicator fi;								// Fire Detection indicator
	Indicator si;								// Fire Detection indicator


	public SecurityMonitor()
	{
		// message manager is on the local system

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is on the local machine
			em = new MessageManagerInterface();
		}

		catch (Exception e)
		{
			System.out.println("SecurityMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch

	} //Constructor

	public SecurityMonitor( String MsgIpAddress )
	{
		// message manager is not on the local system

		MsgMgrIP = MsgIpAddress;

		try
		{
			// Here we create an message manager interface object. This assumes
			// that the message manager is NOT on the local machine

			em = new MessageManagerInterface( MsgMgrIP );
		}

		catch (Exception e)
		{
			System.out.println("SecurityMonitor::Error instantiating message manager interface: " + e);
			Registered = false;

		} // catch

	} // Constructor

	public void run()
	{
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		int MsgId = 0;					// User specified message ID
		int CurrentWindowAlarm = 0;
		int CurrentDoorAlarm= 0;
		int CurrentMotionAlarm = 0;
		int CurrentFireAlarm = 0;
		int CurrentSprinkler = 0;
		int	Delay = 1000;				// The loop delay (1 second)
		boolean Done = false;			// Loop termination flag
		boolean ON = true;				
		boolean OFF = false;			



         if (em != null)
		 {
			
			mw = new MessageWindow("Security Monitoring Console", 0, 0);
			wi = new Indicator ("Window break UNK", mw.GetX()+ mw.Width(), 0);
			di = new Indicator ("Door break UNK", mw.GetX()+ mw.Width(), 0 );
			mi = new Indicator ("Motion detection UNK", mw.GetX()+ mw.Width(), 0);
			 fi = new Indicator ("Fire detection UNK", mw.GetX()+ mw.Width(), 0);
			 si = new Indicator ("Sprinkler UNK", mw.GetX()+ mw.Width(), 0);


			mw.WriteMessage( "Registered with the message manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			while ( !Done )
			{
				// Here we get our message queue from the message manager

				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 1 or 2 or 3. Message IDs of 1 are window break
				// readings from the window sensor; message IDs of 2 are door sensor
				// readings.And messgae IDs of 3 are motion sensor Note that we get all the messages at once... there is a 1
				// second delay between samples,.. so the assumption is that there should
				// only be a message at most. 

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == 21 ) 
					{
						try
						{
							CurrentWindowAlarm = Integer.valueOf(Msg.GetMessage()).intValue();

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading window: " + e);

						} // catch

					} // if

					if ( Msg.GetMessageId() == 22 ) 
					{
						try
						{

							CurrentDoorAlarm = Integer.valueOf(Msg.GetMessage()).intValue();

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading door: " + e);

						} // catch

					} // if

					if ( Msg.GetMessageId() == 23 ) 
					{
						try
						{

							CurrentMotionAlarm = Integer.valueOf(Msg.GetMessage()).intValue();

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading motion: " + e);

						} // catch

					} // if
					if ( Msg.GetMessageId() == 24 )
					{
						try
						{

							CurrentFireAlarm = Integer.valueOf(Msg.GetMessage()).intValue();

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading motion: " + e);

						} // catch

					} // if
					if ( Msg.GetMessageId() == 25 )
					{
						try
						{

							CurrentSprinkler = Integer.valueOf(Msg.GetMessage()).intValue();

						} // try

						catch( Exception e )
						{
							mw.WriteMessage("Error reading motion: " + e);

						} // catch

					} // if

					// If the message ID == 99 then this is a signal that the simulation
					// is to end. At this point, the loop termination flag is set to
					// true and this process unregisters from the message manager.

					if ( Msg.GetMessageId() == 99 )
					{
						Done = true;

						try
						{
							em.UnRegister();

				    	} // try

				    	catch (Exception e)
				    	{
							mw.WriteMessage("Error unregistering: " + e);

				    	} // catch

				    	mw.WriteMessage( "\n\nSimulation Stopped. \n");

						// Get rid of the indicators. The message panel is left for the
						// user to exit so they can see the last message posted.

						wi.dispose();
						di.dispose();
						mi.dispose();
						fi.dispose();
						si.dispose();

					} // if

				} // for

				mw.WriteMessage("Window Alarm:: " + CurrentWindowAlarm + "Door Alarm:: " + CurrentDoorAlarm + "Motion Alarm:: " + CurrentMotionAlarm
				+"Fire Alarm:: " + CurrentFireAlarm + "Sprinkler:: " + CurrentSprinkler );

				// Check temperature and effect control as necessary

				if (CurrentWindowAlarm  == 0) // window break is disarm
				{
					wi.SetLampColorAndMessage("window break is arm", 26);
					ArmWindowBreak(ON);

				} else {
					
					wi.SetLampColorAndMessage("window break is disarm", 26);
					DisarmWindowBreak(ON);

					
				} // if
				
				if (CurrentDoorAlarm == 0)
				{
					di.SetLampColorAndMessage("door break is arm", 26); // door break is disarm
					ArmDoorBreak(ON);

				} else {

					di.SetLampColorAndMessage("door break is disarm", 26);
					DisarmDoorBreak(ON);
					
				} // if

				if (CurrentMotionAlarm == 0)
				{
					mi.SetLampColorAndMessage("motion detection is on", 26); // motion detection is off
					ArmMotionDetection(ON);

				} else {

					mi.SetLampColorAndMessage("motion detection is off", 26);
					DisarmMotionDetection(ON);

				} // if

				if (CurrentFireAlarm == 0)
				{
					mi.SetLampColorAndMessage("fire detection is on", 26); // motion detection is off
					ArmFireDetection(ON);

				} else {

					mi.SetLampColorAndMessage("fire detection is off", 26);
					DisarmFireDetection(ON);

				} // if

				if (CurrentSprinkler == 0)
				{
					mi.SetLampColorAndMessage("sprinkler is on", 26); // motion detection is off
					confirmSprinkler(ON);

				} else {

					mi.SetLampColorAndMessage("sprinkler is off", 26);
					cancelSprinkler(ON);

				} // if
				// This delay slows down the sample rate to Delay milliseconds

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					System.out.println( "Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

	/***************************************************************************
	* CONCRETE METHOD:: IsRegistered
	* Purpose: This method returns the registered status
	*
	* Arguments: none
	*
	* Returns: boolean true if registered, false if not registered
	*
	* Exceptions: None
	*
	***************************************************************************/

	public boolean IsRegistered()
	{
		return( Registered );

	} // SetTemperatureRange


	/***************************************************************************
	* CONCRETE METHOD:: Halt
	* Purpose: This method posts an message that stops the environmental control
	*		   system.
	*
	* Arguments: none
	*
	* Returns: none
	*
	* Exceptions: Posting to message manager exception
	*
	***************************************************************************/

	public void Halt()
	{
		mw.WriteMessage( "***HALT MESSAGE RECEIVED - SHUTTING DOWN SYSTEM***" );

		// Here we create the stop message.

		Message msg;

		msg = new Message( (int) 99, "XXX" );

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending halt message:: " + e);

		} // catch

	} // Halt

	// raise window, door, motion alarm

	public void SetWindowBroken(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 35, "wba1" );

		} else {

			msg = new Message( (int) 35, "wba0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void StopWindowAlarm(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 35, "wba0" );

		} else {

			msg = new Message( (int) 35, "wba1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void SetDoorBroken(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 36, "dba1" );

		} else {

			msg = new Message( (int) 36, "dba0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void StopDoorAlarm(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 36, "dba0" );

		} else {

			msg = new Message( (int) 36, "dba1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void SetMotionDetection(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 37, "ma1" );

		} else {

			msg = new Message( (int) 37, "ma0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void StopMotionAlarm(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 37, "ma0" );

		} else {

			msg = new Message( (int) 37, "ma1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	/*
	  Fire detection
	 */
	public void SetFireAlarm(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 38, "fire1" );

		} else {

			msg = new Message( (int) 38, "fire0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void StopFireAlarm(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 38, "fire0" );

		} else {

			msg = new Message( (int) 38, "fire1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	public void SetSprinkler(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 39, "sprinkler1" );

		} else {

			msg = new Message( (int) 39, "sprinkler0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}
	public void StopSprinkler(int status )
	{

		Message msg;

		if ( status == 1 )
		{
			msg = new Message( (int) 39, "sprinkler0" );

		} else {

			msg = new Message( (int) 39, "sprinkler1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}

	/**********
	Window break alarm
	***********/


	public void ArmWindowBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 27, "wb1" );

		} else {

			msg = new Message( (int) 27, "wb0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	} 

    public void DisarmWindowBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 27, "wb0" );

		} else {

			msg = new Message( (int) 27, "wb1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	}


	public void ArmDoorBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 28, "db1" );

		} else {

			msg = new Message( (int) 28, "db0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	} 

	public void DisarmDoorBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 28, "db0" );

		} else {

			msg = new Message( (int) 28, "db1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message:: " + e);

		} // catch

	} 


	public void ArmMotionDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 29, "m1" );

		} else {

			msg = new Message( (int) 29, "m0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	} 

	public void DisarmMotionDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 29, "m0" );

		} else {

			msg = new Message( (int) 29, "m1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	}

	public void ArmFireDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 30, "f1" );

		} else {

			msg = new Message( (int) 30, "f0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	}

	public void DisarmFireDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 30, "f0" );

		} else {

			msg = new Message( (int) 30, "f1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	}

	public void confirmSprinkler( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 31, "s1" );

		} else {

			msg = new Message( (int) 31, "s0" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	}

	public void cancelSprinkler( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 31, "s0" );

		} else {

			msg = new Message( (int) 31, "s1" );

		} // if

		// Here we send the message to the message manager.

		try
		{
			em.SendMessage( msg );

		} // try

		catch (Exception e)
		{
			System.out.println("Error sending control message::  " + e);

		} // catch

	}


} 