/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
*/

import java.io.*;

public class regexTestEngine {
	
	public static void main (String args[]) {
		
		String inputS = null, regexS = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Input the Regex string: ");
		
		try {
			regexS = reader.readLine();
			//System.out.println("You entered: "+regexS);
		}
		
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for regexS");
		}
		
		System.out.print("Input the Test string: ");
		
		try {
			inputS = reader.readLine();
			//System.out.println("You entered: "+inputS);
		}
		
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for inputS");
		}
		
		if (inputS.matches(regexS))
			System.out.println("Congratulations! it matches!! :D");
		
		else
			System.out.println("oops, it does not match! :'(");
		
		
		/*
		if (getInput(regexS)) {
			
			System.out.println("Successfully input regex string: "+regexS);
			System.out.println("Input the test string: ");
				
			if (getInput(inputS)) {
				
				System.out.println("Successfully input test string: ");
				
				if (inputS.matches(regexS))
					System.out.println("Congratulations! it matches!! :D");
				
				else
					System.out.println("oops, it does not match! :'(");
				
			}
			
			else
				System.out.println("An unexpected error occured while getting test string");
			
		}
		
		else
			System.out.println("An unexpected error occured while getting regex string");
		
		*/
	}
}

	/*
	public static String getInput() {
		
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {	
			System.out.println("you input this: "+reader.readLine());
			return reader.readLine();
		}
		
		catch(IOException ioe) {
			return "error!";
		}
		
	}

	
	

	public static boolean getInput(String s) {
		
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {		
			s = reader.readLine();
			System.out.println("you input this: "+s);
			return true;
		}
		
		catch(IOException ioe) {
			return false;
		}
		
	}

*/
