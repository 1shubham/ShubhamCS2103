package operation;

import java.util.Arrays;
import java.util.Comparator;

import storagecontroller.StorageManager;
import constant.OperationFeedback;
import data.CompareByDate;
import data.Task;
import data.TaskDateTime;
public class Schedule extends Operation{
	private String commandName;
	public Schedule (){
		commandName="schedule";
	}
	public Schedule(String userInput){
		commandName=userInput;
	}
	public Task[] executeAll(Task t) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub
		Search SearchObj=new Search();
		TaskDateTime newDate=new TaskDateTime();
		Task findTask=new Task();
		Comparator<Task> compareByDate=new CompareByDate();
		findTask.setStart(newDate);
		Task[] allSortedTasks=SearchObj.returnAllTasks();
		Task[] specificDateTask=SearchObj.search(findTask);
		Arrays.sort(specificDateTask,compareByDate);
		for (int i=0;i<specificDateTask.length;i++){
			
		}
		return null;
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task[] redo() {
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
	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return null;
	}

}
