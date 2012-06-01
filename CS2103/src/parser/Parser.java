package parser;

import data.Task;
import data.DateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser {
	private final  String RECUR_REGEX = "(?i)(weekly|monthly|yearly)";
	private final  String LABEL_REGEX = "@(\\w+)";
	private final String ID_REGEX = "(\\$\\$__)(\\d{2}-\\d{2}-\\d+[A-Z])(__\\$\\$)";
	
	boolean important;
	boolean deadline;
	DateTime startDateTime, endDateTime;
	String recurring = null;
	List<String> labelList = null;
	String taskDetails=null;
	
	private String command;
	
	public Parser () {
	}
	
	public void initAttributesDefault(String inputCommand) {
		important=false;
		deadline=false;
		startDateTime=null; endDateTime=null;
		recurring = null;
		labelList = null;
		taskDetails="";
		
		command = inputCommand;
		command = command.trim();
		removeExtraSpaces (command);	
	}
	
	public String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	
	public void setImportant () {
		if (command.startsWith("*")){
			command = command.replace('*', '\0');
			//s = s.trim();
			important = true;
		}
		//return s;
	}
	
	public void extractRecurString () {
		Pattern p = Pattern.compile(RECUR_REGEX);
		Matcher m = p.matcher(command);
		
		if (m.find()) {
			recurring = m.group();
			recurring = recurring.toLowerCase();
			command = command.replaceFirst(RECUR_REGEX, "");
			command = removeExtraSpaces(command);
			command = command.trim();
		}
		//return s;
	}
	
	public String[] getLabels() {
		Pattern p = Pattern.compile(LABEL_REGEX);
		Matcher m = p.matcher(command);
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
		labelList = Arrays.asList(labelArr);
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
		
		if(m.find())
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
		
		initAttributesDefault(userCommand);
		
		setImportant();
		
		//recurring?
		extractRecurString();
		
		if (recurring != null)
			System.out.println("this task is "+recurring);
		else
			System.out.println("this task is not recurring");
		
		System.out.println("left over string after checking for recurring: "+command);
				
		
		 //setLabels: have to change this function, check notes
		String[] labelArr = getLabels();
		
		if(labelArr.length!=0) {
			int i=0;
			while(labelArr[i]!=null){
				System.out.println("label "+i+": "+labelArr[i]);
				command = command.replaceFirst(LABEL_REGEX, "");
				i++;
			}
			System.out.println("left over string after fetching labels: "+command);
		}
		
		//time and date extraction
		TimeParser timeParser = new TimeParser();
		DateParser dateParser = new DateParser();
		
		if (extractDateTime(timeParser, dateParser))
			System.out.println("time/date extracted!");
		else
			System.out.println("time/date NOT extracted!");
		
		
		System.out.println();
		System.out.println();
		System.out.println("--------post extraction TESTING--------");
		
		
		setDateTimeAttributes(timeParser, dateParser);

		setDeadline ();
		
		if(deadline)
			System.out.println("this task has a deadline!");
		else
			System.out.println("this task does NOT have deadline!");
		
		if(important)
			System.out.println("is important!");
		else
			System.out.println("is NOT important!");
		
		if(recurring!=null)
			System.out.println("has to be done: "+recurring);
		else
			System.out.println("it is not recurring");
		
		
		taskDetails = command;
		System.out.println("task details: "+taskDetails);
		
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
		Pattern pTime = Pattern.compile(TimeParser.getGeneralPattern());//"((1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))"
		Pattern pDate = Pattern.compile(" "+DateParser.getGeneralPattern());
		
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
		//confirm the use of removeExtraSpaces
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
			
			command = mAtTimeDate.replaceFirst("");
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
			
			command = mByTimeDate.replaceFirst("");
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
			
			command = mFromTimeToTimeDate.replaceFirst("");
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
			
			
			command = mFromTimeDateToTimeDate.replaceFirst("");
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
			endDateString = startDateString;
			System.out.println("start date string: "+startDateString);
			System.out.println("end date string: "+endDateString);
			
			
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
				System.out.println("End date is set!");
			else
				System.out.println("End date could NOT be set!");
			
			
			command = mTimeToTimeDate.replaceFirst("");
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

			command = mTimeDateToTimeDate.replaceFirst("");
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

			command = mAtTime.replaceFirst("");
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

			command = mByTime.replaceFirst("");
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
		
			command = mFromTimeToTime.replaceFirst("");
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
		
			command = mTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeDate.find()) {
			System.out.println("-----time date only format-------");
			
			System.out.println("groups: "+mTimeDate.groupCount());
			for (int i=0; i<mTimeDate.groupCount(); i++)
				System.out.println("group "+i+": "+mTimeDate.group(i));
			
			startTimeString = mTimeDate.group(1);
			startDateString = mTimeDate.group(14);
			
			System.out.println("start time string: "+startTimeString);
			if (timeParser.setStartTime(startTimeString)) 
				System.out.println("Start time is set!");
			else
				System.out.println("Start time could NOT be set!");
			
			System.out.println("start date string: "+startDateString);
			if (dateParser.setStartDate(startDateString)) 
				System.out.println("Start date is set!");
			else
				System.out.println("Start date could NOT be set!");
			
			command = mTimeDate.replaceFirst("");
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
			
			command = mTime.replaceFirst("");
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
			
			command = mDate.replaceFirst("");
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

