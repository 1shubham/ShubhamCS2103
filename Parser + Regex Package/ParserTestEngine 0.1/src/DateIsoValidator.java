import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class DateIsoValidator{
 
  private Pattern pattern1, pattern2, pattern3, pattern4;
  private Matcher matcher1, matcher2, matcher3, matcher4;
 
  private static final String MONTH_IN_DIGIT_DATE_WITH_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ -](0?[1-9]|1[012])[/ -]((19|20)\\d\\d)";
  private static final String MONTH_IN_TEXT_DATE_WITH_YEAR = "((0?[1-9]|[12][0-9]|3[01])(?i)(th)?)[/ - \\s \\,](\\s)?((?i)(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|Jule|Aug|August|Sep|September|Oct|October|Nov|November|Dec|December))[/ - \\s \\,](\\s)?((19|20)\\d\\d)";
  private static final String MONTH_IN_DIGIT_DATE_WITHOUT_YEAR = "(0?[1-9]|[12][0-9]|3[01])[/ -](0?[1-9]|1[012])";
  private static final String MONTH_IN_TEXT_DATE_WITHOUT_YEAR = "((0?[1-9]|[12][0-9]|3[01])(?i)(th)?)[/ - \\s \\,](\\s)?((?i)(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|Jule|Aug|August|Sep|September|Oct|October|Nov|November|Dec|December))";
  
  public DateIsoValidator(){
	  pattern1 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITH_YEAR);
	  pattern2 = Pattern.compile(MONTH_IN_TEXT_DATE_WITH_YEAR);
	  pattern3 = Pattern.compile(MONTH_IN_DIGIT_DATE_WITHOUT_YEAR);
	  pattern4 = Pattern.compile(MONTH_IN_TEXT_DATE_WITHOUT_YEAR);
  }
 
  /**
   * Validate date format with regular expression
   * @param date date address for validation
   * @return true valid date format, false invalid date format
   */
  public boolean isValidGeneral (final String date) {
	  boolean format1, format2, format3, format4;
	  format1 = isValidMonthInDigitWithYear(date);
	  format2 = isValidMonthInTextWithYear(date);
	  format3 = isValidMonthInDigitWithoutYear(date);
	  format4 = isValidMonthInTextWithoutYear(date);
	  
	  return (format1||format2||format3||format4);		  
  }
  
  public boolean isValidMonthInDigitWithYear(final String date){
	  
	  matcher1 = pattern1.matcher(date);
	  
	  if(matcher1.matches()){
		   matcher1.reset();

		   if(matcher1.find()){
			   String day = matcher1.group(1);
			   String month = matcher1.group(2);
			   int year = Integer.parseInt(matcher1.group(3));
			   
			   if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") || 
					   month.equals("11") || month.equals("04") || month .equals("06") || 
					   month.equals("09"))) 
				   return false; // only 1,3,5,7,8,10,12 has 31 days
			   
			   //----ATTENTION!------add the correct definition of leap year!!!  
			   else if (month.equals("2") || month.equals("02")) {  //leap year
				   if(year % 4==0){
                	  if(day.equals("30") || day.equals("31"))
               	   		return false;
               	   else
               		   return true; 
                  }
                  else {
               	   if(day.equals("29")||day.equals("30")||day.equals("31"))
               		   return false;
               	   else
               		   return true;
                  }
			   }
			   else				 
				   return true;				 
		   }
		   else
   	      return false;		  
	   }
	   else
		   return false;
	   
  }

  public boolean isValidMonthInTextWithYear(final String date){
	   final String FEB = "(?i)(Feb|February)";
	   final String APR = "(?i)(Apr|April)";
	   final String JUN = "(?i)(Jun|June)";
	   final String SEP = "(?i)(Sep|September)";
	   final String NOV = "(?i)(Nov|November)";
	   
	   matcher2 = pattern2.matcher(date);
	   
	   if(matcher2.matches()){
		   matcher2.reset();
 
		   if(matcher2.find()){
			   String day = matcher2.group(2);
			   String month = matcher2.group(6);
			   int year = Integer.parseInt(matcher2.group(8));
			   
			   if (day.equals("31") && (month.matches(APR) || month.matches(JUN) || month.matches(SEP) || month.matches(NOV))) 
				   return false; // only 1,3,5,7,8,10,12 has 31 days
			   
			   //----ATTENTION!------add the correct definition of leap year!!
			   else if (month.matches(FEB)) {  //leap year
                   if(year % 4==0){
                	   if(day.equals("30") || day.equals("31"))
                		   return false;
                	   else
                		   return true; 
                   }
                   else {
                	   if(day.equals("29")||day.equals("30")||day.equals("31"))
                		   return false;
                	   else
                		   return true;
			  
                   }
			   }
			   
			   else				 
				   return true;				 
	      
		   }
		   else
    	      return false;
	   		  
	   }
	   else
		   return false;

   }
  
  public boolean isValidMonthInDigitWithoutYear(final String date){
	  matcher3 = pattern3.matcher(date);
	    
	  if(matcher3.matches()){
		   
		   String dayString = matcher3.group(1);
		   String monthString = matcher3.group(2);
		   int dayInt = Integer.parseInt(dayString);
		   int monthInt = Integer.parseInt(monthString);
		   
		   GregorianCalendar calen = new GregorianCalendar();
		   int currMonth = calen.get(GregorianCalendar.MONTH) + 1; 
		   int currDay = calen.get(GregorianCalendar.DATE); 
		   int year;
		   
		   /*
		   System.out.println("inputDay= "+dayInt);
		   System.out.println("currDay= "+currDay);
		   System.out.println("inputMonth= "+monthInt);
		   System.out.println("currMonth= "+currMonth);
		   */
		   
		   if (monthInt<currMonth) 
			   year = calen.get(GregorianCalendar.YEAR) + 1;
		   else if (monthInt==currMonth){
			   if (dayInt<currDay)
				   year = calen.get(GregorianCalendar.YEAR) + 1;
			   else
				   year = calen.get(GregorianCalendar.YEAR);	   
		   }
		   else
			   year = calen.get(GregorianCalendar.YEAR);
		   
		   //System.out.println("year= "+year);
		   
		   if (dayInt==31 && ((monthInt==4)||(monthInt==6)||(monthInt==9)||(monthInt==11)))
			   return false; // only 1,3,5,7,8,10,12 has 31 days
		   
		   //----ATTENTION!------add the correct definition of leap year!!!  -----current=julian calender-----
		   else if (monthInt == 2) {  //leap year testing
			   if(year%4 == 0){
				   if(dayInt == 30 || dayInt == 31)
					   return false;
				   else
					   return true;
			   }
			   else {
				   if(dayInt == 29 || dayInt == 30 || dayInt == 31)
				      return false;
				   else
					   return true;
			   }
		   }
		   else
			   return true;		
			   	   		 
	  }
	  
	  return false;	  
  }
  
  public boolean isValidMonthInTextWithoutYear(final String date){
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
	  
	  if(matcher4.matches()){
		  
		  String dayString = matcher4.group(2);
		  String monthString = matcher4.group(6);
		  int dayInt = Integer.parseInt(dayString);
		  int monthInt=-1;
		   
		  if (monthString.matches(JAN)) monthInt = 1;  
		  if (monthString.matches(FEB)) monthInt = 2;  
		  if (monthString.matches(MAR)) monthInt = 3;  
		  if (monthString.matches(APR)) monthInt = 4;  
		  if (monthString.matches(MAY)) monthInt = 5;  
		  if (monthString.matches(JUN)) monthInt = 6;  
		  if (monthString.matches(JUL)) monthInt = 7;  
		  if (monthString.matches(AUG)) monthInt = 8;  
		  if (monthString.matches(SEP)) monthInt = 9;  
		  if (monthString.matches(OCT)) monthInt = 10;  
		  if (monthString.matches(NOV)) monthInt = 11;  
		  if (monthString.matches(DEC)) monthInt = 12;  
		  
		    
		   GregorianCalendar calen = new GregorianCalendar();
		   int currMonth = calen.get(GregorianCalendar.MONTH) + 1; 
		   int currDay = calen.get(GregorianCalendar.DATE); 
		   int year;
		   
		   /*
		   System.out.println("inputDay= "+dayInt);
		   System.out.println("currDay= "+currDay);
		   System.out.println("inputMonth= "+monthInt);
		   System.out.println("currMonth= "+currMonth);
		   */
		   
		   if (monthInt<currMonth) 
			   year = calen.get(GregorianCalendar.YEAR) + 1;
		   else if (monthInt==currMonth){
			   if (dayInt<currDay)
				   year = calen.get(GregorianCalendar.YEAR) + 1;
			   else
				   year = calen.get(GregorianCalendar.YEAR);	   
		   }
		   else
			   year = calen.get(GregorianCalendar.YEAR);
		   
		   //System.out.println("year= "+year);
		   
		   if (dayInt==31 && ((monthInt==4)||(monthInt==6)||(monthInt==9)||(monthInt==11)))
			   return false; // only 1,3,5,7,8,10,12 has 31 days
		   
		   //----ATTENTION!------add the correct definition of leap year!!!  -----current=julian calender-----
		   else if (monthInt == 2) {  //leap year testing
			   if(year%4 == 0){
				   if(dayInt == 30 || dayInt == 31)
					   return false;
				   else
					   return true;
			   }
			   else {
				   if(dayInt == 29 || dayInt == 30 || dayInt == 31)
				      return false;
				   else
					   return true;
			   }
		   }
		   else
			   return true;		
			   	   		 
	  }
	  
	  return false;	
	  
  }
  
  public boolean dummyFunction (final String s) { 
	  matcher3 = pattern3.matcher(s);
	  
	  if (matcher3.matches()) {
		  
		  String whole = matcher3.group(0);
		  String part1 = matcher3.group(1);
		  String part2 = matcher3.group(2);
		  //String part3 = matcher3.group(3);
	   
		  //-----ATTENTION!----when two digit month, this is only storing 1 digit
		  System.out.println("whole= "+whole);
		  System.out.println("part1= "+part1);
		  System.out.println("part2= "+part2);
		  //System.out.println("part3= "+part3);
		  
		  return true;
	  }
	  
	  return false;

  }
  
}

