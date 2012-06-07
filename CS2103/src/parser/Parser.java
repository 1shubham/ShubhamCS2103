package parser;

import data.Task;
import data.DateTime;

import java.util.ArrayList;
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
	
	private boolean important;
	private boolean deadline;
	private DateTime startDateTime, endDateTime;
	private String recurring = null;
	private List<String> labelList = null;
	private String taskDetails=null;
	
	private Task task;
	private String command;
	
	public Parser () {
	}
	/**
	 * 
	 * @param inputCommand
	 */
	public void initAttributesDefault(String inputCommand) {
		important=false;
		deadline=false;
		startDateTime=null; endDateTime=null;
		recurring = null;
		labelList = null;
		taskDetails=null;
		
		task=null;
		
		command = inputCommand;
		command = command.trim();
		removeExtraSpaces (command);	
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	/**
	 * 
	 */
	public void setImportant () {
		if (command.startsWith("*")){
			command = command.replace('*', '\0');
			//s = s.trim();
			important = true;
		}
		//return s;
	}
	/**
	 * 
	 */
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
	/**
	 * 
	 * @return
	 */
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
	/**
	 * 
	 * @param t
	 * @param d
	 */
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
	/**
	 * 
	 */
	public void setDeadline () {
		if (startDateTime==null && endDateTime!=null)
			deadline=true;	
	}
	/**
	 * 
	 * @param inputS
	 * @return
	 */
	public String fetchTaskId (String inputS) {
		String id = null;
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		if(m.find())
			id = m.group();
		
		return id;
	}
	/**
	 * 
	 * @param inputS
	 * @return
	 */
	public String[] fetchTaskIds (String inputS) {
		ArrayList<String> idList = new ArrayList<String>();
		
		Pattern p = Pattern.compile(ID_REGEX);
		Matcher m = p.matcher(inputS);
		
		while (m.find()) 
			idList.add(m.group());

		if (idList.isEmpty())
			return null;
		
		String []ids = idList.toArray(new String[idList.size()]);
		return ids;
	}
	
	public Task parseForSearch (String userCommand) {
		
		parse (userCommand);
		
		logger.debug("this is parse for SEARCH before initializing task obj");
		
		task = new Task(taskDetails,null,startDateTime,endDateTime,labelList,recurring,deadline,important);
		
		//logger.debug("task to string: "+task.toString());
		
		return task;
	}
	
	public Task parseForAdd (String userCommand) {
		
		parse (userCommand);
		
		logger.debug("this is parse for ADD before initializing task obj");
		
		if ((startDateTime!=null || endDateTime!=null) && !(taskDetails.isEmpty())) {
			/*
			logger.debug("startDateTime has something: "+(startDateTime!=null));
			logger.debug("endDateTime has something: "+(endDateTime!=null));
			logger.debug("task details:"+taskDetails);
			logger.debug("inside the if statement for add");
			*/
			task = new Task(taskDetails,null,startDateTime,endDateTime,labelList,recurring,deadline,important);	
		}
			
		//logger.debug("task to string: "+task.toString());
		
		return task;
	}
	
	private boolean extractDateTimeForSearch (TimeParser timeParser, DateParser dateParser) {
		final String DATE = "[ ]("+DateParser.getGeneralPattern()+")";
		final String TIME = "[ ]("+TimeParser.getGeneralPattern()+")";
		
		Pattern pDate = Pattern.compile(DATE);
		Pattern pTime = Pattern.compile(TIME);
		
		Matcher mDate = pDate.matcher(command);
		Matcher mTime = pTime.matcher(command);
		
		String startTimeString=null, startDateString=null, endTimeString=null, endDateString=null;
		command = removeExtraSpaces(command);
		
		if (command.matches(DateParser.getGeneralPattern())) {
			
		}
		
		else if (command.matches(TimeParser.getGeneralPattern())) {
			
		}
		
		
		/*
		 * matches
		 * finds
		 */
		
		return false;
	}
	/**
	 * 
	 * @param userCommand
	 * @return
	 */
	private void parse (String userCommand) {
		
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
		taskDetails = taskDetails.trim();
		
		logger.debug("task details: "+taskDetails);
		
		/*
		 * if the task returned is null, should not add it!
		 */
	}
	/**
	 * 
	 * @param timeParser
	 * @param dateParser
	 * @return
	 */
	private boolean extractDateTime (TimeParser timeParser, DateParser dateParser) {
		
		final String FROM_TIME_DATE_TO_TIME_DATE = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String FROM_DATE_TIME_TO_DATE_TIME = "((from)|(FROM))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")";
		final String TIME_DATE_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; 
		final String DATE_TIME_TO_DATE_TIME = "("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")"; 
		final String FROM_TIME_TO_TIME_DATE = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String FROM_DATE_TIME_TO_TIME = "((from)|(FROM))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String TIME_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String DATE_TIME_TO_TIME = "("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String FROM_TIME_TO_TIME = "((from)|(FROM))[ ]("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String FROM_DATE_TO_DATE = "((from)|(FROM))[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")"; 
		final String DATE_TO_DATE = "("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")";
		final String AT_TIME_DATE = "((at)|(AT))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String BY_TIME_DATE = "((by)|(BY))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String BY_DATE_TIME = "((by)|(BY))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")";
		final String TIME_TO_TIME = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		final String TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		final String DATE_TIME = "(("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+"))";
		
		final String AT_TIME = "((at)|(AT))[ ]("+TimeParser.getGeneralPattern()+")";
		final String BY_TIME = "((by)|(BY))[ ]("+TimeParser.getGeneralPattern()+")";
		final String BY_DATE = "((by)|(BY))[ ]("+DateParser.getGeneralPattern()+")";
		
		
		Pattern pFromDateTimeToDateTime = Pattern.compile(FROM_DATE_TIME_TO_DATE_TIME);
		Pattern pDateTimeToDateTime = Pattern.compile(DATE_TIME_TO_DATE_TIME);
		Pattern pFromDateTimeToTime = Pattern.compile(FROM_DATE_TIME_TO_TIME);
		Pattern pDateTimeToTime = Pattern.compile(DATE_TIME_TO_TIME);
		Pattern pFromDateToDate = Pattern.compile(FROM_DATE_TO_DATE);
		Pattern pDateToDate = Pattern.compile(DATE_TO_DATE);
		Pattern pByDate = Pattern.compile(BY_DATE);
		
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
		Pattern pDateTime = Pattern.compile(DATE_TIME);
		Pattern pByDateTime = Pattern.compile(BY_DATE_TIME);
		
		Pattern pOnlyTime = Pattern.compile("[ ]("+TimeParser.getGeneralPattern()+")");
		Pattern pOnlyDate = Pattern.compile("[ ]("+DateParser.getGeneralPattern()+")");
		Pattern pTimeForSearch = Pattern.compile(TimeParser.getGeneralPattern());//"((1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))"
		Pattern pDateForSearch = Pattern.compile(DateParser.getGeneralPattern());
		
		
		
		Matcher mFromDateTimeToDateTime = pFromDateTimeToDateTime.matcher(command);
		Matcher mDateTimeToDateTime = pDateTimeToDateTime.matcher(command);
		Matcher mFromDateTimeToTime = pFromDateTimeToTime.matcher(command);
		Matcher mDateTimeToTime = pDateTimeToTime.matcher(command);
		Matcher mFromDateToDate = pFromDateToDate.matcher(command);
		Matcher mDateToDate = pDateToDate.matcher(command);
	
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
		command = removeExtraSpaces(command);
		
		if (mDateForSearch.matches()) {
			logger.debug("-----date only FOR SEARCH format-------");
			
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
			
			startTimeString = mTimeForSearch.group(0);
			startTimeString = startTimeString.trim();
			command = mTimeForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
			logger.debug("start time string: "+startTimeString);
		}
		
		else if (mFromTimeDateToTimeDate.find()) {
			logger.debug("-----from_time_date_to_time_date format-------");
			
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
			
			command = mFromTimeDateToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateTimeToDateTime.find()) {
			logger.debug("-----from_date_time_to_date_time format-------");
			
			
			logger.debug("groups: "+mFromDateTimeToDateTime.groupCount());
			for (int i=0; i<mFromDateTimeToDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateTimeToDateTime.group(i));
			
			
			startTimeString = mFromDateTimeToDateTime.group(45);
			startDateString = mFromDateTimeToDateTime.group(8);
			endTimeString = mFromDateTimeToDateTime.group(98);
			endDateString = mFromDateTimeToDateTime.group(61);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateTimeToDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
		}
		
		else if (mFromTimeToTimeDate.find()) {
			logger.debug("-----from_time_to_time_date format-------");
			
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
				
			command = mFromTimeToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);	
		}
		
		else if (mFromDateTimeToTime.find()) {
			logger.debug("-----from_date_time_to_time format-------");
			
			
			logger.debug("groups: "+mFromDateTimeToTime.groupCount());
			for (int i=0; i<mFromDateTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateTimeToTime.group(i));
			
			
			startTimeString = mFromDateTimeToTime.group(45);
			startDateString = mFromDateTimeToTime.group(8);
			endTimeString = mFromDateTimeToTime.group(57);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mTimeDateToTimeDate.find()) {
			logger.debug("-----time_date_to_time_date format-------");
			
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
			
			command = mTimeDateToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateTimeToDateTime.find()) {
			logger.debug("-----date_time_to_date_time format-------");
			
			logger.debug("groups: "+mDateTimeToDateTime.groupCount());
			for (int i=0; i<mDateTimeToDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mDateTimeToDateTime.group(i));
			
			startTimeString = mDateTimeToDateTime.group(42);
			startDateString = mDateTimeToDateTime.group(5);
			endTimeString = mDateTimeToDateTime.group(95);
			endDateString = mDateTimeToDateTime.group(58);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mDateTimeToDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mTimeToTimeDate.find()) {
			logger.debug("-----time_to_time_date format-------");
			
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
			
			command = mTimeToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateTimeToTime.find()) {
			logger.debug("-----date_time_to_time format-------");
			
			
			logger.debug("groups: "+mDateTimeToTime.groupCount());
			for (int i=0; i<mDateTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mDateTimeToTime.group(i));
			
			startTimeString = mDateTimeToTime.group(42);
			startDateString = mDateTimeToTime.group(5);
			endTimeString = mDateTimeToTime.group(54);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mDateTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mAtTimeDate.find()) {
			logger.debug("-----at_time_date format-------");
			
			startTimeString = mAtTimeDate.group(4);
			startDateString = mAtTimeDate.group(17);	
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string : "+startDateString);
			
			command = mAtTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTimeDate.find()) {
			logger.debug("-----by_time_date format-------");
			
			endTimeString = mByTimeDate.group(4);
			endDateString = mByTimeDate.group(17);		
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("end date string : "+endDateString);
			
			command = mByTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDateTime.find()) {
			logger.debug("-----by_date_time format-------");
			
			endTimeString = mByDateTime.group(45);
			endDateString = mByDateTime.group(8);
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("end date string : "+endDateString);
			
			command = mByDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}

		else if (mFromTimeToTime.find()) {
			logger.debug("-----from_time_to_time format-------");
			
			startTimeString = mFromTimeToTime.group(4);
			endTimeString = mFromTimeToTime.group(16);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
		
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			
			command = mFromTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateToDate.find()) {
			logger.debug("-----from_date_to_date format-------");
			
			
			logger.debug("groups: "+mFromDateToDate.groupCount());
			for (int i=0; i<mFromDateToDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateToDate.group(i));
			
			startDateString = mFromDateToDate.group(8);
			endDateString = mFromDateToDate.group(48);
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateToDate.find()) {
			logger.debug("-----Date_to_date format-------");
			
			logger.debug("groups: "+mDateToDate.groupCount());
			for (int i=0; i<mDateToDate.groupCount(); i++)
				logger.debug("group "+i+": "+mDateToDate.group(i));
			
			startDateString = mDateToDate.group(5);
			endDateString = mDateToDate.group(45);
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mDateToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mTimeDate.find()) {
			logger.debug("-----time date only format-------");
			
			startTimeString = mTimeDate.group(1);
			startDateString = mTimeDate.group(14);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			command = mTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateTime.find()) {
			logger.debug("-----date time only format-------");
			
			startTimeString = mDateTime.group(43);
			startDateString = mDateTime.group(6);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			command = mDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mTimeToTime.find()) {
			logger.debug("-----time_to_time format-------");
			
			startTimeString = mTimeToTime.group(1);
			endTimeString = mTimeToTime.group(13);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();

			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			
			command = mTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mAtTime.find()) {
			logger.debug("-----at_time format-------");
			
			startTimeString = mAtTime.group(4);
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			command = mAtTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTime.find()) {
			logger.debug("-----by_time format-------");
			
			endTimeString = mByTime.group(4);
			endTimeString = endTimeString.trim();
			
			logger.debug("end time string: "+endTimeString);
			
			command = mByTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDate.find()) {
			logger.debug("-----by date format-------");
			
			endDateString = mByDate.group(8);
			endDateString = endDateString.trim();
			
			logger.debug("end date string: "+endDateString);
			
			command = mByDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mOnlyDate.find()) {
			logger.debug("-----date only format-------");
			
			startDateString = mOnlyDate.group(5);
			startDateString = startDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			
			command = mOnlyDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mOnlyTime.find()) {
			logger.debug("-----time only format-------");
			
			startTimeString = mOnlyTime.group(1);
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			command = mOnlyTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		if (startTimeString==null && endTimeString==null && startDateString==null && endDateString==null)
			return false;	
		
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
		
		return true;
	}

}

