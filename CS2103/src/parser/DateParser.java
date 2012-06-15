/**
 *
 * This class features the date parsing abilities of Jot It Down
 * Enables the user to enter the desirable date in a variety of formats
 * 
 * @author Shubham Kaushal
 */

package parser;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DateParser {
	
	private Pattern pattern1, pattern2, pattern3, pattern4, pattern5, pattern;
	private Matcher matcher1, matcher2, matcher3, matcher4, matcher5, matcher;
	
	private int startDay, startMonth, startYear;
	private int endDay, endMonth, endYear;
	private int dummyDay, dummyMonth, dummyYear;
	
	private static final String TH ="((?i)(th)?)";
	private static final String ST ="((?i)(st)?)";
	private static final String ND ="((?i)(nd)?)";
	private static final String RD ="((?i)(rd)?)";
	private static final String MONTH_TEXT = "((?i)(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec))";
	private static final String MONTH_IN_DIGIT_DATE_WITH_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ \\-](0?[1-9]|1[012])[/ \\-]((19|20)?\\d\\d)";
	private static final String MONTH_IN_TEXT_DATE_WITH_YEAR = "((0?(1"+ST+"|2"+ND+"|3"+RD+"|[4-9]"+TH+")|1[0-9]"+TH+"|2(0"+TH+"|1"+ST+"|2"+ND+"|3"+RD+"|[4-9]"+TH+")|30"+TH+")|31"+ST+")[/ \\-\\,]"+MONTH_TEXT+"[/ \\-\\,]((19|20)?\\d\\d)";
	private static final String MONTH_IN_DIGIT_DATE_WITHOUT_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ \\-](0?[1-9]|1[012])";
	private static final String MONTH_IN_TEXT_DATE_WITHOUT_YEAR = "((0?(1"+ST+"|2"+ND+"|3"+RD+"|[4-9]"+TH+")|1[0-9]"+TH+"|2(0"+TH+"|1"+ST+"|2"+ND+"|3"+RD+"|[4-9]"+TH+")|30"+TH+")|31"+ST+")[/ \\-\\,]"+MONTH_TEXT;
	
	private static final String NEXT = "(?i)(next)";
	private static final String TODAY = "(?i)(today)";
	private static final String TOMORROW = "(?i)(tomorrow|tmr)";
	private static final String WEEKDAY = "("+NEXT+"[ ])?((?i)(monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun))";
	private static final String TODAY_TMR_WEEKDAY = "("+TODAY+")|("+TOMORROW+")|("+WEEKDAY+")";
	
	private static final String GENERAL_DATE_PATTERN = "(((?i)(on))[\\s])?(("
			+ MONTH_IN_DIGIT_DATE_WITH_YEAR + ")|("
			+ MONTH_IN_TEXT_DATE_WITH_YEAR + ")|("
			+ MONTH_IN_DIGIT_DATE_WITHOUT_YEAR + ")|("
			+ MONTH_IN_TEXT_DATE_WITHOUT_YEAR + ")|(" + TODAY_TMR_WEEKDAY + "))";
	
	/**
	 * Constructor
	 */
	public DateParser() {
		
		pattern1 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITH_YEAR);
		pattern2 = Pattern.compile(MONTH_IN_TEXT_DATE_WITH_YEAR);
		pattern3 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITHOUT_YEAR);
		pattern4 = Pattern.compile(MONTH_IN_TEXT_DATE_WITHOUT_YEAR);
		pattern5 = Pattern.compile(TODAY_TMR_WEEKDAY);
		
		pattern = Pattern.compile(GENERAL_DATE_PATTERN);
		
		startDay=-1; startMonth=-1; startYear=-1;
		endDay=-1; endMonth=-1; endYear=-1;
		dummyDay=-1; dummyMonth=-1; dummyYear=-1;
	}
	/**
	 * Resets the local attributes representing dummy date 
	 */
	private void resetDummyDate() {
		dummyDay=-1; dummyMonth=-1; dummyYear=-1;
	}
	/**fetches the local integer attributes representing start date
	 * 
	 * @return int[]
	 */
	public int[] getStartDate() {
		int[] startDateArr = {-1,-1,-1};
		
		if (startDay>0 && startMonth>0 && startYear>0) {
			startDateArr[0] =startDay;
			startDateArr[1] =startMonth;
			startDateArr[2] =startYear;
		}
		
		return startDateArr;
	}
	/**fetches the local integer attributes representing end date
	 * 
	 * @return int[]
	 */
	public int[] getEndDate() {
		int[] endDateArr = {-1,-1,-1};
		
		if (endDay>0 && endMonth>0 && endYear>0) {
			endDateArr[0] =endDay;
			endDateArr[1] =endMonth;
			endDateArr[2] =endYear;
		}
		
		return endDateArr;
	}
	/**Returns the Regular Expression string representing an acceptable date pattern
	 * 
	 * @return String Obj
	 */
	public static String getGeneralPattern() {
		return GENERAL_DATE_PATTERN;
	}
	
	/**Tries to set the local attributes representing start date
	 * 
	 * @param String Obj representing start date
	 * @return TRUE/FAlSE
	 */
	public boolean setStartDate(String startD) {
		if (startD==null || startD.isEmpty())
			return false;
		
		if (setMonthInDigitWithYear(startD) || setMonthInTextWithYear(startD) || setMonthInDigitWithoutYear(startD) || setMonthInTextWithoutYear(startD) || setByWeekday(startD)) {
			if (dummyDay>0 && dummyMonth>0 && dummyYear>0){
				startDay = dummyDay;
				startMonth = dummyMonth;
				startYear = dummyYear;
				resetDummyDate();
				return true;
			}
			
			else {
				return false;
			}
		}
		return false;
	}
	/**Tries to set the local attributes representing end date
	 * 
	 * @param String Obj representing start date
	 * @return TRUE/FAlSE
	 */
	public boolean setEndDate(String endD) {
		if (endD==null || endD.isEmpty())
			return false;
		
		if (setMonthInDigitWithYear(endD) || setMonthInTextWithYear(endD) || setMonthInDigitWithoutYear(endD) || setMonthInTextWithoutYear(endD) || setByWeekday(endD)) {
			if (dummyDay>0 && dummyMonth>0 && dummyYear>0){
				endDay = dummyDay;
				endMonth = dummyMonth;
				endYear = dummyYear;
				resetDummyDate();
				return true;
			}
			
			else {
				return false;
			}
		}
		return false;
	}
	/**Validates an input String Date
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FASLE
	 */
	public boolean isValidGeneral(final String date) {
		matcher = pattern.matcher(date);
		return matcher.matches();
	}
	/**Tries to set the local attributes representing Dummy Date based on the Month in Digit with year format
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FALSE
	 */
	private boolean setMonthInDigitWithYear(final String date) {
		
		matcher1 = pattern1.matcher(date);
		
		if (matcher1.matches()) {
			if (matcher1.group(1)!=null && matcher1.group(2)!=null && matcher1.group(3)!=null) {
				String dayString = matcher1.group(1);
				String monthString = matcher1.group(2);
				String yearString = matcher1.group(3);
				
				if (matcher1.group(4)==null)
					yearString = "20" + yearString;
				
				int dayInt = Integer.parseInt(dayString);
				int monthInt = Integer.parseInt(monthString);
				int yearInt = Integer.parseInt(yearString);
				 
				return setDummyDate(dayInt, monthInt, yearInt);
			}
		}
		return false;
	}
	/**Tries to set the local attributes representing Dummy Date based on the Month in text with year format
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FALSE
	 */
	private boolean setMonthInTextWithYear(final String date) {
		final String JAN = "(?i)(Jan|January)";
		final String FEB = "(?i)(Feb|February)";
		final String MAR = "(?i)(Mar|March)";
		final String APR = "(?i)(Apr|April)";
		final String MAY = "(?i)(May)";
		final String JUN = "(?i)(Jun|June)";
		final String JUL = "(?i)(Jul|July)";
		final String AUG = "(?i)(Aug|August)";
		final String SEP = "(?i)(Sep|September)";
		final String OCT = "(?i)(Oct|October)";
		final String NOV = "(?i)(Nov|November)";
		final String DEC = "(?i)(Dec|December)";

		matcher2 = pattern2.matcher(date);

		if (matcher2.matches()) {
			
			if (matcher2.group(29)!=null && matcher2.group(1)!=null && matcher2.group(31)!=null) {
				String monthString = matcher2.group(29);
				String dayString = matcher2.group(1);
				String yearString = matcher2.group(31);
				
				if (matcher2.group(32)==null)
					yearString = "20" + yearString;
				
				if (dayString.length()==3)
					dayString = dayString.substring(0, 1);
				else if (dayString.length()==4)
					dayString = dayString.substring(0, 2);
					
				int dayInt = Integer.parseInt(dayString);
				int yearInt = Integer.parseInt(yearString);
				int monthInt = -1;

				if (monthString.matches(JAN))		monthInt = 1;
				if (monthString.matches(FEB))		monthInt = 2;
				if (monthString.matches(MAR))		monthInt = 3;
				if (monthString.matches(APR))		monthInt = 4;
				if (monthString.matches(MAY))		monthInt = 5;
				if (monthString.matches(JUN))		monthInt = 6;
				if (monthString.matches(JUL))		monthInt = 7;
				if (monthString.matches(AUG))		monthInt = 8;
				if (monthString.matches(SEP))		monthInt = 9;
				if (monthString.matches(OCT))		monthInt = 10;
				if (monthString.matches(NOV))		monthInt = 11;
				if (monthString.matches(DEC))		monthInt = 12;
				
				return setDummyDate(dayInt, monthInt, yearInt);
			}
			
		}

		return false;
	}
	/**Tries to set the local attributes representing Dummy Date based on the Month in Digit without year format
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FALSE
	 */
	private boolean setMonthInDigitWithoutYear(final String date) {
		matcher3 = pattern3.matcher(date);
		
		if (matcher3.matches()) {
			if(matcher3.group(1)!=null && matcher3.group(2)!=null) {
				String dayString = matcher3.group(1);
				String monthString = matcher3.group(2);
				int dayInt = Integer.parseInt(dayString);
				int monthInt = Integer.parseInt(monthString);
				
				return setDummyDate(dayInt, monthInt);
			}
		}

		return false;
	}
	/**Tries to set the local attributes representing Dummy Date based on the Month in text without year format
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FALSE
	 */
	private boolean setMonthInTextWithoutYear(final String date) {
		final String JAN = "(?i)(Jan|January)";
		final String FEB = "(?i)(Feb|February)";
		final String MAR = "(?i)(Mar|March)";
		final String APR = "(?i)(Apr|April)";
		final String MAY = "(?i)(May)";
		final String JUN = "(?i)(Jun|June)";
		final String JUL = "(?i)(Jul|July)";
		final String AUG = "(?i)(Aug|August)";
		final String SEP = "(?i)(Sep|September)";
		final String OCT = "(?i)(Oct|October)";
		final String NOV = "(?i)(Nov|November)";
		final String DEC = "(?i)(Dec|December)";

		matcher4 = pattern4.matcher(date);

		if (matcher4.matches()) {
			
			for(int i=0; i<matcher4.groupCount(); i++)
				
			if (matcher4.group(1)!=null && matcher4.group(29)!=null) {
				String dayString = matcher4.group(1);
				String monthString = matcher4.group(29);
				
				if (dayString.length()==3)
					dayString = dayString.substring(0, 1);
				else if (dayString.length()==4)
					dayString = dayString.substring(0, 2);
				
				int dayInt = Integer.parseInt(dayString);
				int monthInt = -1;

				if (monthString.matches(JAN))		monthInt = 1;
				if (monthString.matches(FEB))		monthInt = 2;
				if (monthString.matches(MAR))		monthInt = 3;
				if (monthString.matches(APR))		monthInt = 4;
				if (monthString.matches(MAY))		monthInt = 5;
				if (monthString.matches(JUN))		monthInt = 6;
				if (monthString.matches(JUL))		monthInt = 7;
				if (monthString.matches(AUG))		monthInt = 8;
				if (monthString.matches(SEP))		monthInt = 9;
				if (monthString.matches(OCT))		monthInt = 10;
				if (monthString.matches(NOV))		monthInt = 11;
				if (monthString.matches(DEC))		monthInt = 12;
				
				return setDummyDate(dayInt, monthInt);
			}
			
		}
		
		return false;
	}
	/**Tries to set the local attributes representing Dummy Date based on the weekday format
	 * 
	 * @param String Obj representing Date
	 * @return TRUE/FALSE
	 */
	private boolean setByWeekday (final String s) {
		final String MON = "(?i)(mon|monday)";
		final String TUE = "(?i)(tue|tuesday)";
		final String WED = "(?i)(wed|wednesday)";
		final String THU = "(?i)(thu|thursday)";
		final String FRI = "(?i)(fri|friday)";
		final String SAT = "(?i)(sat|saturday)";
		final String SUN = "(?i)(sun|sunday)";
		
		matcher5 = pattern5.matcher(s);
		
		GregorianCalendar calen = new GregorianCalendar();
		
		if (matcher5.matches()) {
			if (s.matches(TODAY)) {
				setDummyDate(calen);
				return true;
			}
			else if (s.matches(TOMORROW)) {
				calen.add(GregorianCalendar.DATE, 1);
				setDummyDate(calen);
				return true;
			}
			else if (s.matches(WEEKDAY)) {
				
				String nextString = matcher5.group(6);
				String inputWeekString = matcher5.group(8);
				
				int inputWeekDay = -1;

				if (inputWeekString.matches(SUN)) inputWeekDay = 1;
				if (inputWeekString.matches(MON)) inputWeekDay = 2;
				if (inputWeekString.matches(TUE)) inputWeekDay = 3;
				if (inputWeekString.matches(WED)) inputWeekDay = 4;
				if (inputWeekString.matches(THU)) inputWeekDay = 5;
				if (inputWeekString.matches(FRI)) inputWeekDay = 6;
				if (inputWeekString.matches(SAT)) inputWeekDay = 7;
				
				if (inputWeekDay>0) {
					int dayDiff = (inputWeekDay - calen.get(GregorianCalendar.DAY_OF_WEEK));
					
					if (nextString!=null && nextString.matches(NEXT))
						calen.add(GregorianCalendar.DATE, 7);
					
					if (dayDiff<0)
						calen.add(GregorianCalendar.DATE, (7+dayDiff));
					else
						calen.add(GregorianCalendar.DATE, (dayDiff));
					
					setDummyDate(calen);
					return true;
				}
				
				return false;
			}
			else
				return false;
		}
		
		return false;
		
	}
	/**Tries to set the dummy date based on the integer parameters
	 * 
	 * @param integer Day
	 * @param integer Mon
	 * @return TRUE/FALSE
	 */
	private boolean setDummyDate (int dayInt, int monthInt) {
		GregorianCalendar calen = new GregorianCalendar();
		int currMonth = calen.get(GregorianCalendar.MONTH) + 1;
		int currDay = calen.get(GregorianCalendar.DATE);
		int yearInt;
		
		if (monthInt < currMonth)
			yearInt = calen.get(GregorianCalendar.YEAR) + 1;
		else if (monthInt == currMonth) {
			if (dayInt < currDay)
				yearInt = calen.get(GregorianCalendar.YEAR) + 1;
			else
				yearInt = calen.get(GregorianCalendar.YEAR);
		} 
		else
			yearInt = calen.get(GregorianCalendar.YEAR);
		
		// only 1,3,5,7,8,10,12 has 31 days
		if (dayInt == 31 && ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
			return false; 
		
		// leap year testing
		else if (monthInt == 2) { // leap year testing
			if (yearInt%4==0) {
				if (dayInt == 30 || dayInt == 31)
					return false;
			} 
			else {
				if (dayInt == 29 || dayInt == 30 || dayInt == 31)
					return false;
			}
		} 
		
		dummyDay=dayInt;
		dummyMonth=monthInt;
		dummyYear=yearInt;
		
		return true;
	}
	/**Tries to set the dummy date based on the integer parameters
	 * 
	 * @param integer Day
	 * @param integer Mon
	 * @param integer year
	 * @return TRUE/FALSE
	 */
	private boolean setDummyDate (int dayInt, int monthInt, int yearInt) {
		// only 1,3,5,7,8,10,12 has 31 days
		if (dayInt == 31 && ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
			return false; 
		
		 // leap year testing
		else if (monthInt == 2) {
			if (yearInt % 4 == 0) {
				if (dayInt == 30 || dayInt == 31)
					return false;
			} 
			else {
				if (dayInt == 29 || dayInt == 30 || dayInt == 31)
					return false;
			}
		} 
		
		dummyDay=dayInt;
		dummyMonth=monthInt;
		dummyYear=yearInt;
		return true;
	}
	/**Tries to set the dummy date based on the GregorianCalendar Obj as parameter
	 * 
	 * @param GregorianCalendar Obj
	 * @return TRUE/FALSE
	 */
	private void setDummyDate (GregorianCalendar c) {
		dummyDay = c.get(GregorianCalendar.DATE);
		dummyMonth = c.get(GregorianCalendar.MONTH) + 1 ;
		dummyYear = c.get(GregorianCalendar.YEAR);
	}

}
