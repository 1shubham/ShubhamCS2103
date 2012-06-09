package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.JIDLogic;
import org.apache.log4j.Logger;

public class TimeParser {
	private Logger logger=Logger.getLogger(JIDLogic.class);
	
	int startHour = -1, endHour = -1, startMin = -1, endMin = -1;
	int dummyHour = -1, dummyMin = -1;
	private Pattern pattern12, pattern24;
	private Matcher matcher12, matcher24;
	
	private static final String TIME_12_PATTERN = "(1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; 
	private static final String TIME_24_PATTERN = "(2[0-3]|[01]?[0-9])[:.]([0-5][0-9])([ ]?(?i)(hours|hour|hrs|hr))?";
	private static final String GENERAL_TIME_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";
	
	/**
	 * 
	 */
	public TimeParser( ) {
		pattern12 = Pattern.compile(TIME_12_PATTERN);
		pattern24 = Pattern.compile(TIME_24_PATTERN);
		//pattern = Pattern.compile(GENERAL_TIME_PATTERN);
		startHour = -1; endHour = -1; startMin = -1; endMin = -1;
		dummyHour = -1; dummyMin = -1;
		
	}
	/**
	 * 
	 * @return
	 */
	public static String getGeneralPattern() {
		return GENERAL_TIME_PATTERN;
	}
	/**
	 * 
	 * @return
	 */
	public int[] getStartTime() {
		int[] startTimeArr = {-1,-1};
		
		if (startHour>=0 && startMin>=0) {
			startTimeArr[0] =startHour;
			startTimeArr[1] =startMin;
		}
		
		return startTimeArr;
	}
	/**
	 * 
	 * @return
	 */
	public int[] getEndTime() {
		int[] endTimeArr = {-1,-1};
		
		if (endHour>=0 && endMin>=0) {
			endTimeArr[0] =endHour;
			endTimeArr[1] =endMin;
		}
		
		return endTimeArr;
	}
	/**
	 * 
	 */
	private void resetDummyTime() {
		dummyHour = -1; dummyMin = -1;
	}
	/*
	public void printTimes() { //for your testing
		if ((startHour>=0 && startMin>=0)) {
			logger.debug("startHour: "+startHour);
			logger.debug("startMin: "+startMin);
			return;
		}
		
		if ((endHour>=0 && endMin>=0)) {
			logger.debug("endHour: "+endHour);
			logger.debug("endMin: "+endMin);
			return;
		}
		
		logger.debug("no attributes exist!");
	}
	
	public String extractTime(String inputS) {
		String s = null;
		matcher = pattern.matcher(inputS);
		
		if(matcher.find())
			s = matcher.group(0);
		
		return s;
	}
	*/
	/**
	 * 
	 * @param startT
	 * @return
	 */
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
					logger.debug("1st return of setStartDate: false");
					return false;
				}
			}
			logger.debug("2nd return of setStartDate: false");
			return false;
		}
		logger.debug("3rd return of setStartDate: false");
		return false;
	}
	/**
	 * 
	 * @param endT
	 * @return
	 */
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
					//logger.debug("1st return of setStartDate: false");
					return false;
				}
			}
			//logger.debug("2nd return of setStartDate: false");
			return false;
		}
		return false;
	}
	/**
	 * 
	 * @param time
	 * @return
	 */
	public boolean isValid(String time) {
		return time.matches(GENERAL_TIME_PATTERN);
	}
	/**
	 * 
	 * @param time
	 * @return
	 */
	private boolean set12Hour (String time) {
		matcher12 = pattern12.matcher(time);
		final String AM_REGEX = "(?i)(am)";
		final String PM_REGEX = "(?i)(pm)";
		
		if(matcher12.matches()) {
			//logger.debug("groupcount "+matcher12.groupCount());
			
			String hour = matcher12.group(1);
			String min = matcher12.group(2);
			String AMPM = matcher12.group(4);
			
			if (AMPM.matches(AM_REGEX)){
				if(min != null) {
					min = min.replaceAll("[:.]", "");
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = Integer.parseInt(min);

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = Integer.parseInt(min);

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = 0;

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						
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

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = Integer.parseInt(min);

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = 0;

						//logger.debug("dummy time is "+dummyHour+":"+dummyMin);
						return true;
					}
				}
			}
			
			logger.debug("first false of set12hour");
			return false;
		}
		logger.debug("second false of set12hour");
		return false;
	}
	/**
	 * 
	 * @param time
	 * @return
	 */
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