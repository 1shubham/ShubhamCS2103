
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import java.io.*;

public class TimeDateTestEngine {
	
	public static void main (String args[]) {
		
		String input = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Input Time to compare with the acceptable format: ");
		try {
			input = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
		
		
		/*
		Time12HourTester tester12Hour = new Time12HourTester();
		Time24HourTester tester24Hour = new Time24HourTester();
		*/
		if (test12format(input))
			System.out.println("The time you input was in 12-hour format");
		
		else if (test24format(input))
			System.out.println("The time you input was in 24-hour format");
		
		else
			System.out.println("The time you entered was not matched with any recognizable formats");
		
		
	}
	
	public static boolean test12format(String time) {
		
		final String TIME12_PATTERN = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
		//final String TIME12_PATTERN_2 = "1[012]|[1-9](\\s)?(?i)(am|pm)";
		return Pattern.matches(TIME12_PATTERN, time);
		
	}
	
	public static boolean test24format(String time) {
		
		final String TIME24_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
		return Pattern.matches(TIME24_PATTERN, time);
	}
	
}