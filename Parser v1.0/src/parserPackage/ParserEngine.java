package parserPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserEngine {
	

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
		
		
		Parser parserObject = new Parser();
		
		parserObject.parse(inputString);
		
		/*
		DateParser dpObject = new DateParser();
		dpObject.dummyFunction();
		*/
		/*
		Parser pObject = new Parser();
		pObject.dummyFunction();
		*/
	}
}
