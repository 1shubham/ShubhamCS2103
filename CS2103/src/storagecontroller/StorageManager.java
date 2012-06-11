package storagecontroller;
import data.TaskHashMap;
import data.Task;
import gcal.GoogleCalendar;

import java.util.ArrayList;

import operation.Add;

import org.apache.log4j.Logger;

public class StorageManager 
{
	private static TaskHashMap liveStorage=new TaskHashMap();
	private static TaskHashMap liveArchives=new TaskHashMap();
	private static Logger logger = Logger.getLogger(StorageManager.class);
	private static GoogleCalendar gCal;
	/** default constructor
	 * 
	 */
	public StorageManager()
	{}
	public static boolean addArchivedTask(Task taskToBeAdded)
	{
		return liveArchives.addTask(taskToBeAdded);
	}
	public static boolean deleteArchivedTask(Task taskToBeRemoved)
	{
		return liveArchives.deleteTask(taskToBeRemoved);
	}
	/** adds the task to liveStorage
	 * 
	 * @param taskToBeAdded the task to be added
	 * @return true if the task is added without any errors, otherwise false.
	 */
	public static boolean addTask(Task taskToBeAdded)
	{
		return liveStorage.addTask(taskToBeAdded);
	}
	/** deletes the task from liveStorage
	 * 
	 * @param taskToBeRemoved the task to be removed
	 * @return true if the task is deleted without any errors.
	 */
	public static boolean deleteTask(Task taskToBeRemoved)
	{
		return liveStorage.deleteTask(taskToBeRemoved);
	}
	/** gets all the tasks from liveStorage  
	 * 
	 * @return an array of all tasks in liveStorage
	 */
	public static Task[] getAllTasks()
	{
		ArrayList<Task> tasks=new ArrayList<Task>();
		for(String key: liveStorage.getKeySet())
		tasks.add(liveStorage.getTaskById(key));
		Task[] taskArray=new Task[tasks.size()];
		tasks.toArray(taskArray);
		return taskArray;
	}	
	public static Task[] getAllArchivedTasks(){
		ArrayList<Task> tasks=new ArrayList<Task>();
		for(String key: liveArchives.getKeySet())
		tasks.add(liveArchives.getTaskById(key));
		Task[] taskArray=new Task[tasks.size()];
		tasks.toArray(taskArray);
		return taskArray;
	}
	/** 
	 * 
	 * @param id id of the task to be returned
	 * @return the task of the taskId 'id'
	 */
	public static Task getTaskById(String id)
	{
		return liveStorage.getTaskById(id.trim());
	}
	/** loads to the liveStorage from the file 
	 * 
	 * @return true if the file has been loaded without any errors, otherwise false
	 */
	public static boolean loadFile()
	{
		FileHandler handler=new FileHandler("JotItDownDatabase.xml");
		if(liveStorage.getKeySet().size()!=0)
		{
			logger.debug("Clearing HashMap");
			liveStorage.clearHashMap();
		}
		return handler.readFromFile(liveStorage);
	}
	/** saves to the file from the liveStorage
	 * 
	 * @return true if all the tasks have been loaded without errors, otherwise false.
	 */
	
	
	
	public static boolean saveFile()
	{
		FileHandler handler=new FileHandler("JotItDownDatabase.xml");
		if(handler.writeToFile(liveStorage))
			return true;
		else 
			return false;
	}
	/** replaces tasks
	 * 
	 * @param taskToBeReplaced
	 * @param taskToReplaceBy
	 * @return true if the task has been replaced without errors, otherwise false
	 */
	public static boolean replaceTask(Task taskToBeReplaced,Task taskToReplaceBy)
	{
		if(taskToBeReplaced==null || taskToReplaceBy==null)
			return false;
		System.out.println(getAllTasks().length);
		liveStorage.deleteTask(taskToBeReplaced);
		taskToReplaceBy.setTaskId(taskToBeReplaced.getTaskId());
		liveStorage.addTask(taskToReplaceBy);
		taskToReplaceBy.setDescription(taskToBeReplaced.getDescription());
		return true;
	}
	/** exports from the xml file to a text file with name as fileName
	 * 
	 * @param fileName name of the txt file
	 */
	public static void exportToTxt(String fileName)
	{
		FileHandler handler=new FileHandler(fileName);
		handler.writeToFile(liveStorage);
	}
	/** deletes task from liveStorage
	 * 
	 * @param id taskId of the task to be deleted
	 * @return true if the task has been successfully deleted, otherwise false
	 */
	public static boolean deleteTask(String id)
	{
		return liveStorage.deleteTaskById(id);
	}
	public static boolean saveArchive()
	{
		FileHandler handler=new FileHandler("JotItDownArchives.xml");
		if(handler.writeToFile(liveStorage))
			return true;
		else 
			return false;
	}
	public static boolean clearArchive()
	{
		FileHandler handler=new FileHandler("JotItDownArchives.xml");
		TaskHashMap newTaskMap=new TaskHashMap();
		if(handler.writeToFile(newTaskMap))
			return true;
		else 
			return false;
	}
	public static boolean loadArchive(){
		FileHandler handler=new FileHandler("JotItDownArchives.xml");
		if(liveArchives.getKeySet().size()!=0)
		{
			logger.debug("Clearing Archived HashMap");
			liveArchives.clearHashMap();
		}
		return handler.readFromFile(liveArchives);
		
	}
	public static GoogleCalendar getGCalObject(){
		return gCal;
	}
	public static void setGCalObject(GoogleCalendar obj){
		gCal=obj;
	}
	public static Task[] getTaskByRecurrenceID(String Id){
		ArrayList<Task> tasks=new ArrayList<Task>();
		for(String key: liveStorage.getKeySet()){
			if (liveStorage.getTaskById(key).getRecurringId().contains(Id))
			{
				tasks.add(liveStorage.getTaskById(key));
			}
		}
		Task[] taskArray=new Task[tasks.size()];
		tasks.toArray(taskArray);
		return taskArray;
		
	}
}

