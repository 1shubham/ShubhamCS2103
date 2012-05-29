package parser;

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
	
	private final  String RECUR_REGEX = "(?i)(weekly|monthly|yearly)";
	private final  String LABEL_REGEX = "@(\\w+)";
	private final String ID_REGEX = "($$__)(\\d{2}-\\d{2}-\\d{14}[A-Z])(__$$)"; //do u wanna check if its a valid YYYYMMDD thing between the crazy signs?
	
	boolean important;
	boolean deadline;
	DateTime startDateTime, endDateTime;
	String recurring = null;
	List<String> labelList = null;
	String taskDetails=null;
	
	public  String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	
	public boolean markImportant (String s) {
		if (s.startsWith("*")){
			//s = s.replace('*', '\0');
			//s = s.trim();
			important = true;
			return true;
		}
		return false;
	}
	
	public String getRecurString (String s) {
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
	
	public String[] getLabels(String s) {
		Pattern p = Pattern.compile(LABEL_REGEX);
		Matcher m = p.matcher(s);
		String labelString = null;
		String[] labelArr= new String[50];
		
		int i=0;
		while(m.find()) {
				labelString = m.group();
				labelString = labelString.replace('@',' ');
				labelString = labelString.trim();
				labelArr[i]=labelString;
				i++;
		}
		return labelArr;
	}
	
	public void setDateTimeAttributes () {
		TimeParser t = new TimeParser();
		DateParser d = new DateParser();
		boolean startDateExists, endDateExists, startTimeExists, endTimeExists;
		
		int[] startTimeArr = t.getStartTime();
		int[] endTimeArr = t.getEndTime();
		int[] startDateArr = d.getStartDate();
		int[] endDateArr = d.getEndDate();
		
		
		/*
		 * now it can construct DateTime objects with just dates too!
		 */
		
		startDateExists = ((startDateArr[0]>0) && (startDateArr[1]>0) && (startDateArr[2]>0));
		endDateExists = ((endDateArr[0]>0) && (endDateArr[1]>0) && (endDateArr[2]>0));
		startTimeExists = ((startTimeArr[0]>=0) && (startTimeArr[1]>=0));
		endTimeExists = ((endTimeArr[0]>=0) && (endTimeArr[1]>=0));
		
		if (startDateExists) {
			if (startTimeExists)
				startDateTime = new DateTime(startDateArr[2],startDateArr[1],startDateArr[0],startTimeArr[0],startTimeArr[1]);
			else
				startDateTime = new DateTime(startDateArr[2],startDateArr[1],startDateArr[0]);
		}
		
		if (endDateExists) {
			if (endTimeExists)
				endDateTime = new DateTime(endDateArr[2],endDateArr[1],endDateArr[0],endTimeArr[0],endTimeArr[1]);
			else
				endDateTime = new DateTime(endDateArr[2],endDateArr[1],endDateArr[0]);
		}
		/*
		 * tester print functions
		 */
		
		if (startDateTime!=null)
			System.out.println("start date time: "+startDateTime.formattedToString());
		
		if(endDateTime!=null)
			System.out.println("end date time: "+endDateTime.formattedToString());
	}
	
	public void setDeadline () {
		if (startDateTime==null && endDateTime!=null)
			deadline=true;
		
	}
	
	/*
	 * NOT TESTED!
	 */
	public String fetchTaskId (String inputS) {
		String Id = null;
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		if(m.matches())
			Id = m.group();
		
		return Id;
	}
	
	/*
	 * NOT TESTED!
	 */
	public String[] fetchTaskIds (String inputS) {
		String[] Ids = null;
		int i=0;
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		while (m.find()) {
			Ids[i] = m.group();
			i++;
		}
			
		return Ids;
	}
	
	public Task parse (String inputS) {
		
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
		 * recurring ?
		 */	
		String recurring = getRecurString (inputS);
		
		if (recurring != null)
			System.out.println("this task is "+recurring);
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
		
		
		TimeParser timeParser = new TimeParser(inputS);
		
		if(timeParser.extractStartEnd())
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
		
		System.out.println("task details: "+timeParser.getinputCommand());
		
		setDeadline ();
		
		if(deadline)
			System.out.println("this task has a deadline you dumbass!");
		else
			System.out.println("this task does NOT have deadline you numbskull!");
		
		List<String> labelList = Arrays.asList(labelArr);
		
		taskDetails = timeParser.getinputCommand();
		
		Task t = new Task(taskDetails,null,startDateTime,endDateTime,labelList,recurring,deadline,important);	
		
		return t;
	}
	
	

}

