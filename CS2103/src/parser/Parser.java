package parser;

import data.Task;
import data.DateTime;
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
	private final String ID_REGEX = "(\\$\\$__)(\\d{2}-\\d{2}-\\d+[A-Z])(__\\$\\$)";
	private String command;
	
	boolean important;
	boolean deadline;
	DateTime startDateTime, endDateTime;
	String recurring = null;
	List<String> labelList = null;
	String taskDetails=null;
	
	public Parser () {
	}
	
	public String removeExtraSpaces (String s) {
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
	
	public void setDateTimeAttributes (TimeParser t, DateParser d) {
		//TimeParser t = new TimeParser();
		//DateParser d = new DateParser();
		boolean startDateExists, endDateExists, startTimeExists, endTimeExists;
		
		int[] startTimeArr = t.getStartTime();
		int[] endTimeArr = t.getEndTime();
		int[] startDateArr = d.getStartDate();
		int[] endDateArr = d.getEndDate();
		
		
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
	
		if (!startDateExists) 
			if (startTimeExists)
				startDateTime = new DateTime(startTimeArr[0],startTimeArr[1]);
		
		if (!endDateExists) 
			if (endTimeExists)
				endDateTime = new DateTime(endTimeArr[0],endTimeArr[1]);
		
		
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
	
	public String fetchTaskId (String inputS) {
		String id = null;
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		if(m.matches())
			id = m.group();
		
		return id;
	}
	
	public String[] fetchTaskIds (String inputS) {
		String[] ids = null;
		int i=0;
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		while (m.find()) {
			ids[i] = m.group();
			i++;
		}
			
		return ids;
	}
	
	public Task parse (String userCommand) {
		important=false;
		deadline=false;
		startDateTime=null; endDateTime=null;
		recurring = null;
		labelList = null;
		taskDetails="";
		
		//taskID=null;

		
		command = userCommand;
		command = command.trim();
		
		/*
		 * markImportant
		 */
		if(markImportant(command)) {
			System.out.println("IMPORTANT TASK!");
			command = command.replace('*', '\0');
			command = command.trim();
		}
		
		/*
		 * recurring ?
		 */	
		recurring = getRecurString (command);
		
		if (recurring != null)
			System.out.println("this task is "+recurring);
		else
			System.out.println("this task is not recurring");
		
		command = command.replaceFirst(RECUR_REGEX, "");
		command = removeExtraSpaces(command);
		command = command.trim();
		
		System.out.println("left over string after checking for recurring: "+command);
				
		/*
		 * setLabels
		 */

		String[] labelArr = getLabels (command);
		
		if(labelArr.length!=0) {
			int i=0;
			while(labelArr[i]!=null){
				System.out.println("label "+i+": "+labelArr[i]);
				command = command.replaceFirst(LABEL_REGEX, "");
				i++;
			}
			System.out.println("left over string after fetching labels: "+command);
		}
		
		TimeParser timeParser = new TimeParser();
		DateParser dateParser = new DateParser();
		
		if (extractDateTime(timeParser, dateParser))
			System.out.println("time/date extracted!");
		else
			System.out.println("time/date NOT extracted!");
		
		
		System.out.println();
		System.out.println();
		
		setDateTimeAttributes(timeParser, dateParser);
		
		
		if(important)
			System.out.println("is important!");
		else
			System.out.println("is NOT important!");
		
		
		
		if(recurring!=null)
			System.out.println("has to be done: "+recurring);
		else
			System.out.println("it is not recurring");
		
		System.out.println("task details: "+command);
		
		setDeadline ();
		
		if(deadline)
			System.out.println("this task has a deadline you dumbass!");
		else
			System.out.println("this task does NOT have deadline you numbskull!");
		
		List<String> labelList = Arrays.asList(labelArr);
		
		taskDetails = command;
		
		Task t = new Task(taskDetails,null,startDateTime,endDateTime,labelList,recurring,deadline,important);	
		
		return t;
	}
	
	public boolean extractDateTime (TimeParser timeParser, DateParser dateParser) {
		final String AT_TIME_DATE = "((at)|(AT))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String BY_TIME_DATE = "((by)|(BY))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String FROM_TIME_TO_TIME_DATE = "((from)|(FROM))("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String FROM_TIME_DATE_TO_TIME_DATE = "((from)|(FROM))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; //
		final String TIME_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String TIME_DATE_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; 
		final String AT_TIME = "((at)|(AT))("+TimeParser.getGeneralPattern()+")";
		final String BY_TIME = "((by)|(BY))("+TimeParser.getGeneralPattern()+")";
		final String FROM_TIME_TO_TIME = "((from)|(FROM))("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")";
		final String TIME_TO_TIME = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))("+TimeParser.getGeneralPattern()+")";
		final String TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		
		Pattern pAtTimeDate = Pattern.compile(AT_TIME_DATE);
		Pattern pByTimeDate = Pattern.compile(BY_TIME_DATE);
		Pattern pFromTimeToTimeDate = Pattern.compile(FROM_TIME_TO_TIME_DATE);
		Pattern pFromTimeDateToTimeDate = Pattern.compile(FROM_TIME_DATE_TO_TIME_DATE);
		Pattern pTimeToTimeDate = Pattern.compile(TIME_TO_TIME_DATE);
		Pattern pTimeDateToTimeDate = Pattern.compile(TIME_DATE_TO_TIME_DATE);
		Pattern pAtTime = Pattern.compile(AT_TIME);
		Pattern pByTime = Pattern.compile(BY_TIME);
		Pattern pFromTimeToTime = Pattern.compile(FROM_TIME_TO_TIME);
		Pattern pTimeToTime = Pattern.compile(TIME_TO_TIME);
		Pattern pTimeDate = Pattern.compile(TIME_DATE);
		Pattern pTime = Pattern.compile("("+TimeParser.getGeneralPattern()+")");//"((1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))");//TimeParser.getGeneralPattern());
		Pattern pDate = Pattern.compile("[ ]"+DateParser.getGeneralPattern());
		
		Matcher mAtTimeDate = pAtTimeDate.matcher(command);
		Matcher mByTimeDate = pByTimeDate.matcher(command);
		Matcher mFromTimeToTimeDate = pFromTimeToTimeDate.matcher(command);
		Matcher mFromTimeDateToTimeDate = pFromTimeDateToTimeDate.matcher(command);
		Matcher mTimeToTimeDate = pTimeToTimeDate.matcher(command);
		Matcher mTimeDateToTimeDate = pTimeDateToTimeDate.matcher(command);
		Matcher mAtTime = pAtTime.matcher(command);
		Matcher mByTime = pByTime.matcher(command);
		Matcher mFromTimeToTime = pFromTimeToTime.matcher(command);
		Matcher mTimeToTime = pTimeToTime.matcher(command);
		Matcher mTimeDate = pTimeDate.matcher(command);
		Matcher mTime = pTime.matcher(command);
		Matcher mDate = pDate.matcher(command);
		
		
		String startTimeString=null, startDateString=null, endTimeString=null, endDateString=null;
		
		command = removeExtraSpaces(command);
		
		if (mAtTimeDate.find()) {
			System.out.println("-----at_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mAtTimeDate.groupCount());
			for (int i=0; i<mAtTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mAtTimeDate.group(i));
			
			
			startTimeString = mAtTimeDate.group(4);
			startDateString = mAtTimeDate.group(17);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("start date string : "+startDateString);
			
			
			startDateString = startDateString.trim();
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			
			command = command.replaceFirst(AT_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByTimeDate.find()) {
			System.out.println("-----by_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mByTimeDate.groupCount());
			for (int i=0; i<mByTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mByTimeDate.group(i));
			
			
			endTimeString = mByTimeDate.group(4);
			
			
			
			System.out.println("end time string: "+endTimeString);
			
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("end time is set!");
			else
				System.out.println("end time could NOT be set!");
			
			
			
			endDateString = mByTimeDate.group(17);
			
			endDateString = endDateString.trim();
			
			System.out.println("end date string : "+endDateString);
			
			if (dateParser.setEndDate(endDateString)) 
				System.out.println("end date is set!");
			else
				System.out.println("end date could NOT be set!");
			
			command = command.replaceFirst(BY_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mFromTimeToTimeDate.find()) {
			System.out.println("-----from_time_to_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mFromTimeToTimeDate.groupCount());
			for (int i=0; i<mFromTimeToTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mFromTimeToTimeDate.group(i));
			
			
			startTimeString = mFromTimeToTimeDate.group(4);
			endTimeString = mFromTimeToTimeDate.group(16);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
			
			
			
			startDateString = mFromTimeToTimeDate.group(29);
			startDateString = startDateString.trim();
			endDateString = startDateString;
			
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				System.out.println("end date is set!");
			else
				System.out.println("end date could NOT be set!");
			
			command = command.replaceFirst(FROM_TIME_TO_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mFromTimeDateToTimeDate.find()) {
			System.out.println("-----from_time_date_to_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mFromTimeDateToTimeDate.groupCount());
			for (int i=0; i<mFromTimeDateToTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mFromTimeDateToTimeDate.group(i));
			
			
			startTimeString = mFromTimeDateToTimeDate.group(4);
			startDateString = mFromTimeDateToTimeDate.group(17);
			endTimeString = mFromTimeDateToTimeDate.group(53);
			endDateString = mFromTimeDateToTimeDate.group(66);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			System.out.println("start date string: "+startDateString);
			System.out.println("end date string: "+endDateString);
			
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				System.out.println("end date is set!");
			else
				System.out.println("end date could NOT be set!");
			
			
			command = command.replaceFirst(FROM_TIME_DATE_TO_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeToTimeDate.find()) {
			System.out.println("-----time_to_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mTimeToTimeDate.groupCount());
			for (int i=0; i<mTimeToTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mTimeToTimeDate.group(i));
			
			startTimeString = mTimeToTimeDate.group(1);
			endTimeString = mTimeToTimeDate.group(13);
			startDateString = mTimeToTimeDate.group(26);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			System.out.println("start date string: "+startDateString);
			
			
			
			startDateString = startDateString.trim();
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			
			
			command = command.replaceFirst(TIME_TO_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeDateToTimeDate.find()) {
			System.out.println("-----time_date_to_time_date format-------");
			
			//just for testing
			
			System.out.println("groups: "+mTimeDateToTimeDate.groupCount());
			for (int i=0; i<mTimeDateToTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mTimeDateToTimeDate.group(i));
			
			
			startTimeString = mTimeDateToTimeDate.group(1);
			startDateString = mTimeDateToTimeDate.group(14);
			endTimeString = mTimeDateToTimeDate.group(50);
			endDateString = mTimeDateToTimeDate.group(63);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			System.out.println("start date string: "+startDateString);
			System.out.println("end date string: "+endDateString);
			
			
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				System.out.println("end date is set!");
			else
				System.out.println("end date could NOT be set!");
			
			command = command.replaceFirst(TIME_DATE_TO_TIME_DATE, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mAtTime.find()) {
			System.out.println("-----at_time format-------");
			
			//just for testing
			
			System.out.println("groups: "+mAtTime.groupCount());
			for (int i=0; i<mAtTime.groupCount(); i++)
				System.out.println("group "+i+": "+mAtTime.group(i));
			
			startTimeString = mAtTime.group(4);
			System.out.println("start time string: "+startTimeString);
			
			
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			
			command = command.replaceFirst(AT_TIME, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByTime.find()) {
			System.out.println("-----by_time format-------");
			
			//just for testing
			
			System.out.println("groups: "+mByTime.groupCount());
			for (int i=0; i<mByTime.groupCount(); i++)
				System.out.println("group "+i+": "+mByTime.group(i));
			
			endTimeString = mByTime.group(4);
			System.out.println("end time string: "+endTimeString);
			
			
			
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("end time is set!");
			else
				System.out.println("end time could NOT be set!");
			
			command = command.replaceFirst(BY_TIME, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mFromTimeToTime.find()) {
			System.out.println("-----from_time_to_time format-------");
			
			//just for testing
			
			System.out.println("groups: "+mFromTimeToTime.groupCount());
			for (int i=0; i<mFromTimeToTime.groupCount(); i++)
				System.out.println("group "+i+": "+mFromTimeToTime.group(i));
			
			startTimeString = mFromTimeToTime.group(4);
			endTimeString = mFromTimeToTime.group(16);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			
			
			
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
		
			command = command.replaceFirst(FROM_TIME_TO_TIME, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeToTime.find()) {
			System.out.println("-----time_to_time format-------");
			
			//just for testing
			
			System.out.println("groups: "+mTimeToTime.groupCount());
			for (int i=0; i<mTimeToTime.groupCount(); i++)
				System.out.println("group "+i+": "+mTimeToTime.group(i));
			
			startTimeString = mTimeToTime.group(1);
			endTimeString = mTimeToTime.group(13);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("end time string: "+endTimeString);
			
			
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				System.out.println("End time is set!");
			else
				System.out.println("End time could NOT be set!");
		
			command = command.replaceFirst(TIME_TO_TIME, "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTime.find()) {
			System.out.println("-----time only format-------");
			
			System.out.println("groups: "+mTime.groupCount());
			for (int i=0; i<mTime.groupCount(); i++)
				System.out.println("group "+i+": "+mTime.group(i));
			
			startTimeString = mTime.group(0);
			
			System.out.println("start time string: "+startTimeString);
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			
			command = command.replaceFirst(TimeParser.getGeneralPattern(), "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mDate.find()) {
			System.out.println("-----date only format-------");
			
			System.out.println("groups: "+mDate.groupCount());
			for (int i=0; i<mDate.groupCount(); i++)
				System.out.println("group "+i+": "+mDate.group(i));
			
			startDateString = mDate.group(0);
			
			System.out.println("start date string: "+startDateString);
			
			startDateString = startDateString.trim();
			
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			
			command = command.replaceFirst(DateParser.getGeneralPattern(), "");
			command = removeExtraSpaces(command);
			
			return true;
		}
		/*
		else {
			System.out.println("-----none of the registered matches-------");
			
			startTimeString = timeParser.extractTime(command);
			startDateString = dateParser.extractDate(command);
			
			System.out.println("start time string: "+startTimeString);
			System.out.println("start date string: "+startDateString);
			
			if (startDateString==null&&endDateString==null&&startTimeString==null&&endTimeString==null)
				return false;
			else {
				command = command.replaceFirst(DateParser.getGeneralPattern(), "");
				command = command.replaceFirst(TimeParser.getGeneralPattern(), "");
				command = removeExtraSpaces(command);
				return true;
			}
		}
		*/
		return false;
		
	}
	
	public void dummyFunction() {
		String id = "$$__04-05-2012070000D__$$";
		
		if(id.matches(ID_REGEX))
			System.out.println("it matches!");
		else
			System.out.println("nope!");
	}

}

