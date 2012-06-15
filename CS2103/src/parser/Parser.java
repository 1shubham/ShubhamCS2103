/**
 *
 * This class accumulates the parsing features of Jot It Down
 * -features Natural Language Processing for easy interaction of User with the software
 * -includes various helper parser functions such as email address validator, fetcher functions for other classes
 * -houses regular expressions for different formats the user can enter the input
 * 
 * @author Shubham Kaushal
 */

package parser;

import constant.OperationFeedback;
import data.Task;
import data.TaskDateTime;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import org.apache.log4j.Logger;

public class Parser {
	
	private final int RECUR_TIMES_CAP = 61;
	private final int DEFAULT_RECUR_TIMES = 10;
	private final String EMAIL_ADD = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private final String DONT_PARSE = "(\'(\\s?\\w+\\s?)*\')";
	private final String RECUR_REGEX = "((?i)(daily|weekly|monthly|yearly))[ ]?(-[ ]?(\\d+)[ ]?(times?)?)?";
	private final String LABEL_REGEX = "@(\\w+)";
	private final String ID_REGEX = "(\\$\\$__)(\\d{2}-\\d{2}-\\d+[A-Z])(__\\$\\$)";
	private final String G_CAL_DES = "<CMPT:(true|false)><IMPT:(true|false)><DEAD:(true|false)><RECUR:(daily|weekly|monthly|yearly)?><RECURID:("+ID_REGEX+")?><LABEL:((\\w+ )+)?>";
	private String FROM_TIME_DATE_TO_TIME_DATE;
	private String FROM_DATE_TIME_TO_DATE_TIME;
	private String FROM_TIME_TO_TIME_DATE;
	private String FROM_DATE_TIME_TO_TIME;
	private String FROM_TIME_TO_TIME;
	private String FROM_DATE_TO_DATE;
	private String AT_TIME_DATE;
	private String TIME_DATE;
	private String BY_TIME_DATE;
	private String BY_DATE_TIME;
	private String DATE_TIME;
	private String AT_TIME;
	private String BY_TIME;
	private String BY_DATE;
	private String FROM_DATE_TIME_TO_DATE;
	private String FROM_DATE_TO_DATE_TIME;
	private String FROM_TIME_DATE_TO_TIME;
	private String DATE_FROM_TIME_TO_TIME;
	
	private boolean important;
	private boolean deadline;
	private TaskDateTime startDateTime, endDateTime;
	private String recurring;
	private int recurringTimes;
	private ArrayList<String> labelList;
	private String taskDetails;
	String[] dontParse;
	String[] tempReplace;
	
	private String command;
	private OperationFeedback error;
	
	//private Logger logger=Logger.getLogger(Parser.class);
	
	/**
	 * Default constructor
	 */
	public Parser () {
	}
	/**Common Attributes Initializer
	 * 
	 * @param inputCommand
	 */
	private void initCommon(String inputCommand) {
		important=false;
		deadline=false;
		startDateTime=null; endDateTime=null;
		recurring = null;
		recurringTimes = -1;
		labelList = new ArrayList<String>();
		taskDetails=null;
		dontParse=null;
		tempReplace=null;
		
		error=OperationFeedback.VALID;
		
		command = inputCommand;
		command = command.trim();
		command = removeExtraSpaces (command);
		
	}
	/**Initializes REGEX strings for Add function
	 * 
	 * @param inputCommand
	 */
	private void initForAdd(String inputCommand) {
		initCommon(inputCommand);
		
		FROM_TIME_DATE_TO_TIME_DATE = "([ ]((?i)(from)))?[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_DATE_TIME = "([ ]((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_TO_TIME_DATE = "([ ]((?i)(from)))?[ ]("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_TIME = "([ ]((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_TO_TIME = "([ ]((?i)(from)))?[ ]("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		FROM_DATE_TO_DATE = "([ ]((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")"; 
		AT_TIME_DATE = "([ ]((?i)(at)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		TIME_DATE = "[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";						
		BY_TIME_DATE = "([ ]((?i)(by)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		BY_DATE_TIME = "([ ]((?i)(by)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		DATE_TIME = "([ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+"))";
		AT_TIME = "([ ]((?i)(at)))[ ]("+TimeParser.getGeneralPattern()+")";
		BY_TIME = "([ ]((?i)(by)))[ ]("+TimeParser.getGeneralPattern()+")";
		BY_DATE = "([ ]((?i)(by)))[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_DATE = "([ ]((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TO_DATE_TIME = "([ ]((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_DATE_TO_TIME = "([ ]((?i)(from)))?[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		DATE_FROM_TIME_TO_TIME = "[ ]("+DateParser.getGeneralPattern()+")([ ]((?i)(from)))?([ ]((?i)(at)))?[ ]("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
	}
	/**Initializes REGEX strings for Search function
	 * 
	 * @param inputCommand
	 */
	private void initForSearch(String inputCommand) {
		initCommon(inputCommand);
		
		FROM_TIME_DATE_TO_TIME_DATE = "(((?i)(from))[ ])?("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_DATE_TIME = "(((?i)(from))[ ])?("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_TO_TIME_DATE = "(((?i)(from))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_TIME = "(((?i)(from))[ ])?("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_TO_TIME = "(((?i)(from))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		FROM_DATE_TO_DATE = "(((?i)(from))[ ])?("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")"; 
		AT_TIME_DATE = "(((?i)(at))[ ])("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";						
		BY_TIME_DATE = "(((?i)(by)))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		BY_DATE_TIME = "(((?i)(by)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		DATE_TIME = "(("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+"))";
		AT_TIME = "(((?i)(at))[ ])("+TimeParser.getGeneralPattern()+")";
		BY_TIME = "(((?i)(by)))[ ]("+TimeParser.getGeneralPattern()+")";
		BY_DATE = "(((?i)(by)))[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TIME_TO_DATE = "(((?i)(from))[ ])?("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TO_DATE_TIME = "(((?i)(from))[ ])?("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_DATE_TO_TIME = "(((?i)(from))[ ])?("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
		DATE_FROM_TIME_TO_TIME = "("+DateParser.getGeneralPattern()+")([ ]((?i)(from)))?([ ]((?i)(at)))?[ ]("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
	}
	/**sets the local error code attribute
	 * 
	 * @param error code
	 */
	private void setErrorCode (OperationFeedback e) {
		error = e;
	}
	/**fetches the local error code attribute
	 * 
	 * @return error code
	 */
	public OperationFeedback getErrorCode (){
		return error;
	}
	/**Removes extra spaces in the input command
	 * 
	 * @param String s
	 * @return updated s
	 */
	private String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	/**Sets the boolean Important variable to be TRUE if important
	 */
	private void setImportant () {
		if (command.startsWith("*")){
			command = command.replace('*', '\0');
			important = true;
		}
	}
	/**Extracts the recurring string and how many times to repeat
	 */
	private void extractRecur () {
		Matcher m = Pattern.compile(RECUR_REGEX).matcher(command);
		
		if (m.find()) {
			recurring = m.group(2);
			recurring = recurring.toLowerCase();
			
			if (m.group(4)!=null)
				recurringTimes = Integer.parseInt(m.group(4));
			
			
			command = command.replaceFirst(m.group(), "");
			command = removeExtraSpaces(command);
			command = command.trim();
		}
	}
	/**Extracts the labels, if any, and sets them
	 */
	private void setLabels() {
		Pattern p = Pattern.compile(LABEL_REGEX);
		Matcher m = p.matcher(command);
		String labelString = null;
		
		while(m.find()) {
				labelString = m.group();
				labelString = labelString.replace('@','\0');
				labelString = labelString.trim();
				labelList.add(labelString);
		}	
	}
	/**sets the default date if not supplied by the user
	 * 
	 * @param TaskDateTime Obj
	 */
	private void setDefaultDateForAdd (TaskDateTime dtObj) {
		
		GregorianCalendar gcObj = new GregorianCalendar();
		
		if (dtObj.getTime().getTimeMilli() <= TaskDateTime.getCurrentDateTime().getTime().getTimeMilli())
			gcObj.add(GregorianCalendar.DATE, 1);
		
		int year = gcObj.get(GregorianCalendar.YEAR);
		int month =	gcObj.get(GregorianCalendar.MONTH) + 1;
		int day = gcObj.get(GregorianCalendar.DATE);
		
		dtObj.set(year, month, day);
		
	}
	/**Sets the DateTime attributes
	 * 
	 * @param TimeParser Obj
	 * @param DateParser Obj
	 */
	private void setLocalDateTime (TimeParser t, DateParser d) {
		
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
				startDateTime = new TaskDateTime(startDateArr[2],startDateArr[1],startDateArr[0],startTimeArr[0],startTimeArr[1]);
			else
				startDateTime = new TaskDateTime(startDateArr[2],startDateArr[1],startDateArr[0]);
		}
		
		if (endDateExists) {
			if (endTimeExists)
				endDateTime = new TaskDateTime(endDateArr[2],endDateArr[1],endDateArr[0],endTimeArr[0],endTimeArr[1]);
			else
				endDateTime = new TaskDateTime(endDateArr[2],endDateArr[1],endDateArr[0]);
		}
	
		if (!startDateExists) 
			if (startTimeExists)
				startDateTime = new TaskDateTime(startTimeArr[0],startTimeArr[1]);
		
		if (!endDateExists) 
			if (endTimeExists)
				endDateTime = new TaskDateTime(endTimeArr[0],endTimeArr[1]);
		
	}
	/**sets the start DateTime attribute based on the GregorianCalendar Obj
	 * 
	 * @param GregorianCalendar Obj
	 */
	private void setLocalStartDateTime (GregorianCalendar start) {
		startDateTime = new TaskDateTime (start.get(GregorianCalendar.YEAR), start.get(GregorianCalendar.MONTH)+1, start.get(GregorianCalendar.DATE), start.get(GregorianCalendar.HOUR_OF_DAY), start.get(GregorianCalendar.MINUTE), start.get(GregorianCalendar.SECOND));
	}
	/**sets the end DateTime attribute based on the GregorianCalendar Obj
	 * 
	 * @param GregorianCalendar Obj
	 */
	private void setLocalEndDateTime (GregorianCalendar end) {
		endDateTime = new TaskDateTime (end.get(GregorianCalendar.YEAR), end.get(GregorianCalendar.MONTH)+1, end.get(GregorianCalendar.DATE), end.get(GregorianCalendar.HOUR_OF_DAY), end.get(GregorianCalendar.MINUTE), end.get(GregorianCalendar.SECOND));
	}
	/**Sets the boolean deadline variable to TRUE if deadline should be set
	 */
	private void setDeadline () {
		if (startDateTime==null && endDateTime!=null)
			deadline=true;	
	}
	/**Extracts and returns a single task id found in inputString
	 * 
	 * @param String input
	 * @return String id 
	 */
	public String fetchTaskId (String inputS) {
		String id = null;
		Matcher m = Pattern.compile(ID_REGEX).matcher(inputS);
		
		if(m.find())
			id = m.group();
		
		return id;
	}
	/**Extracts and returns an array of task ids found in inputString
	 * 
	 * @param String input
	 * @return String[] ids
	 */
	public String[] fetchTaskIds (String inputS) {
		ArrayList<String> idList = new ArrayList<String>();
		Matcher m = Pattern.compile(ID_REGEX).matcher(inputS);
		
		while (m.find()) 
			idList.add(m.group());

		if (idList.isEmpty())
			return null;
		
		String []ids = idList.toArray(new String[idList.size()]);
		return ids;
	}
	/**
	 * extracts the strings in the user input which are not supposed to be parsed into the local attribute dontParse[]
	 */
	private void extractDontParseStrings()  {
		ArrayList<String> dontParseList = new ArrayList<String>();
		ArrayList<String> tempReplaceList = new ArrayList<String>();
		Matcher mDontParse = Pattern.compile(DONT_PARSE).matcher(command);
		String temp;
		
		while (mDontParse.find()) {
			dontParseList.add(mDontParse.group());
			
			temp = "&";
			for (int j=2; j<dontParseList.get(dontParseList.size()-1).length(); j++) {
				temp += "%";
			}
			temp += "&";
			tempReplaceList.add(temp);
			
			command = command.replaceFirst(dontParseList.get(dontParseList.size()-1), tempReplaceList.get(tempReplaceList.size()-1));
			command = removeExtraSpaces(command);
		
		}
		
		if (!dontParseList.isEmpty()) {
			dontParse = dontParseList.toArray(new String[dontParseList.size()]);
			tempReplace = tempReplaceList.toArray(new String[dontParseList.size()]);
		}
		
	}
	/**
	 * restores the strings in the user input which are not supposed to be parsed back to the task details
	 */
	private void restoreDontParseStrings () {
		for (int k=0; k<dontParse.length; k++)
			command = command.replaceFirst(tempReplace[k], dontParse[k].substring(1, dontParse[k].length()-1));
	}
	/**Returns a task[] based on the number of recurring times
	 * 
	 * @param number of recurring times
	 * @return Task[]
	 */
	private Task[] fetchTaskArray (int numRecurr) {
		
		ArrayList<Task> taskList = new ArrayList<Task> ();
		GregorianCalendar startDT =null;
		GregorianCalendar endDT =null;
		
		
		if (startDateTime!=null)
			startDT = new GregorianCalendar(startDateTime.get(GregorianCalendar.YEAR),startDateTime.get(GregorianCalendar.MONTH)-1,startDateTime.get(GregorianCalendar.DATE),startDateTime.get(GregorianCalendar.HOUR_OF_DAY),startDateTime.get(GregorianCalendar.MINUTE),startDateTime.get(GregorianCalendar.SECOND));
		if (endDateTime!=null)
			endDT = new GregorianCalendar(endDateTime.get(GregorianCalendar.YEAR),endDateTime.get(GregorianCalendar.MONTH)-1,endDateTime.get(GregorianCalendar.DATE),endDateTime.get(GregorianCalendar.HOUR_OF_DAY),endDateTime.get(GregorianCalendar.MINUTE),endDateTime.get(GregorianCalendar.SECOND));
		
		
		if (recurring==null)
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
		else if (recurring.matches("daily")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) { 
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 1);
					endDT.add(GregorianCalendar.DATE, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) {
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.DATE, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) { 
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 1);
					setLocalStartDateTime(startDT);
					
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
				
				}
			}
		}
		else if (recurring.matches("weekly")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) { 
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 7);
					endDT.add(GregorianCalendar.DATE, 7);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { 
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.DATE, 7);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) {
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 7);
					setLocalStartDateTime(startDT);
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
					
				}
			}
		}
		else if (recurring.matches("monthly")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) {
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.MONTH, 1);
					endDT.add(GregorianCalendar.MONTH, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { 
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.MONTH, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) {
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.MONTH, 1);
					setLocalStartDateTime(startDT);
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
		}
		else if (recurring.matches("yearly")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) {
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.YEAR, 1);
					endDT.add(GregorianCalendar.YEAR, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { 
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.YEAR, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) {
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.YEAR, 1);
					setLocalStartDateTime(startDT);
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
		}
		
		if (taskList.isEmpty())
			return null;
		else
			return taskList.toArray(new Task[taskList.size()]);
	}
	/**FOR SEARCH OPERATION
	 * creates a Task Obj based on local attributes to be returned to the Logic component
	 * 
	 * @param String userCommand
	 * @return Task Obj
	 */
	public Task parseForSearch (String userCommand) {
		
		initForSearch(userCommand);
		
		parse (userCommand);
		
		Task taskForSearch = new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important);

		return taskForSearch;
	}
	
	/**FOR ADD FUNCTIONS
	 * creates a Task Obj[] based on local attributes to be returned to the Logic component
	 * 
	 * @param String userCommand
	 * @return Task Obj[]
	 */
	public Task[] parseForAdd (String userCommand) {
		
		initForAdd(userCommand);
		
		parse (userCommand);
		
		Task[] taskArr=null;
		
		if (startDateTime!=null && !startDateTime.getHasDate()) {
			setDefaultDateForAdd (startDateTime);
		}
		
		if (endDateTime!=null && !endDateTime.getHasDate())
			setDefaultDateForAdd (endDateTime);
		
		if (startDateTime!=null && endDateTime!=null) {
			if (startDateTime.getTimeMilli() >= endDateTime.getTimeMilli())
				setErrorCode(OperationFeedback.START_DATE_TIME_MORE_THAN_END_DATE_TIME);
			if (startDateTime.getTimeMilli() <= TaskDateTime.getCurrentDateTime().getTimeMilli())
				setErrorCode(OperationFeedback.START_DATE_TIME_LESS_THAN_CURRENT_DATE_TIME);
		}
		else if(startDateTime==null && endDateTime!=null) {
			if (endDateTime.getTimeMilli() <= TaskDateTime.getCurrentDateTime().getTimeMilli())
				setErrorCode(OperationFeedback.END_DATE_TIME_LESS_THAN_CURRENT_DATE_TIME);		
		}
		else if (endDateTime==null && startDateTime!=null){
			if (startDateTime.getTimeMilli() <= TaskDateTime.getCurrentDateTime().getTimeMilli())
				setErrorCode(OperationFeedback.START_DATE_TIME_LESS_THAN_CURRENT_DATE_TIME);		
		}
		
		
		if (error==OperationFeedback.VALID) {
			if (recurringTimes>0) {
				if (recurringTimes < RECUR_TIMES_CAP) {
					taskArr = fetchTaskArray(recurringTimes);
				}
				else
					setErrorCode(OperationFeedback.RECURRING_TIMES_EXCEEDED);
			}
	
			if (recurringTimes<0 && error==OperationFeedback.VALID)
				taskArr = fetchTaskArray(DEFAULT_RECUR_TIMES);
			
		}
		
		
		return taskArr;
	}
	/**Returns a found GCal description in the inputString
	 * 
	 * @param inputString
	 * @return String representing GCal description
	 */
	public String[] fetchGCalDes (String input) {
		String[] arr= null;
		Matcher m = Pattern.compile(G_CAL_DES).matcher(input);
		
		if (m.matches()) {
	
			arr = new String[] {"","","","","",""};
		for (int i=0; i<5; i++) {
			if (m.group(i+1)!=null)
				arr[i] = m.group(i+1);
		}
		if (m.group(9)!=null)
			arr[5] = m.group(9);
		
		
		}
		
		return arr;
	}
	
	/**Understands the user input and sets local attributes for Task Obj to be created
	 * 
	 * @param String userCommand
	 */
	private void parse (String userCommand) {
	
		extractDontParseStrings();
		
		setImportant();
		
		extractRecur();
		
		setLabels();
		
		if(labelList.size()!=0) {
			
			for(int i=0;i<labelList.size();i++){
				command = command.replaceFirst(LABEL_REGEX, "");
			}
			command = command.trim();
		}
		
		TimeParser timeParser = new TimeParser();
		DateParser dateParser = new DateParser();
		
		extractDateTime(timeParser, dateParser);
		
		setLocalDateTime(timeParser, dateParser);
		setDeadline();
		
		if (dontParse!=null)
			restoreDontParseStrings();
		
		taskDetails = command;
		taskDetails = taskDetails.trim();
		
		if (taskDetails==null || taskDetails.isEmpty())
			setErrorCode(OperationFeedback.INVALID_TASK_DETAILS);
		
	}
	/**Extracts date and time from the user input and sets the local DateTime Attributes
	 * 
	 * @param timeParser Obj
	 * @param dateParser Obj
	 * @return TRUE/FALSE depending on whether DateTime attributes were set or not
	 */
	private boolean extractDateTime (TimeParser timeParser, DateParser dateParser) {
		
		Pattern pFromDateTimeToDateTime = Pattern.compile(FROM_DATE_TIME_TO_DATE_TIME);
		Pattern pFromDateTimeToTime = Pattern.compile(FROM_DATE_TIME_TO_TIME);
		Pattern pFromDateToDate = Pattern.compile(FROM_DATE_TO_DATE);
		Pattern pByDate = Pattern.compile(BY_DATE);
		
		Pattern pAtTimeDate = Pattern.compile(AT_TIME_DATE);
		Pattern pTimeDate = Pattern.compile(TIME_DATE);
		Pattern pByTimeDate = Pattern.compile(BY_TIME_DATE);
		Pattern pFromTimeToTimeDate = Pattern.compile(FROM_TIME_TO_TIME_DATE);
		Pattern pFromTimeDateToTimeDate = Pattern.compile(FROM_TIME_DATE_TO_TIME_DATE);
		Pattern pAtTime = Pattern.compile(AT_TIME);
		Pattern pByTime = Pattern.compile(BY_TIME);
		Pattern pFromTimeToTime = Pattern.compile(FROM_TIME_TO_TIME);
		Pattern pDateTime = Pattern.compile(DATE_TIME);
		Pattern pByDateTime = Pattern.compile(BY_DATE_TIME);
		Pattern pOnlyTime = Pattern.compile("[ ]("+TimeParser.getGeneralPattern()+")");
		Pattern pOnlyDate = Pattern.compile("[ ]("+DateParser.getGeneralPattern()+")");
		Pattern pTimeForSearch = Pattern.compile(TimeParser.getGeneralPattern());
		Pattern pDateForSearch = Pattern.compile(DateParser.getGeneralPattern());
		Pattern pFromTimeDateToTime = Pattern.compile(FROM_TIME_DATE_TO_TIME);
		Pattern pFromDateTimeToDate = Pattern.compile(FROM_DATE_TIME_TO_DATE);
		Pattern pFromDateToDateTime = Pattern.compile(FROM_DATE_TO_DATE_TIME);
		Pattern pDateFromTimeToTime = Pattern.compile(DATE_FROM_TIME_TO_TIME);
	
		Matcher mFromDateTimeToDateTime = pFromDateTimeToDateTime.matcher(command);
		Matcher mFromDateTimeToTime = pFromDateTimeToTime.matcher(command);
		Matcher mFromDateToDate = pFromDateToDate.matcher(command);
		Matcher mAtTimeDate = pAtTimeDate.matcher(command);
		Matcher mTimeDate = pTimeDate.matcher(command);
		Matcher mByTimeDate = pByTimeDate.matcher(command);
		Matcher mFromTimeToTimeDate = pFromTimeToTimeDate.matcher(command);
		Matcher mFromTimeDateToTimeDate = pFromTimeDateToTimeDate.matcher(command);
		Matcher mAtTime = pAtTime.matcher(command);
		Matcher mByTime = pByTime.matcher(command);
		Matcher mFromTimeToTime = pFromTimeToTime.matcher(command);
		Matcher mOnlyDate = pOnlyDate.matcher(command);
		Matcher mTimeForSearch = pTimeForSearch.matcher(command);
		Matcher mDateForSearch = pDateForSearch.matcher(command);
		Matcher mByDate = pByDate.matcher(command);
		Matcher mDateTime = pDateTime.matcher(command);
		Matcher mByDateTime = pByDateTime.matcher(command);
		Matcher mOnlyTime = pOnlyTime.matcher(command);
		Matcher mFromTimeDateToTime = pFromTimeDateToTime.matcher(command);
		Matcher mFromDateTimeToDate = pFromDateTimeToDate.matcher(command);
		Matcher mFromDateToDateTime = pFromDateToDateTime.matcher(command);
		Matcher mDateFromTimeToTime = pDateFromTimeToTime.matcher(command);
		
		
		String startTimeString=null, startDateString=null, endTimeString=null, endDateString=null;
		command = removeExtraSpaces(command);
		
		if (mDateForSearch.matches()) {
			
			startDateString = mDateForSearch.group(4);
			
			startDateString = startDateString.trim();
			
			command = mDateForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
			return true;
		}
		
		else if (mTimeForSearch.matches()) {
			
			startTimeString = mTimeForSearch.group(0);
			startTimeString = startTimeString.trim();
			command = mTimeForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
		}
		
		else if (mFromTimeDateToTimeDate.find()) {
			
			startTimeString = mFromTimeDateToTimeDate.group(4);
			startDateString = mFromTimeDateToTimeDate.group(19);
			endTimeString = mFromTimeDateToTimeDate.group(105);
			endDateString = mFromTimeDateToTimeDate.group(120);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			command = mFromTimeDateToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateTimeToDateTime.find()) {
			
			startTimeString = mFromDateTimeToDateTime.group(95);
			startDateString = mFromDateTimeToDateTime.group(8);
			endTimeString = mFromDateTimeToDateTime.group(200);
			endDateString = mFromDateTimeToDateTime.group(113);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			command = mFromDateTimeToDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
			
		}
		
		else if (mFromTimeToTimeDate.find()) {
			
			startTimeString = mFromTimeToTimeDate.group(4);
			endTimeString = mFromTimeToTimeDate.group(18);
			startDateString = mFromTimeToTimeDate.group(33);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
				
			command = mFromTimeToTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);	
		}
		
		else if (mDateFromTimeToTime.find()) {
				
			startTimeString = mDateFromTimeToTime.group(94);
			endTimeString = mDateFromTimeToTime.group(108);
			startDateString = mDateFromTimeToTime.group(5);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
				
			command = mDateFromTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);	
			
		}
		
		else if (mFromDateTimeToTime.find()) {
			
			startTimeString = mFromDateTimeToTime.group(91);
			startDateString = mFromDateTimeToTime.group(8);
			endTimeString = mFromDateTimeToTime.group(105);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			command = mFromDateTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromTimeDateToTime.find()) {
			
			startTimeString = mFromTimeDateToTime.group(4);
			startDateString = mFromTimeDateToTime.group(19);
			endTimeString = mFromTimeDateToTime.group(105);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			command = mFromTimeDateToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateTimeToDate.find()) {
			
			startTimeString = mFromDateTimeToDate.group(95);
			startDateString = mFromDateTimeToDate.group(8);
			endDateString = mFromDateTimeToDate.group(113);
			
			startTimeString = startTimeString.trim();
			endDateString = endDateString.trim();
			startDateString = startDateString.trim();
			
			command = mFromDateTimeToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateToDateTime.find()) {
			
			endDateString = mFromDateToDateTime.group(98);
			startDateString = mFromDateToDateTime.group(8);
			endTimeString = mFromDateToDateTime.group(185);
			
			endDateString = endDateString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			command = mFromDateToDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mAtTimeDate.find()) {
			
			startTimeString = mAtTimeDate.group(4);
			startDateString = mAtTimeDate.group(19);	
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			command = mAtTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTimeDate.find()) {
			
			endTimeString = mByTimeDate.group(4);
			endDateString = mByTimeDate.group(19);		
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			command = mByTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDateTime.find()) {
			
			endTimeString = mByDateTime.group(95);
			endDateString = mByDateTime.group(8);
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			command = mByDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}

		else if (mFromTimeToTime.find()) {
			
			startTimeString = mFromTimeToTime.group(4);
			endTimeString = mFromTimeToTime.group(18);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
		
			command = mFromTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateToDate.find()) {
			
			startDateString = mFromDateToDate.group(8);
			endDateString = mFromDateToDate.group(94);
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			command = mFromDateToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mTimeDate.find()) {
			
			startTimeString = mTimeDate.group(1);
			startDateString = mTimeDate.group(16);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			command = mTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateTime.find()) {
			
			startTimeString = mDateTime.group(93);
			startDateString = mDateTime.group(6);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			command = mDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mAtTime.find()) {
			
			startTimeString = mAtTime.group(4);
			startTimeString = startTimeString.trim();
			
			command = mAtTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTime.find()) {
			
			endTimeString = mByTime.group(4);
			endTimeString = endTimeString.trim();
			
			command = mByTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDate.find()) {
			
			endDateString = mByDate.group(8);
			endDateString = endDateString.trim();
			
			command = mByDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mOnlyDate.find()) {
			
			startDateString = mOnlyDate.group(5);
			startDateString = startDateString.trim();
			
			command = mOnlyDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mOnlyTime.find()) {
			startTimeString = mOnlyTime.group(1);
			startTimeString = startTimeString.trim();
			
			command = mOnlyTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		if (startTimeString==null && endTimeString==null && startDateString==null && endDateString==null) {
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
			return false;
		}
			
		if (startTimeString!=null && !timeParser.setStartTime(startTimeString))
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
		if (endTimeString!=null && timeParser.setEndTime(endTimeString)) 
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
		if (startDateString!=null && !dateParser.setStartDate(startDateString)) 
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
		if (endDateString!=null && !dateParser.setEndDate(endDateString)) 
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
		
		return true;
	}
	
	/**Validates an email address format
	 * 
	 * @param email address to be tested
	 * @return TRUE/FALSE, whether its an acceptable format or not
	 */
	public boolean validateEmailAdd (String inputEmail) {
		return inputEmail.matches(EMAIL_ADD);
	}
}

