package parser;

import constant.OperationFeedback;
import data.Task;
import data.TaskDateTime;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import logic.JIDLogic;
import org.apache.log4j.Logger;

public class Parser {
	
	private final int RECUR_TIMES_CAP = 61;
	private final int DEFAULT_RECUR_TIMES = 10;
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
	
	private boolean important;
	private boolean deadline;
	private TaskDateTime startDateTime, endDateTime;
	private String recurring;
	private int recurringTimes;
	private ArrayList<String> labelList;
	private String taskDetails;
	
	private Task taskForSearch;
	private String command;
	private OperationFeedback error;
	
	private Logger logger=Logger.getLogger(JIDLogic.class);
	
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
		labelList = null;
		taskDetails=null;
		
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
		
		//tasksForAdd=null;
		
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
	}
	/**Initializes REGEX strings for Search function
	 * 
	 * @param inputCommand
	 */
	private void initForSearch(String inputCommand) {
		initCommon(inputCommand);
		
		taskForSearch=null;
		
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
		FROM_DATE_TIME_TO_DATE = "(((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")";
		FROM_DATE_TO_DATE_TIME = "(((?i)(from)))?[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+DateParser.getGeneralPattern()+")[ ]((((?i)(at)))[ ])?("+TimeParser.getGeneralPattern()+")";
		FROM_TIME_DATE_TO_TIME = "(((?i)(from)))?[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ](((?i)(to)))[ ]("+TimeParser.getGeneralPattern()+")";
	}
	/**
	 * 
	 * @param e
	 */
	private void setErrorCode (OperationFeedback e) {
		error = e;
	}
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
			
			logger.debug(recurringTimes);
			
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
	private void setDefaultDateForAdd (TaskDateTime dtObj) {
		
		GregorianCalendar gcObj = new GregorianCalendar();
		
		logger.debug("gcObj: "+gcObj.toString());
		
		logger.debug("dtObj time: "+dtObj.getTime().getTimeMilli());
		logger.debug("current time: "+TaskDateTime.getCurrentDateTime().getTime().getTimeMilli());
		
		if (dtObj.getTime().getTimeMilli() <= TaskDateTime.getCurrentDateTime().getTime().getTimeMilli())
			gcObj.add(GregorianCalendar.DATE, 1);
		
		int year = gcObj.get(GregorianCalendar.YEAR);
		int month =	gcObj.get(GregorianCalendar.MONTH) + 1;
		int day = gcObj.get(GregorianCalendar.DATE);
		
		logger.debug("year: "+year);
		logger.debug("month: "+month);
		logger.debug("day: "+day);
		
		dtObj.set(year, month, day);
		
		logger.debug("dtObj: "+dtObj.formattedToString());
		
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
		
		//for local testing
		if (startDateTime!=null)
			logger.debug("start date time: "+startDateTime.formattedToString());
		
		if(endDateTime!=null)
			logger.debug("end date time: "+endDateTime.formattedToString());
	}
	private void setLocalStartDateTime (GregorianCalendar start) {
		startDateTime = new TaskDateTime (start.get(GregorianCalendar.YEAR), start.get(GregorianCalendar.MONTH)+1, start.get(GregorianCalendar.DATE), start.get(GregorianCalendar.HOUR_OF_DAY), start.get(GregorianCalendar.MINUTE), start.get(GregorianCalendar.SECOND));
	}
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
	/**FOR ADD FUNCTION
	 * creates a Task Obj based on local attributes to be returned to the Logic component
	 * 
	 * @param String userCommand
	 * @return Task Obj
	 */
	public Task parseForSearch (String userCommand) {
		
		initForSearch(userCommand);
		
		parse (userCommand);
		
		logger.debug("this is parse for SEARCH before initializing task obj");
		
		taskForSearch = new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important);

		logger.debug("task before returning: "+taskForSearch.toString());
		
		return taskForSearch;
	}
	/**FOR SEARCH FUNCTIONS
	 * creates a Task Obj based on local attributes to be returned to the Logic component
	 * 
	 * @param String userCommand
	 * @return Task Obj
	 */
	public Task[] parseForAdd (String userCommand) {
		
		initForAdd(userCommand);
		
		parse (userCommand);
		
		logger.debug("this is parse for ADD before initializing task[]");
		
		Task[] taskArr=null;//- have a fetchTaskArr(ArrayList<Task> taskList) instead
		
		//logger.debug("startDateTime: "+startDateTime.toString());
		
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
		
		//if the error != valid, return null; ??? do u need to say this? cant u just return taskArray?
		
		logger.debug("any error?: "+error);
		logger.debug("recurring: "+recurring);
		logger.debug("recurring times: "+recurringTimes);
		
		
		if (taskArr!=null) {
			for (int i=0; i<taskArr.length; i++) {
				logger.debug("task number "+(i+1)+": "+taskArr[i].toString());
			}
		}
		else
			logger.debug("taskArray is null!");
		
		//return only if error=valid?
		//return new Task();
		return taskArr;
	}
	private Task[] fetchTaskArray (int numRecurr) {
		
		logger.debug("-----------fetchTaskArray() is called!-------------");
		
		ArrayList<Task> taskList = new ArrayList<Task> ();
		GregorianCalendar startDT =null;
		GregorianCalendar endDT =null;
		
		//logger.debug("initial startDateTime value: "+startDateTime.formattedToString());
		
		
		if (startDateTime!=null)
			startDT = new GregorianCalendar(startDateTime.get(GregorianCalendar.YEAR),startDateTime.get(GregorianCalendar.MONTH)-1,startDateTime.get(GregorianCalendar.DATE),startDateTime.get(GregorianCalendar.HOUR_OF_DAY),startDateTime.get(GregorianCalendar.MINUTE),startDateTime.get(GregorianCalendar.SECOND));
		if (endDateTime!=null)
			endDT = new GregorianCalendar(endDateTime.get(GregorianCalendar.YEAR),endDateTime.get(GregorianCalendar.MONTH)-1,endDateTime.get(GregorianCalendar.DATE),endDateTime.get(GregorianCalendar.HOUR_OF_DAY),endDateTime.get(GregorianCalendar.MINUTE),endDateTime.get(GregorianCalendar.SECOND));
		
		
		if (recurring==null)
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
		else if (recurring.matches("daily")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) { //if both startDT and endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 1);
					endDT.add(GregorianCalendar.DATE, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { //if only endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.DATE, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) { //if only startDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 1);
					setLocalStartDateTime(startDT);
					
					//logger.debug("inside else if startDateTime value: "+startDateTime.formattedToString());
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
				
					//for (int k=0;k<taskList.size();k++){
						//logger.debug("all task added: "+taskList.get(k).toString());
					//}
					
				}
			}
		}
		else if (recurring.matches("weekly")) {
			//logger.debug("--------IF STATEMENT, recurring=weekly--------");
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			//logger.debug("tasklist now: "+taskList.toString());
			
			if(startDT!=null && endDT!=null) { //if both startDT and endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 7);
					endDT.add(GregorianCalendar.DATE, 7);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { //if only endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.DATE, 7);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) { //if only startDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.DATE, 7);
					setLocalStartDateTime(startDT);
					
					//logger.debug("inside else if startDateTime value: "+startDateTime.formattedToString());
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
				
					//for (int k=0;k<taskList.size();k++){
						//logger.debug("all task added: "+taskList.get(k).toString());
					//}
					
				}
			}
		}
		else if (recurring.matches("monthly")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) { //if both startDT and endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.MONTH, 1);
					endDT.add(GregorianCalendar.MONTH, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { //if only endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.MONTH, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) { //if only startDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.MONTH, 1);
					setLocalStartDateTime(startDT);
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
		}
		else if (recurring.matches("yearly")) {
			taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));
			
			if(startDT!=null && endDT!=null) { //if both startDT and endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.YEAR, 1);
					endDT.add(GregorianCalendar.YEAR, 1);
					setLocalStartDateTime(startDT);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT==null && endDT!=null) { //if only endDT exist
				for (int i=0;i<numRecurr-1;i++) {
					endDT.add(GregorianCalendar.YEAR, 1);
					setLocalEndDateTime(endDT);
	
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
			else if(startDT!=null && endDT==null) { //if only startDT exist
				for (int i=0;i<numRecurr-1;i++) {
					startDT.add(GregorianCalendar.YEAR, 1);
					setLocalStartDateTime(startDT);
					
					taskList.add(new Task(taskDetails,"",startDateTime,endDateTime,labelList,recurring,deadline,important));	
				}
			}
		}
		
		//logger.debug("task list's last item: "+(taskList.get(taskList.size()-1)).toString());
		//logger.debug("task list's second last item: "+(taskList.get(taskList.size()-2)).toString());
		
		//logger.debug
		//for (int j=0; j<taskList.size(); j++)
			//logger.debug("task list: "+(taskList.get(j)).toString());
		
		if (taskList.isEmpty())
			return null;
		else
			return taskList.toArray(new Task[taskList.size()]);
	}
	public String[] fetchGCalDes (String input) {
		String[] arr= null;
		Matcher m = Pattern.compile(G_CAL_DES).matcher(input);
		
		if (m.matches()) {
		//for(int i=1; i<m.groupCount(); i++)
			//logger.debug("group"+i+": "+m.group(i));
		
		arr = new String[6];
		for (int i=0; i<5; i++) {
			arr[i] = m.group(i+1);
		}
		arr[5] = m.group(9);
		
		//for (int i=0; i<arr.length; i++)
			//logger.debug("arr: "+arr[i]);
		
		}
		
		return arr;
	}
	/**Understands the user input and sets local attributes for Task Obj to be created
	 * 
	 * @param String userCommand
	 */
	private void parse (String userCommand) {
		
		Matcher mDontParse = Pattern.compile(DONT_PARSE).matcher(command);
		String[] dontParseStrings = {null,null,null,null,null,null,null,null,null,null};
		int currIndex=0;
		String[] tempReplaceStrings = {null,null,null,null,null,null,null,null,null,null};
		
		while (mDontParse.find()) {
			dontParseStrings[currIndex] = mDontParse.group();
			
			logger.debug("current dont parse string extracted: "+dontParseStrings[currIndex]);
			logger.debug("length of current dont parse string extracted: "+dontParseStrings[currIndex].length());
			
			tempReplaceStrings[currIndex]= "\'";
			for (int j=2; j<dontParseStrings[currIndex].length(); j++) {
				tempReplaceStrings[currIndex] = tempReplaceStrings[currIndex]+"%";
			}
			tempReplaceStrings[currIndex]= tempReplaceStrings[currIndex]+"\'";
			
			logger.debug("current temp replacement string: "+tempReplaceStrings[currIndex]);
			
			command = command.replaceFirst(DONT_PARSE, tempReplaceStrings[currIndex]);
			command = removeExtraSpaces(command);
		
			currIndex++;
			
			logger.debug("input now after extracting "+currIndex+"th dont parse string:" +command);
		}
		
		setImportant();
		
		extractRecur();
		
		if (recurring != null)
			logger.debug("this task is "+recurring);
		else
			logger.debug("this task is not recurring");
		logger.debug("left over string after checking for recurring: "+command);
				
		labelList =new ArrayList<String>();
		setLabels();
		
		if(labelList.size()!=0) {
			
			for(int i=0;i<labelList.size();i++){
				logger.debug("label "+i+": "+labelList.get(i));
				command = command.replaceFirst(LABEL_REGEX, "");
			}
			command = command.trim();
			logger.debug("left over string after fetching labels: "+command);
		}
		
		TimeParser timeParser = new TimeParser();
		DateParser dateParser = new DateParser();
		
		if (extractDateTime(timeParser, dateParser))
			logger.debug("time/date extracted!");
		else
			logger.debug("time/date NOT extracted!");
		
		setLocalDateTime(timeParser, dateParser);
		setDeadline();
		
		//restoring don't parse Strings
		for (int k=0; k<currIndex; k++)
			command = command.replaceFirst(tempReplaceStrings[k], dontParseStrings[k]);
		
		taskDetails = command;
		taskDetails = taskDetails.trim();
		
		if (taskDetails==null || taskDetails.isEmpty())
			setErrorCode(OperationFeedback.INVALID_TASK_DETAILS);
		
		postExtractTest();
	}
	/**Extracts date and time from the user input and sets the local DateTime Attributes
	 * 
	 * @param timeParser Obj
	 * @param dateParser Obj
	 * @return TRUE/FALSE depending on whether DateTime attributes were set or not
	 */
	private boolean extractDateTime (TimeParser timeParser, DateParser dateParser) {
		
		//obsolete regex:
		//final String TIME_DATE_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")"; 
		//final String DATE_TIME_TO_DATE_TIME = "("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")"; 
		//final String TIME_TO_TIME_DATE = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")[ ]("+DateParser.getGeneralPattern()+")";
		//final String DATE_TIME_TO_TIME = "("+DateParser.getGeneralPattern()+")[ ](((at)|(AT))[ ])?("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		//final String TIME_TO_TIME = "("+TimeParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+TimeParser.getGeneralPattern()+")";
		//final String DATE_TO_DATE = "("+DateParser.getGeneralPattern()+")[ ]((to)|(TO))[ ]("+DateParser.getGeneralPattern()+")";
		
		
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
	
		//obsolete patterns:
		//Pattern pDateTimeToDateTime = Pattern.compile(DATE_TIME_TO_DATE_TIME);
		//Pattern pDateTimeToTime = Pattern.compile(DATE_TIME_TO_TIME);
		//Pattern pDateToDate = Pattern.compile(DATE_TO_DATE);
		//Pattern pTimeToTimeDate = Pattern.compile(TIME_TO_TIME_DATE);
		//Pattern pTimeDateToTimeDate = Pattern.compile(TIME_DATE_TO_TIME_DATE);
		//Pattern pTimeToTime = Pattern.compile(TIME_TO_TIME);
				
		
		
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
		
		//obsolete matchers:
		//Matcher mDateTimeToDateTime = pDateTimeToDateTime.matcher(command);
		//Matcher mDateTimeToTime = pDateTimeToTime.matcher(command);
		//Matcher mDateToDate = pDateToDate.matcher(command);
		//Matcher mTimeToTimeDate = pTimeToTimeDate.matcher(command);
		//Matcher mTimeDateToTimeDate = pTimeDateToTimeDate.matcher(command);
		//Matcher mTimeToTime = pTimeToTime.matcher(command);
						
		
		String startTimeString=null, startDateString=null, endTimeString=null, endDateString=null;
		command = removeExtraSpaces(command);
		
		if (mDateForSearch.matches()) {
			logger.debug("-----date only FOR SEARCH format-------");
			
			for (int i=0; i<mDateForSearch.groupCount(); i++)
				logger.debug("group "+i+": "+mDateForSearch.group(i));
			
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
			
			for (int i=0; i<mTimeForSearch.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeForSearch.group(i));
			
			startTimeString = mTimeForSearch.group(0);
			startTimeString = startTimeString.trim();
			command = mTimeForSearch.replaceFirst("");
			command = removeExtraSpaces(command);
			
			logger.debug("start time string: "+startTimeString);
		}
		
		else if (mFromTimeDateToTimeDate.find()) {
			logger.debug("-----from_time_date_to_time_date format-------");
			
			for (int i=0; i<mFromTimeDateToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeDateToTimeDate.group(i));
			
			startTimeString = mFromTimeDateToTimeDate.group(4);
			startDateString = mFromTimeDateToTimeDate.group(19);
			endTimeString = mFromTimeDateToTimeDate.group(101);
			endDateString = mFromTimeDateToTimeDate.group(116);
			
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
			
			for (int i=0; i<mFromDateTimeToDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateTimeToDateTime.group(i));
			
			startTimeString = mFromDateTimeToDateTime.group(91);
			startDateString = mFromDateTimeToDateTime.group(8);
			endTimeString = mFromDateTimeToDateTime.group(192);
			endDateString = mFromDateTimeToDateTime.group(109);
			
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
			
			for (int i=0; i<mFromTimeToTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeToTimeDate.group(i));
			
			startTimeString = mFromTimeToTimeDate.group(4);
			endTimeString = mFromTimeToTimeDate.group(18);
			startDateString = mFromTimeToTimeDate.group(33);
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
			
			for (int i=0; i<mFromDateTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateTimeToTime.group(i));
			
			startTimeString = mFromDateTimeToTime.group(91);
			startDateString = mFromDateTimeToTime.group(8);
			endTimeString = mFromDateTimeToTime.group(105);
			
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
		
		else if (mFromTimeDateToTime.find()) {
			logger.debug("-----from_time_date_to_time format-------");
			
			for (int i=0; i<mFromTimeDateToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeDateToTime.group(i));
			
			startTimeString = mFromTimeDateToTime.group(4);
			startDateString = mFromTimeDateToTime.group(19);
			endTimeString = mFromTimeDateToTime.group(101);
			
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			endDateString = startDateString;
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromTimeDateToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateTimeToDate.find()) {
			logger.debug("-----from_date_time_to_date format-------");
			
			for (int i=0; i<mFromDateTimeToDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateTimeToDate.group(i));
			
			startTimeString = mFromDateTimeToDate.group(91);
			startDateString = mFromDateTimeToDate.group(8);
			endDateString = mFromDateTimeToDate.group(109);
			
			startTimeString = startTimeString.trim();
			endDateString = endDateString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateTimeToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateToDateTime.find()) {
			logger.debug("-----from_date_to_date_time format-------");
			
			for (int i=0; i<mFromDateToDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateToDateTime.group(i));
			
			endDateString = mFromDateToDateTime.group(94);
			startDateString = mFromDateToDateTime.group(8);
			endTimeString = mFromDateToDateTime.group(177);
			
			endDateString = endDateString.trim();
			endTimeString = endTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateToDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		/*
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
		*/
		else if (mAtTimeDate.find()) {
			logger.debug("-----at_time_date format-------");
			
			for (int i=0; i<mAtTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mAtTimeDate.group(i));
			
			startTimeString = mAtTimeDate.group(4);
			startDateString = mAtTimeDate.group(19);	
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string : "+startDateString);
			
			command = mAtTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTimeDate.find()) {
			logger.debug("-----by_time_date format-------");
			
			for (int i=0; i<mByTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mByTimeDate.group(i));
			
			endTimeString = mByTimeDate.group(4);
			endDateString = mByTimeDate.group(19);		
			endTimeString = endTimeString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("end time string: "+endTimeString);
			logger.debug("end date string : "+endDateString);
			
			command = mByTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDateTime.find()) {
			logger.debug("-----by_date_time format-------");
			
			for (int i=0; i<mByDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mByDateTime.group(i));
			
			endTimeString = mByDateTime.group(91);
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
			
			for (int i=0; i<mFromTimeToTime.groupCount(); i++)
				logger.debug("group "+i+": "+mFromTimeToTime.group(i));
			
			startTimeString = mFromTimeToTime.group(4);
			endTimeString = mFromTimeToTime.group(18);
			startTimeString = startTimeString.trim();
			endTimeString = endTimeString.trim();
		
			logger.debug("start time string: "+startTimeString);
			logger.debug("end time string: "+endTimeString);
			
			command = mFromTimeToTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mFromDateToDate.find()) {
			logger.debug("-----from_date_to_date format-------");
			
			for (int i=0; i<mFromDateToDate.groupCount(); i++)
				logger.debug("group "+i+": "+mFromDateToDate.group(i));
			
			startDateString = mFromDateToDate.group(8);
			endDateString = mFromDateToDate.group(94);
			
			startDateString = startDateString.trim();
			endDateString = endDateString.trim();
			
			logger.debug("start date string: "+startDateString);
			logger.debug("end date string: "+endDateString);
			
			command = mFromDateToDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		/*
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
		*/

		else if (mTimeDate.find()) {
			logger.debug("-----time date only format-------");
			
			for (int i=0; i<mTimeDate.groupCount(); i++)
				logger.debug("group "+i+": "+mTimeDate.group(i));
			
			startTimeString = mTimeDate.group(1);
			startDateString = mTimeDate.group(16);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			command = mTimeDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mDateTime.find()) {
			logger.debug("-----date time only format-------");
			
			for (int i=0; i<mDateTime.groupCount(); i++)
				logger.debug("group "+i+": "+mDateTime.group(i));
			
			startTimeString = mDateTime.group(89);
			startDateString = mDateTime.group(6);
			startTimeString = startTimeString.trim();
			startDateString = startDateString.trim();
			
			logger.debug("start time string: "+startTimeString);
			logger.debug("start date string: "+startDateString);
			
			command = mDateTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		/*
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
		*/
		else if (mAtTime.find()) {
			logger.debug("-----at_time format-------");
			
			for (int i=0; i<mAtTime.groupCount(); i++)
				logger.debug("group "+i+": "+mAtTime.group(i));
			
			startTimeString = mAtTime.group(4);
			startTimeString = startTimeString.trim();
			
			logger.debug("start time string: "+startTimeString);
			
			command = mAtTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByTime.find()) {
			logger.debug("-----by_time format-------");
			
			for (int i=0; i<mByTime.groupCount(); i++)
				logger.debug("group "+i+": "+mByTime.group(i));
			
			endTimeString = mByTime.group(4);
			endTimeString = endTimeString.trim();
			
			logger.debug("end time string: "+endTimeString);
			
			command = mByTime.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mByDate.find()) {
			logger.debug("-----by date format-------");
			
			for (int i=0; i<mByDate.groupCount(); i++)
				logger.debug("group "+i+": "+mByDate.group(i));
			
			endDateString = mByDate.group(8);
			endDateString = endDateString.trim();
			
			logger.debug("end date string: "+endDateString);
			
			command = mByDate.replaceFirst("");
			command = removeExtraSpaces(command);
		}
		
		else if (mOnlyDate.find()) {
			logger.debug("-----date only format-------");
			
			for (int i=0; i<mOnlyDate.groupCount(); i++)
				logger.debug("group "+i+": "+mOnlyDate.group(i));
			
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
		
		if (startTimeString==null && endTimeString==null && startDateString==null && endDateString==null) {
			setErrorCode (OperationFeedback.INVALID_DATE_TIME);
			return false;
		}
			
		
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
	/**local tester function
	 */
	private void postExtractTest () {
		
		logger.debug("-------post extraction TESTING-------");
		
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
		
		logger.debug("task details: "+taskDetails);
	}
}

