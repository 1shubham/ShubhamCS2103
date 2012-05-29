package operation;

import data.Task;


public abstract class Operation {

	
	
	
	boolean isUndoAble=false;
	
	public static Operation getOperationObj(String userCommand)
	{
		Operation object;
		
		String intendedOperation;
		intendedOperation=userCommand.trim().split("\\s+")[0];
		intendedOperation = intendedOperation.toLowerCase();
		
		if (intendedOperation.equals("add") || intendedOperation.equals("insert")){
			object = new Add(intendedOperation);
			}
		else if (intendedOperation.equals("delete") || intendedOperation.equals("remove")){
			object = new Delete(intendedOperation);
		}
		else if (intendedOperation.equals("modify") || intendedOperation.equals("update") || 
				intendedOperation.equals("edit")) {
			object =new Modify(intendedOperation);
		}
		else if (intendedOperation.equals("search") || intendedOperation.equals("find")){
			object = new Search(intendedOperation);
		}
		else if (intendedOperation.equals("completed") || intendedOperation.equals("done")){
			object = new Completed(intendedOperation);
		}
		else if (intendedOperation.equals("archive")){
			object = new Archive(intendedOperation);
		}
		else if (intendedOperation.equals("overdue")){
			object= new Overdue(intendedOperation);
		}
		else
		{
			object=new Default();
		}

		
		
		return object; 
	}
	
	
	public abstract Task[] execute(String userCommand);
	
	public abstract Task[] undo();
	
	protected Task[] execute(Task taskToBeExecuted)
	{
		return null;
		
	}		
	public abstract boolean isUndoAble();
	
	public abstract boolean isInputCorrect(String command);
	public abstract String getErrorMessage();
	
	public abstract String getOperationName();
	
	/**
	 * @param args
	 */


}
