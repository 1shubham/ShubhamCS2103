package Test;
import data.TaskDateTime;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.TaskHashMap;
import data.Task;
public class TaskHashMapTest {
	TaskHashMap test=new TaskHashMap();
	TaskDateTime start=new TaskDateTime(2012,6,3,14,0,0);
	TaskDateTime end=new TaskDateTime(2012,6,3,17,0,0);
	Task testTask=new Task("lecture",null,start,end,null,"weekly");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAddTask() {
		test.addTask(testTask);
		assertEquals(1,test.getKeySet().size());
	}

	@Test
	public final void testGenerateUniqueId() {
		assertEquals("$$__03-06-2012170000W__$$", test.generateUniqueId(testTask));
	}

	@Test
	public final void testDeleteTask() {
		test.addTask(testTask);
		assertEquals(1,test.getKeySet().size());
		test.deleteTask(testTask);
		assertEquals(0,test.getKeySet().size());
	}

	@Test
	public final void testGetTaskById() {
		test.addTask(testTask);
		assertEquals(testTask,test.getTaskById(testTask.getTaskId()));
	}

	@Test
	public final void testGetHashMapSize() {
		test.addTask(testTask);
		assertEquals(1,test.getKeySet().size());
	}

}
