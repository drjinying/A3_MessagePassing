/******************************************************************************************************************
* File:FireSensor.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a window sensor. This class gets the action from simulation monitor, which gets the direct input
* user by simulation console.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*   PostWindowState(MessageManagerInterface ei, boolean State)
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;

class FireSensor
{
	public static void main(String args[])
	{
		String MsgMgrIP;				// Message Manager IP address
		Message Msg = null;				// Message object
		MessageQueue eq = null;			// Message Queue
		MessageManagerInterface em = null;// Interface object to the message manager
		boolean FireState = false;	// Fire state: false == no fire, true == FIRE
		int	Delay = 2500;				// The loop delay (2.5 seconds)
		boolean Done = false;			// Loop termination flag

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

 		if ( args.length == 0 )
 		{
			// message manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine..." );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is on the local machine

				em = new MessageManagerInterface();
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} else {

			// message manager is not on the local system

			MsgMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + MsgMgrIP );

			try
			{
				// Here we create an message manager interface object. This assumes
				// that the message manager is NOT on the local machine

				em = new MessageManagerInterface( MsgMgrIP );
			}

			catch (Exception e)
			{
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.


		if (em != null)
		{

			// We create a message window. Note that we place this panel about 1/2 across
			// and 1/3 down the screen

			float WinPosX = 0.5f; 	//This is the X position of the message window in terms
								 	//of a percentage of the screen height
			float WinPosY = 0.3f; 	//This is the Y position of the message window in terms
								 	//of a percentage of the screen height

			MessageWindow mw = new MessageWindow("Window Sensor", WinPosX, WinPosY );

			mw.WriteMessage("Registered with the message manager." );

	    	try
	    	{
				mw.WriteMessage("   Participant id: " + em.GetMyId() );
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime() );

			} // try

	    	catch (Exception e)
			{
				mw.WriteMessage("Error:: " + e);

			} // catch

			mw.WriteMessage("\nInitializing Sensor Simulation::" );

			/********************************************************************
			** Here we start the main simulation loop
			*********************************************************************/

			mw.WriteMessage("Beginning Simulation... ");


			while ( !Done )
			{
				// Post the current fire state only if the sensor is armed
				PostFireState(em, FireState);
				if (FireState)
				{
					mw.WriteMessage("Current status::  FIRE");
				} else
				{
					mw.WriteMessage("Current status::  No fire");
				}

				
				// Get the message queue

				try
				{
					eq = em.GetMessageQueue();

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Error getting message queue::" + e );

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 10, this means there is a fire/ not fire. 
				// Note that we get all the messages
				// at once... there is a 2.5 second delay between samples,.. so
				// the assumption is that there should only be a message at most.
				// If there are more, it is the last message that will effect the
				// output of the window sensor as it would in reality.

				int qlen = eq.GetSize();

				for ( int i = 0; i < qlen; i++ )
				{
					Msg = eq.GetMessage();

					if ( Msg.GetMessageId() == 10 )
					{
						if (Msg.GetMessage().equalsIgnoreCase("F1")) // there is a fire
						{
							FireState = true;
						} // if

						if (Msg.GetMessage().equalsIgnoreCase("F0")) // there is no fire
						{
							FireState = false;

						} // if		
						

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

				    	mw.WriteMessage("\n\nSimulation Stopped. \n");

					} // if

				} // for


				// Here we wait for a 2.5 seconds before we start the next sample

				try
				{
					Thread.sleep( Delay );

				} // try

				catch( Exception e )
				{
					mw.WriteMessage("Sleep error:: " + e );

				} // catch

			} // while

		} else {

			System.out.println("Unable to register with the message manager.\n\n" );

		} // if

	} // main

	/***************************************************************************
	* CONCRETE METHOD:: PostWindowState
	* Purpose: This method posts the specified window state to the
	* specified message manager. This method assumes an message ID of 30.
	*
	* Arguments: MessageManagerInterface ei - this is the messagemanger interface
	*			 where the message will be posted.
	*
	*			 boolean state - this is the state of the window.
	*
	* Returns: none
	*
	* Exceptions: None
	*
	***************************************************************************/

	static private void PostFireState(MessageManagerInterface ei, boolean State)
	{
		// Here we create the message.
		Message msg = null;
		if (State)
		{
			msg = new Message( (int) 30, "F1" );
		}
		if (!State)
		{
			msg = new Message( (int) 30, "F0" );
		}
		// Here we send the message to the message manager.

		try
		{
			ei.SendMessage( msg );
			//System.out.println( "Sent Window Message" );

		} // try

		catch (Exception e)
		{
			System.out.println( "Error Posting Window state:: " + e );

		} // catch

	} // PostFireState

} // WindowSensor