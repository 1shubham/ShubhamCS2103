package storage;

import java.util.Vector;
public class Storage
{
	Vector<Task> taskList;
	/** constructor*/
	public Storage()
	{
		taskList=new Vector<Task>();
	}
	/** Member function to add task
	 * 
	 * @param newTask task to be added
	 */
	void add(Task newTask)
	{
		taskList.addElement(newTask);
	}
	/** Member function to delete task
	 * 
	 * @param taskId id of the task to be deleted
	 */
	void delete(int taskId)
	{
		taskList.removeElementAt(taskId-1);
	}
	/** Member function to get task
	 * 
	 * @param taskId id of the task needed
	 * @return the task of the provided taskId
	 */
	public Task getTask(int taskId)
	{
		return taskList.elementAt(taskId-1);
	}
	
}