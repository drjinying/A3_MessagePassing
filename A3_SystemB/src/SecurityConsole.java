
/******************************************************************************************************************
* File:SecurityConsole.java
* Course: 17655
* Project: Assignment 3
* Copyright: Copyright (c) 2009 Carnegie Mellon University
* Versions:
*	1.0 February 2009 - Initial rewrite of original assignment 3 (ajl).
*
* Description: This class is the console for the museum security control system. This process consists of two
* threads. The SecurityMonitor object is a thread that is started that is responsible for the monitoring and control of
* the museum security systems. The main thread provides a text interface for the user to arm and disarm the window sensor,
* door sensor, motion detection sensor, as well as shut down the system.
*
* Parameters: None
*
* Internal Methods: None
*
******************************************************************************************************************/
import java.awt.Window;

import MessagePackage.Message;
import TermioPackage.Termio;

public class SecurityConsole {

	static boolean showMessage = false;
	static boolean sprinkleStarted = false;
	static boolean isFire = false;
	static Window[] w;
	
	
	public static void main(String args[]) {
		Termio UserInput = new Termio(); // Termio IO Object
		boolean Done = false; // Main loop flag
		String Option = null; // Menu choice from user
		String Option2 = null;
		Message Msg = null; // Message object
		boolean Error = false; // Error flag

		SecurityMonitor Monitor = null; // The environmental control system
										// monitor

		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////
		w = Window.getWindows();

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
				System.out.println("1. Set Window Alarm State");
				System.out.println("2. Set Door break Alarm State");
				System.out.println("3. Set Motion Detection Alarm State");

				if (showMessage) {
					if (!sprinkleStarted)
						System.out.println(
								"\n A fire has risen. Sprinklers will start in 10 seconds. Press 'n' to stop the sprinklers.");
					else
						System.out.println("4. Stop the sprinklers.");

				}
				
				System.out.println("X: Stop System\n");
				System.out.print("\n>>>> ");
				Option = UserInput.KeyboardReadString();
				//////////// option 1 ////////////

				if (Option.equals("1")) {
					// Here we get the window alarm state
					Error = true;
					while (Error) {
						System.out.println("\nSelect the alarm state ");
						System.out.println("\n 1. Arm Window Sensor ");
						System.out.println("\n 2. Disarm Window Sensor");
						System.out.println("\n 3. Go back ");
						Option2 = UserInput.KeyboardReadString();
						if (UserInput.IsNumber(Option2)) {
							switch (Option2) {
							case "1":
								// Call a method in the security monitor
								// that will allow me to arm window sensor.
								Monitor.ArmWindowBreak(true);
								Error = false;
								break;
							case "2":
								// Call a method in the security monitor
								// that will allow me to disarm window sensor.
								Monitor.ArmWindowBreak(false);
								Error = false;
								break;
							case "3":
								Error = false;
								break;
							default:
								System.out.println("Not a valid number, please try again...");
								break;

							}

						} else {
							System.out.println("Not a valid number, please try again...");
						} // if
					} // while

				} // if

				//////////// option 2 ////////////

				if (Option.equals("2")) {
					// Here we get the door alarm state
					Error = true;
					while (Error) {
						System.out.println("\nSelect the alarm state ");
						System.out.println("\n 1. Arm Door Sensor");
						System.out.println("\n 2. Disarm Door Sensor");
						System.out.println("\n 3. Go back");
						Option2 = UserInput.KeyboardReadString();
						if (UserInput.IsNumber(Option2)) {
							switch (Option2) {
							case "1":
								// Call a method in the security monitor
								// that will allow me to arm door
								// sensor.
								Monitor.ArmDoorBreak(true);
								Error = false;
								break;
							case "2":
								// Call a method in the security monitor
								// that will allow me to disarm door sensor.
								Monitor.ArmDoorBreak(false);
								Error = false;
								break;
							case "3":
								Error = false;
								break;
							default:
								System.out.println("Not a valid number, please try again...");
								break;
							}
						} else {
							System.out.println("Not a valid number, please try again...");
						} // if
					} // while
				} // if

				if (Option.equals("3")) {
					// Here we get the motion sensor alarm state
					Error = true;
					while (Error) {
						System.out.println("\nSelect the alarm state ");
						System.out.println("\n 1. Arm Motion Detection Sensor ");
						System.out.println("\n 2. Disarm Motion Detection Sensor");
						System.out.println("\n 3. Go back ");
						Option2 = UserInput.KeyboardReadString();
						if (UserInput.IsNumber(Option2)) {
							switch (Option2) {
							case "1":
								// Call a method in the security monitor
								// that will allow me to arm motion sensor
								Monitor.ArmMotionDetection(true);
								Error = false;
								break;
							case "2":
								// Call a method in the security monitor
								// that will allow me to disarm motion
								// sensor.
								Monitor.ArmMotionDetection(false);
								Error = false;
								break;
							case "3":
								Error = false;
								break;
							default:
								System.out.println("Not a valid number, please try again...");
								break;
							}
						} else {
							System.out.println("Not a valid number, please try again...");
						} // if
					} // while

				} // if
				if (sprinkleStarted)
					if (Option.equals("4")) {
						Monitor.changeSprinkleState(false, null);
						sprinkleStarted = false;
						showMessage = false;
						
					}
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

				}

				if (showMessage) {
					if (Option.equalsIgnoreCase("n")) {
						
						Monitor.changeSprinkleState(false, "n");
						sprinkleStarted = false;
						showMessage = false;
					}
				}

				// if

			} // while

		} else {

			System.out.println("\n\nUnable start the monitor.\n\n");

		} // if

	} // main

	static void generateAlert() {
		
		System.out.println("\n A fire has started. Press enter in the security console to see your options.");
	}
	
	
	static void generatePostAlert()
	{
		System.out.println("\n Sprinklers have started. Press enter to continue.");
	}
	

} // SecurityConsole
