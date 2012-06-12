package data;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;


public class TaskHashMap
{
	private Map<String,Task> taskList;
	private static Logger logger = Logger.getLogger(TaskHashMap.class);
	/** constructor*/
	public TaskHashMap()
	{
		taskList=new HashMap<String,Task>();
	}
	/** Member function to add task
	 * 
	 * @param newTask task to be added
	 */
	public boolean addTask(Task taskToBeAdded)
	{
	
		if(taskToBeAdded == null)
			return false;
		String uniqueID=generateUniqueId(taskToBeAdded);
		if (uniqueID!=null){
		taskToBeAdded.setTaskId(uniqueID);
		}
		else 
			return false;
		taskList.put(taskToBeAdded.getTaskId(), taskToBeAdded);
		return true;
	}
	/** Member Function to generate a unique id for the task to be added
	 * 
	 * @param taskToBeAdded the task to be added
	 * @return a string containing the unique id
	 */
	public String generateUniqueId(Task taskToBeAdded)
	{
		String taskId;
		Random random=new Random();
		do
		{
		if (taskToBeAdded.getEnd()!=null){
			taskId="$$__" + taskToBeAdded.getEnd().generateDateCode() + taskToBeAdded.getEnd().generateTimeCode() + 
					(char)(random.nextInt('Z'-'A'+1)+'A')+ "__$$";
			}
		else if (taskToBeAdded.getStart()!=null){
			taskId="$$__"+ taskToBeAdded.getStart().generateDateCode()+ taskToBeAdded.getStart().generateTimeCode()+
					(char)(random.nextInt('Z'-'A'+1)+'A')+ "__$$";
		}
		else{
			return null;
		}
		}while((getKeySet().contains(taskId)));
		return taskId;
	}
	/** Member function to delete task
	 * 
	 * @param taskId id of the task to be deleted
	 */
	public boolean deleteTask(Task taskToRemove)
	{
		if(taskToRemove == null){
			logger.debug("task is null");
			return false;
		}
			
		taskList.remove(taskToRemove.getTaskId());
		
		return true;
	}
	/** Member function to remove the task by id
	 * 
	 * @param key contains the id of the task to be deleted
	 * @return true if the task is deleted without any errors, otherwise false.
	 */
	public boolean deleteTaskById(String key)
	{
		if(taskList.keySet().contains(key))
		{
			taskList.remove(key);
			return true;
		}
		else
			return false;
	}
	/** Member function to get task
	 * 
	 * @param taskId id of the task needed
	 * @return the task of the provided taskId
	 */
	public Task getTaskById(String id)
	{
		return taskList.get(id);
	}
	/** Member function to add the task by id
	 * 
	 * @param taskToBeAdded the task to be added
	 */
	public void addTaskById(Task taskToBeAdded)
	{
		taskList.put(taskToBeAdded.getTaskId(), taskToBeAdded);
	}
	/** Member function which returns the key set of the hash map.
	 * 
	 * @return a set of strings containing the ids
	 */
	public Set<String> getKeySet()
	{
		return taskList.keySet();
	}
	/** Member function to clear the hash map
	 * 
	 */
	public void clearHashMap()
	{
		logger.debug("Clearing HashMap");
		taskList.clear();
	}
	/** Member function to get the size of the Hash map
	 * 
	 * @return the size of the Hash Map
	 */
	public int getHashMapSize()
	{
		return getKeySet().size();
	}
}
