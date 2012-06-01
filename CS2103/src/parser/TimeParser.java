package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//maybe a lot of its functions can be private


public class TimeParser {
	
	int startHour = -1, endHour = -1, startMin = -1, endMin = -1;
	int dummyHour = -1, dummyMin = -1;
	private Pattern pattern12, pattern24, pattern;
	private Matcher matcher12, matcher24, matcher;
	
	private static final String TIME_12_PATTERN = "(1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; //([:.] not seperated out because of a good reason :D
	private static final String TIME_24_PATTERN = "(2[0-3]|[01]?[0-9])[:.]?([0-5][0-9])";
	private static final String GENERAL_TIME_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";//"((1[012]|(0?[1-9]))([:.][0-5][0-9])?(\\s)?(?i)(am|pm))|((2[0-3]|[01]?[0-9])[:.]?([0-5][0-9]))";
			//"("++")|("++")"
	
	public TimeParser( ) {
		pattern12 = Pattern.compile(TIME_12_PATTERN);
		pattern24 = Pattern.compile(TIME_24_PATTERN);
		pattern = Pattern.compile(GENERAL_TIME_PATTERN);
		startHour = -1; endHour = -1; startMin = -1; endMin = -1;
		dummyHour = -1; dummyMin = -1;
		
	}
	
	public static String getGeneralPattern() {
		return GENERAL_TIME_PATTERN;
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
	
	public String extractTime(String inputS) {
		String s = null;
		matcher = pattern.matcher(inputS);
		
		if(matcher.find())
			s = matcher.group(0);
		
		return s;
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
					System.out.println("1st return of setStartDate: false");
					return false;
				}
			}
			System.out.println("2nd return of setStartDate: false");
			return false;
		}
		System.out.println("3rd return of setStartDate: false");
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
		return time.matches(GENERAL_TIME_PATTERN);
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
			
			System.out.println("first false of set12hour");
			return false;
		}
		System.out.println("second false of set12hour");
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