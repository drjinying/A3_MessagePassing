
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class SecurityMonitor extends Thread
{
	private MessageManagerInterface em = null;	
	private String MsgMgrIP = null;				
	// private int WindowArm = 1;			
	// private int WindowDisarm = 0;				
	// private int DoorArm = 1;			
	// private int DoorDisarm = 0;	
	// private int MotionArm = 1;			
	// private int MotionDisarm = 0;			
	boolean Registered = true;					
	MessageWindow mw = null;					// This is the message window
	Indicator wi;								// Window break indicator
	Indicator di;								// Door break indicator
	Indicator mi;								// Motion Detection indicator
	// Indicator won;								// Window break indicator
	// Indicator don;								// Door break indicator
	// Indicator mon;

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
		int CurrentWindow = 0;	
		int CurrentDoor= 0;		
		int CurrentMotion = 0;	
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
			// won = new Indicator ("Window Alarm", mw.GetX()+ mw.Width(), 0);
			// don = new Indicator ("Door Alarm", mw.GetX()+ mw.Width(), 0 );
			// mon = new Indicator ("Motion Alarm", mw.GetX()+ mw.Width(), 0);

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
							CurrentWindow = Integer.valueOf(Msg.GetMessage()).intValue();

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

							CurrentDoor = Integer.valueOf(Msg.GetMessage()).intValue();

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

							CurrentMotion = Integer.valueOf(Msg.GetMessage()).intValue();

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
						// won.dispose();
						// don.dispose();
						// mon.dispose();

					} // if

				} // for

				mw.WriteMessage("Window:: " + CurrentWindow + "Door:: " + CurrentDoor + "Motion:: " + CurrentMotion );

				// Check temperature and effect control as necessary

				if (CurrentWindow  == 0) // window break is disarm
				{
					wi.SetLampColorAndMessage("window break is disarm", 24);
					ArmWindowBreak(ON);
					DisarmWindowBreak(OFF);

				} else {
					
					wi.SetLampColorAndMessage("window break is arm", 24);
					ArmWindowBreak(OFF);
					DisarmWindowBreak(ON);

					
				} // if

				
				if (CurrentDoor == 0)
				{
					di.SetLampColorAndMessage("door break is disarm", 24); // door break is disarm
					ArmDoorBreak(ON);
					DisarmDoorBreak(OFF);

				} else {

					di.SetLampColorAndMessage("door break is arm", 24);
					ArmDoorBreak(OFF);
					DisarmDoorBreak(ON);

					
				} // if

				if (CurrentMotion == 0)
				{
					mi.SetLampColorAndMessage("motion detection is off", 24); // motion detection is off
					ArmMotionDetection(ON);
					DisarmMotionDetection(OFF);

				} else {

					mi.SetLampColorAndMessage("motion detection is on", 24);
					ArmMotionDetection(OFF);
					DisarmMotionDetection(ON);

					
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

	/**********
	Window break
	***********/


	private void ArmWindowBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 25, "ARM1" );

		} else {

			msg = new Message( (int) 25, "ARM0" );

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

    private void DisarmWindowBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 25, "DISARM1" );

		} else {

			msg = new Message( (int) 25, "DISARM0" );

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


	private void ArmDoorBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 26, "ARM1" );

		} else {

			msg = new Message( (int) 26, "ARM0" );

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

	private void DisarmDoorBreak( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 26, "DISARM1" );

		} else {

			msg = new Message( (int) 26, "DISARM0" );

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


	private void ArmMotionDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 27, "ARM1" );

		} else {

			msg = new Message( (int) 27, "ARM0" );

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

	private void DisarmMotionDetection( boolean ON )
	{
		// Here we create the message.

		Message msg;

		if ( ON )
		{
			msg = new Message( (int) 27, "DISARM1" );

		} else {

			msg = new Message( (int) 27, "DISARM0" );

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