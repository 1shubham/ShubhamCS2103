package Test;

import static org.junit.Assert.*;
import data.TaskDateTime;
import data.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskTesting {
	TaskDateTime temp2=new TaskDateTime();
	TaskDateTime start1=new TaskDateTime(2012,5,31,15,0,0);
	TaskDateTime end1=new TaskDateTime(2012,5,31,16,0,0);
	Task one=new Task("go to temple","with parents",start1,end1,null,"weekly");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToString() {
		System.out.println(one.toString());
	}

}
