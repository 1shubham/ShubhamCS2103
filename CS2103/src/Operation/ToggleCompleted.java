package operation;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import constant.OperationFeedback;

import storagecontroller.StorageManager;
import data.Task;

public class ToggleCompleted extends BaseSearch{
	
	private static Logger logger=Logger.getLogger(BaseSearch.class);
	private ArrayList<Task> taskCompleted=new ArrayList<Task>();
	public ToggleCompleted(){
		commandName="completed";
	}
	public ToggleCompleted(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}
	public Task[] executeAll(Task taskToComplete) {
		// TODO Auto-generated method stub
		if (taskToComplete.getRecurringId()=="")
			return execute(taskToComplete);
		Task[] taskToBeCompleted = StorageManager
				.getTaskByRecurrenceID(taskToComplete.getRecurringId());
		logger.debug(taskToBeCompleted.length);
		for (int i=0;i<taskToBeCompleted.length;i++)
		{
			boolean completed = toggleComplete(taskToBeCompleted[i]);
			if (completed) {
				isUndoAble = true;
				taskCompleted.add(taskToBeCompleted[i]);
			
				logger.debug("completed succesfully");
				//return resultOfComplete;
			}
		}
		if (taskCompleted.size()!=0)
			return (Task[]) taskCompleted.toArray(new Task[taskCompleted.size()]);
		else 
			return null;
	}
	public Task[] execute(Task taskToBeCompleted){
		Task taskToComplete = StorageManager
				.getTaskById(taskToBeCompleted.getTaskId());

		boolean completed = toggleComplete(taskToComplete);
		if (completed) {
			isUndoAble = true;
			taskCompleted.add(taskToComplete);
			Task[] resultOfComplete = new Task[1];
			resultOfComplete[0] = taskToComplete;
			logger.debug("completed succesfully");
			return resultOfComplete;
		}

		return null;
		
	}

	private boolean toggleComplete(Task taskToComplete) {
		// TODO Auto-generated method stub
		Task completeTask=StorageManager.getTaskById(taskToComplete.getTaskId());
		if (completeTask!=null){
			completeTask.toggleCompleted();
		
			return true;
		}
		else {
			return false;
		}
		
	}
	@Override
	public Task[] undo() {
		ArrayList<Task> undoneTasks=new ArrayList<Task>();
		for (int i=0;i<taskCompleted.size();i++){
			Task completeTask=StorageManager.getTaskById(taskCompleted.get(i).getTaskId());
			if (completeTask!=null){
				completeTask.toggleCompleted();
				logger.debug("Can undo");
				undoneTasks.add(completeTask);
			}
			
		}
		if (undoneTasks.size()!=0)
			return undoneTasks.toArray(new Task[undoneTasks.size()]);
		else 
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
	public Task[] redo() {
		// TODO Auto-generated method stub
		ArrayList<Task> redoneTasks=new ArrayList<Task>();
		for (int i=0;i<taskCompleted.size();i++){
			Task completeTask=StorageManager.getTaskById(taskCompleted.get(i).getTaskId());
			if (completeTask!=null){
				completeTask.toggleCompleted();
				logger.debug("Can undo");
				redoneTasks.add(completeTask);
			}
			
		}
		if (redoneTasks.size()!=0)
			return redoneTasks.toArray(new Task[redoneTasks.size()]);
		else 
			return null;
	}

	
	
	
	
}
