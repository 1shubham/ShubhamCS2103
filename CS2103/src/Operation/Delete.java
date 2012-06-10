package operation;


import org.apache.log4j.Logger;

import constant.OperationFeedback;

import storagecontroller.StorageManager;

import data.Task;

public class Delete extends BaseSearch {
	
	private Logger logger=Logger.getLogger(Delete.class);
	private Task taskDeleted;
	public Delete(){
		commandName="delete";
	}
	public Delete(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}

	
	public boolean delete(Task taskToBeDeleted)
	
	{
		if (taskToBeDeleted!=null)
		{
			return StorageManager.deleteTask(taskToBeDeleted);
		}
		return false;
	}

	public Task[] execute(Task taskToBeDeleted)
	{
		Task taskToDelete = StorageManager
				.getTaskById(taskToBeDeleted.getTaskId());

		
		boolean deleted = delete(taskToDelete);
		if (deleted) {
			isUndoAble = true;
			logger.debug("isUndoAble value changed" );
			taskDeleted = taskToDelete;
			Task[] resultOfDelete = new Task[1];
			resultOfDelete[0] = taskToDelete;
			logger.debug("deleted succesfully");
			return resultOfDelete;
		}

		return null;
		
	}
	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		Task[] undoneArray = new Task[1];
		Add addObject = new Add();
		if (addObject.add(taskDeleted)) {
			undoneArray[0] = taskDeleted;
			return undoneArray;
		}
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
	
	public Task[] redo() {
		// TODO Auto-generated method stub
		Task[] undone = new Task[1];
		
		logger.debug("task to be deleted name:"+taskDeleted.getName());
		if (delete(taskDeleted)) {
			logger.debug("Task deleted");
			undone[0] = taskDeleted;
			return undone;
		
		}
		logger.debug("Task not deleted");
		return null;
	}


	
	

}
