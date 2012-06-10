/**
 * 
 */
package operation;

import org.apache.log4j.Logger;

import constant.OperationFeedback;
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

		String params = "";
		logger.debug("finding objects");
		if (userCommand.startsWith("search ")) {
			params = userCommand.replace("search ", "");
		} else if (userCommand.startsWith("find ")) {
			params = userCommand.replace("find ", "");
		}
		
		if (params.toLowerCase().contains("*.*")) {
			logger.debug("returning all objects");
			return returnAllTasks();
		}
		else if (params.toLowerCase().contains("today*.*")){
			logger.debug("returning todays objects");
			return searchTodaysTasks();
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

	private Task[] returnAllTasks() {
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
	
	private Task[] searchTodaysTasks(){
		Task [] allTasks=returnAllTasks();
		ArrayList<Task> todaysTasks=new ArrayList<Task>();;
		for(Task param:allTasks){
			if (param.getStart()!=null && param.getStart().getDate().getTimeMilli()==TaskDateTime.getCurrentDate().getTimeMilli())
			{
				todaysTasks.add(param);
			} else if (param.getEnd()!=null && param.getEnd().getTimeMilli()==TaskDateTime.getCurrentDate().getTimeMilli()){
				todaysTasks.add(param);
			}
			
		}
		if (todaysTasks.size()!=0)
			return (Task[]) todaysTasks.toArray(new Task[todaysTasks.size()]);
		return null;
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
		/*	if (allTasks[i].getStart()!=null){
			logger.debug("allTasks["+i+"] StartTime:"+allTasks[i].getStart().getTime().getTimeMilli());
			logger.debug("allTasks["+i+"] StartDate:"+allTasks[i].getStart().getDate().getTimeMilli());
			}
			if (allTasks[i].getEnd()!=null){
			logger.debug("allTasks["+i+"] EndTime:"+allTasks[i].getEnd().getTime().getTimeMilli());
			logger.debug("allTasks["+i+"] EndDate:"+allTasks[i].getEnd().getDate().getTimeMilli());
			}
			if (findTask.getStart()!=null){
			logger.debug("searchstring StartTime:"+findTask.getStart().getTime().getTimeMilli());
			logger.debug("searchstring StartDate:"+findTask.getStart().getDate().getTimeMilli());
			}
			if (findTask.getEnd()!=null){
			logger.debug("searchstring EndTime:"+findTask.getEnd().getTime().getTimeMilli());
			logger.debug("searchstring EndDate:"+findTask.getEnd().getDate().getTimeMilli());
			}*/
			if (matches(findTask,allTasks[i])){
				Collections.addAll(foundTasks, allTasks[i]);
			}
			
		}
		if (foundTasks.size()>0){
			return foundTasks.toArray(new Task[foundTasks.size()]);
		}
		return null;
	}

	private boolean doesNameMatch(Task taskToSearch, Task existingTask){
		if (("".equals(taskToSearch.getName()) || existingTask.getName().toLowerCase()
				.contains((taskToSearch.getName().trim()))))
		{
			logger.debug("name matches");
			return true;
		}
		else
		{
			return false;
		}
		
	}
	private TaskDateTime defaultTime=new TaskDateTime();
	private boolean doesStartDateMatch(Task taskToSearch, Task existingTask){
		
		if (taskToSearch.getStart() == null
				|| taskToSearch.getStart().getDate().getTimeMilli()
				== defaultTime.getDate().getTimeMilli() || (existingTask.getStart()!=null 
				&& (existingTask.getStart().getDate().getTimeMilli()
				== taskToSearch.getStart().getDate().getTimeMilli()))){
			logger.debug("start date matches");
			return true;
		
		}
		return false;
		
	}
	private boolean doesStartTimeMatch(Task taskToSearch, Task existingTask){
	//	logger.debug("Default date:"+defaultTime.getDate().getTimeMilli());
	//	logger.debug("Default Time:"+defaultTime.getTime().getTimeMilli());
		if (taskToSearch.getStart() == null
				|| taskToSearch.getStart().getTime().getTimeMilli()
				== defaultTime.getTime().getTimeMilli() || (existingTask.getStart()!=null 
				&&  (existingTask.getStart().getTime().getTimeMilli()
				== taskToSearch.getStart().getTime().getTimeMilli()))){
			logger.debug("start time matches");
					return true;
				}
		return false;
	}
	private boolean doesEndDateMatch(Task taskToSearch, Task existingTask){
		if (taskToSearch.getEnd() == null 
				|| taskToSearch.getEnd().getDate().getTimeMilli()
				== defaultTime.getDate().getTimeMilli() || (existingTask.getEnd()!=null 
				&& (existingTask.getEnd().getDate().getTimeMilli()
				== taskToSearch.getEnd().getDate().getTimeMilli()))){
			logger.debug("end date matches");
			return true;
		}
		return false;
	}
	
	private boolean doesEndTimeMatch(Task taskToSearch, Task existingTask){
		if(taskToSearch.getEnd() == null 
				|| taskToSearch.getEnd().getTime().getTimeMilli()
				== defaultTime.getDate().getTimeMilli() || (existingTask.getEnd()!=null 
				&& (existingTask.getEnd().getTime().getTimeMilli()
				== taskToSearch.getEnd().getTime().getTimeMilli()))){
			logger.debug("end time matches");
			return true;
		}
		return false;
	}
	
	private boolean doesImportantMatch(Task taskToSearch, Task existingTask){
		if  (taskToSearch.getImportant() == false || taskToSearch.getImportant() == 
				existingTask.getImportant())
		{
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param taskToSearch
	 * @param existingTask
	 * @return
	 */
	private boolean doesLabelMatch(Task taskToSearch, Task existingTask){
		
		if (taskToSearch.getLabels()==null || taskToSearch.getLabels().size()==0)
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
				logger.debug("searching in taskToSearch");
				logger.debug(searchlabel);
				searchlabel = searchlabel.toLowerCase();
				flag = false;
				for (String existingLabel : existingTask.getLabels()) {
					//if(existingLabel!=null)
					logger.debug("searching in existing task");
					logger.debug(existingLabel);
					
						if (existingLabel.contains(searchlabel)) {
							logger.debug("its equal");
							flag = true;
							break;
						}
					
				}
				if(!flag)
					break;
			
				}
			if (flag) {
				logger.debug("it matches");
				return true
						;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param taskToSearch
	 * @param existingTask
	 * @return
	 */
	private boolean matches(Task taskToSearch, Task existingTask) {
		// TODO Auto-generated method stub
	
		
		//logger.debug(defaultTime.getTime().getTimeMilli());
		//logger.debug(taskToSearch.getStart().getTime().getTimeMilli());
	
		logger.debug(existingTask.getName());
		
		if (doesNameMatch(taskToSearch, existingTask) && doesStartDateMatch(taskToSearch,existingTask)
				&& doesStartTimeMatch(taskToSearch,existingTask) && doesEndDateMatch(taskToSearch,existingTask) 
				&& doesEndTimeMatch(taskToSearch, existingTask) && doesImportantMatch(taskToSearch,existingTask)
				&& doesLabelMatch(taskToSearch,existingTask)){
			logger.debug("all condition satisfied");
			return true;
		}
		
		
		/*if (("".equals(taskToSearch.getName()) || existingTask.getName().toLowerCase()
				.contains((taskToSearch.getName().trim()))))
		{
			logger.debug("First condition matches");
			if (taskToSearch.getStart() == null
					|| taskToSearch.getStart().getDate().getTimeMilli()
					== defaultTime.getDate().getTimeMilli() || (existingTask.getStart()!=null 
					&& (existingTask.getStart().getDate().getTimeMilli()
					== taskToSearch.getStart().getDate().getTimeMilli())
					))
					{
						logger.debug("second condition matches");
						return true;
					}
		}
		
				
				/*		
				&& (taskToSearch.getStart() == null
						|| taskToSearch.getStart().getDate().getTimeMilli()
						== defaultTime.getDate().getTimeMilli() || (existingTask.getStart()!=null 
						&& (existingTask.getStart().getDate().getTimeMilli()
						== taskToSearch.getStart().getDate().getTimeMilli())
						)))
		{return true;}
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
						.contains(taskToSearch.getRecurring().toLowerCase()))))
		*/
		//{
			//logger.debug("all ok till here");
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
