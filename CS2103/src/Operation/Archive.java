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
		CLEAR_ARCHIVE, EXPORT_ARCHIVE, ARCHIVE, NON;
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
		else if (archiveCommand==archiveStatus.EXPORT_ARCHIVE){
			for (int i=0;i<archiveTasks.size();i++){
				if (archiveTasks.get(i).getCompleted()){
					
					if( StorageManager.addArchivedTask(archiveTasks.get(i)) && StorageManager.deleteTask(archiveTasks.get(i)))
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
		
		if (userCommand.toLowerCase().startsWith("archive")){
			archiveCommand=archiveStatus.ARCHIVE;
			logger.debug("running archvie");
			return archiveTasks();
		}
		else if(userCommand.toLowerCase()=="cleararchive"){
			archiveCommand=archiveStatus.CLEAR_ARCHIVE;
			return clearArchive();
		}
		else if(userCommand.toLowerCase()=="exportarchive"){
			archiveCommand=archiveStatus.EXPORT_ARCHIVE;
			return exportArchive();
		}
		else 
			return null;
	}

	private Task[] exportArchive() {
		// TODO Auto-generated method stub
		Task[] allArchivedTasks=StorageManager.getAllArchivedTasks();
		
		for (int i=0;i<allArchivedTasks.length;i++){
			if (allArchivedTasks[i].getCompleted()){
				
				if( StorageManager.deleteArchivedTask(allArchivedTasks[i]) && StorageManager.addTask(allArchivedTasks[i]))
				{
					isUndoAble=true;
					archiveTasks.add(allArchivedTasks[i]);
				}
				
			}
		}
		if (archiveTasks.size()==0)
			return null;
		else
			return (Task[]) archiveTasks.toArray(new Task[archiveTasks.size()]);

		
		
	}

	private Task[] clearArchive() {
		// TODO Auto-generated method stub
		isUndoAble=false;
		if (StorageManager.clearArchive())
			return new Task[1];
		else 
			return null;
	}

	private Task[] archiveTasks() {
		// TODO Auto-generated method stub
		
		Task[] allTasks=StorageManager.getAllTasks();
		
		for (int i=0;i<allTasks.length;i++){
			if (allTasks[i].getCompleted()){
				boolean addArchive= StorageManager.addArchivedTask(allTasks[i]);
				boolean deleteTask=StorageManager.deleteTask(allTasks[i].getTaskId());
				if(addArchive && deleteTask)
				{
					
					logger.debug(StorageManager.getAllTasks().length);
					isUndoAble=true;
					archiveTasks.add(allTasks[i]);
				}
				
			}
		}
		if (archiveTasks.size()==0)
			return null;
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
		return null;
	}
	

}
