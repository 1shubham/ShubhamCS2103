package operation;
import parser.Parser;
import data.Task;
import java.util.ArrayList;
import operation.Search;
import java.util.Collections;
import storagecontroller.StorageManager;

public class BaseSearch extends Operation{
	
	
	protected String commandName;
	
	public Task[] execute(String userCommand)
	{
		String params = userCommand.toLowerCase().replaceFirst(this.commandName+" ","");	
		
		ArrayList<Task> foundTasks=null;
				
		String[] extractedTaskIds=extractTaskIds(params);
		
		if(extractedTaskIds!=null)
		{
			//userCommand=userCommand.replace(extractedTaskIds[0],"");
			for(int i=0;i<extractedTaskIds.length;i++)
			{
				Task t=StorageManager.getTaskById(extractedTaskIds[i]);
				Task[] result=execute(t);
				if (result!=null)
				{
					Collections.addAll(foundTasks, result);
				}
				
				
			}
			
			return foundTasks.toArray(new Task[foundTasks.size()]);
		}
		
		else 
		{
			Search f=new Search();
			Task findTask=parseEvent(userCommand);
			return f.search(findTask);
			
		}
			
		
		
	}

	private Task parseEvent(String userCommand) {
		// TODO Auto-generated method stub
		Parser newparser= new Parser();
		return newparser.parse(userCommand);
		
	}

	private String[] extractTaskIds(String params) {
		// TODO Auto-generated method stub
		
		Parser newparser= new Parser();
		return newparser.fetchTaskIds(params);
		
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
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
		return null;
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return "baseSearch";
	}

}
