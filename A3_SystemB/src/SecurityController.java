
/******************************************************************************************************************
* File:SecurityController.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 March 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description:
*
* This class simulates a device that turn on and off the security alarm based on the status received from the security 
* monitor.
*
* Parameters: IP address of the message manager (on command line). If blank, it is assumed that the message manager is
* on the local machine.
*
* Internal Methods:
*	static private void ConfirmMessage(MessageManagerInterface ei, String m )
*
******************************************************************************************************************/
import InstrumentationPackage.*;
import MessagePackage.*;
import java.util.*;

class SecurityController {
	public static void main(String args[]) {
		String MsgMgrIP; // Message Manager IP address
		Message Msg = null; // Message object
		MessageQueue eq = null; // Message Queue
		int MsgId = 0; // User specified message ID
		MessageManagerInterface em = null;
		boolean doorBreakAlarmState = false;
		boolean windowBreakAlarmState = false;
		boolean motionBreakAlarmState = false;
		int Delay = 2500; // The loop delay (2.5 seconds)
		boolean Done = false; // Loop termination flag
		
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

		if (args.length == 0) {
			// message manager is on the local system

			System.out.println("\n\nAttempting to register on the local machine...");

			try {
				// Here we create an message manager interface object. This
				// assumes
				// that the message manager is on the local machine

				em = new MessageManagerInterface();
			}

			catch (Exception e) {
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} else {

			// message manager is not on the local system

			MsgMgrIP = args[0];

			System.out.println("\n\nAttempting to register on the machine:: " + MsgMgrIP);

			try {
				// Here we create an message manager interface object. This
				// assumes
				// that the message manager is NOT on the local machine

				em = new MessageManagerInterface(MsgMgrIP);
			}

			catch (Exception e) {
				System.out.println("Error instantiating message manager interface: " + e);

			} // catch

		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (em != null) {
			System.out.println("Registered with the message manager.");

			/*
			 * Now we create the temperature control status and message panel We
			 * put this panel about 1/3 the way down the terminal, aligned to
			 * the left of the terminal. The status indicators are placed
			 * directly under this panel
			 */

			float WinPosX = 0.0f; // This is the X position of the message
									// window in terms
									// of a percentage of the screen height
			float WinPosY = 0.3f; // This is the Y position of the message
									// window in terms
									// of a percentage of the screen height

			MessageWindow mw = new MessageWindow("Security Controller Status Console", WinPosX, WinPosY);

			// Put the status indicators under the panel...

			Indicator wi = new Indicator("Window Alarm", mw.GetX() + mw.Width(), 0);
			Indicator di = new Indicator("Door Alarm", mw.GetX() + mw.Width() + (wi.Width()*2), 0);
			Indicator mi = new Indicator("Motion Alarm", mw.GetX() + mw.Width() + wi.Width()*4, 0);

			mw.WriteMessage("Registered with the message manager.");

			try {
				mw.WriteMessage("   Participant id: " + em.GetMyId());
				mw.WriteMessage("   Registration Time: " + em.GetRegistrationTime());

			} // try

			catch (Exception e) {
				System.out.println("Error:: " + e);

			} // catch

			/********************************************************************
			 ** Here we start the main simulation loop
			 *********************************************************************/

			while (!Done) {

				try {
					eq = em.GetMessageQueue();

				} // try

				catch (Exception e) {
					mw.WriteMessage("Error getting message queue::" + e);

				} // catch

				// If there are messages in the queue, we read through them.
				// We are looking for MessageIDs = 5, this is a request to turn
				// the
				// heater or chiller on. Note that we get all the messages
				// at once... there is a 2.5 second delay between samples,.. so
				// the assumption is that there should only be a message at
				// most.
				// If there are more, it is the last message that will effect
				// the
				// output of the temperature as it would in reality.

				int qlen = eq.GetSize();

				for (int i = 0; i < qlen; i++) {
					Msg = eq.GetMessage();

					if (Msg.GetMessageId() == 35) {
						
						if (Msg.GetMessage().equalsIgnoreCase("WA1")) // turn on window alarm
						{
							windowBreakAlarmState = true;
							mw.WriteMessage("Received turn on window alarm message");
						} // if
						
						if (Msg.GetMessage().equalsIgnoreCase("WA0")) // turn off window alarm
						{
							windowBreakAlarmState = false;
							mw.WriteMessage("Received turn off window alarm message");
						} // if
						
						if (Msg.GetMessage().equalsIgnoreCase("DA1")) // turn on door alarm
						{
							doorBreakAlarmState = true;
							mw.WriteMessage("Received turn on door alarm message");
						} // if

						if (Msg.GetMessage().equalsIgnoreCase("DA0")) // Turn off door alarm
						{
							doorBreakAlarmState = false;
							mw.WriteMessage("Received turn off door alarm message");

						} // if
						
						if (Msg.GetMessage().equalsIgnoreCase("MA1")) // turn on motion alarm
						{
							motionBreakAlarmState = true;
							mw.WriteMessage("Received turn on motion alarm message");
						} // if

						if (Msg.GetMessage().equalsIgnoreCase("MA0")) // Turn off motion alarm
						{
							motionBreakAlarmState = false;
							mw.WriteMessage("Received turn off motion alarm message");

						} // if
						
					}
				

				// If the message ID == 99 then this is a signal that the
				// simulation
				// is to end. At this point, the loop termination flag is set to
				// true and this process unregisters from the message manager.

				if (Msg.GetMessageId() == 99) {
					Done = true;

					try {
						em.UnRegister();

					} // try

					catch (Exception e) {
						mw.WriteMessage("Error unregistering: " + e);

					} // catch

					mw.WriteMessage("\n\nSimulation Stopped. \n");

					// Get rid of the indicators. The message panel is left for
					// the
					// user to exit so they can see the last message posted.

					wi.dispose();
					di.dispose();
					mi.dispose();

				} // if

			} // for

			// Update the lamp status

			if (doorBreakAlarmState)
				di.SetLampColorAndMessage("Door Alarm Active", 3);
			else
				di.SetLampColorAndMessage("Door Alarm Inactive", 1);
			if (windowBreakAlarmState)
				wi.SetLampColorAndMessage("Window Alarm Active", 3);
			else
				wi.SetLampColorAndMessage("Window Alarm Inactive", 1);
			if (motionBreakAlarmState)
				mi.SetLampColorAndMessage("Motion Alarm Active", 3);
			else
				mi.SetLampColorAndMessage("Motion Alarm Inactive", 1);

			try {
				Thread.sleep(Delay);

			} // try

			catch (Exception e) {
				System.out.println("Sleep error:: " + e);

			} // catch

		} // while

	}else

	{

		System.out.println("Unable to register with the message manager.\n\n");

	} // if

	} // main


} // TemperatureController