package parser;

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
		
		
		System.out.print("Input string2:");
		try {
			inputString = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
	
		System.out.println("this is what you entered: "+inputString);
		
		parserObject.parse(inputString);
		/*
		AtomicReference<String> inputRef = new AtomicReference(inputString);
		mutateString(inputRef);
		System.out.println("the reference now holds: "+inputRef.toString());
		System.out.println("inputString: "+inputString);
		*/
		
	}
	/*
	private static void mutateString (AtomicReference<String> stringRef) {
		String s="its no longer the same";
		
		AtomicReference<String> dummyRef = new AtomicReference<String>(s);
		stringRef = dummyRef;
	}
	*/

}
