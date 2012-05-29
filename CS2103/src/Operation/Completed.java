package operation;

import storagecontroller.StorageManager;
import data.Task;

public class Completed extends BaseSearch{
	
	private String commandName;
	private Task taskCompleted;
	public Completed(){
		commandName="completed";
	}
	public Completed(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}

	public Task[] execute(Task taskToBeCompleted){
		Task taskToComplete = StorageManager
				.getTaskById(taskToBeCompleted.getTaskId());

		boolean completed = toggleComplete(taskToComplete);
		if (completed) {
			isUndoAble = true;
			taskCompleted = taskToComplete;
			Task[] resultOfComplete = new Task[1];
			resultOfComplete[0] = taskToComplete;
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
		
		Task completeTask=StorageManager.getTaskById(taskCompleted.getTaskId());
		if (completeTask!=null){
			completeTask.toggleCompleted();
		
			return new Task[]{completeTask};
		}
		else {
			return null;
		}
		
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

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return "Task could not be marked as completed/incomplete";
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}

	
	
	
	
}
