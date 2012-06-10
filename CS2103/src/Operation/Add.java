/**
 * @author Shubhendra Agrawal
 */
package operation;

//import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import constant.OperationFeedback;

import parser.Parser;
import storagecontroller.StorageManager;
import data.Task;


public class Add extends Operation {
	
	private Task addedTask;
	private String commandName;
//	private enum addErrorCode
	/**
	 * Constructor
	 * @param command
	 */
	public Add (String command)
	{
		commandName=command;
		
	}
	/**
	 * Constructor
	 */
	public Add()
	{
		commandName="add";
		
	}
	/**
	 * 
	 */
	public Task[] execute (String userCommand)
	{
		String params=null;
		params = userCommand.toLowerCase().replaceFirst(commandName+" ","");		
		Task newTask= parseCommand(params);
		if (newTask!=null)
		{
			boolean isAdded = add(newTask);
			if (isAdded) {
				isUndoAble = true;
				Task[] resultOfAdd = new Task[1];
				addedTask=newTask;
				resultOfAdd[0] = newTask;
				return resultOfAdd;
			} else {
				return null;
			}
		}
		else
			return null;
		
	}
	/**
	 * 
	 * @param params
	 * @return Task sent for parsing
	 */
	private Task parseCommand(String params) {
		// TODO Auto-generated method stub
		Parser newParser=new Parser();
		return newParser.parseForAdd(params);
		
	}
	@Override
	/**
	 * 
	 */
	public boolean isInputCorrect(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	/**
	 * @return Task array of the task that needs to be deleted in order for this action to be undone
	 */
	public Task[] undo() {
		// TODO Auto-generated method stub
		Task[] undone = new Task[1];
		Delete deleteObj = new Delete();
		logger.debug("task to be deleted name:"+addedTask.getName());
		if (deleteObj.delete(addedTask)) {
			logger.debug("Task deleted");
			undone[0] = addedTask;
			return undone;
		
		}
		logger.debug("Task not deleted");
		return null;
		
		
	}
	/**
	 * @return Task array of the Task that is added again for redone to be completed
	 */
	public Task[] redo() {
		
		Task[] redone = new Task[1];
		
		logger.debug("task to be added name:"+addedTask.getName());
		if (add(addedTask)) {
			logger.debug("Task added");
			redone[0] = addedTask;
			return redone;
		
		}
		logger.debug("Task not added");
		return null;
	}
	

	@Override
	/**
	 * @return Whether add functionality can be redone or not
	 * 
	 */
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return isUndoAble;
	}

	

	@Override
	/**
	 * @return the Operation name of add operation
	 * 
	 */
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}
	
private static Logger logger = Logger.getLogger(Add.class);
    
    
	/**
	 * 
	 * @param taskAdded
	 * @return True/false depending on whether the task can be added
	 */
    public boolean add(Task taskAdded) {
		// TODO Auto-generated method stub
		if (taskAdded!=null)
		{
			return StorageManager.addTask(taskAdded);
		}
		return false;
	}

	@Override
	/**
	 * @return Various status messaged associated with adding of tasks
	 */
	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}      
               
    
	
	
}
