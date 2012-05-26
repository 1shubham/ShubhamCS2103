package ParserPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser {
	/*
	public Task[] getTasks(String command) {
		
		return null;
	}*/
	
	/*
	 * make all helper functions non static
	 */
	private final static String RECUR_REGEX = "(?i)(weekly|monthly|yearly)";
	private final static String LABEL_REGEX = "@((\\s+)?(\\S+(\\s+)?,))+((\\s+)?(\\S+))";
	
	public static String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	
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
		
		startParsing (inputString);
		*/
		
		TimeParser timeParser = new TimeParser();
		
		if (timeParser.setStartTime("12:59pm")) {
			timeParser.printTimes();
		}
		else
			System.out.println("parsing not successful");
	}
	
	public static void startParsing(String inputS) {

		inputS = inputS.trim();
		
		if(checkImportant(inputS)) {
			System.out.println("IMPORTANT TASK!");
			inputS = inputS.replace('*', '\0');
			inputS = inputS.trim();
		}
		
		String recurString = getRecurString (inputS);
		
		if (recurString != null)
			System.out.println("this task is "+recurString);
		else
			System.out.println("this task is not recurring");
		
		
		inputS = inputS.replaceFirst(RECUR_REGEX, "");
		inputS = removeExtraSpaces(inputS);
		inputS = inputS.trim();
		
		System.out.println("left over string after checking for recurring: "+inputS);
		
		
		Vector<String> labelVector = getLabels(inputS);
		
		if(!labelVector.isEmpty()) {
			for (int i=0; i<labelVector.size(); i++)
				System.out.println("label "+i+": "+labelVector.get(i));
			
			inputS = inputS.replaceFirst(LABEL_REGEX, "");
			System.out.println("left over string after fetching labels: "+inputS);
		}
		
		TimeParser timeParser = new TimeParser();
		//DateParser dateParser = new DateParser();
		
		if(timeParser.extractStartEnd(inputS))
			System.out.println("time/date extracted!");
		else
			System.out.println("time/date NOT extracted!");
		
	}
	
	public static boolean checkImportant (String s) {
		if (s.startsWith("*")){
			//s = s.replace('*', '\0');
			//s = s.trim();
			return true;
		}
		return false;
	}
	
	public static String getRecurString (String s) {
		Pattern p = Pattern.compile(RECUR_REGEX);
		Matcher m = p.matcher(s);
		
		String recurString=null;
		
		if (m.find()) {
			recurString = m.group();
			//s = s.replaceFirst("(?i)(weekly|monthly|yearly)", "");
			//s = removeExtraSpaces(s);
		}
		
		return recurString;
	}
	
	public static Vector<String> getLabels(String s) {
		Pattern p = Pattern.compile(LABEL_REGEX);
		Matcher m = p.matcher(s);
		String labelString = null;
		Vector<String> labelVec = null;
		
		if(m.find()) {
			labelString = m.group();
			labelString.replace('@', '\0');
			labelString = labelString.trim();
			
			String[] labelArr = labelString.split("( )?,( )?");
			labelVec = new Vector<String>(Arrays.asList(labelArr));
		}
		
		return labelVec;
	}

}


