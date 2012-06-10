package data;
//import java.text.ParseException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import java.util.Date;
import java.util.GregorianCalendar;


import org.apache.log4j.Logger;
public class TaskDateTime {
	private GregorianCalendar calendar;
	private long timeMilli;
	private static final SimpleDateFormat ISO_DATE_TIME = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");
	private static final SimpleDateFormat ISO_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat DAY_MONTH_YEAR = new SimpleDateFormat(
			"dd MMM yyyy");
	private static final SimpleDateFormat DAY_MONTH_YEAR_CODEFORMAT= new SimpleDateFormat(
			"dd-MM-yyyy");
	private static final SimpleDateFormat DAY_MONTH_YEAR_HOUR_MIN = new SimpleDateFormat(
			"K:mm a dd MMM yyyy");
	private static final SimpleDateFormat DAY_MONTH_YEAR_PRESENTABLE  =new SimpleDateFormat(
			"dd MMM yyyy K:mm a");
	private static final SimpleDateFormat HOUR_MIN=new SimpleDateFormat("K:mm a");
	private boolean hasTime;
	private boolean hasDate;
	static 
	{
		ISO_DATE_TIME.setLenient(false);
		ISO_DATE_TIME.setLenient(false);
	}
	private static Logger logger=Logger.getLogger(TaskDateTime.class);

/** default constructor
 * 
 */
public TaskDateTime()
{
	calendar = new GregorianCalendar( 2000, 0, 1, 23, 59,30);
	calendar.setLenient(true);
	timeMilli = calendar.getTimeInMillis();
	hasTime = false;
	hasDate = false;
}
/** DateTime constructor 1 
 * 
 */
public TaskDateTime(long timeInMillis)
{
	calendar = new GregorianCalendar( 2000, 0, 1, 23, 59,30);
	calendar.setTimeInMillis(timeInMillis);
	calendar.setLenient(true);
	timeMilli=calendar.getTimeInMillis();
	hasDate = true;
	hasTime=true;
}
/** constructor which only sets the date
 * 
 */
public TaskDateTime( int year, int month, int day)
{
	calendar = new GregorianCalendar(year, month-1, day, 23, 59, 30);
	calendar.setLenient(true);
	timeMilli=calendar.getTimeInMillis();
	hasTime = false;
	hasDate = true;
}
/** constructor which only sets the time
 * 
 */
public TaskDateTime(int hour,int minutes)
{
	calendar = new GregorianCalendar(2000, 0, 1, hour, minutes);
	calendar.setLenient(false);
	timeMilli=calendar.getTimeInMillis();
	hasTime = true;
	hasDate=false;
}
/** constructor which sets both date and time 
 * 
 */
public TaskDateTime(int year, int month, int day, int hours, int minutes)
{
	calendar = new GregorianCalendar(year, month-1, day, hours, minutes);
	calendar.setLenient(false);
	timeMilli=calendar.getTimeInMillis();
	hasTime = true;
	hasDate = true;
}
/** constructor which sets the data and time with seconds
 * 
 */
public TaskDateTime(int year, int month, int day, int hours, int minutes, int seconds)
{
	calendar = new GregorianCalendar(year, month-1, day, hours, minutes, seconds);
	calendar.setLenient(false);
	timeMilli = calendar.getTimeInMillis();
	hasTime = true;
	hasDate = true;
}
/**static function to get the current Date and time
 * 
 * @return the DateTime object having the current date and time details
 */
public static TaskDateTime getCurrentDateTime()
{
	GregorianCalendar current = new GregorianCalendar();
	current.setLenient(false);
	current.getTimeInMillis();
	TaskDateTime currDateTime = new TaskDateTime(current.get(GregorianCalendar.YEAR), (current.get(GregorianCalendar.MONTH))+1, current.get(GregorianCalendar.DAY_OF_MONTH), 
			current.get(GregorianCalendar.HOUR_OF_DAY), current.get(GregorianCalendar.MINUTE), current.get(GregorianCalendar.SECOND));
	return currDateTime;
}
/** static function to get the current Date
 * 
 * @return the DateTime object having the current date details
 */
public static TaskDateTime getCurrentDate()
{
	GregorianCalendar current = new GregorianCalendar();
	current.setLenient(false);
	current.getTimeInMillis();
	TaskDateTime currDate = new TaskDateTime(current.get(GregorianCalendar.YEAR), current.get(GregorianCalendar.MONTH)+1, current.get(GregorianCalendar.DAY_OF_MONTH));
	return currDate;
}
/**  setter for the DateTime class
 * 
 * @param year
 * @param month
 * @param day
 */
public void set(int year,int month,int day)
{
	calendar.set(year, month-1, day);
	calendar.setLenient(true);
	timeMilli = calendar.getTimeInMillis();
	hasDate=true;
}
/** setter for the Date Time class
 * 
 * @param year
 * @param month
 * @param day
 * @param hours
 * @param minutes
 * @param seconds
 */
public void set(int year, int month, int day, int hours, int minutes, int seconds)
{
	calendar.set(year, month-1, day, hours, minutes, seconds);
	timeMilli=calendar.getTimeInMillis();
	hasTime = true;
	hasDate=true;
}
/**setter for the DateTime class
 * 
 * @param year
 * @param month
 * @param day
 * @param hours
 * @param minutes
 */
public void set(int year, int month, int day, int hours, int minutes)
{
	calendar.set(year, month-1, day, hours, minutes);
	timeMilli = calendar.getTimeInMillis();
	hasTime = true;
	hasDate=true;
}
/** function to see if two objects are the same
 * 
 * @param ob object being compared to
 * @return true if the objects are the same, otherwise false
 */
public boolean isEqual(Object ob)
{
	if(! (ob instanceof TaskDateTime) )
		return false;
	if(this == ob)
		return true;
	TaskDateTime obj = (TaskDateTime) ob;
		return (this.getTimeMilli()==obj.getTimeMilli()) && (this.getHasTime()==obj.getHasTime());
}
/** function to see if the DateTime object has a time 
 * 
 * @return true if the object has time, otherwise false
 */
public boolean getHasTime()
{
	return hasTime;
}
/** setter to set the hasTime attribute to value
 * 
 * @param value 
 */
public void setHasTime(boolean value)
{
	hasTime=value;
}
/** to get the time in milliseconds
 * 
 * @return the time in milliseconds
 */
public long getTimeMilli()
{
	return timeMilli;
}
/** sets the time in milliseconds
 * 
 * @param time the time in milliseconds to be set to
 */
public void setTimeMilli(long time)
{
	this.timeMilli = time;
	calendar.setTimeInMillis(time);
}
/** compares one DateTime object to another
 * 
 * @param second the other DateTime object
 * @return -1 if the second object is greater than the first, 0 if both are equal, otherwise returns -1
 */
public int compareTo(TaskDateTime second)
{
	long diff = this.getTimeMilli() - second.getTimeMilli();
	if(diff < 0)
		return -1;
	else if(diff == 0)
		return 0;
	else
		return 1;
}
/** COMPONENT		MEANING
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
	int value = calendar.get(component);
	if(component == Calendar.MONTH){
		value += 1;
	}
	return value;
}
public String toString()
{
	return Long.toString(getTimeMilli());
}
/**
 * 
 * @return the formatted string
 */
public String formattedToString()
{
	if(isDefaultTime())
		return "";
	else if(getHasTime() && getHasDate())
		return DAY_MONTH_YEAR_HOUR_MIN.format(calendar.getTimeInMillis());
	else if(! (getHasDate()) && getHasTime() )
		return HOUR_MIN.format(calendar.getTimeInMillis());
	else 
		return DAY_MONTH_YEAR.format(calendar.getTimeInMillis());
}
/** 
 * 
 * @return the presentable string
 */
public String presentableToString()
{
	return DAY_MONTH_YEAR_PRESENTABLE.format(this.calendar.getTimeInMillis());
}
/**
 * 
 * @return the generated dateCode for the HashMap
 */
public String generateDateCode()
{
	try
	{
		return DAY_MONTH_YEAR_CODEFORMAT.format(this.calendar.getTimeInMillis());
	}
	catch(NullPointerException e)
	{
		logger.debug("In generateDateCode");
	}
	return null;
}
/**
 * 
 * @return the generated timeCode for the HashMap
 */
public String generateTimeCode()
{
	if(hasTime)
		return String.format("%02d%02d%02d", this.calendar.get(GregorianCalendar.HOUR_OF_DAY), this.calendar.get(GregorianCalendar.MINUTE), 
				this.calendar.get(GregorianCalendar.SECOND));
	else
		return String.format("%02d%02d%02d", this.calendar.get(GregorianCalendar.HOUR_OF_DAY),this.calendar.get(GregorianCalendar.MINUTE),
				this.calendar.get(GregorianCalendar.SECOND));
}
/** checks if the object is of Default type
 * 
 * @return true if the object is of the Default type
 */
public boolean isDefaultTime()
{
	return this.isEqual(new TaskDateTime()) && !hasTime;
}
/**
 * 
 * @return the Date of the object in the form of a DateTime object
 */
public TaskDateTime getDate()
{
	return new TaskDateTime(this.get(GregorianCalendar.YEAR), this.get(GregorianCalendar.MONTH), this.get(GregorianCalendar.DAY_OF_MONTH));
}
/**
 * 
 * @return the Time of the object in the form of a DateTime object
 */
public TaskDateTime getTime() 
{
	TaskDateTime newEventTime = new TaskDateTime();
	newEventTime.set(Calendar.HOUR_OF_DAY, this.get(Calendar.HOUR_OF_DAY));
	newEventTime.set(Calendar.MINUTE, this.get(Calendar.MINUTE));
	newEventTime.getTimeMilli();
	newEventTime.setHasTime(hasTime);
	return newEventTime;
}
/**sets the Date or time
 * 
 * @param Component the component number in the Gregorian Calendar
 * @param value the value the component 
 */
public void set(int Component,int value)
{
	if(Component == GregorianCalendar.MONTH)
	{
		value -= 1;
	}
	calendar.set(Component,value);
	if(Component == GregorianCalendar.HOUR_OF_DAY || Component == GregorianCalendar.HOUR|| Component == GregorianCalendar.MINUTE)
	{
		hasTime = true;
	}
	timeMilli = calendar.getTimeInMillis();
}
/** sets the hasDate attribute
 * 
 * @param value the value the hasDate atribute is set to.
 */
public void setHasDate(boolean value)
{
	hasDate = value;
}
/**
 * 
 * @return the hasDate attribute
 */
public boolean getHasDate()
{
	return hasDate;
}

public static TaskDateTime xmlToEventTime(String xmlString) {
	Date date;
	date=xmlToDate(xmlString);
	if(date == null){
		return null;
	}
	return new TaskDateTime(date.getTime());
}
private static Date xmlToDate(String xmlString) {
	try
	{
		return ISO_DATE_TIME.parse(xmlString);
	}
	catch(ParseException e1)
	{
		try
		{
			return ISO_DATE.parse(xmlString);
		}
		catch(ParseException e2)
		{
			return null;
		}
	}
}
public String dateToXml()
{
	return ISO_DATE.format(new Date(timeMilli));
}
public String dateTimeToXml()
{
	return ISO_DATE_TIME.format(new Date(timeMilli));
}
}