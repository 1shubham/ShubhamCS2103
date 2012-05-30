package storage;


public class Task {
	private int taskId;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private boolean completed;
	private boolean important;
	/**Constructor*/
	public Task(int id,String Name,String desc,Date start,Date end,Time begin,Time finish)
	{
		taskId=id;
		name=Name;
		description=desc;
		startDate=start;
		endDate=end;
		startTime=begin;
		endTime=finish;
	}
	/** get the taskId of the Task*/
	public int getTaskId()
	{
		return taskId;
	}
	/** mark the Task as important*/
	public void markImportant()
	{
		important=true;
	}
	/** mark the Task as completed*/
	public void markCompleted()
	{
		completed=true;
	}
	/** get the name of the Task*/
	public String getName()
	{
		return name;
	}
	/** get the description of the Task*/
	public String getDescription()
	{
		return description;
	}
	/** get the startDate of the Task*/
	public Date getStartDate()
	{
		return startDate;
	}
	/**get the endDate of the Task*/
	public Date getEndDate()
	{
		return endDate;
	}
	/** get the startTime of the Task*/
	public Time getStartTime()
	{
		return startTime;
	}
	/** get the endTime of the Task*/
	public Time getEndTime()
	{
		return endTime;
	}
	/** check whether the Task is Completed
	 * 
	 * @return true if the task is Completed, otherwise false
	 */
	public boolean isCompleted()
	{
		return completed;
	}
	/** check whether the task is Important
 * 
 * @return true if the task is important, otherwise false
 */
public boolean isImportant()
{
	return important;
}
}
