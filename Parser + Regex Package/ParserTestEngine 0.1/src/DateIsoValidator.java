import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class DateIsoValidator{
 
  private Pattern pattern1, pattern2;
  private Matcher matcher1, matcher2;
 
  private static final String DATE_PATTERN_1 = "(0?[1-9]|[12][0-9]|3[01])[/ -](0?[1-9]|1[012])[/ -]((19|20)\\d\\d)";
  // user can use / or - to separate
  private static final String DATE_PATTERN_2 = "((0?[1-9]|[12][0-9]|3[01])(?i)(th))[/ - \\s \\,](\\s)?((?i)(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|Jule|Aug|August|Sep|September|Oct|October|Nov|November|Dec|December))[/ - \\s \\,](\\s)?((19|20)\\d\\d)";
  
  //(((0?1)(?i)(st))|((0?2)(?i)(nd))|
  
  public DateIsoValidator(){
	  pattern1 = Pattern.compile(DATE_PATTERN_1);
	  pattern2 = Pattern.compile(DATE_PATTERN_2);
  }
 
  /**
   * Validate date format with regular expression
   * @param date date address for validation
   * @return true valid date format, false invalid date format
   */
  public boolean validateGeneral (final String date) {
	  return (validateMonthInText(date)||validateMonthInDigit(date));		  
  }
  
  public boolean validateMonthInText(final String date){
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
  
  public boolean validateMonthInDigit(final String date){
	  
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
}

