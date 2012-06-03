package parser;

import data.Task;
import data.DateTime;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import logic.JIDLogic;

import org.apache.log4j.Logger;

public class Parser {
	private final  String RECUR_REGEX = "(?i)(weekly|monthly|yearly)";
	private final  String LABEL_REGEX = "@(\\w+)";
	private final String ID_REGEX = "(\\$\\$__)(\\d{2}-\\d{2}-\\d+[A-Z])(__\\$\\$)";
	private Logger logger=Logger.getLogger(JIDLogic.class);
	
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
				labelString = labelString.replace('@',' ');//why not replace by null?
				labelString = labelString.trim();
				labelArr[i]=labelString;
				i++;
		}
		labelList = Arrays.asList(labelArr);
		return labelArr;
	}
	
	public void setDateTimeAttributes (TimeParser t, DateParser d) {
		
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
			logger.debug("start date time: "+startDateTime.formattedToString());
		
		if(endDateTime!=null)
			logger.debug("end date time: "+endDateTime.formattedToString());
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
		String[] ids = new String[50];
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
			logger.debug("this task is "+recurring);
		else
			logger.debug("this task is not recurring");
		
		logger.debug("left over string after checking for recurring: "+command);
				
		
		 //setLabels: have to change this function, check notes
		String[] labelArr = getLabels();
		
		if(labelArr.length!=0) {
			int i=0;
			while(labelArr[i]!=null){
				logger.debug("label "+i+": "+labelArr[i]);
				command = command.replaceFirst(LABEL_REGEX, "");
				i++;
			}
			logger.debug("left over string after fetching labels: "+command);
		}
		
		//time and date extraction
		TimeParser timeParser = new TimeParser();
		DateParser dateParser = new DateParser();
		
		if (extractDateTime(timeParser, dateParser))
			logger.debug("time/date extracted!");
		else
			logger.debug("time/date NOT extracted!");
		
		
		
		logger.debug("--------post extraction TESTING--------");
		
		
		setDateTimeAttributes(timeParser, dateParser);

		setDeadline ();
		
		if(deadline)
			logger.debug("this task has a deadline!");
		else
			logger.debug("this task does NOT have deadline!");
		
		if(important)
			logger.debug("is important!");
		else
			logger.debug("is NOT important!");
		
		if(recurring!=null)
			logger.debug("has to be done: "+recurring);
		else
			logger.debug("it is not recurring");
		
		
		taskDetails = command;
		
		logger.debug("task details: "+taskDetails);
		
		Task t = new Task(taskDetails,null,startDateTime,endDateTime,labelList,recurring,deadline,important);	
		
		return t;
	}
	
	public boolean extractDateTime (TimeParser timeParser, DateParser dateParser) {
		final String AT_TIME_DATE = "((at)|(AT))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String BY_TIME_DATE = "((by)|(BY))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String BY_DATE_TIME = "((by)|(BY))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")";
		final String FROM_TIME_TO_TIME_DATE = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String FROM_TIME_DATE_TO_TIME_DATE = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; //
		final String TIME_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String TIME_DATE_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; 
		final String AT_TIME = "((at)|(AT))[ ]("+TimeParser.getGeneralPattern()+")";
		final String BY_TIME = "((by)|(BY))[ ]("+TimeParser.getGeneralPattern()+")";
		final String FROM_TIME_TO_TIME = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String TIME_TO_TIME = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String DATE_TIME = "(("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+"))";
		
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
		Pattern pOnlyTime = Pattern.compile("[ ]("+TimeParser.getGeneralPattern()+")");
		Pattern pOnlyDate = Pattern.compile("[ ]("+DateParser.getGeneralPattern()+")");
		Pattern pTimeForSearch = Pattern.compile(TimeParser.getGeneralPattern());//"((1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))"
		Pattern pDateForSearch = Pattern.compile(DateParser.getGeneralPattern());
		Pattern pByDate = Pattern.compile("((by)|(BY))[ ]("+DateParser.getGeneralPattern()+")");
		Pattern pDateTime = Pattern.compile(DATE_TIME);
		Pattern pByDateTime = Pattern.compile(BY_DATE_TIME);
		
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
		Matcher mOnlyTime = pOnlyTime.matcher(command);
		Matcher mOnlyDate = pOnlyDate.matcher(command);
		Matcher mTimeForSearch = pTimeForSearch.matcher(command);
		Matcher mDateForSearch = pDateForSearch.matcher(command);
		Matcher mByDate = pByDate.matcher(command);
		Matcher mDateTime = pDateTime.matcher(command);
		Matcher mByDateTime = pByDateTime.matcher(command);
		
		String startTimeString=null, startDateString=null, endTimeString=null, endDateString=null;
		//confirm the use of removeExtraSpaces
		command = removeExtraSpaces(command);

		if (mDateForSearch.matches()) {
			logger.debug("-----date only FOR SEARCH format-------");
			/*
			logger.debug("groups: "+mDate.groupCount());
			for (int i=0; i<mDate.groupCount(); i++)
				logger.debug("group "+i+": "+mDate.group(i));
			*/
			startDateString = mDateForSearch.group(4);
			
			startDateString = startDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			
			command = mDateForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeForSearch.matches()) {
			logger.debug("-----time only FOR SEARCH format-------");
			/*
			logger.debug("groups: "+mTime.groupCount());
			for (int i=0; i<mTime.groupCount(); i++)
				logger.debug("group "+i+": "+mTime.group(i));
			*/
			startTimeString = mTimeForSearch.group(0);
			
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			
			command = mTimeForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}

		else if (mFromTimeDateToTimeDate.find()) {
			logger.debug("-----from_time_date_to_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mFromTimeDateToTimeDate.groupCount());
			for (int i=0; i<mFromTimeDateToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeDateToTimeDate.group(i));
			*/
			
			startTimeString = mFromTimeDateToTimeDate.group(4);
			startDateString = mFromTimeDateToTimeDate.group(17);
			endTimeString = mFromTimeDateToTimeDate.group(53);
			endDateString = mFromTimeDateToTimeDate.group(66);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");
			
			
			command = mFromTimeDateToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		

		else if (mFromTimeToTimeDate.find()) {
			logger.debug("-----from_time_to_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mFromTimeToTimeDate.groupCount());
			for (int i=0; i<mFromTimeToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeToTimeDate.group(i));
			*/
			
			startTimeString = mFromTimeToTimeDate.group(4);
			endTimeString = mFromTimeToTimeDate.group(16);
			startDateString = mFromTimeToTimeDate.group(29);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");
			
			command = mFromTimeToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		

		else if (mTimeToTimeDate.find()) {
			logger.debug("-----time_to_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mTimeToTimeDate.groupCount());
			for (int i=0; i<mTimeToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeToTimeDate.group(i));
			*/
			startTimeString = mTimeToTimeDate.group(1);
			endTimeString = mTimeToTimeDate.group(13);
			startDateString = mTimeToTimeDate.group(26);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;

			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("End date is set!");
			else
				logger.debug("End date could NOT be set!");
			
			
			command = mTimeToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}

		else if (mTimeDateToTimeDate.find()) {
			logger.debug("-----time_date_to_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mTimeDateToTimeDate.groupCount());
			for (int i=0; i<mTimeDateToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeDateToTimeDate.group(i));
			*/
			
			startTimeString = mTimeDateToTimeDate.group(1);
			startDateString = mTimeDateToTimeDate.group(14);
			endTimeString = mTimeDateToTimeDate.group(50);
			endDateString = mTimeDateToTimeDate.group(63);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
				
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");

			command = mTimeDateToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mAtTimeDate.find()) {
			logger.debug("-----at_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mAtTimeDate.groupCount());
			for (int i=0; i<mAtTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mAtTimeDate.group(i));
			*/
			
			startTimeString = mAtTimeDate.group(4);
			startDateString = mAtTimeDate.group(17);
			
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string : "+startDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			
			command = mAtTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByTimeDate.find()) {
			logger.debug("-----by_time_date format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mByTimeDate.groupCount());
			for (int i=0; i<mByTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mByTimeDate.group(i));
			*/
			
			endTimeString = mByTimeDate.group(4);
			endDateString = mByTimeDate.group(17);
			
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("end date string : "+endDateString);
			
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("end time is set!");
			else
				logger.debug("end time could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");
			
			command = mByTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByDateTime.find()) {
			logger.debug("-----by_date_time format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mByDateTime.groupCount());
			for (int i=0; i<mByDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mByDateTime.group(i));
			*/
			
			endTimeString = mByDateTime.group(45);
			endDateString = mByDateTime.group(8);
			
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("end date string : "+endDateString);
			
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("end time is set!");
			else
				logger.debug("end time could NOT be set!");
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");
			
			command = mByDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		

		else if (mTimeDate.find()) {
			logger.debug("-----time date only format-------");
			/*
			logger.debug("groups: "+mTimeDate.groupCount());
			for (int i=0; i<mTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeDate.group(i));
			*/
			startTimeString = mTimeDate.group(1);
			startDateString = mTimeDate.group(14);
			
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			
			command = mTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mDateTime.find()) {
			logger.debug("-----date time only format-------");
			
			logger.debug("groups: "+mDateTime.groupCount());
			for (int i=0; i<mDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mDateTime.group(i));
			
			startTimeString = mDateTime.group(43);
			startDateString = mDateTime.group(6);
			
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			
			command = mDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mFromTimeToTime.find()) {
			logger.debug("-----from_time_to_time format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mFromTimeToTime.groupCount());
			for (int i=0; i<mFromTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeToTime.group(i));
			*/
			startTimeString = mFromTimeToTime.group(4);
			endTimeString = mFromTimeToTime.group(16);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
		
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
		
			command = mFromTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeToTime.find()) {
			logger.debug("-----time_to_time format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mTimeToTime.groupCount());
			for (int i=0; i<mTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeToTime.group(i));
			*/
			startTimeString = mTimeToTime.group(1);
			endTimeString = mTimeToTime.group(13);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();

			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("End time is set!");
			else
				logger.debug("End time could NOT be set!");
		
			command = mTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mAtTime.find()) {
			logger.debug("-----at_time format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mAtTime.groupCount());
			for (int i=0; i<mAtTime.groupCount(); i++)
				logger.debug("group "+i+": "+mAtTime.group(i));
			*/
			startTimeString = mAtTime.group(4);
				
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");

			command = mAtTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByTime.find()) {
			logger.debug("-----by_time format-------");
			
			//just for testing
			/*
			logger.debug("groups: "+mByTime.groupCount());
			for (int i=0; i<mByTime.groupCount(); i++)
				logger.debug("group "+i+": "+mByTime.group(i));
			*/
			endTimeString = mByTime.group(4);
			
			endTimeString = endTimeString.trim();
			
			logger.debug("end time string: "+endTimeString);
			
			if (timeParser.setEndTime(endTimeString)) 
				logger.debug("end time is set!");
			else
				logger.debug("end time could NOT be set!");

			command = mByTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mByDate.find()) {
			logger.debug("-----by date format-------");
			/*
			logger.debug("groups: "+mByDate.groupCount());
			for (int i=0; i<mByDate.groupCount(); i++)
				logger.debug("group "+i+": "+mByDate.group(i));
			*/
			endDateString = mByDate.group(8);
			
			endDateString = endDateString.trim();
			
			logger.debug("end date string: "+endDateString);
			
			if (dateParser.setEndDate(endDateString)) 
				logger.debug("end date is set!");
			else
				logger.debug("end date could NOT be set!");
			
			command = mByDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mOnlyDate.find()) {
			logger.debug("-----date only format-------");
			/*
			logger.debug("groups: "+mOnlyDate.groupCount());
			for (int i=0; i<mOnlyDate.groupCount(); i++)
				logger.debug("group "+i+": "+mOnlyDate.group(i));
			*/
			startDateString = mOnlyDate.group(5);
			
			startDateString = startDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			
			if (dateParser.setStartDate(startDateString)) 
				logger.debug("Start date is set!");
			else
				logger.debug("Start date could NOT be set!");
			
			command = mOnlyDate.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mOnlyTime.find()) {
			logger.debug("-----time only format-------");
			/*
			logger.debug("groups: "+mTime.groupCount());
			for (int i=0; i<mTime.groupCount(); i++)
				logger.debug("group "+i+": "+mTime.group(i));
			*/
			startTimeString = mOnlyTime.group(1);
			
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			if (timeParser.setStartTime(startTimeString)) 
				logger.debug("Start time is set!");
			else
				logger.debug("Start time could NOT be set!");
			
			command = mOnlyTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		/*
		else {
			logger.debug("-----none of the registered matches-------");
			
			startTimeString = timeParser.extractTime(command);
			startDateString = dateParser.extractDate(command);
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
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
		
		//if (startTimeString==null && endTimeString==null && startDateString==null && endDateString==null)
			return false;	
		
		
		
	}
	
	public void dummyFunction() {
		String id = "$$__04-05-2012070000D__$$";
		
		if(id.matches(ID_REGEX))
			logger.debug("it matches!");
		else
			logger.debug("nope!");
	}

}

