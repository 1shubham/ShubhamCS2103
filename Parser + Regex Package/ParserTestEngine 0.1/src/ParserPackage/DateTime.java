package ParserPackage;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;
import java.util.GregorianCalendar;
public class DateTime {
	private GregorianCalendar calendar;
	private long timeMilli;
	private static final SimpleDateFormat ISO_DATE_TIME = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static final SimpleDateFormat DAY_MONTH_YEAR = new SimpleDateFormat(
			"dd-MM-yyyy");
	private static final SimpleDateFormat DAY_MONTH_YEAR_HOUR_MIN = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm");
	private boolean hasTime;
	static 
	{
		ISO_DATE_TIME.setLenient(false);
		ISO_DATE_TIME.setLenient(false);
	}

/** default constructor
 * 
 */
public DateTime()
{
	calendar=new GregorianCalendar(2000,0,1,0,0);
	calendar.setLenient(true);
	timeMilli=calendar.getTimeInMillis();
	hasTime=false;
}
public DateTime(long timeInMillis)
{
	calendar = new GregorianCalendar(2000,0,1,0,0);
	calendar.setTimeInMillis(timeInMillis);
	calendar.setLenient(true);
	timeMilli=calendar.getTimeInMillis();
}
public DateTime(int year,int month,int day)
{
	calendar=new GregorianCalendar(year,month-1,day);
	calendar.setLenient(false);
	timeMilli=calendar.getTimeInMillis();
	hasTime=false;
}
public DateTime(int year,int month,int day,int hours,int minutes)
{
	calendar=new GregorianCalendar(year,month,day,hours,minutes);
	calendar.setLenient(false);
	timeMilli=calendar.getTimeInMillis();
	hasTime=true;
}
public DateTime(int year,int month,int day,int hours,int minutes,int seconds)
{
	calendar=new GregorianCalendar(year,month,day,hours,minutes,seconds);
	calendar.setLenient(false);
	timeMilli=calendar.getTimeInMillis();
	hasTime=true;
}
public static DateTime getCurrentDateTime()
{
	GregorianCalendar current=new GregorianCalendar();
	current.setLenient(false);
	current.getTimeInMillis();
	DateTime currDateTime=new DateTime(current.get(GregorianCalendar.YEAR),(current.get(GregorianCalendar.MONTH))+1,current.get(GregorianCalendar.DAY_OF_MONTH),current.get(GregorianCalendar.HOUR_OF_DAY),current.get(GregorianCalendar.MINUTE),current.get(GregorianCalendar.SECOND));
	return currDateTime;
}
public static DateTime getCurrentDate()
{
	GregorianCalendar current=new GregorianCalendar();
	current.setLenient(false);
	current.getTimeInMillis();
	DateTime currDate=new DateTime(current.get(GregorianCalendar.YEAR),current.get(GregorianCalendar.MONTH)+1,current.get(GregorianCalendar.DAY_OF_MONTH));
	return currDate;
}
public void set(int year,int month,int day)
{
	calendar.set(year, month-1,year);
	timeMilli=calendar.getTimeInMillis();
}
public void set(int year,int month,int day,int hours,int minutes,int seconds)
{
	calendar.set(year, month-1,day,hours,minutes,seconds);
	timeMilli=calendar.getTimeInMillis();
	hasTime=true;
}
public void set(int year,int month,int day,int hours,int minutes)
{
	calendar.set(year,month-1,day,hours,minutes);
	timeMilli=calendar.getTimeInMillis();
	hasTime=true;
}
public boolean isEqual(Object ob)
{
	if(!(ob instanceof DateTime))
		return false;
	if(this==ob)
		return true;
	DateTime obj=(DateTime) ob;
		return (this.getTimeMilli()==obj.getTimeMilli()) && (this.getHasTime()==obj.getHasTime());
}
public boolean getHasTime()
{
	return hasTime;
}
public void setHasTime(boolean value)
{
	hasTime=value;
}
public long getTimeMilli()
{
	return timeMilli;
}
public void setTimeMilli(long time)
{
	this.timeMilli=time;
	calendar.setTimeInMillis(time);
}
public void setTime(long Time)
{
	this.timeMilli=Time;
	calendar.setTimeInMillis(Time);
}
public int compareTo(DateTime second)
{
	long diff=this.getTimeMilli()-second.getTimeMilli();
	if(diff<0)
		return -1;
	else if(diff==0)
		return 0;
	else
		return 1;
}
/** COMPONENT		VALUE
 * 		0			 ERA
 * 		1			YEAR
 * 		2			MONTH
 * 		3			DAY
 * 			 		
 * @param component
 * @return
 */
public int get(int component)
{
	int value=calendar.get(component);
	if(component == Calendar.MONTH){
		value+=1;
	}
	return value;
}
public String toString()
{
	return Long.toString(getTimeMilli());
}
public String formattedToString()
{
	if(isDefaultTime())
		return "";
	else if(getHasTime())
		return DAY_MONTH_YEAR_HOUR_MIN.format(calendar.getTimeInMillis());
	else 
		return DAY_MONTH_YEAR.format(calendar.getTimeInMillis());
}
public String generateDateCode()
{
	return DAY_MONTH_YEAR.format(this.calendar.getTimeInMillis());
}
public String generateTimeCode()
{
	if(hasTime)
		return String.format("%02d%02d%02d", this.calendar.get(GregorianCalendar.HOUR_OF_DAY),this.calendar.get(GregorianCalendar.MINUTE),this.calendar.get(GregorianCalendar.SECOND));
	else
		return "";
}
public boolean isDefaultTime()
{
	return this.isEqual(new DateTime()) && !hasTime;
}
public DateTime getDate()
{
	return new DateTime(this.get(GregorianCalendar.YEAR),this.get(GregorianCalendar.MONTH),this.get(GregorianCalendar.DAY_OF_MONTH));
}
public DateTime getTime() 
{
	DateTime newEventTime = new DateTime();
	newEventTime.set(Calendar.HOUR_OF_DAY, this.get(Calendar.HOUR_OF_DAY));
	newEventTime.set(Calendar.MINUTE, this.get(Calendar.MINUTE));
	newEventTime.getTimeMilli();
	newEventTime.setHasTime(hasTime);
	return newEventTime;
}
public void set(int Component,int value)
{
	if(Component==GregorianCalendar.MONTH)
	{
		value-=1;
	}
	calendar.set(Component,value);
	if(Component == GregorianCalendar.HOUR_OF_DAY || Component == GregorianCalendar.HOUR|| Component == GregorianCalendar.MINUTE)
	{
		hasTime=true;
	}
	timeMilli = calendar.getTimeInMillis();
}
}
