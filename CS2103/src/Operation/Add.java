package operation;

//import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import parser.Parser;
import storagecontroller.StorageManager;
import data.Task;


public class Add extends Operation {
	
	private Task addedTask;
	private String commandName;
	public Add (String command)
	{
		commandName=command;
		
	}
	public Add()
	{
		commandName="add";
		
	}
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
				resultOfAdd[0] = newTask;
				return resultOfAdd;
			} else {
				return null;
			}
		}
		else
			return null;
		
	}
	private Task parseCommand(String params) {
		// TODO Auto-generated method stub
		Parser newParser=new Parser();
		return newParser.parse(params);
		
	}
	@Override
	public boolean isInputCorrect(String command) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		Task[] undone = new Task[1];
		Delete deleteObj = new Delete();
		if (deleteObj.delete(addedTask)) {
			undone[0] = addedTask;
			return undone;
		}
		return null;
		
	}

	@Override
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return isUndoAble;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return "Task could not be added.";
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}
	
private static Logger logger = Logger.getLogger(Add.class);
    
    public static void main(String[] args) {
        logger.info("hi");
    	Add adder=new Add();
    	
    	Task[] abc=adder.execute("add *go to meet nirav weekly by 3.45pm 3/8/2012  @work @home");
    	if (abc[0]!=null)
    	System.out.println(abc[0].getName());
    }
	public boolean add(Task taskAdded) {
		// TODO Auto-generated method stub
		if (taskAdded!=null)
		{
			return StorageManager.addTask(taskAdded);
		}
		return false;
	}      
               
    
	
	
}
