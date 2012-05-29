package parserPackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//maybe a lot of its functions can be private


public class TimeParser {
	
	static int startHour = -1, endHour = -1, startMin = -1, endMin = -1;
	static int dummyHour = -1, dummyMin = -1;
	private String inputS;
	private Pattern pattern12, pattern24, pattern;
	private Matcher matcher12, matcher24, matcher;
	
	private final String TIME_12_PATTERN = "[ ](1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; //([:.] not seperated out because of a good reason :D
	private final String TIME_24_PATTERN = "[ ](2[0-3]|[01]?[0-9])[:.]?([0-5][0-9])";
	private final String TIME_12_OR_24_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";//"((1[012]|(0?[1-9]))([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))";
			//"("++")|("++")"
			
	public TimeParser(String userCommand) {
		inputS=userCommand;
		pattern12 = Pattern.compile(TIME_12_PATTERN);
		pattern24 = Pattern.compile(TIME_24_PATTERN);
		pattern = Pattern.compile(TIME_12_OR_24_PATTERN);
	}
	public TimeParser( ) {
		inputS=null;
		pattern12 = Pattern.compile(TIME_12_PATTERN);
		pattern24 = Pattern.compile(TIME_24_PATTERN);
		pattern = Pattern.compile(TIME_12_OR_24_PATTERN);
	}
	public int[] getStartTime() {
		int[] startTimeArr = {-1,-1};
		
		if (startHour>=0 && startMin>=0) {
			startTimeArr[0] =startHour;
			startTimeArr[1] =startMin;
		}
		
		return startTimeArr;
	}
	
	public int[] getEndTime() {
		int[] endTimeArr = {-1,-1};
		
		if (endHour>=0 && endMin>=0) {
			endTimeArr[0] =endHour;
			endTimeArr[1] =endMin;
		}
		
		return endTimeArr;
	}
	
	private void resetDummyTime() {
		dummyHour = -1; dummyMin = -1;
	}
	
	public String removeExtraSpaces (String s) {
		return s.replaceAll("\\s+", " ");
	}
	
	public void printTimes() { //for your testing
		if ((startHour>=0 && startMin>=0)) {
			System.out.println("startHour: "+startHour);
			System.out.println("startMin: "+startMin);
			return;
		}
		
		if ((endHour>=0 && endMin>=0)) {
			System.out.println("endHour: "+endHour);
			System.out.println("endMin: "+endMin);
			return;
		}
		
		System.out.println("no attributes exist!");
	}
	
	public String getinputCommand(){
		return inputS;
	}
	
	public boolean extractStartEnd () {
	
		final String AT_TIME = "((at)|(AT))("+TIME_12_OR_24_PATTERN+")";
		final String BY_TIME = "((by)|(BY))("+TIME_12_OR_24_PATTERN+")";
		final String TO_TIME = "((to)|(TO))("+TIME_12_OR_24_PATTERN+")";
		final String FROM_TIME_TO_TIME = "((from)|(FROM))("+TIME_12_OR_24_PATTERN+")[ ]((to)|(TO))("+TIME_12_OR_24_PATTERN+")";
		final String TIME_TO_TIME = "("+TIME_12_OR_24_PATTERN+")[ ]((to)|(TO))("+TIME_12_OR_24_PATTERN+")";
		
		matcher = pattern.matcher(inputS);
		
		inputS = removeExtraSpaces(inputS);
		
		if (matcher.find()) {
			DateParser dateParser = new DateParser();
			String startDateString=null, endDateString = null;
			
			inputS = removeExtraSpaces(inputS);
			
			String startTimeString=null, endTimeString=null;
			String dummyTime = matcher.group();
			
			System.out.println("dummytimeString:"+dummyTime);
			System.out.println("group1:"+matcher.group(1));
			System.out.println("group2:"+matcher.group(2));
			
			/*
			 * cases to be implemented:
			 * 1. input cases for search results
			 * 2. today/tmr
			 * 3. weekdays
			 * 4. without date/time
			 * 5. 2 dates
			 * 
			 * Also, if startDateTime > endDateTime, swap them!
			 */
			
			if ( (inputS.contains("at") || inputS.contains("AT") ) && ( (matcher.start() - 2) == inputS.indexOf("at") || (matcher.start() - 2) == inputS.indexOf("AT") ) ) {
				startTimeString = dummyTime;
				inputS = inputS.replaceFirst(AT_TIME, "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start time: "+startTimeString);
				System.out.println("left over: "+inputS);
				
				if (setStartTime(startTimeString)) 
					System.out.println("Start time is set!");
				else
					System.out.println("Start time could NOT be set!");
				
				startDateString=dateParser.extractDate(inputS);
				
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start date: "+startDateString);
				System.out.println("left over: "+inputS);
				
				
				if (dateParser.setStartDate(startDateString)) 
					System.out.println("Start date is set!");
				else
					System.out.println("Start date could NOT be set!");
				
				//setTime (startTimeString, endTimeString);
				return true;
			}
			
			/*
			 * project deadline by 6pm sdfsd
			 */
			else if ( (inputS.contains("by") || inputS.contains("BY") ) && ( (matcher.start() - 2) == inputS.indexOf("by") || (matcher.start() - 2) == inputS.indexOf("BY") ) ) {
				endTimeString = dummyTime;
				inputS = inputS.replaceFirst(BY_TIME, "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("end time: "+endTimeString);
				System.out.println("left over: "+inputS);
				
				if (setEndTime(endTimeString)) 
					System.out.println("End time is set!");
				else
					System.out.println("End time could NOT be set!");
				
				
				endDateString=dateParser.extractDate(inputS);
				
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);

				System.out.println("end date: "+endDateString);
				System.out.println("left over: "+inputS);
				
				if (dateParser.setEndDate(endDateString)) 
					System.out.println("end date is set!");
				else
					System.out.println("end date could NOT be set!");
				
				//setTime (startTimeString, endTimeString);
				return true;
			}
			
			/*
			 * meeting from 5pm to 6pm at utown
			 * not combined with the next one because, you need to make sure you remove the correct "from"!
			 */
			else if ( (inputS.contains("from") || inputS.contains("FROM") ) && ( (matcher.start() - 4) == inputS.indexOf("from") || (matcher.start() - 4) == inputS.indexOf("FROM") ) ) {
				startTimeString = dummyTime;
				
				matcher.find();
				endTimeString = matcher.group();
				inputS = inputS.replaceFirst(FROM_TIME_TO_TIME, "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start time: "+startTimeString);
				System.out.println("end time: "+endTimeString);
				System.out.println("left over: "+inputS);
				

				if (setStartTime(startTimeString)) 
					System.out.println("Start time is set!");
				else
					System.out.println("Start time could NOT be set!");
				
				if (setEndTime(endTimeString)) 
					System.out.println("End time is set!");
				else
					System.out.println("End time could NOT be set!");
				
				
				
				startDateString=dateParser.extractDate(inputS);
				endDateString=dateParser.extractDate(inputS);
				
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start date: "+startDateString);
				System.out.println("end date: "+endDateString);
				System.out.println("left over: "+inputS);
				
				
				if (dateParser.setStartDate(startDateString)) 
					System.out.println("Start date is set!");
				else
					System.out.println("Start date could NOT be set!");
				
				if (dateParser.setEndDate(endDateString))
					System.out.println("end date is set!");
				else
					System.out.println("end date could NOT be set!");
				
				//setTime (startTimeString, endTimeString);
				return true;
			}
			
			/*
			 * meeting 5pm to 6pm at utown
			 */
			else if ((inputS.contains("to") || inputS.contains("TO") ) && ((matcher.end()+1) == inputS.indexOf("to") || (matcher.end()+1) == inputS.indexOf("TO"))) {
				startTimeString = dummyTime;
				matcher.find();
				endTimeString = matcher.group();
				inputS = inputS.replaceFirst(TIME_TO_TIME, "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start time: "+startTimeString);
				System.out.println("end time: "+endTimeString);
				System.out.println("left over: "+inputS);
				

				if (setStartTime(startTimeString)) 
					System.out.println("Start time is set!");
				else
					System.out.println("Start time could NOT be set!");
				
				if (setEndTime(endTimeString)) 
					System.out.println("End time is set!");
				else
					System.out.println("End time could NOT be set!");
				
				
				startDateString=dateParser.extractDate(inputS);
				endDateString=dateParser.extractDate(inputS);
						
				
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start date: "+startDateString);
				System.out.println("end date: "+endDateString);
				System.out.println("left over: "+inputS);
				

				if (dateParser.setStartDate(startDateString)) 
					System.out.println("Start date is set!");
				else
					System.out.println("Start date could NOT be set!");
				
				if (dateParser.setEndDate(endDateString))
					System.out.println("end date is set!");
				else
					System.out.println("end date could NOT be set!");
				
				
				//setTime (startTimeString, endTimeString);
				return true;
			}
			
			/*
			 * meeting 5pm at utown
			 */
			else {
				startTimeString = dummyTime;
				inputS = inputS.replaceFirst(TIME_12_OR_24_PATTERN, "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start time: "+startTimeString);
				System.out.println("left over: "+inputS);
				
				
				if (setStartTime(startTimeString)) 
					System.out.println("Start time is set!");
				else
					System.out.println("Start time could NOT be set!");
				
				
				
				startDateString=dateParser.extractDate(inputS);
				
				inputS = inputS.replaceFirst(dateParser.getGeneralPattern(), "");
				inputS = removeExtraSpaces(inputS);
				
				System.out.println("start date: "+startDateString);
				System.out.println("left over: "+inputS);
				
				
				if (dateParser.setStartDate(startDateString)) 
					System.out.println("Start date is set!");
				else
					System.out.println("Start date could NOT be set!");
				
				
				
				return true;
			}
			
		}
		return false;
	}
	
	public boolean setStartTime (String startT) {
		if (startT != null) {
			if (set12Hour(startT) || (set24Hour(startT))) {
				if (dummyHour>=0 && dummyMin>=0){
					startHour = dummyHour;
					startMin = dummyMin;
					resetDummyTime();
					return true;
				}
				
				else {
					//System.out.println("1st return of setStartDate: false");
					return false;
				}
			}
			//System.out.println("2nd return of setStartDate: false");
			return false;
		}
		return false;
	}
	
	public boolean setEndTime (String endT) {
		if (endT != null) {
			if (set12Hour(endT) || (set24Hour(endT))) {
				if (dummyHour>=0 && dummyMin>=0){
					endHour = dummyHour;
					endMin = dummyMin;
					resetDummyTime();
					return true;
				}
				
				else {
					//System.out.println("1st return of setStartDate: false");
					return false;
				}
			}
			//System.out.println("2nd return of setStartDate: false");
			return false;
		}
		return false;
	}
	
	
	public boolean isValid(String time) {
		//return isValid12Hour(time) || isValid24Hour(time);
		return time.matches(TIME_12_OR_24_PATTERN);
	}
	
	private boolean set12Hour (String time) {
		matcher12 = pattern12.matcher(time);
		final String AM_REGEX = "(?i)(am)";
		final String PM_REGEX = "(?i)(pm)";
		
		if(matcher12.matches()) {
			//System.out.println("groupcount "+matcher12.groupCount());
			
			String hour = matcher12.group(1);
			String min = matcher12.group(2);
			String AMPM = matcher12.group(4);
			
			if (AMPM.matches(AM_REGEX)){
				if(min != null) {
					min = min.replaceAll("[:.]", "");
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = Integer.parseInt(min);

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = Integer.parseInt(min);

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = 0;

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
				}
			}
			
			else if (AMPM.matches(PM_REGEX)){
				if(min != null) {
					min = min.replaceAll("[:.]", "");
					if(hour.matches("12")) {
						dummyHour = Integer.parseInt(hour);
						dummyMin = Integer.parseInt(min);

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = Integer.parseInt(min);

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = 0;

						//System.out.println("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
				}
			}
			
			//System.out.println("first false of set12hour");
			return false;
		}
		//System.out.println("second false of set12hour");
		return false;
	}
	
	private boolean set24Hour (String time) {
		matcher24 = pattern24.matcher(time);
		
		if(matcher24.matches()) {
			String hour = matcher24.group(1);
			String min = matcher24.group(2);
			
			if (min!=null) {
				dummyHour = Integer.parseInt(hour);
				dummyMin = Integer.parseInt(min);
				return true;
			}
			else {
				dummyHour = Integer.parseInt(hour);
				dummyMin = 0;
				return true;
			}
		}
		return false;
	}
	
}