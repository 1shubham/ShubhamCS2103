package ParserPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
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
	private final static String LABEL_REGEX = "@(\\w+)";
	
	static boolean important;
	static boolean deadline;
	static DateTime startDateTime, endDateTime;
	static String recurring = null;
	static List<String> labelList = null;
	static String taskDetails=null;
	
	public static String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	
	public static void main (String args[]) {
		
		String inputS = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Input string:");
		try {
			inputS = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
		
		
		/*
		 * this will be parser function
		 * for deadline, u will have to check if byDATETIME exists
		 */
		/*
		TimeParser timeParser = new TimeParser();
		
		if (timeParser.setStartTime("23.40")) {
			timeParser.printTimes();
		}
		else
			System.out.println("parsing not successful");
			*/
		

		inputS = inputS.trim();
		
		
		/*
		 * markImportant
		 */
		if(markImportant(inputS)) {
			System.out.println("IMPORTANT TASK!");
			inputS = inputS.replace('*', '\0');
			inputS = inputS.trim();
		}
		
		
		
		/*
		 * recurring 
		 */	
		String recurString = getRecurString (inputS);
		
		if (recurString != null)
			System.out.println("this task is "+recurString);
		else
			System.out.println("this task is not recurring");
		
		inputS = inputS.replaceFirst(RECUR_REGEX, "");
		inputS = removeExtraSpaces(inputS);
		inputS = inputS.trim();
		
		System.out.println("left over string after checking for recurring: "+inputS);
		
		
		
		/*
		 * setLabels
		 */

		String[] labelArr = getLabels (inputS);
		
		if(labelArr.length!=0) {
			int i=0;
			while(labelArr[i]!=null){
				System.out.println("label "+i+": "+labelArr[i]);
				inputS = inputS.replaceFirst(LABEL_REGEX, "");
				i++;
			}
			System.out.println("left over string after fetching labels: "+inputS);
		}
		//--------YET TO SET TO LIST-------
		//labelList = toList(labelVector);
		
		
		
		TimeParser timeParser = new TimeParser();
		//DateParser dateParser = new DateParser();
		
		if(timeParser.extractStartEnd(inputS))
			System.out.println("time/date extracted!");
		else
			System.out.println("time/date NOT extracted!");
		
		

		System.out.println();
		System.out.println();
		setDateTimeAttributes();
		if(important)
			System.out.println("is important!");
		else
			System.out.println("is NOT important!");
		if(recurring!=null)
			System.out.println("has to be done: "+recurring);
		else
			System.out.println("it is not recurring");
		if(recurring!=null)
			System.out.println("has to be done: "+recurring);
		else
			System.out.println("it is not recurring");
		
		System.out.println("task details: "+inputS);
		
		
		

	}
	
	public static void startParsing(String inputS) {

	}
	
	public static boolean markImportant (String s) {
		if (s.startsWith("*")){
			//s = s.replace('*', '\0');
			//s = s.trim();
			important = true;
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
			recurString = recurString.toLowerCase();
			//s = s.replaceFirst("(?i)(weekly|monthly|yearly)", "");
			//s = removeExtraSpaces(s);
		}
		
		return recurString;
	}
	
	public static String[] getLabels(String s) {
		Pattern p = Pattern.compile(LABEL_REGEX);
		Matcher m = p.matcher(s);
		String labelString = null;
		String[] labelArr= new String[50];
		
		int i=0;
		while(m.find()) {
				labelString = m.group();
				labelString.replace('@',' ');
				labelString = labelString.trim();
				labelArr[i]=labelString;
				i++;
		}
		return labelArr;
	}
	
	public static void setDateTimeAttributes () {
		TimeParser t = new TimeParser();
		DateParser d = new DateParser();
		boolean startDateTimeExists, endDateTimeExists;
		
		int[] startTimeArr = t.getStartTime();
		int[] endTimeArr = t.getEndTime();
		int[] startDateArr = d.getStartDate();
		int[] endDateArr = d.getEndDate();
		
		startDateTimeExists = ((startTimeArr[0]>=0) && (startTimeArr[1]>=0) && (startDateArr[0]>0) && (startDateArr[1]>0) && (startDateArr[2]>0));
		
		if (startDateTimeExists) {
			startDateTime = new DateTime(startDateArr[2],startDateArr[1],startDateArr[0],startTimeArr[0],startTimeArr[1]);
		}
		
		endDateTimeExists = ((endTimeArr[0]>=0) && (endTimeArr[1]>=0) && (endDateArr[0]>0) && (endDateArr[1]>0) && (endDateArr[2]>0));
		
		if (endDateTimeExists) {
			endDateTime = new DateTime(endDateArr[2],endDateArr[1],endDateArr[0],endTimeArr[0],endTimeArr[1]);
		}
		
		/*
		 * tester print functions
		 */
		
		System.out.println("start date time: "+startDateTime.formattedToString());
		System.out.println("end date time: "+endDateTime.formattedToString());
	}

}


