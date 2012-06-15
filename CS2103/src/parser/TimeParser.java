/**
 *
 * This class features the time parsing abilities of Jot It Down
 * Enables the user to enter the desirable time in a variety of formats
 * 
 * @author Shubham Kaushal
 */

package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TimeParser {
	int startHour, endHour, startMin, endMin;
	int dummyHour, dummyMin;
	private Pattern pattern12, pattern24;
	private Matcher matcher12, matcher24;
	
	private static final String TIME_12_PATTERN = "(1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; 
	private static final String TIME_24_PATTERN = "(2[0-3]|[01]?[0-9])[:.]([0-5][0-9])([ ]?(?i)(hours|hour|hrs|hr))?";
	private static final String GENERAL_TIME_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";
	
	/**
	 * Constructor
	 */
	public TimeParser( ) {
		pattern12 = Pattern.compile(TIME_12_PATTERN);
		pattern24 = Pattern.compile(TIME_24_PATTERN);
		startHour = -1; endHour = -1; startMin = -1; endMin = -1;
		dummyHour = -1; dummyMin = -1;
		
	}
	/**Returns a string representing the regular expression for acceptable time formats
	 * 
	 * @return StringObj
	 */
	public static String getGeneralPattern() {
		return GENERAL_TIME_PATTERN;
	}
	/**fetches the local integer attributes representing the Start time
	 * 
	 * @return int[] 
	 */
	public int[] getStartTime() {
		int[] startTimeArr = {-1,-1};
		
		if (startHour>=0 && startMin>=0) {
			startTimeArr[0] =startHour;
			startTimeArr[1] =startMin;
		}
		
		return startTimeArr;
	}
	/**fetches the local integer attributes representing the End time
	 * 
	 * @return int[] 
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
	 * resets the local dummy time attributes
	 */
	private void resetDummyTime() {
		dummyHour = -1; dummyMin = -1;
	}
	
	/**Tries to set the local start time attributes
	 * 
	 * @param String Obj representing start time 
	 * @return TRUE/FALSE
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
					return false;
				}
			}
			return false;
		}
		return false;
	}/**Tries to set the local end time attributes
	 * 
	 * @param String Obj representing end time 
	 * @return TRUE/FALSE
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
					return false;
				}
			}
			return false;
		}
		return false;
	}
	/**validates the time represented by a string
	 * 
	 * @param String Obj representing time
	 * @return TRUE/FALSE
	 */
	public boolean isValid(String time) {
		return time.matches(GENERAL_TIME_PATTERN);
	}
	/**Tries to set the dummy time attributes based on the 12 hour time format
	 * 
	 * @param String Obj representing time
	 * @return TRUE/FALSE
	 */
	private boolean set12Hour (String time) {
		matcher12 = pattern12.matcher(time);
		final String AM_REGEX = "(?i)(am)";
		final String PM_REGEX = "(?i)(pm)";
		
		if(matcher12.matches()) {
			
			String hour = matcher12.group(1);
			String min = matcher12.group(2);
			String AMPM = matcher12.group(4);
			
			if (AMPM.matches(AM_REGEX)){
				if(min != null) {
					min = min.replaceAll("[:.]", "");
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = Integer.parseInt(min);
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = Integer.parseInt(min);
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = 0;
						dummyMin = 0;
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;
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
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = Integer.parseInt(min);
						return true;
					}
				}
				else {
					if(hour.matches("12")) {
						dummyHour = Integer.parseInt(hour);
						dummyMin = 0;
						return true;
					}
					else {
						dummyHour = Integer.parseInt(hour) +12;
						dummyMin = 0;
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}
	/**Tries to set the dummy time attributes based on the 24 hour time format
	 * 
	 * @param String Obj representing time
	 * @return TRUE/FALSE
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
		}
		return false;
	}
	
}