package operation;

import parser.Parser;
import storagecontroller.StorageManager;

import data.Task;

public class Search extends Operation {
	
	private String commandName;
	public Search(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}
	
	public Search(){
		commandName="search";
	}

	public Task[] execute(){
		
		
		return null;
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
		return null;
	}

	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub

		String params = "";
		if (userCommand.startsWith("search ")) {
			params = userCommand.replace("search ", "");
		} else if (userCommand.startsWith("find ")) {
			params = userCommand.replace("find ", "");
		}
		if (params.toLowerCase().contains("*.*")) {
			return returnAllTasks(params);
		}
		Task parsedTask=parserTask(params);
		//return find(findEvent);
		
		return null;
	}

	private Task parserTask(String params) {
		// TODO Auto-generated method stub
		Parser newParser=new Parser();
		return newParser.parseForSearch(params);
	}

	private Task[] returnAllTasks(String params) {
		// TODO Auto-generated method stub
		return StorageManager.getAllTasks();
		//return null;
	}

	public Task[] search(Task findTask) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
