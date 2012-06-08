

//import java.io.FileNotFoundException;
import gui.UIController;

import java.util.Stack;

import logic.JIDLogic;

import operation.*;
//import org.apache.log4j.Logger;
import data.Task;
//import org.apache.log4j.Level;
import org.apache.log4j.Logger;
//import gui.UIController;
import storagecontroller.StorageManager;

public class test_main {
	
	private static Logger logger=Logger.getLogger(test_main.class);

	public static void main(String[] args) {
        //logger.info("hi");
		
		logger.debug(StorageManager.loadFile());
		JIDLogic.setCommand("search");
		Task[] def=JIDLogic.executeCommand("find *.*");
    	if (def!=null)
    	{
    		for (int i=0;i<def.length;i++)
    		{
    			System.out.println(def[i].getTaskId());
    		}
    	}
		
    	/*Add adder=new Add();
    	
    	Task[] abc=adder.execute("add *go to meet bhairav weekly by 3.45pm 3/5/2013  @work @home");
    	Task[] efg=adder.execute("add blah blah from 6am to 7am 4/5/2012");
    	System.out.println(StorageManager.saveFile());
    	
    	if (abc[0]!=null)
    	{
    	System.out.println(abc[0].getName());
    	System.out.println(abc[0].getTaskId());
    	}
    	
    	for (int i=0;i<StorageManager.getAllTasks().length;i++)
		{
    		if (StorageManager.getAllTasks()[i].getStartDateTime()!=null)
    			{
    			logger.debug(StorageManager.getAllTasks()[i].getStartDateTime());
    			logger.debug(StorageManager.getAllTasks()[i].getStartDateTime().getDate().getTimeMilli());
    			logger.debug(StorageManager.getAllTasks()[i].getStartDateTime().formattedToString());
    			}
    		else
    			logger.debug(StorageManager.getAllTasks()[i].getEndDateTime().getDate());
		}
    	
    
    	*/
    	//command="delete";
    	Task[] xyz=JIDLogic.executeCommand("edit meet");
    	if (xyz!=null)
    	logger.debug("printing search"+ xyz.length);
    	else
    		logger.debug("No Search results");
    	if (xyz!=null)
    	{
    		for (int i=0;i<xyz.length;i++)
    		{
    			System.out.println(xyz[i].getTaskId());
    		}
    	}
    	
    	//logger.debug(StorageManager.saveFile());
    	Task efg[]=JIDLogic.executeCommand("edit $$__03-05-2013154500J__$$" );
    	
    	
    	
    	
    	if (efg!=null)
    	{
    		for (int i=0;i<efg.length;i++)
    		{
    			logger.debug(efg[i].getCompleted());
    		}
    	}
    	
    	
    	Task bgh[]=JIDLogic.executeCommand("edit go to ramon weekly by 3.45pm 3/5/2013  @work @home");
    	
    	
    	def=JIDLogic.executeCommand("find *.*");
    	if (def!=null)
    	{
    		for (int i=0;i<def.length;i++)
    		{
    			logger.debug(def[i].getName());
    		}
    	}/*
    	command="undo";
    	def=executeCommand("undo");
    	logger.debug("Executing undo");
    	if (def!=null)
    	{
    		for (int i=0;i<def.length;i++)
    		{
    			logger.debug(def[i].getName());
    			logger.debug(def[i].getCompleted());
    		}
    	}*/
    	logger.debug(StorageManager.saveFile());
    	def=JIDLogic.executeCommand("find *.*");
    	if (def!=null)
    	{
    		for (int i=0;i<def.length;i++)
    		{
    			System.out.println(def[i].getTaskId());
    		}
    	}
		//JIDLogic_init();
		//UIController ui=new UIController();
		
		
	
}
	
}
