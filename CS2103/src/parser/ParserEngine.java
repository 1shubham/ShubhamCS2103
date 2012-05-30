package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		TimeParser tp = new TimeParser();
		
		String time = tp.extractTime("fssd 2359 sddf");
		
		System.out.println(time);
		*/
		
		//DateParser dpObject = new DateParser();
		//dpObject.dummyFunction();
		
		
		//Parser pObject = new Parser();
		//pObject.dummyFunction();
		/*
		final String TIME_12_PATTERN = "[ ](1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; //([:.] not seperated out because of a good reason :D
		final String TIME_24_PATTERN = "[ ](2[0-3]|[01]?[0-9])[:.]?([0-5][0-9])";
		final String TIME_12_OR_24_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";//"((1[012]|(0?[1-9]))([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))";
		final String FROM_TIME_DATE_TO_TIME_DATE = "((from)|(FROM))("+TIME_12_OR_24_PATTERN+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))("+TIME_12_OR_24_PATTERN+")[ ]("+DateParser.getGeneralPattern()+")"; //
		
		Pattern pFromTimeDateToTimeDate = Pattern.compile(FROM_TIME_DATE_TO_TIME_DATE);
		Matcher mFromTimeDateToTimeDate = pFromTimeDateToTimeDate.matcher("from 5pm 13/3 to 6pm 15/3");
		
		if(.matches(FROM_TIME_DATE_TO_TIME_DATE))
			System.out.println("match!");
		else
			System.out.println("no match!");
	*/
	}
}
