package data;

import java.util.List;

public class Task {


	private String taskId;// changed to string id for hashing.
	private String name;
	private String description;
	private DateTime start;
	private DateTime end;
	private boolean completed;
	private boolean important;
	private boolean deadline;
	private List<String> labels;
	private String recurring;

public Task()
{
	taskId=null;
	name="";
	description="";
	start=null;
	end=null;
	completed=false;
	important=false;
	deadline=false;
	labels=null;
	recurring=null;
}
/**Constructor*/
public Task(String Name,String desc,DateTime startDateTime,DateTime endDateTime,String recurring)
{
	this();
	name = Name;
	description = desc;
	start = startDateTime;
	end = endDateTime;
	this.recurring=recurring;
}
public Task(String Name,String desc,DateTime startDateTime,DateTime endDateTime,List<String> Labels,String recurring,boolean Deadline,boolean Important)
{
	this();
	name = Name;
	description = desc;
	start = startDateTime;
	end = endDateTime;
	labels=Labels;
	this.recurring=recurring;
	deadline=Deadline;
	important=Important;
}
public Task(String Name,String desc,DateTime startDateTime,DateTime endDateTime,List<String> Labels,String recurring)
{
	this();
	name = Name;
	description = desc;
	start = startDateTime;
	end = endDateTime;
	labels=Labels;
	this.recurring=recurring;
}
public Task(String Name)
{
	this();
	this.name=Name;
}
/** get the taskId of the Task*/
public String getTaskId()
{
	return taskId;
}
public void setTaskId(String id)
{
	taskId=id;
}

public List<String> getLabels()
{
	return labels;
}
public void setLabels(List<String> labels2)
{
	labels=labels2;
}
/** get the name of the Task*/
public String getName()
{
	return name;
}
public void setName(String Name)
{
	name=Name;
}
/** get the description of the Task*/
public String getDescription()
{
	return description;
}
public void setDescription(String desc)
{
	description=desc;
}
/** get the startDate of the Task*/
public DateTime getStartDateTime()
{
	return start;
}
public void setStartDateTime(DateTime startDateTime)
{
	start=startDateTime;
}
/**get the endDate of the Task*/
public DateTime getEndDateTime()
{
	return end;
}
public void setEndDateTime(DateTime endDateTime)
{
	end=endDateTime;
}
/** check whether the Task is Completed
 * 
 * @return true if the task is Completed, otherwise false
 */
public boolean getCompleted()
{
	return completed;
}
public void setCompleted(boolean value)
{
	this.completed=value;
}
/** check whether the task is Important
 * 
 * @return true if the task is important, otherwise false
 */
public boolean getImportant()
{
	return important;
}
public void setImportant(boolean value)
{
	this.important=value;
}
public boolean getDeadline()
{
	return deadline;
}
public void setDeadline(boolean value)
{
	this.deadline=value;
}
public String getRecurring()
{
	return recurring;
}
public void setRecurring(String recurringString)
{
	recurring=recurringString;
}
public boolean isEqual(Object to) {
	String thisObjString = null;
	String compareToObjString = null;
	if (to == null) {
		return false;
	}
	if (!(to instanceof Task)) {
		return false;
	}
	Task compareTo = (Task) to;
	thisObjString = this.toString();
	compareToObjString = compareTo.toString();
	return thisObjString.equals(compareToObjString);
}
public boolean isIdenticalTo(Task second)
{
	if(this.description==second.description && this.name==second.name && 
			this.end==second.end && this.start==second.start && this.recurring==second.recurring && this.taskId==second.taskId)
		return true;
	else
		return false;
}
/** mark the Task as important or unimportant*/
public void toggleImportant()
{
	important = !important;
}

/** mark the Task as completed or unimportant*/
public void toggleCompleted()
{
	completed = !completed;
}
public void toggleDeadline()
{
	deadline=!deadline;
}
}