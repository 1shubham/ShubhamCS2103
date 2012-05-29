package logic;

//import java.io.FileNotFoundException;
import java.util.Stack;

import operation.*;
//import org.apache.log4j.Logger;
import data.Task;
//import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import gui.UIController;

import storagecontroller.StorageManager;

public class JIDLogic {
	
		private static Logger logger=Logger.getLogger(JIDLogic.class);
		public static void main(String[] args) {
	        //logger.info("hi");
			/*
			System.out.println(StorageManager.loadFile());
	    	Add adder=new Add();
	    	
	    	Task[] abc=adder.execute("add *go to meet bhairav weekly by 3.45pm 3/8/2012  @work @home");
	    	
	    	logger.debug("inside JIDLogic");
	    	if (abc[0]!=null)
	    	{
	    	System.out.println(abc[0].getName());
	    	System.out.println(abc[0].getTaskId());
	    	}
	    	Search searcher=new Search();
	    	Task[] xyz=searcher.execute("find *.*");
	    	if (abc[0]!=null)
	    	{
	    		System.out.println(xyz[3].getTaskId());
	    		System.out.println(xyz[2].getTaskId());
	    	}
	    	System.out.println(StorageManager.saveFile());*/
			
			JIDLogic_init();
			UIController ui=new UIController();
			
			
		
	}
	private static Stack<Operation> undoStack= new Stack<Operation>();
	
	private static String command;
	//private Logger logger = Logger.getLogger(JIDLogic.class.getName());
	/**
	 * controller constructor. Builds the controller object
	 */
	public JIDLogic() {
		//ui = new UiController();
		
		
		
	}
	public static Task[] executeCommand (String commandFromUser) {
		Operation op = null;
		logger.debug("inside execute command");
		if (command == null || command.equals("")) {
			logger.debug("inside first cond");
			return null;
		} else if (command.trim().equals("exit")) {
			logger.debug("inside second cond");
			exit();
			return null;
		} else if (commandFromUser.trim().equals("undo") && !undoStack.empty()) {
			logger.debug("inside third cond");
			Operation undoAction = undoStack.pop();
			return undoAction.undo();
			
		} else {
			logger.debug("inside fourth cond");
			op = Operation.getOperationObj(commandFromUser);
			logger.fatal("Inside the actual execution");
			if (op.isUndoAble()) {
				undoStack.push(op);
			}
			
			return  op.execute(commandFromUser);
			
			
		}
		
		}
		
	public static void JIDLogic_init()
	{
		
		StorageManager.loadFile();
		
	}
	
	public static void JIDLogic_close()
	{
		
		StorageManager.saveFile();
		
	}

	/**
	 * Sets the view to the specified value
	 * 
	 * @param view
	 */
	
	/**
	 * Sets the command to the command specified
	 * 
	 * @param command
	 */
	public static void setCommand(String _command) {
		command = _command;
	}

	/**
	 * executes the command and starts the chain of events
	 */
	public void run() {
	}

	/**
	 * exits the code and closes the UI window
	 */
	public static void exit() {
	
	}

	/**
	 * 
	 * @return command entered
	 */
	public String getCommand() {
		return command;
	}
}



