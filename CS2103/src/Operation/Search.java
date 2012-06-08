package operation;

import org.apache.log4j.Logger;
import data.TaskDateTime;
import data.CompareByDate;
import java.util.ArrayList;
import java.util.Collections;
import parser.Parser;
import java.util.Comparator;
import storagecontroller.StorageManager;
import java.util.Arrays;

import data.Task;

public class Search extends Operation {
	
	private Logger logger=Logger.getLogger(Search.class);
	
	private String commandName;
	public Search(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}
	
	public Search(){
		commandName="search";
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
		return "Search is unavailable";
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return commandName;
	}

	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub

		String params = "";
		logger.debug("finding objects");
		if (userCommand.startsWith("search ")) {
			params = userCommand.replace("search ", "");
		} else if (userCommand.startsWith("find ")) {
			params = userCommand.replace("find ", "");
		}
		
		if (params.toLowerCase().contains("*.*")) {
			logger.debug("returning all objects");
			return returnAllTasks(params);
		}
		Task parsedTask=parseCommand(params);
		
		if (parsedTask.getStart()!=null){
			logger.debug(parsedTask.getStart().getDate().getTimeMilli());
		}
		return search(parsedTask);
		
		
	}

	private Task parseCommand(String params) {
		// TODO Auto-generated method stub
		Parser newParser=new Parser();
		return newParser.parseForSearch(params);
	}

	private Task[] returnAllTasks(String params) {
		// TODO Auto-generated method stub
		Task[] unsorted=StorageManager.getAllTasks();
		Comparator<Task> compareByDate = new CompareByDate();
		logger.debug("before sorting");
		
		for (int i=0;i<unsorted.length;i++)
		{
			//logger.debug(unsorted[i].toString());
		}
		
		Arrays.sort(unsorted, compareByDate);
		logger.debug("after sorting");
		for (int i=0;i<unsorted.length;i++)
		{
		//	logger.debug(unsorted[i].toString());
		}
		return unsorted;
		//return null;
	}

	public Task[] search(Task taskToSearch) {
		// TODO Auto-generated method stub
		if (taskToSearch.getTaskId() != null) {
			return new Task[]{StorageManager.getTaskById(taskToSearch.getTaskId())};
		}
		else {
			return search(taskToSearch, StorageManager.getAllTasks());
		}
		
		
	}

	@SuppressWarnings("null")
	private Task[] search(Task findTask, Task[] allTasks) {
		// TODO Auto-generated method stub
		ArrayList<Task> foundTasks=new ArrayList<Task>();
		for(int i=0;i<allTasks.length;i++)
		{
			logger.debug("Matching task"+i);
		//	logger.debug("allTasks["+i+"] StartTime:"+allTasks[i].getStart().getTime().getTimeMilli());
		//	logger.debug("allTasks["+i+"] EndTime:"+allTasks[i].getEnd().getTime().getTimeMilli());
		//	logger.debug("searchstring StartTime:"+findTask.getStart().getTime().getTimeMilli());
		//	logger.debug("searchstring EndTime:"+findTask.getEnd().getTime().getTimeMilli());
			if (matches(findTask,allTasks[i])){
				Collections.addAll(foundTasks, allTasks[i]);
			}
			
		}
		if (foundTasks.size()>0){
			return foundTasks.toArray(new Task[foundTasks.size()]);
		}
		return null;
	}

	private boolean matches(Task taskToSearch, Task existingTask) {
		// TODO Auto-generated method stub
	
		TaskDateTime defaultTime=new TaskDateTime();
		//logger.debug(defaultTime.getTime().getTimeMilli());
		//logger.debug(taskToSearch.getStart().getTime().getTimeMilli());
	
		if (("".equals(taskToSearch.getName()) || existingTask.getName().toLowerCase()
				.contains((taskToSearch.getName().trim()))))
				{}
				/*		
				&& (taskToSearch.getStart() == null
						|| taskToSearch.getStart().getDate().getTimeMilli()
						== defaultTime.getDate().getTimeMilli() || (existingTask.getStart()!=null 
						&& (existingTask.getStart().getDate().getTimeMilli()
						== taskToSearch.getStart().getDate().getTimeMilli())
						))
				&& (taskToSearch.getStart() == null
						|| taskToSearch.getStart().getTime().getTimeMilli()
						== defaultTime.getTime().getTimeMilli() || (existingTask.getStart()!=null 
						&&  (existingTask.getStart().getTime().getTimeMilli()
						== taskToSearch.getStart().getTime().getTimeMilli())) 
						)
				&& (taskToSearch.getEnd() == null 
						|| taskToSearch.getEnd().getDate().getTimeMilli()
						== defaultTime.getDate().getTimeMilli() || (existingTask.getEnd()!=null 
						&& (existingTask.getEnd().getDate().getTimeMilli()
						== taskToSearch.getEnd().getDate().getTimeMilli()))
						)
			    && (taskToSearch.getEnd() == null
						|| taskToSearch.getEnd().getTime().getTimeMilli()
						== defaultTime.getTime().getTimeMilli() || (existingTask.getEnd()!=null 
						&& existingTask.getEnd().getTime().getTimeMilli()
						== taskToSearch.getStart().getTime().getTimeMilli())
						)
				&& (taskToSearch.getDescription() == null || existingTask.getDescription()
						.toLowerCase().contains(taskToSearch.getDescription()))
				&& (taskToSearch.getImportant() == false || taskToSearch.getImportant() == 
						existingTask.getImportant())
				&& (taskToSearch.getRecurring() == null || (existingTask.getRecurring()!=null 
						&& existingTask.getRecurring().toLowerCase()
						.contains(taskToSearch.getRecurring().toLowerCase()))))*/
		
	//	{
		//	logger.debug("all ok till here");
			//return true;
			 
			/* 
			if (taskToSearch.getLabels()==null)
			{
				logger.debug("task to search has no labels");
				logger.debug("it matches");
				return true;
			}
			else if (existingTask.getLabels() != null) {
				logger.debug("matching labels");
				
				boolean flag = false;
				for (String searchlabel : taskToSearch.getLabels()) {
					//if (searchlabel!=null)
					{
					searchlabel = searchlabel.toLowerCase();
					flag = false;
					for (String existingLabel : existingTask.getLabels()) {
						//if(existingLabel!=null)
						{
							if (existingLabel.toLowerCase().contains(searchlabel)) {
								flag = true;
								break;
							}
						}
					}
				
					if (flag) {
						break;
					}
				}
				if (flag) {
					logger.debug("it matches");
					return true
							;
				}
				
			}}*/
			
		
				
			
		return false;
	}

	@Override
	public Task[] redo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
