package storagecontroller;
import data.DateTime;
import data.TaskHashMap;
import data.Task;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StorageManager 
{
	private static TaskHashMap liveStorage=new TaskHashMap();
	public StorageManager()
	{
	}
	
	public static boolean addTask(Task taskToBeAdded)
	{
		return liveStorage.addTask(taskToBeAdded);
		
	}
	public static boolean deleteTask(Task taskToBeRemoved)
	{
		return liveStorage.deleteTask(taskToBeRemoved);
			
	}
	public static Task[] getAllTasks()
	{
		ArrayList<Task> tasks=new ArrayList<Task>();
		for(String key: liveStorage.getKeySet())
		tasks.add(liveStorage.getTaskById(key));
		Task[] taskArray=new Task[tasks.size()];
		tasks.toArray(taskArray);
		return taskArray;
	}	
	public static Task getTaskById(String id)
	{
		return liveStorage.getTaskById(id);
	}
	public static boolean loadFile()
	{
		FileHandler handler=new FileHandler("JotItDownDatabase.xml");
		if(liveStorage.getKeySet().size()!=0)
			liveStorage.clearHashMap();
		return handler.readFromFile(liveStorage);
	}
	public static boolean saveFile()
	{
	FileHandler handler=new FileHandler("JotItDownDatabase.xml");
	if(handler.writeToFile(liveStorage))
		return true;
	else 
		return false;
	}
	public static boolean replaceTask(Task taskToBeReplaced,Task taskToReplaceBy)
	{
		return ((liveStorage.deleteTask(taskToBeReplaced)) && (liveStorage.addTask(taskToReplaceBy)));
	}
	public static void exportToTxt(String fileName)
	{
		FileHandler handler=new FileHandler(fileName);
		handler.writeToFile(liveStorage);
	}
	public static boolean deleteTask(String id)
	{
		return liveStorage.deleteTaskById(id);
	}
	public static boolean saveArchive()
	{
		return false;
	}
	public static boolean clearArchive()
	{
		return false;
	}
	public static void main(String args[])
	{
		DateTime start = new DateTime(2012,5,30,18,0,0);
		DateTime end = new DateTime(2012,5,30,18,30,0);
		DateTime start2=new DateTime(2012,5,30,19,0,0);
		DateTime end2=new DateTime(2012,5,30,20,0,0);
		Task one=new Task("Go to school","for buying a nb",start,end,"monthly");
		Task two=new Task("Wash Clothes","in R3",start2,end2,"weekly");
		StorageManager.addTask(one);
		StorageManager.addTask(two);
		StorageManager.saveFile();
		
		
		
		
	}
}

