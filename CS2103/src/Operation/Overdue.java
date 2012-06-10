package operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import data.TaskDateTime;
import data.Task;
import data.CompareByDate;

import org.apache.log4j.Logger;

import constant.OperationFeedback;

import storagecontroller.StorageManager;
public class Overdue extends Operation {
	
	private String commandName;
	private static Logger logger=Logger.getLogger(Overdue.class);
	public Overdue(){
		commandName="overdue";
	}
	
	public Overdue(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}

	
	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return isUndoAble;
	}

	@Override
	public boolean isInputCorrect(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}      
               
    
	
	

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}

	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub
		TaskDateTime currDateTime =	TaskDateTime.getCurrentDateTime();
		Comparator<Task> compareByDate=new CompareByDate();
		TaskDateTime defaultDateTime = new TaskDateTime();
		logger.debug(currDateTime.formattedToString());
		Task[] allTasks=StorageManager.getAllTasks();
		ArrayList<Task> overdueTasks=new ArrayList<Task>();
		for (Task curTask : allTasks)
		{
			if (curTask.getStart() != null
					&& curTask.getStart().getTimeMilli()
					!= defaultDateTime.getTimeMilli())
			{
				if (curTask.getStart().compareTo(currDateTime)==-1)
				{
					overdueTasks.add(curTask);
				}
			}
			else if (curTask.getEnd()!=null 
						&& curTask.getEnd().getTimeMilli()!= defaultDateTime.getTimeMilli())
			{
				if (curTask.getEnd().compareTo(currDateTime)==-1)
				{
					overdueTasks.add(curTask);
				}
			}
			else
			{}
		}
		if (overdueTasks.size()==0)	{
			return null;
		} else {
			Collections.sort(overdueTasks,compareByDate);
			return (Task[]) overdueTasks.toArray(new Task[overdueTasks.size()]);
		}
		
		
	
	}

	@Override
	public Task[] redo() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
