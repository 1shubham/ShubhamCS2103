package parser;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {

	private Pattern pattern1, pattern2, pattern3, pattern4, pattern5, pattern;
	private Matcher matcher1, matcher2, matcher3, matcher4, matcher5, matcher;
	
	private static int startDay=-1, startMonth=-1, startYear=-1;
	private static int endDay=-1, endMonth=-1, endYear=-1;
	private static int dummyDay=-1, dummyMonth=-1, dummyYear=-1;
	
	private static final String MONTH_IN_DIGIT_DATE_WITH_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ -](0?[1-9]|1[012])[/ -]((19|20)\\d\\d)";
	private static final String MONTH_IN_TEXT_DATE_WITH_YEAR = "((0?[1-9]|[12][0-9]|3[01])(?i)(th)?)[/ - \\s \\,](\\s)?((?i)(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec))[/ - \\s \\,](\\s)?((19|20)\\d\\d)";
	private static final String MONTH_IN_DIGIT_DATE_WITHOUT_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ -](0?[1-9]|1[012])";
	private static final String MONTH_IN_TEXT_DATE_WITHOUT_YEAR = "((0?[1-9]|[12][0-9]|3[01])(?i)(th)?)[/ - \\s \\,](\\s)?((?i)(January|Jan|February|Feb|March|Mar|April|Apr|May|June|Jun|July|Jul|August|Aug|September|Sep|October|Oct|November|Nov|December|Dec))";
	
	private static final String TODAY_REGEX = "(?i)(today)";
	private static final String TOMORROW_REGEX = "(?i)(tmr|tomorrow)";
	private static final String WEEKDAY_REGEX = "(?i)(monday|mon|tuesday|tue|wednesday|wed|thursday|thu|friday|fri|saturday|sat|sunday|sun)";
	private static final String TODAY_TMR_WEEKDAY_REGEX = "("+TODAY_REGEX+")|("+TOMORROW_REGEX+")|("+WEEKDAY_REGEX+")";
	
	private static final String GENERAL_DATE_PATTERN = "(((?i)(on))[\\s])?(("
			+ MONTH_IN_DIGIT_DATE_WITH_YEAR + ")|("
			+ MONTH_IN_TEXT_DATE_WITH_YEAR + ")|("
			+ MONTH_IN_DIGIT_DATE_WITHOUT_YEAR + ")|("
			+ MONTH_IN_TEXT_DATE_WITHOUT_YEAR + ")|(" + TODAY_TMR_WEEKDAY_REGEX + "))";
	
	private void resetDummyDate() {
		dummyDay=-1; dummyMonth=-1; dummyYear=-1;
	}
	
	public int[] getStartDate() {
		int[] startDateArr = {-1,-1,-1};
		
		if (startDay>0 && startMonth>0 && startYear>0) {
			startDateArr[0] =startDay;
			startDateArr[1] =startMonth;
			startDateArr[2] =startYear;
		}
		
		return startDateArr;
	}
	
	public int[] getEndDate() {
		int[] endDateArr = {-1,-1,-1};
		
		if (endDay>0 && endMonth>0 && endYear>0) {
			endDateArr[0] =endDay;
			endDateArr[1] =endMonth;
			endDateArr[2] =endYear;
		}
		
		return endDateArr;
	}
	
	public DateParser() {
		
		/*
		 * assign more meaningful names to these patterns
		 */
		pattern1 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITH_YEAR);
		pattern2 = Pattern.compile(MONTH_IN_TEXT_DATE_WITH_YEAR);
		pattern3 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITHOUT_YEAR);
		pattern4 = Pattern.compile(MONTH_IN_TEXT_DATE_WITHOUT_YEAR);
		pattern5 = Pattern.compile(TODAY_TMR_WEEKDAY_REGEX);
		
		pattern = Pattern.compile(GENERAL_DATE_PATTERN);
	}

	private String removeExtraSpaces(String s) {
		return s.replaceAll("\\s+", " ");
	}

	public static String getGeneralPattern() {
		return GENERAL_DATE_PATTERN;
	}

	public String extractDate(String inputS) {
		String s = null;
		matcher = pattern.matcher(inputS);

		if (matcher.find())
			s = matcher.group(4);
		
		/*
		System.out.println("no. of groups in date string: "+matcher.groupCount());
		System.out.println("group 1 string: "+matcher.group(1));
		System.out.println("group 2 string: "+matcher.group(2));
		System.out.println("group 3 string: "+matcher.group(3));
		System.out.println("group 4 string: "+matcher.group(4));
		System.out.println("group 5 string: "+matcher.group(5));
		*/
		
		/*
		 * do not include "on" if its included
		 */
		
		return s;
	}
	
	public void printDates() { //for your testing
		if ((startDay>0 && startMonth>0 && startYear>0)) {
			System.out.println("startDay: "+startDay);
			System.out.println("startMon: "+startMonth);
			System.out.println("startYear: "+startYear);
		}
		
		if ((endDay>0 && endMonth>0 && endYear>0)) {
			System.out.println("endDay: "+startDay);
			System.out.println("endMon: "+endMonth);
			System.out.println("endYear: "+endYear);
		}
		
		else
			System.out.println("no attributes exist!");
	}
	
	public boolean setStartDate(String startD) {
		if (setMonthInDigitWithYear(startD) || (setMonthInTextWithYear(startD)) || (setMonthInDigitWithoutYear(startD)) || (setMonthInTextWithoutYear(startD)) || (inferAndSetDate(startD))) {
			if (dummyDay>0 && dummyMonth>0 && dummyYear>0){
				startDay = dummyDay;
				startMonth = dummyMonth;
				startYear = dummyYear;
				resetDummyDate();
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
	
	public boolean setEndDate(String endD) {
		if (setMonthInDigitWithYear(endD) || (setMonthInTextWithYear(endD)) || (setMonthInDigitWithoutYear(endD)) || (setMonthInTextWithoutYear(endD)) || (inferAndSetDate(endD))) {
			if (dummyDay>0 && dummyMonth>0 && dummyYear>0){
				endDay = dummyDay;
				endMonth = dummyMonth;
				endYear = dummyYear;
				resetDummyDate();
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
	
	public boolean isValidGeneral(final String date) {
		matcher = pattern.matcher(date);
		return matcher.matches();
	}

	public boolean setMonthInDigitWithYear(final String date) {
		
		matcher1 = pattern1.matcher(date);
		
		if (matcher1.matches()) {
			String dayString = matcher1.group(1);
			String monthString = matcher1.group(2);
			String yearString = matcher1.group(3);
			int dayInt = Integer.parseInt(dayString);
			int monthInt = Integer.parseInt(monthString);
			int year = Integer.parseInt(yearString);
			
			/*
			 * System.out.println("inputDay= "+dayInt);
			 * System.out.println("currDay= "+currDay);
			 * System.out.println("inputMonth= "+monthInt);
			 * System.out.println("currMonth= "+currMonth);
			 */

			if (dayInt == 31
					&& ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
				return false; // only 1,3,5,7,8,10,12 has 31 days

			// ----ATTENTION!------add the correct definition of leap year!!!
			// -----current=julian calender-----
			else if (monthInt == 2) {
				if (year % 4 == 0) {
					if (dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				} 
				else {
					if (dayInt == 29 || dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				}
			} 
			else {
				dummyDay=dayInt;
				dummyMonth=monthInt;
				dummyYear=year;
				return true;
			}

		}

		return false;
		/*
		matcher1 = pattern1.matcher(date);

		if (matcher1.matches()) {
			matcher1.reset();

			if (matcher1.find()) {
				String day = matcher1.group(1);
				String month = matcher1.group(2);
				int year = Integer.parseInt(matcher1.group(3));

				if (day.equals("31")
						&& (month.equals("4") || month.equals("6")
								|| month.equals("9") || month.equals("11")
								|| month.equals("04") || month.equals("06") || month
									.equals("09")))
					return false; // only 1,3,5,7,8,10,12 has 31 days

				// ----ATTENTION!------add the correct definition of leap
				// year!!!
				else if (month.equals("2") || month.equals("02")) { // leap year
					if (year % 4 == 0) {
						if (day.equals("30") || day.equals("31"))
							return false;
						else
							return true;
					} else {
						if (day.equals("29") || day.equals("30")
								|| day.equals("31"))
							return false;
						else
							return true;
					}
				} else
					return true;
			} else
				return false;
		} else
			return false;
*/
	}

	public boolean setMonthInTextWithYear(final String date) {
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
			String monthString = matcher2.group(6);
			int dayInt = Integer.parseInt(matcher2.group(2));
			int year = Integer.parseInt(matcher2.group(8));
			int monthInt = -1;

			if (monthString.matches(JAN))
				monthInt = 1;
			if (monthString.matches(FEB))
				monthInt = 2;
			if (monthString.matches(MAR))
				monthInt = 3;
			if (monthString.matches(APR))
				monthInt = 4;
			if (monthString.matches(MAY))
				monthInt = 5;
			if (monthString.matches(JUN))
				monthInt = 6;
			if (monthString.matches(JUL))
				monthInt = 7;
			if (monthString.matches(AUG))
				monthInt = 8;
			if (monthString.matches(SEP))
				monthInt = 9;
			if (monthString.matches(OCT))
				monthInt = 10;
			if (monthString.matches(NOV))
				monthInt = 11;
			if (monthString.matches(DEC))
				monthInt = 12;
			
			/*
			System.out.println("day int: "+dayInt);
			System.out.println("month int: "+monthInt);
			System.out.println("year int: "+year);
			*/

			if (dayInt == 31
					&& ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
				return false; // only 1,3,5,7,8,10,12 has 31 days

			// ----ATTENTION!------add the correct definition of leap year!!!
			// -----current=julian calender-----
			else if (monthInt == 2) { // leap year testing
				if (year % 4 == 0) {
					if (dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						/*
						System.out.println("dummyDay: "+dummyDay);
						System.out.println("dummyMon: "+dummyMonth);
						System.out.println("dummyYear: "+dummyYear);
						*/
						return true;
					}
				} else {
					if (dayInt == 29 || dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						/*
						System.out.println("dummyDay: "+dummyDay);
						System.out.println("dummyMon: "+dummyMonth);
						System.out.println("dummyYear: "+dummyYear);
						*/
						return true;
					}
				}
			} else {
				dummyDay=dayInt;
				dummyMonth=monthInt;
				dummyYear=year;
				/*
				System.out.println("dummyDay: "+dummyDay);
				System.out.println("dummyMon: "+dummyMonth);
				System.out.println("dummyYear: "+dummyYear);
				*/
				return true;
			}
		}

		return false;
	}

	public boolean setMonthInDigitWithoutYear(final String date) {
		matcher3 = pattern3.matcher(date);
		
		if (matcher3.matches()) {
			String dayString = matcher3.group(1);
			String monthString = matcher3.group(2);
			int dayInt = Integer.parseInt(dayString);
			int monthInt = Integer.parseInt(monthString);

			GregorianCalendar calen = new GregorianCalendar();
			int currMonth = calen.get(GregorianCalendar.MONTH) + 1;
			int currDay = calen.get(GregorianCalendar.DATE);
			int year;

			/*
			 * System.out.println("inputDay= "+dayInt);
			 * System.out.println("currDay= "+currDay);
			 * System.out.println("inputMonth= "+monthInt);
			 * System.out.println("currMonth= "+currMonth);
			 */

			if (monthInt < currMonth)
				year = calen.get(GregorianCalendar.YEAR) + 1;
			else if (monthInt == currMonth) {
				if (dayInt < currDay)
					year = calen.get(GregorianCalendar.YEAR) + 1;
				else
					year = calen.get(GregorianCalendar.YEAR);
			} else
				year = calen.get(GregorianCalendar.YEAR);

			
			if (dayInt == 31
					&& ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
				return false; // only 1,3,5,7,8,10,12 has 31 days

			// ----ATTENTION!------add the correct definition of leap year!!!
			// -----current=julian calender-----
			else if (monthInt == 2) {
				if (year % 4 == 0) {
					if (dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				} 
				else {
					if (dayInt == 29 || dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				}
			} 
			else {
				dummyDay=dayInt;
				dummyMonth=monthInt;
				dummyYear=year;
				return true;
			}

		}

		return false;
	}

	public boolean setMonthInTextWithoutYear(final String date) {
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

			String dayString = matcher4.group(2);
			String monthString = matcher4.group(6);
			int dayInt = Integer.parseInt(dayString);
			int monthInt = -1;

			if (monthString.matches(JAN))
				monthInt = 1;
			if (monthString.matches(FEB))
				monthInt = 2;
			if (monthString.matches(MAR))
				monthInt = 3;
			if (monthString.matches(APR))
				monthInt = 4;
			if (monthString.matches(MAY))
				monthInt = 5;
			if (monthString.matches(JUN))
				monthInt = 6;
			if (monthString.matches(JUL))
				monthInt = 7;
			if (monthString.matches(AUG))
				monthInt = 8;
			if (monthString.matches(SEP))
				monthInt = 9;
			if (monthString.matches(OCT))
				monthInt = 10;
			if (monthString.matches(NOV))
				monthInt = 11;
			if (monthString.matches(DEC))
				monthInt = 12;

			GregorianCalendar calen = new GregorianCalendar();
			int currMonth = calen.get(GregorianCalendar.MONTH) + 1;
			int currDay = calen.get(GregorianCalendar.DATE);
			int year;

			/*
			 * System.out.println("inputDay= "+dayInt);
			 * System.out.println("currDay= "+currDay);
			 * System.out.println("inputMonth= "+monthInt);
			 * System.out.println("currMonth= "+currMonth);
			 */

			if (monthInt < currMonth)
				year = calen.get(GregorianCalendar.YEAR) + 1;
			else if (monthInt == currMonth) {
				if (dayInt < currDay)
					year = calen.get(GregorianCalendar.YEAR) + 1;
				else
					year = calen.get(GregorianCalendar.YEAR);
			} else
				year = calen.get(GregorianCalendar.YEAR);

			// System.out.println("year= "+year);

			if (dayInt == 31
					&& ((monthInt == 4) || (monthInt == 6) || (monthInt == 9) || (monthInt == 11)))
				return false; // only 1,3,5,7,8,10,12 has 31 days

			// ----ATTENTION!------add the correct definition of leap year!!!
			// -----current=julian calender-----
			else if (monthInt == 2) { // leap year testing
				if (year % 4 == 0) {
					if (dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				} else {
					if (dayInt == 29 || dayInt == 30 || dayInt == 31)
						return false;
					else {
						dummyDay=dayInt;
						dummyMonth=monthInt;
						dummyYear=year;
						return true;
					}
				}
			} else {
				dummyDay=dayInt;
				dummyMonth=monthInt;
				dummyYear=year;
				return true;
			}
		}

		return false;

	}
	
	// inferAndSetDate() also, include condition in setStartDate, setEndDate
	public boolean inferAndSetDate (final String s) {
		final String MON = "(?i)(mon|monday)";
		final String TUE = "(?i)(tue|tuesday)";
		final String WED = "(?i)(wed|wednesday)";
		final String THU = "(?i)(thu|thursday)";
		final String FRI = "(?i)(fri|friday)";
		final String SAT = "(?i)(sat|saturday)";
		final String SUN = "(?i)(sun|sunday)";
		/*
		 * set numbers to weeks for easy comparison!
		 */
		
		matcher5 = pattern5.matcher(s);
		
		GregorianCalendar calen = new GregorianCalendar();
		
		if (matcher5.matches()) {
			if (s.matches(TODAY_REGEX)) {
				dummyDay = calen.get(GregorianCalendar.DATE);
				dummyMonth = calen.get(GregorianCalendar.MONTH) + 1 ;
				dummyYear = calen.get(GregorianCalendar.YEAR);
				return true;
			}
			else if (s.matches(TOMORROW_REGEX)) {
				calen.add(GregorianCalendar.DATE, 1);
				dummyDay = calen.get(GregorianCalendar.DATE);
				dummyMonth = calen.get(GregorianCalendar.MONTH) + 1;
				dummyYear = calen.get(GregorianCalendar.YEAR);
				return true;
			}
			else if (s.matches(WEEKDAY_REGEX)) { //sunday is considered day 1 of the week
				int inputWeekDay = -1;

				if (s.matches(SUN)) inputWeekDay = 1;
				if (s.matches(MON)) inputWeekDay = 2;
				if (s.matches(TUE)) inputWeekDay = 3;
				if (s.matches(WED)) inputWeekDay = 4;
				if (s.matches(THU)) inputWeekDay = 5;
				if (s.matches(FRI)) inputWeekDay = 6;
				if (s.matches(SAT)) inputWeekDay = 7;
				
				if (inputWeekDay>0) {
					int addDay = (inputWeekDay - calen.get(GregorianCalendar.DAY_OF_WEEK));
					
					if (addDay<0)
						calen.add(GregorianCalendar.DATE, (7+addDay));
					else
						calen.add(GregorianCalendar.DATE, (addDay));
					
					dummyDay = calen.get(GregorianCalendar.DATE);
					dummyMonth = calen.get(GregorianCalendar.MONTH) + 1;
					dummyYear = calen.get(GregorianCalendar.YEAR);
					return true;
				}
				
				return false;
			}
			else
				return false;
		}
		
		return false;
			
		/*
		int currMonth 
		int currDay
		int year;
		*/
		
	}
	
	public void dummyFunction() {
		GregorianCalendar calen = new GregorianCalendar();
		
		System.out.println("date:"+calen.get(GregorianCalendar.DATE));
		System.out.println("month:"+calen.get(GregorianCalendar.MONTH));
		System.out.println("year:"+calen.get(GregorianCalendar.YEAR));
		System.out.println("day of month:"+calen.get(GregorianCalendar.DAY_OF_MONTH));
		System.out.println("day of week:"+calen.get(GregorianCalendar.DAY_OF_WEEK));
		
		calen.add(GregorianCalendar.DATE, 5);
		
		System.out.println("date:"+calen.get(GregorianCalendar.DATE));
		System.out.println("month:"+calen.get(GregorianCalendar.MONTH));
		System.out.println("year:"+calen.get(GregorianCalendar.YEAR));
		System.out.println("day of month:"+calen.get(GregorianCalendar.DAY_OF_MONTH));
		System.out.println("day of week:"+calen.get(GregorianCalendar.DAY_OF_WEEK));
		
	}

}
