package Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.TaskDateTime;
import data.Task;
import storagecontroller.StorageManager;

public class TaskTest {
	TaskDateTime temp2=new TaskDateTime();
	TaskDateTime start1=new TaskDateTime(2012,7,31,16,0,0);
	TaskDateTime end1=new TaskDateTime(2012,7,1,18,0,0);
	Task one=new Task("NUS Parade","with DEGEA",start1,null,null,"weekly");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetTaskId() {
		StorageManager managerTest =new StorageManager();
		managerTest.addTask(one);
		assertEquals("",one.getTaskId());
	}

	@Test
	public final void testGetStartDateTime() {
		assertEquals("31-05-2012",one.getStart().formattedToString());
	}

	@Test
	public final void testSetStartDateTime() {
		TaskDateTime newDateTime=new TaskDateTime();
		one.setStart(newDateTime);
		assertEquals("",one.getStart().formattedToString());
	}

	@Test
	public final void testGetEndDateTime() {
		assertEquals("01-06-2012",one.getEnd().formattedToString());
	}

	@Test
	public final void testSetEndDateTime() {
		TaskDateTime newDateTime=new TaskDateTime();
		one.setEnd(newDateTime);
		assertEquals("",one.getEnd().formattedToString());
	}

	@Test
	public final void testIsEqual() {
		TaskDateTime start2=new TaskDateTime(2012,7,31,16,0,0);
		TaskDateTime end2=new TaskDateTime(2012,7,1,18,0,0);
		Task two=new Task("NUS Parade","with DEGEA",start2,end2,null,"weekly");
		System.out.println(one.isEqual(two));
	}

	@Test
	public final void testIsIdenticalTo() {
		TaskDateTime start2=new TaskDateTime(2012,5,31);
		TaskDateTime end2=new TaskDateTime(2012,6,1);
		Task two=new Task("go to temple","with parents",start2,end2,null,"weekly");
		assertEquals(true,one.isIdenticalTo(two));
	}

	@Test
	public final void testToString() {
		TaskDateTime start2=new TaskDateTime(2012,7,4,13,0,0);
		TaskDateTime end2=new TaskDateTime(2012,7,4,14,0,0);
		Task two=new Task("Go to school",null,start2,end2,null,"daily");
		System.out.println(one.toString());
		System.out.println(two.toString());
	}
	
	@Test
	public final void testToString2(){
		TaskDateTime start2=new TaskDateTime(2012,10,11,18,0,0);
		TaskDateTime end2=new TaskDateTime(2012,7,4,14,0,0);
		Task two=new Task("Go to school",null,null,end2,null,"daily");
		System.out.println(one.toString2());
		System.out.println(two.toString2());
		
	}

}
