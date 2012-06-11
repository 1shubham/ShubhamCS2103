package Test;
import java.util.ArrayList;

import data.TaskDateTime;
import data.Task;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import storagecontroller.StorageManager;

public class StorageManagerTest {
		//StorageManager managerTest=new StorageManager();
	TaskDateTime start1=new TaskDateTime(2012,6,6,14,0,0);
	TaskDateTime end1=new TaskDateTime(2012,6,6,15,30,0);
	ArrayList<String> labelsTest=new ArrayList<String>();

	
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

/*	public final void testAddTask() {
		labelsTest.add("NUS");
		labelsTest.add("Study");
		Task first=new Task("Go for tutorial",null,start1,end1,labelsTest,null,false,true);
		StorageManager.addTask(first);
		System.out.println(StorageManager.getAllTasks()[0].toString());
	}

	@Test
	public final void testDeleteTaskTask() {
		labelsTest.add("NUS");
		labelsTest.add("Study");
		Task first=new Task("Go for tutorial",null,start1,end1,labelsTest,null,false,true);
		StorageManager.addTask(first);
		assertEquals(2,StorageManager.getAllTasks().length);
	}

	@Test
	public final void testGetAllTasks() {
		for(int i=0;i<StorageManager.getAllTasks().length;i++)
		{
			System.out.println(StorageManager.getAllTasks()[i].toString());
		}
	}
	
	@Test
	public final void testSaveFile() {
		StorageManager.saveFile();
		assertEquals(0,StorageManager.getAllTasks().length);
	}*/
	
	@Test
	public final void testLoadFile() {
		//StorageManager.loadFile();
		//assertEquals(2,StorageManager.getAllTasks().length);
		//System.out.println(StorageManager.getAllTasks()[0].toString());
	}
	@Test
	public final void testReplaceTask(){
		Task test1=new Task("Go UTOWN",null,start1, end1,null,"Daily");
		StorageManager.addTask(test1);
		Task test2=new Task("Go UTOWN's office",null, start1,end1,null,"One");
		StorageManager.replaceTask(test1, test2);
		for(int i=0;i<StorageManager.getAllTasks().length;i++){
			System.out.println(StorageManager.getAllTasks()[i].toString());
		}
	}

}
