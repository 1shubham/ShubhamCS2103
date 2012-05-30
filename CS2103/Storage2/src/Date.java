import java.util.Calendar;
import java.text.SimpleDateFormat;
public class Date 
{
private int day;
private int month;
private int year;
private boolean correct;
/** Constructor*/
public Date(int theDay,int theMonth,int theYear)
{
	correct=true;
	checkMonth(theMonth);
	if(correct)
	checkYear(theYear);
	if(correct)
	checkDay(theDay);
}
/** returns the day in int format
 * 
 * @return the attribute day
 */
public int getDay()
{
	return day;
}
/** returns the month in int format
 * 
 * @return the attribute month
 */
public int getMonth()
{
	return month;
}
/** returns the year in int format
 * 
 * @return the attribute year
 */
public int getYear()
{
	return month;
}
/** checks if the provided Day is correct
 * 
 * @param d the day you want to set
 */
public void checkDay(int d)
{
	int daysPerMonth[]={0,31,28,31,30,31,30,31,31,30,31,30,31};
	if(year%400==0) 
	   daysPerMonth[2]=29;
	else if (year%100==0)
	   daysPerMonth[2]=28;
	else if (year%4==0) 
	   daysPerMonth[2]=29;
	else
	   daysPerMonth[2]=28;
	if(d>0 && d<=daysPerMonth[month])
		day=d;
	else 
		correct=false;
}
/** checks if the provided Month is correct
 * 
 * @param m the month you want to set
 */
public void checkMonth(int m)
{
	if(m>0 && m<=12)
		month=m;
	else
		correct=false;
}
/** check if the provided Year is correct
 * 
 * If the provided year is more than 100 years from the current year, correct should be false.
 * @param y the year you want to set
 */
public void checkYear(int y)
{
	 Calendar currentDate = Calendar.getInstance();
	  SimpleDateFormat formatter= 
	  new SimpleDateFormat("yyyy");
	  String dateNow = formatter.format(currentDate.getTime());
	  int currentYear=Integer.parseInt(dateNow);
	  if(y>=currentYear && y<=currentYear+100)
		  year=y;
	  else
		  correct=false;
}
/** checks if the format of the Date given is correct
 * 
 * @return true if the format is correct, otherwise false
 */
public boolean isCorrect()
{
	return correct;
}
/** outputs the date in the format: dd/mm/yy
 * 
 * @return the date in the form of a string
 */
public String toString()
{
	return String.format("%02d/%02d/%02d", day,month,year);
}
}
