package data;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Set;
public class TaskHashMap
{
	private Map<String,Task> taskList;
	
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
	
		if(taskToBeAdded==null)
			return false;
		taskToBeAdded.setTaskId(generateUniqueId(taskToBeAdded));
		taskList.put(taskToBeAdded.getTaskId(), taskToBeAdded);
		return true;
	}
	public String generateUniqueId(Task taskToBeAdded)
	{
		String taskId;
		Random random=new Random();
		if (taskToBeAdded.getEndDateTime()!=null){
			taskId=taskToBeAdded.getEndDateTime().generateDateCode()+taskToBeAdded.getEndDateTime().generateTimeCode()+(char)(random.nextInt('Z'-'A'+1)+'A');
			}
		else
		{
			taskId=taskToBeAdded.getStartDateTime().generateDateCode()+taskToBeAdded.getStartDateTime().generateTimeCode()+(char)(random.nextInt('Z'-'A'+1)+'A');
			
		}
		return taskId;
	}
	/** Member function to delete task
	 * 
	 * @param taskId id of the task to be deleted
	 */
	public boolean deleteTask(Task taskToRemove)
	{
		if(taskToRemove==null)
			return false;
		taskList.remove(taskToRemove.getTaskId());
		return true;
	}
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
	public void addTaskById(Task taskToBeAdded)
	{
		taskList.put(taskToBeAdded.getTaskId(), taskToBeAdded);
		
	}
	public Set<String> getKeySet()
	{
		return taskList.keySet();
	}
	public void clearHashMap()
	{
		taskList.clear();
	}
}
