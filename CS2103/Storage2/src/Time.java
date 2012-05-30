public class Time {
private int hour;
private int minute;
private int second;
private boolean Correct;
/** Constructor */
public Time(int theHour,int theMinute,int theSecond)
{
	setHour(theHour);
	setMinute(theMinute);
	setSecond(theSecond);
	Correct=((hour==-1 ||minute==-1 || second==-1)?false:true);
}
/** return the hour in int format
 * 
 * @return the attribute hour
 */
public int getHour()
{
	return hour;
}
/** return the minute in int format
 * 
 * @return the attribute minute
 */
public int getMinute()
{
	return minute;
}
/** return the second in int format
 * 
 * @return the attribute second
 */
public int getSecond()
{
	return second;
}
/** sets the hour attribute of Time
 * 
 * @param h the hour you want to set
 */
public void setHour(int h)
{
	hour=((h>=0 && h<24)?h:-1);// -1 to indicate that hour is not within the required boundary. 
}
/** sets the minute attribute of Time
 * 
 * @param m the minute you want to set
 */
public void setMinute(int m)
{
	minute=((m>=0 && m<60)?m:-1);// -1 to indicate that minute is not within the required boundary.
}
/** sets the second attribute of Time
 * 
 * @param s the second you want to set
 */
public void setSecond(int s)// -1 to indcate that minute is not within the required boundary.
{
	second=((s>=0 && s<60)?s:-1);
}
/** checks if the format of the Time given is correct
 * 
 * @return true if the format of the Time is Correct, otherwise false
 */
public boolean isCorrect()
{
	return Correct;
}
/** outputs the time in the fomat: hh:mm:ss
 * 
 * @ return the string containing the date
 */
public String toString()
{
	return String.format("%d:%02d:%02d", hour,minute,second);
}
/**compares two time argument
 * 
 * @param time1 1st time argument
 * @param time2 2nd time argument
 * @return true if time1 > time2, otherwise false
 */
public boolean compareTime(Time time1,Time time2)
{
	if(time1.hour>time2.hour)
		return true;
	else if(time1.hour<time2.hour)
		return false;
	else
	{
		if(time1.minute>time2.minute)
			return true;
		else if(time1.minute<time2.minute)
			return false;
		else
		{
			if(time1.second>time2.second)
				return true;
			else
				return false;
		}
	}
}
}
