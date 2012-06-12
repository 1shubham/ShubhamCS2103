package operation;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import storagecontroller.StorageManager;
import constant.OperationFeedback;
import data.Task;

public class Archive extends Operation{
	
	private String commandName;
	private static Logger logger=Logger.getLogger(Archive.class);
	private enum archiveStatus{
		CLEAR_ARCHIVE, IMPORT_ARCHIVE, ARCHIVE, NON;
	}
	private archiveStatus archiveCommand=archiveStatus.NON;
	private ArrayList<Task> archiveTasks=new ArrayList<Task>();
	public Archive(){
		commandName="archive";
	}
	
	public Archive(String intendedOperation) {
		// TODO Auto-generated constructor stub
		commandName=intendedOperation;
	}

	public Task[] execute(){
		return null;
	}

	@Override
	public Task[] undo() {
		// TODO Auto-generated method stub
		ArrayList<Task> undoneTasks=new ArrayList<Task>();
		if (archiveCommand==archiveStatus.CLEAR_ARCHIVE){
			return null;
		}
		else if (archiveCommand==archiveStatus.IMPORT_ARCHIVE){
			for (int i=0;i<archiveTasks.size();i++){
				if (archiveTasks.get(i).getCompleted()){
					
					if( StorageManager.deleteTask(archiveTasks.get(i)) && StorageManager.addArchivedTask(archiveTasks.get(i)))
					{
						undoneTasks.add(archiveTasks.get(i));
					}
					
				}
			}
			if (archiveTasks.size()==0)
				return null;
			else
				return (Task[]) archiveTasks.toArray(new Task[archiveTasks.size()]);
		}
		else if (archiveCommand==archiveStatus.ARCHIVE){
			for (int i=0;i<archiveTasks.size();i++){
				if (archiveTasks.get(i).getCompleted()){
					
					if( StorageManager.deleteArchivedTask(archiveTasks.get(i)) && StorageManager.addTask(archiveTasks.get(i)))
					{
						undoneTasks.add(archiveTasks.get(i));
					}
					
				}
			}
			if (archiveTasks.size()==0)
				return null;
			else
				return (Task[]) archiveTasks.toArray(new Task[archiveTasks.size()]);
		}
		else
			return null;
	}

	@Override
	public boolean isUndoAble() {
		// TODO Auto-generated method stub
		return isUndoAble;
	}

	@Override
	public boolean isInputCorrect(String command) {
		// TODO Auto-generated method stub
		return true;
	}

	

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return "archive";
	}

	@Override
	public Task[] execute(String userCommand) {
		// TODO Auto-generated method stub
		logger.debug(userCommand.trim());
		if (userCommand.toLowerCase().startsWith("archive")){
			archiveCommand=archiveStatus.ARCHIVE;
			logger.debug("running archvie");
			return archiveTasks();
		}
		else if(userCommand.toLowerCase().trim()==("cleararchive")){
			logger.debug("running clear archvie");
			archiveCommand=archiveStatus.CLEAR_ARCHIVE;
			return clearArchive();
		}
		else if(userCommand.toLowerCase().trim()=="importarchive"){
			archiveCommand=archiveStatus.IMPORT_ARCHIVE;
			return importArchive();
		}
		else 
			return null;
	}

	private Task[] importArchive() {
		// TODO Auto-generated method stub
		Task[] allArchivedTasks=StorageManager.getAllArchivedTasks();
		
		for (int i=0;i<allArchivedTasks.length;i++){
			if( StorageManager.deleteArchivedTask(allArchivedTasks[i]) && StorageManager.addTask(allArchivedTasks[i]))
			{
				isUndoAble=true;
				archiveTasks.add(allArchivedTasks[i]);
			}
			else{
				feedback=OperationFeedback.TASK_COULD_NOT_BE_EXPORTED_FROM_ARCHIVES;
				return null;
			}
		}
		if (archiveTasks.size()==0){
			feedback=OperationFeedback.NO_TASK_IN_ARCHIVE;
			return null;
		}
		else
			return (Task[]) archiveTasks.toArray(new Task[archiveTasks.size()]);
	}

	private Task[] clearArchive() {
		// TODO Auto-generated method stub
		isUndoAble=false;
		StorageManager.clearArchive();
		return new Task[1];
		
	}

	private Task[] archiveTasks() {
		// TODO Auto-generated method stub
		
		Task[] allTasks=StorageManager.getAllTasks();
		logger.debug("inside archvietasks()");
		for (int i=0;i<allTasks.length;i++){
			if (allTasks[i].getCompleted()){
				
							
				
				if(StorageManager.deleteTask(allTasks[i]) && StorageManager.addArchivedTask(allTasks[i]))
				{
					
					
					isUndoAble=true;
					archiveTasks.add(allTasks[i]);
				}
				else{
					feedback=OperationFeedback.TASK_COULD_NOT_BE_EXPORTED_TO_ARCHIVES;
					return null;
				}
				
			}
		}
		if (archiveTasks.size()==0){
			feedback=OperationFeedback.NO_TASK_TO_ARCHIVE;
			return null;
			
		}
		else
			return (Task[]) archiveTasks.toArray(new Task[archiveTasks.size()]);
	}

	@Override
	public Task[] redo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return feedback;
	}
	

}
