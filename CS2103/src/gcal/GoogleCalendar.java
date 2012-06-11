package gcal;
import storagecontroller.StorageManager;
import org.apache.log4j.Logger;
/*import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;*/
import com.google.gdata.client.calendar.CalendarService;
//import com.google.gdata.data.Content;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
//import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import data.Task;
import data.TaskDateTime;
//import parser.Parser;
public class GoogleCalendar
{
	private Logger logger = Logger.getLogger(GoogleCalendar.class.getName());
	private static final String USER_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/private/full";
	private static final String APPLICATION_NAME="JotItDown";
	private CalendarService calenService=new CalendarService(APPLICATION_NAME);
	public static final String APPLICATION_NAME_ID_PROPERTY = String.format(":%s:%s:",APPLICATION_NAME, "Id");
	private String username;
	private String password;
	private static boolean loggedIn;
	private static URL userCalendarUrl;
	//Parser testParser=new Parser();
	/*private Task task1=testParser.parseForAdd("Go to Airport on 10 june 2012 at 4 pm");
	private Task task2=testParser.parseForAdd("Go for a movie on 8 june 2012 at 9 pm");
	private Task task3=testParser.parseForAdd("Go buy shoes at Vivo on 9 june 2012 at 10 am");*/
		
	public boolean login(String userName,String passWord)
	{
		this.username=userName;
		this.password=passWord;
		try
		{
			calenService.setUserCredentials(username,password);
			userCalendarUrl=new URL(USER_CALENDAR_URL);
			loggedIn=true;
		}
		catch(AuthenticationException e){
			loggedIn=false;
		}
		catch(MalformedURLException e){
			loggedIn=false;
		}
		return loggedIn;
		
	}
	public boolean logout(){
		calenService=new CalendarService(APPLICATION_NAME);
		this.username=null;
		this.username=null;
		loggedIn=false;
		return true;
	}
	public List<CalendarEventEntry> getAllEntries(){
		CalendarEventFeed resultFeed=null;
		if(!loggedIn){
			return null;
		}
		try
		{
			resultFeed=calenService.getFeed(userCalendarUrl, CalendarEventFeed.class);
		}
		catch(IOException e)
		{
			System.out.println("IO Exception");
		}
		catch(ServiceException e)
		{
			System.out.println("Service Exception");
		}
			List<CalendarEventEntry> entries=resultFeed.getEntries();
			return entries;
	}
	public Task[] calendarEventListToTaskArray(List<CalendarEventEntry> userEntries){
		ArrayList<Task> list= new ArrayList<Task>();
		for(CalendarEventEntry entry : userEntries ){
			list.add(calendarEntryToTask(entry));
		}
			return list.toArray(new Task[list.size()]);
	}
	public Task calendarEntryToTask(CalendarEventEntry entry){
		Task googleCalTask=new Task();
		ArrayList<String> labelList=new ArrayList<String>();
		googleCalTask.setName(entry.getTitle().getPlainText());
		String labels=entry.getContent().getLang();
		if(labels!=null)
		{
		String[] arr = labels.split("\\s+");
		for(int i=0;i<arr.length;i++)
		{
			labelList.add(arr[i]);
		}
		googleCalTask.setLabels(labelList);
		}
		String desc=entry.getId().replaceAll("http://www.google.com/calendar/feeds/default/events/", "");
		googleCalTask.setDescription(desc);
		List<When> eventTime=entry.getTimes();
		googleCalTask.setStart(getTaskDateTime(eventTime.get(0).getStartTime()));
		googleCalTask.getStart().setHasDate(true);
		googleCalTask.getStart().setHasTime(!eventTime.get(0).getStartTime().isDateOnly());
		logger.debug(getTaskDateTime(eventTime.get(0).getStartTime()).toString());
		logger.debug(getTaskDateTime(eventTime.get(0).getEndTime()).toString());
		logger.debug(googleCalTask.getName());
		if(getTaskDateTime(eventTime.get(0).getStartTime()).toString().equals(getTaskDateTime(eventTime.get(0).getEndTime()).toString())){
			logger.debug("Start end date time is same");
			googleCalTask.setEnd(null);
		}
		else{
		googleCalTask.setEnd(getTaskDateTime(eventTime.get(0).getEndTime()));
		googleCalTask.getEnd().setHasDate(true);
		googleCalTask.getEnd().setHasTime(!eventTime.get(0).getEndTime().isDateOnly());
		}
		//googleCalTask.setLabels(labelList);
		return googleCalTask;
	}
	
	private data.TaskDateTime getTaskDateTime(DateTime dateTime) {
		return TaskDateTime.xmlToEventTime(dateTime.toString());
	}
	
	public CalendarEventEntry addTask(Task task,int reminderMins,Method reminderMethod)
	{
		logger.debug("In addTask");
		String labels=new String();
		CalendarEventEntry newEntry = new CalendarEventEntry();
		newEntry.setTitle(new PlainTextConstruct(task.getName()));
		if(task.getLabels()!=null)
		{
		for(int i=0;i<task.getLabels().size();i++)
		{
			labels += task.getLabels().get(i) + " ";
		}
		newEntry.setContent(new PlainTextConstruct(labels));
		}
		DateTime start=getDateTime(task.getStart());
		DateTime end=getDateTime(task.getEnd());
		if(start==null)
			return new CalendarEventEntry();
		When eventTime=new When();
			eventTime.setStartTime(start);
		if(end!=null)
		{
			eventTime.setEndTime(end);
		}
		newEntry.addTime(eventTime);
		try
		{
			CalendarEventEntry entry=calenService.insert(userCalendarUrl,newEntry);
			if(entry==null)
				return null;
			task.setDescription(entry.getId().replaceAll("http://www.google.com/calendar/feeds/default/events/", ""));
			System.out.println(task.getDescription());
			Method type=reminderMethod;
			Reminder reminder=new Reminder();
			reminder.setMinutes(reminderMins);
			reminder.setMethod(type);
			entry.getReminder().add(reminder);
			entry.update();
			return entry;
		}
		catch(IOException e1){
			return null;
		}
		catch(ServiceException e2){
			return null;
		}
		
	}
	public boolean deleteEvent(Task taskToBeDeleted)
	{
		List<CalendarEventEntry> entriesList=getAllEntries();
		for(CalendarEventEntry entry : entriesList){
			Task gTask=calendarEntryToTask(entry);
			logger.debug(gTask.isEqual((taskToBeDeleted)));
			if(gTask.getName().equalsIgnoreCase(taskToBeDeleted.getName())
					&& gTask.getStart().isEqual(taskToBeDeleted.getStart())){
				try
				{
				logger.debug("Task=Event");
				entry.delete();
				return true;
				}
				catch(IOException e)
				{
					logger.debug("IOException");
					return false;
				}
				catch(ServiceException e)
				{
					logger.debug("Service Exception");
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean updateEvent(Task oldTask,Task newTask)
	{
		/*List<CalendarEventEntry> entriesList=getAllEntries();
		for(CalendarEventEntry entry : entriesList){
			Task gTask=calendarEntryToTask(entry);
			logger.debug(gTask.isEqual((oldTask)));
			if(gTask.getName().equalsIgnoreCase(oldTask.getName())
					&& gTask.getStart().isEqual((oldTask.getStart()))){*/
				deleteEvent(oldTask);
				addTask(newTask,0,Method.NONE);
				return true;
	
	}
	public DateTime getDateTime(TaskDateTime taskDateTime){
		if(taskDateTime==null)
			return null;
		else if(taskDateTime.getHasTime())
		{
			return DateTime.parseDateTime(taskDateTime.dateTimeToXml());
		}
		else
			return DateTime.parseDate(taskDateTime.dateToXml());
	}
	public boolean sync()
	{
		boolean isPresent = false;
		boolean isPresent2=false;
		Task[] taskArray=calendarEventListToTaskArray(getAllEntries());
		System.out.println(taskArray.length);
		
		for(int i=0;i<taskArray.length;i++)
		{
			isPresent=false;
			logger.debug("isPresent resetted to false");
			if(StorageManager.getAllTasks().length==0)
			{
				logger.debug("Length 0" + taskArray[0].getDescription());
				StorageManager.addTask(taskArray[0]);
				System.out.println(taskArray[0].getTaskId());
			}
			else
			{
				for(int j=0;j<StorageManager.getAllTasks().length;j++){
					logger.debug(taskArray[i].getDescription() + " " + StorageManager.getAllTasks()[j].getDescription());
					if(taskArray[i].getDescription().equals(StorageManager.getAllTasks()[j].getDescription()))
					{
						logger.debug(" Is present");
						isPresent=true;
						break;
					}
				}
				if(!isPresent){System.out.println("In sync");
					StorageManager.addTask(taskArray[i]);System.out.println(taskArray[i].getTaskId());}
		}
		}
		logger.debug("In export");
		for(int i=0;i<StorageManager.getAllTasks().length;i++)
		{
			isPresent2=false;
			logger.debug("Again!!!!!!!!!");
			if(taskArray.length==0)
			{
				logger.debug("Length of entries is 0");
				addTask(StorageManager.getAllTasks()[0],0,Method.NONE);
			}
			else
			{
				for(int j=0;j<taskArray.length;j++)
				{
					logger.debug(StorageManager.getAllTasks()[i].getDescription() + " " + taskArray[j].getDescription());
					logger.debug(StorageManager.getAllTasks()[i].toString());
					if(StorageManager.getAllTasks()[i].getDescription().equals(taskArray[j].getDescription()))
					{
						isPresent2=true;
						logger.debug("the desc id of google cal task is same as live storage" );
						if(!(StorageManager.getAllTasks()[i].isEqual(taskArray[j])))
						{
							logger.debug(StorageManager.getAllTasks()[i].toString());
							logger.debug(taskArray[j].toString());
							logger.debug("The two tasks with same description are not identical");
							updateEvent(taskArray[j],StorageManager.getAllTasks()[i]);
						}
						break;
					}
				}
					if(!isPresent2){
						addTask(StorageManager.getAllTasks()[i], 0, Method.NONE);
					}
			}
		}
		return true;
	}
	public boolean isLoggedIn(){
		return loggedIn;
	}
}

