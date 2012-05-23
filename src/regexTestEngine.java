
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


//import java.io.*;

public class regexTestEngine {
	
	public static void main (String args[]) {
		
		String inputS = null, regexS = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Input the Regex string: ");
		
		try {
			regexS = reader.readLine();
		}
		
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for regexS");
		}
		
		
		System.out.print("Input the Test string: ");
		
		try {
			inputS = reader.readLine();
		}
		
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for inputS");
		}
		
		
		if (inputS.matches(regexS))
			System.out.println("Congratulations! it matches!! :D");
		
		else
			System.out.println("oops, it does not match! :'(");
		
		
	}
}