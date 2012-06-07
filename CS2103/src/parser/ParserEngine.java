package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ParserEngine {
	

	public static void main (String args[]) {
		/*
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
		parserObject.parseForAdd(inputString);
		
		
		System.out.print("Input string2:");
		try {
			inputString = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
		
		parserObject.parseForSearch(inputString);
		*/
		
		Parser parserObject = new Parser();
		parserObject.parseForAdd("meeting for cs2103 on 15/3 to on 16/3 @work");
		
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
