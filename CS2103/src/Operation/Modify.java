package operation;

import parser.Parser;
import data.Task;
import storagecontroller.StorageManager;
public class Modify extends BaseSearch{
	
	private Task oldTask;
	private Task newTask;
	private static Task taskBeingEdited=null;

	private String commandName;
	public Modify(){
		commandName="modify";
	}
	
	public Modify(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}

	

	public Task[] execute(Task taskToBeEdited){
		if(taskBeingEdited==null)
		{
			taskBeingEdited=StorageManager.getTaskById(taskToBeEdited.getTaskId());
			return new Task[]{taskBeingEdited};
		}
		else{
			
			boolean isEdited=modify(taskBeingEdited,taskToBeEdited);
			if(isEdited)
			{
				isUndoAble=true;
				oldTask=taskBeingEdited;
				newTask=taskToBeEdited;
				taskBeingEdited=null;
				return new Task[]{taskToBeEdited};
				
			}
			return null;	
		}
	}

	private boolean modify(Task oldTask, Task newTask) {
		return StorageManager.replaceTask(oldTask, newTask);
		
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		Task[] undoneArray = new Task[1];
		if (modify(newTask, oldTask)) {
			undoneArray[0] = oldTask;
			return undoneArray;
		}
		return null;
		
	}

	@Override
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInputCorrect(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return "Task could not be edited.";
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}

	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub
		if (taskBeingEdited==null)
		{
			return super.execute(userCommand);
		}
		
		else
		{
			Task taskToBeEdited=parseTask(userCommand);
			return execute(taskToBeEdited);
			
		}
	}

	private Task parseTask(String userCommand) {
		// TODO Auto-generated method stub
		
		Parser newParser=new Parser();
		return newParser.parse(userCommand);
		
	}
	


}

