import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;

public class ParserTestEngine1 {
	
public static void main (String args[]) {
		
		String inputString = null;
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		String cmdString=null, startTimeString=null, endTimeString=null, dateString=null, taskDetailsString=null;
		/*
		 * throw exception? if these strings are left null by the end of the program
		 */
		
		
		System.out.print("Input string:");
		try {
			inputString = reader.readLine();
		}
		catch(IOException ioe) {
			System.out.println("An unexpected error occured for your input");
		}
		
		inputString = inputString.trim();
		
		Time12HourTester test12hour = new Time12HourTester();
		Time24HourTester test24hour = new Time24HourTester();
		
		DateIsoValidator isoDateTester = new DateIsoValidator();
		
		//isoDateTester.dummyFunction(inputString);
		
		if (isoDateTester.validateGeneral(inputString)) {
			System.out.println("The date format is acceptable. :)");
			
			if (isoDateTester.validateMonthInDigitWithYear(inputString))
				System.out.println("You specified a year and input month in digits!");
			if (isoDateTester.validateMonthInDigitWithoutYear(inputString))
				System.out.println("You did NOT specify a year and input month in digits!");
			if (isoDateTester.validateMonthInTextWithYear(inputString))
				System.out.println("You specified a year and input month in text!");
			if (isoDateTester.validateMonthInTextWithoutYear(inputString))
				System.out.println("You did NOT specify a year and input month in text!");
		}
		else
			System.out.println("The date is not in an acceptable format.");
		
		/*
		if(test12hour.isValid(inputString))
			System.out.println("valid 12 hour");
		else
			System.out.println("invalid 12 hour");
		
		if(test24hour.isValid(inputString))
			System.out.println("valid 24 hour");
		else
			System.out.println("invalid 24 hour");
		*/
		
		/*
		 *
		String[] tokenArr = inputString.split("\\s");
		Vector<String> tokenVector = new Vector<String>(Arrays.asList(tokenArr));
		
		cmdString = extractCmd(tokenVector);
		
		removeSpaceAMPM (tokenVector);
		
		if (extractFromToFormat(tokenVector, startTimeString, endTimeString, dateString, taskDetailsString)) {
			
			 //if above is true, all these strings would have some values by now. 
			 //now pass the individual strings to execute the relevant functions
			 
			System.out.println("Start time:" +startTimeString);
			System.out.println("End time:" +endTimeString);
			System.out.println("Date:" +dateString);
			System.out.println("Task Details:" +taskDetailsString);
		}
		
		
		//if the above statement is not true, input in not in fromTo format
		 
		
		
		for(int i=0; i<tokenVector.size(); i++)
			System.out.println("element at "+i+"th position is:"+tokenVector.get(i));
		
		
		//System.out.println("The start time is:"+startTimeString);
		
		
		CmdTester testerCmd = new CmdTester();
		
		if(testerCmd.isValid(cmdString)) {
			cmdString = cmdString.toUpperCase();
			
			//pass this command string to operators to process it
			
			System.out.println("command string:"+cmdString);
		}
		
		else
			System.out.println("Invalid command used!");
		
		*/
		
		
		
		
		
		
		//parsedString = parsedString.substring(indexOfFirstSpace+1);
		//System.out.println("updated String:"+parsedString);
		
		/*
		 * from & to parsing
		 */
		
		/*
		if (parsedString.contains("from")) {
			if (parsedString.contains("to")) {
				startIndexOfStartTime = parsedString.indexOf("from") + 4;
				endIndexOfStartTime = parsedString.indexOf("to") - 1;
				startTimeString = parsedString.substring(startIndexOfStartTime, endIndexOfStartTime);
				System.out.println("start time string:"+startTimeString);
				
				
				startIndexOfEndTime = parsedString.indexOf("to") + 3;
				endIndexOfEndTime = parsedString.indexOf(" ");
				startTimeString = parsedString.substring(startIndexOfEndTime, endIndexOfEndTime);
				System.out.println("start time string:"+startTimeString);
				
				
				dummyString1 = parsedString.substring(0, parsedString.indexOf("from")-1);
				dummyString2 = parsedString.substring(parsedString.indexOf("to")+3);
				parsedString = dummyString1.concat(dummyString2);
				System.out.println("updated String:"+parsedString);
				
			}
			else
				System.out.println("If you specify 'from', you have to specify 'to'!");
		}
		*/
		
		/*
		if (testerCmd.isValid(input))
			System.out.println("VALID command!");
		else
			System.out.println("INVALID command!");
		*/
		
	}

	public static void removeSpaceAMPM (Vector<String> v) {
		String AM_PM_REGEX = "(?i)(am|pm)";
		String s=null;
		
		for(int i=0; i<v.size(); i++) {
			if (v.get(i).matches(AM_PM_REGEX)) {
				s = v.get(i-1) + v.get(i);
				v.remove(i-1);
				v.remove(i-1);
				v.add(i-1, s);		
			}			
		}		
	}
	
	public static String extractCmd (Vector<String> v) {
		String s = v.get(0);
		v.remove(0);
		return s;
	}
	
	public static boolean extractFromToFormat (Vector<String> v, String startTime, String endTime, String date, String taskDetails) {
		int indexFROM = -1, indexTO = -1, indexON = -1, currIndex;
		
		indexFROM = v.indexOf("from");
		indexTO = v.indexOf("to");
		
		if (indexFROM<0 || indexTO<0)
			return false;
		
		currIndex = indexFROM;
		
		if (indexTO - indexFROM==2) {
			startTime = v.get(currIndex+1);	
			v.remove(currIndex);
			v.remove(currIndex);
			v.remove(currIndex);
		}
		
		else
			System.out.println("Task Start Time incorrectly formatted or does not exist"); //throw exception?
		

		System.out.println("Inside function, Start time:" +startTime);
		
		endTime = v.get(currIndex);
		v.remove(currIndex);
		
		System.out.println("Inside function, End time:" +endTime);
		
		indexON = v.indexOf("on");
		
		if (indexON>0) //user specifies "on" <date>
			v.remove(indexON); //junk that string
			
		date = v.get(currIndex);
		v.remove(currIndex);
		
		System.out.println("Inside function, date:" +date);
		
		/*
		 * rest whatever is left are job details
		 */
		
		taskDetails = v.get(0);
		while (!v.isEmpty()) {
			taskDetails = taskDetails.concat(v.get(0));
			v.remove(0);
		}
		
		System.out.println("Inside function, Task details:" +taskDetails);
			
		/*
		 * empty vector?
		 */
		
		return true;	
		/*
		 * replace all "from" / "to" by regular expressions
		 */
	}
	



	public static String dumpInclusive (int start, int end) {
		String s = null;
		return s;
	}
}
