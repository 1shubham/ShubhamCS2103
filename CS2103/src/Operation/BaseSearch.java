/**
 * @author Shubhendra Agrawal
 */
package operation;
import parser.Parser;
import data.Task;
import java.util.ArrayList;
import operation.Search;
import java.util.Collections;
import storagecontroller.StorageManager;
import org.apache.log4j.Logger;

import constant.OperationFeedback;
public class BaseSearch extends Operation{
	
	
	protected String commandName;
	private static Logger logger=Logger.getLogger(BaseSearch.class);
	/**
	 * 
	 */
	public Task[] execute(String userCommand)
	{
		String params = userCommand.toLowerCase().replaceFirst(this.commandName+" ","");
		logger.debug(commandName);
		logger.debug("inside basesearch");
		ArrayList<Task> foundTasks=new ArrayList<Task>();
				
		String[] extractedTaskIds=extractTaskIds(params);
		
		if(extractedTaskIds!=null)
		{
			logger.debug("going to the id part");
			logger.debug(extractedTaskIds[0]);
			
			//userCommand=userCommand.replace(extractedTaskIds[0],"");
			for(int i=0;i<extractedTaskIds.length;i++)
			{
				Task t=StorageManager.getTaskById(extractedTaskIds[i]);
				//logger.debug(t.getTaskId());
				Task[] result;
				if (!commandName.contains("all")){
					result=execute(t);
				}
				else{
					result=executeAll(t);
				}
					
				
				if (result!=null)
				{
					Collections.addAll(foundTasks, result);
					logger.debug("Result Added");
				}
				
				
			}
			
			return foundTasks.toArray(new Task[foundTasks.size()]);
		}
		
		else 
		{
			logger.debug("going for new search");
			Search f=new Search();
			Task findTask=parseEvent(params);
			logger.debug("finished searching");
			return f.search(findTask);
			
		}
			
		
		
	}

	protected Task[] executeAll(Task t) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param userCommand
	 * @return
	 */
	private Task parseEvent(String userCommand) {
		// TODO Auto-generated method stub
		Parser newparser= new Parser();
		return newparser.parseForSearch(userCommand);
		
	}
	/**
	 * 
	 * @param params
	 * @return
	 */
	private String[] extractTaskIds(String params) {
		// TODO Auto-generated method stub
		
		Parser newparser= new Parser();
		
		String[] extractedIds= newparser.fetchTaskIds(params.toUpperCase());
		
		return extractedIds;
		
		
		
		
	}

	@Override
	/**
	 * 
	 */
	public Task[] undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * 
	 */
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return false;
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
	 * 
	 */
	public String getOperationName() {
		// TODO Auto-generated method stub
		return "baseSearch";
	}

	@Override
	/**
	 * 
	 */
	public Task[] redo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * 
	 */
	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}

}
