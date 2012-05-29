package Storage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class StorageManager 
{
	private TaskHashMap liveStorage;
	public StorageManager()
	{
		liveStorage=new TaskHashMap();
	}
	
	public void addTask(Task taskToBeAdded)
	{
		liveStorage.addTask(taskToBeAdded);
	}
	public void deleteTask(Task taskToBeRemoved)
	{
		liveStorage.deleteTask(taskToBeRemoved);
	}
	public Task[] getAllTasks()
	{
		int size=liveStorage.getKeySet().size();
		ArrayList<Task> tasks=new ArrayList<Task>();
		for(String key: liveStorage.getKeySet())
		tasks.add(liveStorage.getTaskById(key));
		Task[] taskArray=new Task[3];
		tasks.toArray(taskArray);
		return taskArray;
	}	
	public Task getTaskById(String id)
	{
		return liveStorage.getTaskById(id);
	}
	public void loadFile() throws ArrayIndexOutOfBoundsException, FileNotFoundException
	{
		FileHandler handler=new FileHandler("JotItDownDatabase.xml");
		if(liveStorage.getKeySet().size()!=0)
			liveStorage.clearHashMap();
		handler.readFromFile(liveStorage);
	}
	public void saveFile() throws FileNotFoundException
	{
	FileHandler handler=new FileHandler("JotItDownDatabase.xml");
	handler.writeToFile(liveStorage);
	}
	public void replaceTask(Task taskToBeReplaced,Task taskToReplaceBy)
	{
		liveStorage.deleteTask(taskToBeReplaced);
		liveStorage.addTask(taskToReplaceBy);
	}
	public void exportToTxt(String fileName) throws FileNotFoundException
	{
		FileHandler handler=new FileHandler("JotItDownDataBase.txt");
		handler.writeToFile(liveStorage);
	}
	public void deleteTask(String id)
	{
		liveStorage.deleteTaskById(id);
	}
	public void saveArchive()
	{
		
	}
	public void clearArchive()
	{
		
	}
}

