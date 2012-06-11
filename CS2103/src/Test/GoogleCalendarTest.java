package Test;
import data.Task;
import data.TaskDateTime;
import gcal.GoogleCalendar;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;
import storagecontroller.StorageManager;

import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.Reminder.Method;

public class GoogleCalendarTest {
	GoogleCalendar test=new GoogleCalendar();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLogin() {
		/*test.login("ngandhi1993@gmail.com", "ndms1993");
		Task[] taskArrayTest=test.calendarEventListToTaskArray(test.getAllEntries());
		for(int i=0;i<taskArrayTest.length;i++)
		{
			System.out.println(taskArrayTest[i].getDescription());
		}*/
		/*assertEquals(false,test.login("ngandhi1993@gmail.com", "bbbbbbbbbb"));
		test.logout();
		assertEquals(false,test.login("niravgandhi93@gmail.com","cccccccccc" ));
		test.logout();
		assertEquals(false,test.login("ngandhi1993@gmail.com", "aaaaaaaaaa"));*/
	}
	@Test
	public final void testGetAllEntries() {
		/*test.login("ngandhi1993@gmail.com", "ndms1993");
		List<CalendarEventEntry> testList=test.getAllEntries();
		assertEquals(2,testList.size());
		test.logout();*/
	}

	@Test
	public final void testCalendarEventListToTaskArray() {
		/*test.login("ngandhi1993@gmail.com","ndms1993");
		List<CalendarEventEntry> testList=test.getAllEntries();
		System.out.println(test.calendarEventListToTaskArray(testList)[0].toString());*/
	}

	@Test
	public final void testAddTask() {
		/*test.login("ngandhi1993@gmail.com", "ndms1993");
		TaskDateTime start1=new TaskDateTime(2012,6,10,21,0,0);
		TaskDateTime end1=new TaskDateTime(2012,6,10,23,0,0);
		Task newTask=new Task("Go to office","",start1,end1,null,"once");
		//test2=parser.parse("Go to the utown office at 4 pm on 7 June 2012");
		test.addTask(newTask, 0, Method.NONE);
		System.out.println(newTask.getDescription());*/
		
		//System.out.println(test2.toString());
		//test.addTask(test1, 0, Method.NONE);
	}

	@Test
	public final void testDeleteEvent() {
		/*test.login("ngandhi1993@gmail.com", "ndms1993");
		TaskDateTime start1=new TaskDateTime(2012,6,10,21,0,0);
		TaskDateTime end1=new TaskDateTime(2012,6,10,23,0,0);
		test2=new Task("Go to airport",null,start1,end1,null,"");
		test.deleteEvent(test2);*/
	}

	@Test
	public final void testUpdateEvent() {
		/*test.login("ngandhi1993@gmail.com", "ndms1993");
		TaskDateTime start1=new TaskDateTime(2012,6,10,21,0,0);
		TaskDateTime end1=new TaskDateTime(2012,6,10,23,0,0);
		test2=new Task("Go to airport",null,start1,end1,null,"");
		test1=new Task("Go to airport terminal 2",null,start1,end1,null,"");
		test.updateEvent(test2, test1);*/
	}
	
	@Test
	public final void sync()
	{
		test.login("ngandhi1993@gmail.com", "ndms1993");
		TaskDateTime start1=new TaskDateTime(2012,6,10,21,0,0);
		TaskDateTime end1=new TaskDateTime(2012,6,10,23,0,0);
		Task oldTask=new Task("Go to office","",start1,end1,null,"once");
		Task newTask=new Task("Go to office in UTOWN","",start1,end1,null,"once");
		test.addTask(oldTask, 0, Method.NONE);
		
		test.sync();
		for(int i=0;i<StorageManager.getAllTasks().length;i++)
		{
			if(oldTask.getName().equalsIgnoreCase(StorageManager.getAllTasks()[i].getName()))
				oldTask.setTaskId(StorageManager.getAllTasks()[i].getTaskId());
		}
		System.out.println(StorageManager.replaceTask(oldTask,newTask));
		System.out.println(StorageManager.getAllTasks().length);
		test.sync();
	}

}
