package data;
import data.Task;
import java.util.ArrayList;

public class TaskArrayList 
{
static ArrayList<Task> arrayList;

public TaskArrayList()
{
	arrayList=new ArrayList<Task>();
}
public static void addTask(Object obj)
{
	Task taskToBeAdded=(Task) obj; 
	arrayList.add(taskToBeAdded);
}
public static void removeTask(Task taskToBeRemoved)
{
	arrayList.remove(taskToBeRemoved);
}
public static int getSize()
{
	return arrayList.size();
}
public static Task get(int index)
{
return arrayList.get(index);
}
}
