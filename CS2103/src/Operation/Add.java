/**
 * @author Shubhendra Agrawal
 */
package operation;

//import org.apache.log4j.Level;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import constant.OperationFeedback;

import parser.Parser;
import storagecontroller.StorageManager;
import data.Task;



public class Add extends Operation {
	
	private ArrayList<Task> addedTask=new ArrayList<Task>();
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
		ArrayList<Task> newTask= parseCommand(params);
		
		if (newTask!=null)
		{
			for(int i=0;i<newTask.size();i++){
				
				boolean isAdded = add(newTask.get(i));
				newTask.get(i).setRecurringId(newTask.get(0).getTaskId());
				if (isAdded) {
					isUndoAble = true;
					
					//Task[] resultOfAdd = new Task[1];
					addedTask.add(newTask.get(i));
					//resultOfAdd[0] = newTask;
					//return resultOfAdd;
				}
				else {
					//feedback=OperationFeedback.ADD_FAILED;
					return null;
				}
				
			}
			if (addedTask.size()!=0)
				return (Task[]) addedTask.toArray(new Task[addedTask.size()]);
			else return null;
		}
		else{
			logger.debug("Task Not added");
			return null;
		}
		
	}
	
	
	private ArrayList<Task> parseCommand(String params) {
		// TODO Auto-generated method stub
		Parser newParser=new Parser();
		ArrayList<Task> TaskList=new ArrayList<Task>();
		Task[] parsedTasks=newParser.parseForAdd(params);
		feedback=newParser.getErrorCode();
		if (parsedTasks==null || parsedTasks.length==0){
			return null;
		}
		
		for (int i=0;i<parsedTasks.length;i++){
			TaskList.add(parsedTasks[i]);
		}
		return TaskList;		
	
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
		ArrayList<Task> undoneTasks=new ArrayList<Task>();
		Delete delObject = new Delete();
		for (int i=0;i<addedTask.size();i++){
			
			if (delObject.delete(addedTask.get(i))) {
				undoneTasks.add(addedTask.get(i));
			}
			
		}
		if (undoneTasks.size()!=0)
			return undoneTasks.toArray(new Task[undoneTasks.size()]);
		else 
			return null;
		
		
		
	}
	/**
	 * @return Task array of the Task that is added again for redone to be completed
	 */
	public Task[] redo() {
		
		ArrayList<Task> redoneTasks=new ArrayList<Task>();
		//Add addObject = new Add();
		for (int i=0;i<addedTask.size();i++){
			
			if (add(addedTask.get(i))) {
				redoneTasks.add(addedTask.get(i));
			}
			
		}
		if (redoneTasks.size()!=0)
			return redoneTasks.toArray(new Task[redoneTasks.size()]);
		else 
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
		return feedback;
	}      
               
    
	
	
}
