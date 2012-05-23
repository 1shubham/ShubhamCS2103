
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
		
		Time12HourTester tester12Hour = new Time12HourTester();
		Time24HourTester tester24Hour = new Time24HourTester();
		
		if (tester12Hour.isValid(input))
			System.out.println("The time you input was in 12-hour format");
		
		else if (tester24Hour.isValid(input))
			System.out.println("The time you input was in 24-hour format");
		
		else
			System.out.println("The time you entered was not matched with any recognizable formats");
		
	}
	/*
	public static boolean shubhendraTester (String testString) {
		final String TIME12_PATTERN_1 = "((((0?[1-9])|(1[0-2]))(([:\\.][0-5][0-9])?(\\s?[AP]M)))|"+"((([01][0-9])|(2[0-3]))([:\\.][0-5][0-9])))";
		return testString.matches(TIME12_PATTERN_1);
	}
	*/
	
}