
/******************************************************************************************************************
* File:ECSConsole.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class is the console for the museum environmental control system. This process consists of two
* threads. The ECSMonitor object is a thread that is started that is responsible for the monitoring and control of
* the museum environmental systems. The main thread provides a text interface for the user to change the temperature
* and humidity ranges, as well as shut down the system.
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import MessagePackage.Message;
import TermioPackage.Termio;

public class SecuritySimulationConsole {
	public static void main(String args[]) {
		Termio UserInput = new Termio(); // Termio IO Object
		boolean Done = false; // Main loop flag
		String Option = null; // Menu choice from user
		Message Msg = null; // Message object
		boolean Error = false; // Error flag
		SecurityMonitor Monitor = null; // The environmental control system monitor

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////

		if (args.length != 0) {
			// message manager is not on the local system
			Monitor = new SecurityMonitor(args[0]);
		} else {
			Monitor = new SecurityMonitor();
		} // if

		// Here we check to see if registration worked. If ef is null then the
		// message manager interface was not properly created.

		if (Monitor.IsRegistered()) {
			Monitor.start(); // Here we start the monitoring and control thread

			while (!Done) {
				// Here, the main thread continues and provides the main menu

				System.out.println("\n\n\n\n");
				System.out.println("Security Control System (SCS) Command Console: \n");

				if (args.length != 0)
					System.out.println("Using message manger at: " + args[0] + "\n");
				else
					System.out.println("Using local message manger \n");

				System.out.println("Select an Option: \n");
				System.out.println("1. Window Alarm Simulator");
				System.out.println("2. Door break Alarm Simulator");
				System.out.println("3. Motion Detection Alarm Simulator");
				System.out.println("X: Stop System\n");
				System.out.print("\n>>>> ");
				Option = UserInput.KeyboardReadString();

				boolean innerError = false;
				//////////// option 1 ////////////

				if (Option.equals("1")) {
					// Here we get the window alarm state
					Error = true;
					while (Error) {
						innerError = true;
						while (innerError) {
							System.out.println("\nSelect the alarm state ");
							System.out.println("\n 1. Raise Window alarm ");
							System.out.println("\n 2. Stop Window alarm");
							System.out.println("\n 3. Go back ");
							Option = UserInput.KeyboardReadString();
							if (UserInput.IsNumber(Option)) {
								Error = false;

								switch (Option) {
								case "1":
									// Call a method in the security monitor
									// that will allow me to raise window alarm
									// Monitor.
									Monitor.SetWindowBroken(1);
									innerError = false;
									break;
								case "2":
									// Call a method in the security monitor
									// that will allow me to stop window alarm
									// Monitor.
									Monitor.StopWindowAlarm(1);
									innerError = false;
									break;
								case "3":
									innerError = false;
									break;
								default:
									System.out.println("Not a valid number, please try again...");
									break;

								}

							} else {
								System.out.println("Not a valid number, please try again...");
							} // if
						} // while
					}

				} // if

				//////////// option 2 ////////////

				if (Option.equals("2")) {
					// Here we get the door alarm state
					Error = true;
					while (Error) {
						innerError = true;
						while (innerError) {
							System.out.println("\nSelect the alarm state ");
							System.out.println("\n 1. Raise Door break alarm ");
							System.out.println("\n 2. Stop Door break alarm");
							System.out.println("\n 3. Go back ");
							Option = UserInput.KeyboardReadString();
							if (UserInput.IsNumber(Option)) {
								Error = false;

								switch (Option) {
								case "1":
									// Call a method in the security monitor
									// that will allow me to arm door alarm
									// Monitor.
									Monitor.SetDoorBroken(1);
									innerError = false;
									break;
								case "2":
									// Call a method in the security monitor
									// that will allow me to disarm door alarm
									// Monitor.
									Monitor.StopDoorAlarm(1);
									innerError = false;
									break;
								case "3":
									innerError = false;
									break;
								default:
									System.out.println("Not a valid number, please try again...");
									break;
								}
							} else {
								System.out.println("Not a valid number, please try again...");
							} // if
						} // while
					}
				} // if

				if (Option.equals("3")) {
					// Here we get the motion sensor alarm state
					Error = true;
					while (Error) {
						innerError = true;
						while (innerError) {
							System.out.println("\nSelect the alarm state ");
							System.out.println("\n 1. Raise Motion Sensor Alarm ");
							System.out.println("\n 2. Stop Motion Sensor Alarm");
							System.out.println("\n 3. Go back ");
							Option = UserInput.KeyboardReadString();
							if (UserInput.IsNumber(Option)) {
								Error = false;

								switch (Option) {
								case "1":
									// Call a method in the security monitor
									// that will allow me to raise motion sensor
									// alarm
									// Monitor.

									Monitor.SetMotionDetection(1);
									innerError = false;
									break;
								case "2":
									// Call a method in the security monitor
									// that will allow me to stop motion
									// sensor alarm
									// Monitor.
									Monitor.StopMotionAlarm(1);
									innerError = false;
									break;
								case "3":
									innerError = false;
									break;
								default:
									System.out.println("Not a valid number, please try again...");
									break;
								}
							} else {
								System.out.println("Not a valid number, please try again...");
							} // if
						} // while
					}

				} // if

				//////////// option X ////////////

				if (Option.equalsIgnoreCase("X")) {
					// Here the user is done, so we set the Done flag and halt
					// the environmental control system. The monitor provides a
					// method
					// to do this. Its important to have processes release their
					// queues
					// with the message manager. If these queues are not
					// released these
					// become dead queues and they collect messages and will
					// eventually
					// cause problems for the message manager.

					Monitor.Halt();
					Done = true;
					System.out.println("\nConsole Stopped... Exit monitor mindow to return to command prompt.");
					Monitor.Halt();

				} // if

			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n");

		} // if

	} // main

} // SecurityConsole