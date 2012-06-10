package operation;

import org.apache.log4j.Logger;

import constant.OperationFeedback;

import storagecontroller.StorageManager;

import data.Task;


public class ToggleImportant extends BaseSearch {

	
	private static Logger logger=Logger.getLogger(BaseSearch.class);
	private Task taskStarred;
	
	public ToggleImportant(){
		commandName="star";
	}
	
	public ToggleImportant(String intendedOperation){
		commandName=intendedOperation;
	}
	

	
	public Task[] execute(Task taskToBeStarred){
		Task taskToStar = StorageManager
				.getTaskById(taskToBeStarred.getTaskId());

		boolean starred = toggleImportant(taskToStar);
		if (starred) {
			isUndoAble = true;
			taskStarred = taskToStar;
			Task[] resultOfStar = new Task[1];
			resultOfStar[0] = taskToStar;
			logger.debug("starred succesfully");
			return resultOfStar;
		}

		return null;
		
	}

	private boolean toggleImportant(Task taskToStar) {
		// TODO Auto-generated method stub
		Task starredTask=StorageManager.getTaskById(taskToStar.getTaskId());
		if (starredTask!=null){
			starredTask.toggleImportant();
		
			return true;
		}
		else {
			return false;
		}
		
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		Task starredTask=StorageManager.getTaskById(taskStarred.getTaskId());
		if (starredTask!=null){
			starredTask.toggleImportant();
			logger.debug("Can undo");
			return new Task[]{starredTask};
		}
		else {
			return null;
		}
	}

	@Override
	public Task[] redo() {
		
		// TODO Auto-generated method stub
		Task starredTask=StorageManager.getTaskById(taskStarred.getTaskId());
		if (starredTask!=null){
			starredTask.toggleImportant();
			logger.debug("Can redo");
			return new Task[]{starredTask};
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

	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}      
               
    
	
	
	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}

}
