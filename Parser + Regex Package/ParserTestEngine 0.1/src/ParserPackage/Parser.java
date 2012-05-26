package ParserPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
	/*
	public Task[] getTasks(String command) {
		
		return null;
	}*/
	
	public static void main (String args[]) {
		String inputString = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Input string:");
		try {
			inputString = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
		
		inputString = inputString.trim();
		
		TimeParser timeParser = new TimeParser();
		//DateParser dateParser = new DateParser();
		
		if(timeParser.extractTime(inputString))
			System.out.println("extracted!");
		else
			System.out.println("NOT extracted!");
	}

}


