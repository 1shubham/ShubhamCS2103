package operation;

import org.apache.log4j.Logger;

import constant.OperationFeedback;

import data.Task;
import storagecontroller.StorageManager;
import gcal.GoogleCalendar;
public class GoogleCalendarOp extends Operation {

	private static Logger logger=Logger.getLogger(GoogleCalendarOp.class);
	@Override
	public Task[] execute(String userCommand) {
		logger.debug(userCommand);
		// TODO Auto-generated method stub
		if(userCommand.startsWith("logout")){
					
			return logout();
			
		}
		else if (userCommand.startsWith("login"))
		{
			return login(userCommand);
			
		
		}
		else if (userCommand.startsWith("sync"))
		{
			return sync();
			
		}
		else if (userCommand.startsWith("import"))
		{
			return importTasks();
			
		}
		else if (userCommand.startsWith("export"))
		{
			return exportTasks();
			
		}
		
		return null;
		
		
	}

	private Task[] exportTasks() {
		// TODO Auto-generated method stub
		GoogleCalendar obj=StorageManager.getGCalObject();
		
		if (obj.isLoggedIn()){
			
			if (StorageManager.getGCalObject().exportToGcal()){
				logger.debug("logged and exported successfully");
				return new Task[1];
			}
			else{
				return null;
				
			}
		}
		return null;
	}

	private Task[] importTasks() {
		// TODO Auto-generated method stub
		GoogleCalendar obj=StorageManager.getGCalObject();
		
		if (obj.isLoggedIn()){
			
			if (StorageManager.getGCalObject().importFromGcal()){
				logger.debug("logged and imported successfully");
				return new Task[1];
			}
			else{
				return null;
				
			}
		}
		return null;
	}

	private Task[] sync() {
		// TODO Auto-generated method stub
		GoogleCalendar obj=StorageManager.getGCalObject();
		
		if (obj.isLoggedIn()){
			
			if (StorageManager.getGCalObject().sync()){
				logger.debug("logged and synced successfully");
				return new Task[1];
			}
			else{
				return null;
				
			}
		}
		return null;
	}

	private Task[] login(String userCommand) {
		// TODO Auto-generated method stub
		userCommand.trim().replaceAll("login","");
		logger.debug(userCommand);
		String params[]=userCommand.split("\\s+");
		String username=params[1];
		String password=params[2];
		logger.debug(username);
		logger.debug(password);
		GoogleCalendar obj=new GoogleCalendar();
		obj.login(username, password);
		if (obj.isLoggedIn()){
			StorageManager.setGCalObject(obj);
			if (StorageManager.getGCalObject().sync()){
				logger.debug("logged and synced successfully");
				return new Task[1];
			}
			else{
				return null;
				
			}
		}
		else
			return null;
	
	}

	private Task[] logout() {
		// TODO Auto-generated method stub
		if (StorageManager.getGCalObject().logout()){
			StorageManager.setGCalObject(null);
			feedback=OperationFeedback.LOGGED_OUT_SUCCESSFULLY;
			return new Task[1];
		}
		else {
			feedback=OperationFeedback.LOGOUT_FAILED;
			return null;
		}
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
		return true;
	}

	public OperationFeedback getOpFeedback() {
		// TODO Auto-generated method stub
		return null;
	}      
               
    
	
	
	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return "googlecalendar";
	}

}
