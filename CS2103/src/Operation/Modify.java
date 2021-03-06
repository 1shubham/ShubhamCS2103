package operation;
import org.apache.log4j.Logger;

import constant.OperationFeedback;
import parser.Parser;
import data.Task;
import storagecontroller.StorageManager;
public class Modify extends BaseSearch{
	
	private Task oldTask;
	private Task newTask;
	private static Task taskBeingEdited=null;
	private static Logger logger=Logger.getLogger(Modify.class);

	public Modify(){
		commandName="modify";
	}
	public Task[] executeAll(Task t) {
		// TODO Auto-generated method stub
		return null;
	}
	public Modify(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
		if (commandName=="canceledit")
		{
			taskBeingEdited=null;
		}
		
	}

	

	public Task[] execute(Task taskToBeEdited){
		if(taskBeingEdited==null)
		{
			taskBeingEdited=StorageManager.getTaskById(taskToBeEdited.getTaskId());
			logger.debug("taskBeingEdited"+taskBeingEdited.getName());
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
				logger.debug("Editing succesful");
				return new Task[]{taskToBeEdited};
				
			}
			feedback=OperationFeedback.EDIT_FAILED;
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
		Task[] def=StorageManager.getAllTasks();
    	if (def!=null)
    	{
    		for (int i=0;i<def.length;i++)
    		{
    			logger.debug(def[i].toString()+" "+def[i].getTaskId());
    		}
    	}
		
		if (taskBeingEdited==null)
		{
			return super.execute(userCommand);
		}
		
		else
		{
			String params = userCommand.toLowerCase().replaceFirst(commandName+" ","");
			Task taskToBeEdited=parseTask(params);
			logger.debug("Task To be edited"+taskToBeEdited.getName());
			return execute(taskToBeEdited);
			
		}
	}

	private Task parseTask(String userCommand) {
		// TODO Auto-generated method stub
		
		Parser newParser=new Parser();
		return newParser.parseForSearch(userCommand);
		
	}
	
	public Task[] redo(){
		Task[] redoneArray = new Task[1];
		if (modify(oldTask, newTask)) {
			redoneArray[0] = newTask;
			return redoneArray;
		}
		return null;
	}


}

