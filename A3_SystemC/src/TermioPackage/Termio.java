
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// This class contains methods that allow the callers do the
// following:
//
//   public String KeyboardReadString():
//      Allows the caller to read a string from the keyboard.
//
//   public char KeyboardReadChar()
//      Allows the caller to read a single unicode char from
//      the keyboard.
//
//   public boolean IsNumber( String StringItem )
//      Allows the caller to test StringItem to see if it is
//      a numeric value.
//
//   public float ToFloat( String StringItem )
//      Allows the caller to convert StringItem into a
//      float.
//
//   public double ToDouble(String StringItem)
//      Allows the caller to convert StringItem into a
//      double.
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package TermioPackage;
import java.io.*;

public class Termio
{
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	
	public static enum Colors {
		RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE
	}
	
	public static void Print(Colors color, String content) {
		String ansiClr = ANSI_WHITE;;
		switch (color) {
		case RED: ansiClr = ANSI_RED; break;
		case GREEN: ansiClr = ANSI_GREEN; break;
		case YELLOW: ansiClr = ANSI_YELLOW; break;
		case BLUE: ansiClr = ANSI_BLUE; break;
		case PURPLE: ansiClr = ANSI_PURPLE; break;
		case CYAN: ansiClr = ANSI_CYAN; break;
		case WHITE: ansiClr = ANSI_WHITE; break;
		}
		System.out.println(ansiClr + content + ANSI_RESET);
	}
	
   public String KeyboardReadString()
   {
      BufferedReader MyReader =
      new BufferedReader(new InputStreamReader(System.in));

      String StringItem = "";
      try {
          StringItem = MyReader.readLine();
      }
      catch (IOException IOError){
          System.out.println( "Read Error in Termio.KeyboardReadString method" );
      }
      return StringItem;
   }

   public char KeyboardReadChar() {
      BufferedReader MyReader =
      new BufferedReader(new InputStreamReader(System.in));
      char CharItem = ' ';
      try {
          CharItem = (char)MyReader.read();
      }catch (IOException IOError) {
          System.out.println( "Read Error in Termio.KeyboardReadChar method" );
      }
      return CharItem;
   }

   public boolean IsNumber( String StringItem )
   {
      Float FloatItem = new Float(0.0);
      try {
          FloatItem = FloatItem.valueOf( StringItem );
      } // try
      catch (NumberFormatException IOError)
      {
          return false;
      } // catch
      return true;
   } //IsNumber

   public float ToFloat( String StringItem )
   {
      Float FloatItem = new Float(0.0);
      try {
          FloatItem = FloatItem.valueOf( StringItem );
      } // try
      catch (NumberFormatException IOError)
      {
          System.out.print(   "Error converting " + StringItem );
          System.out.print( " to a floating point number::");
          System.out.println( " Termio.ToFloat method.");
      } // catch
      return FloatItem.floatValue();
   } //ToFloat

   public double ToDouble(String StringItem)
   {
      Float FloatItem = new Float(0.0);
      try {
          FloatItem = FloatItem.valueOf( StringItem );
      } // try
      catch (NumberFormatException IOError)
      {
          System.out.print(   "Error converting " + StringItem );
          System.out.print( " to a floating point number::");
          System.out.println( " Termio.ToDouble method.");
      } // catch
      return FloatItem.doubleValue();
   } //ToDouble

   public int ToInteger(String StringItem)
   {
      Integer IntegerItem = new Integer(0);
      try {
          IntegerItem = IntegerItem.valueOf( StringItem );
      } // try
      catch (NumberFormatException IOError)
      {
          System.out.print(   "Error converting " + StringItem );
          System.out.print( " to an integer number::");
          System.out.println( " Termio.ToInteger method.");
      } // catch
      return IntegerItem.intValue();
   } //ToDouble
} //class
