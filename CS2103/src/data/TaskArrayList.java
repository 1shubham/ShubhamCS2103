package data;
import java.util.ArrayList;

public class TaskArrayList 
{
ArrayList<Task> arrayList;

public TaskArrayList()
{
	arrayList=new ArrayList<Task>();
}
public void addTask(Task taskToBeAdded)
{
	arrayList.add(taskToBeAdded);
}
public void removeTask(Task taskToBeRemoved)
{
	arrayList.remove(taskToBeRemoved);
}
public int getSize()
{
	return arrayList.size();
}
public Task get(int index)
{
return arrayList.get(index);
}
}
