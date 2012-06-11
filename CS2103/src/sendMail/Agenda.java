package sendMail;
import java.text.SimpleDateFormat;
import data.TaskDateTime;



import org.tiling.scheduling.Scheduler;
import org.tiling.scheduling.SchedulerTask;
import org.tiling.scheduling.iterators.DailyIterator;
import java.io.*;
public class Agenda {
private final Scheduler scheduler=new Scheduler();
private final int hourOfDay,minute,second;
private BufferedWriter writer;
private BufferedReader reader;
public Agenda(int hour,int min,int sec)
{
	hourOfDay=hour;
	minute=min;
	second=sec;
}
private TaskDateTime getDate(String result)
{
	System.out.println("The string is:" + result);
	int day=Integer.parseInt(result.substring(0, 2));
	String mon=result.substring(3,6);
	int month;
	int year=Integer.parseInt(result.substring(7,11));
	if(mon.matches("Jan"))
		month=1;
	else if(mon.matches("Feb"))
		month=2;
	else if(mon.matches("Mar"))
		month=3;
	else if(mon.matches("Apr"))
		month=4;
	else if(mon.matches("May"))
		month=5;
	else if(mon.matches("Jun"))
		month=6;
	else if(mon.matches("Jul"))
		month=7;
	else if(mon.matches("Aug"))
		month=8;
	else if(mon.matches("Sep"))
		month=9;
	else if(mon.matches("Oct"))
		month=10;
	else if(mon.matches("Nov"))
		month=11;
	else
		month=12;
	TaskDateTime date=new TaskDateTime(year,month,day);
	return date;
}
public void sendMail()
{
try
{
	System.out.println("In sendMail");
	reader=new BufferedReader(new FileReader("JotItDownAgenda.txt"));
	String buffer,result="";
	while ((buffer = reader.readLine()) != null)
	{
		System.out.println(buffer);
		result+=buffer;
	}
	reader.close();
	TaskDateTime sentDate = getDate(result);
	System.out.println(" The date extracted  is " + sentDate.formattedToString());
	writer=new BufferedWriter(new FileWriter("JotItDownAgenda.txt"));
	TaskDateTime default2=new TaskDateTime(2002,1,1);
	if(sentDate.compareTo(new TaskDateTime().getDate())==0)
	{
		System.out.println("Default");
		System.out.println(default2.getDate().formattedToString());
		writer.write(default2.getDate().formattedToString());
		writer.close();
		start();
	}
	else
	{
	if(sentDate.compareTo(TaskDateTime.getCurrentDate())==-1)
	{
		writer.write(TaskDateTime.getCurrentDate().formattedToString());
		writer.close();
		Send send=new Send();
		System.out.println(send.sendMail("niravgandhi93@gmail.com","Agenda","Do homework today at 6 pm\n"));
	}
	else if(sentDate.compareTo(TaskDateTime.getCurrentDate())==0)
	{
		writer.write(TaskDateTime.getCurrentDate().formattedToString());
		writer.close();
	}
	}
}
	catch(FileNotFoundException e)
	{

	} 
	catch (IOException e) {
		System.out.println("IO Exception");
	}
}
public void start()
{
	scheduler.schedule(new SchedulerTask()
		{
			public void run()
			{
				sendMail();
			}
				}
				, new DailyIterator(hourOfDay,minute,second));
		}
			
	
public static void main(String[] args){
	Agenda alarmClock=new Agenda(20,43,0);
	alarmClock.sendMail();
}

}

