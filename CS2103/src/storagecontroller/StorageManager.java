package storagecontroller;
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
	public static boolean saveFile() throws FileNotFoundException
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
}

