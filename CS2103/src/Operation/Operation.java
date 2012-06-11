/**
 * @author Shubhendra Agrawal
 */

package operation;

import constant.OperationFeedback;

import org.apache.log4j.Logger;

import data.Task;


public abstract class Operation {

	
	
	protected OperationFeedback feedback=OperationFeedback.VALID;
	protected boolean isUndoAble=false;
	private static Logger logger=Logger.getLogger(Operation.class);
	/**
	 * used to instantiate the operation class that needs to be executed
	 * @param userCommand
	 * @return Operation that the user wants to carry out
	 */
	public static Operation getOperationObj(String userCommand)
	{
		Operation object;
		
		String intendedOperation;
		intendedOperation=userCommand.trim().split("\\s+")[0];
		intendedOperation = intendedOperation.toLowerCase();
		logger.debug(intendedOperation);
		
		
		if (intendedOperation.equals("add") || intendedOperation.equals("insert")){
			object = new Add(intendedOperation);
			}
		else if (intendedOperation.equals("delete") || intendedOperation.equals("remove") || 
				intendedOperation.equals("deleteall")){
			object = new Delete(intendedOperation);
		}
		else if (intendedOperation.equals("modify") || intendedOperation.equals("update") || 
				intendedOperation.equals("edit") || intendedOperation.equals("canceledit")) {
			object =new Modify(intendedOperation);
		}
		else if (intendedOperation.equals("search") || intendedOperation.equals("find")){
			object = new Search(intendedOperation);
		}
		else if (intendedOperation.equals("completed") || intendedOperation.equals("done") ||
				intendedOperation.equals("completedall")){
			object = new ToggleCompleted(intendedOperation);
		}
		else if(intendedOperation.equals("star") || intendedOperation.equals("important") ||
				intendedOperation.equals("starall")){
			object = new ToggleImportant(intendedOperation);
		}
		else if (intendedOperation.equals("archive") || intendedOperation.equals("clearachrive") ||
				intendedOperation.equals("exportarchive")){
			object = new Archive(intendedOperation);
		}
		else if (intendedOperation.equals("overdue")){
			object= new Overdue(intendedOperation);
		}
		else if (intendedOperation.equals("login")){
			object= new GoogleCalendarOp();
		}
		else
		{
			object=new Default();
		}

		
		
		return object; 
	}
	
	/**
	 * 
	 * @param userCommand
	 * @return
	 */
	public abstract Task[] execute(String userCommand);
	/**
	 * Function for undoing previous operation
	 * @return Task that was undone
	 */
	public abstract Task[] undo();
	/**
	 * Function for redoing previous operation
	 * @return Task that was redone
	 */
	public abstract Task[] redo();
	/**
	 * to carry out operation on individual task
	 * @param taskToBeExecuted
	 * @return Task that was successfully executed else null
	 */
	protected Task[] execute(Task taskToBeExecuted)
	{
		return null;
		
	}		
	/**
	 * 
	 * @return Whether the operation in undoable
	 */
	public abstract boolean isUndoAble();
	/**
	 * 
	 * @param command
	 * @return Return whether the input String is correct
	 */
	public abstract boolean isInputCorrect(String command);
	/**
	 * Used to return the status of execution in operation
	 * @return Status of operation
	 */
	public abstract OperationFeedback getOpFeedback();
	/**
	 * 
	 * @return Operation name
	 */
	public abstract String getOperationName();
	
	

}
