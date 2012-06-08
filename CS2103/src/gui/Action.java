package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;

import logic.JIDLogic;
import data.Task;

public class Action {
	
	private static Logger logger=Logger.getLogger(JIDLogic.class);
	
	
	static class ExitAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			JIDLogic.JIDLogic_close();
			System.exit(0);
		}
	}
	
    static class UndoAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
        	logger.debug("*****EXECMD: UNDO*******");
        	JIDLogic.setCommand("UNDO");
        	Task[] task = JIDLogic.executeCommand("UNDO");
        	if(task == null)
        		MainJFrame.showPopup("UNDO unsuccessfully!");
        	else {
        		MainJFrame.showPopup("UNDO: "+task[0].getName());
            	ExpandJPanel.updateJTable();
        	}
        }
    }
	
    static class DeleteAction extends AbstractAction {
    	Task[] task;
        @Override
        public void actionPerformed(ActionEvent e) {
        	Task[] taskList = ExpandJPanel.getTasks();
        	
        	JIDLogic.setCommand("DELETE");
       		logger.debug("******execmd: " + "DELETE " 
        					+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());
       		task = JIDLogic.executeCommand("DELETE " 
       				+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());


        	if(task == null)
        		MainJFrame.showPopup("DELETE unsuccessfully!");
        	else {
        		MainJFrame.showPopup("DELETE: "+task[0].getName());
            	ExpandJPanel.updateJTable();
        	}        	
        }
    }
    
    static class CompletedAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
        	Task[] taskList = ExpandJPanel.getTasks();
        	
        	JIDLogic.setCommand("COMPLETED");
        	logger.debug("COMPLETED "
        			+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());
        	Task[] task = JIDLogic.executeCommand("COMPLETED " 
        			+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());
        	
        	if(task == null)
        		MainJFrame.showPopup("COMPLETED unsuccessfully!");
        	else {
        		MainJFrame.showPopup("COMPLETED: "+task[0].getName());
            	ExpandJPanel.updateJTable();
        	}        	
        }
    }
    
    static class ImportantAction extends AbstractAction {
    	@Override
        public void actionPerformed(ActionEvent e) {
        	Task[] taskList = ExpandJPanel.getTasks();
        	
        	JIDLogic.setCommand("IMPORTANT");
        	logger.debug("IMPORTANT "
        			+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());
        	Task[] task = JIDLogic.executeCommand("IMPORTANT " 
        			+ taskList[ExpandJPanel.jTable1.getSelectedRow()].getTaskId());
        		
        	if(task == null)
        		MainJFrame.showPopup("IMPORTANT unsuccessfully!");
        	else {
        		MainJFrame.showPopup("IMPORTANT: "+task[0].getName());
            	ExpandJPanel.updateJTable();
        	}        	
        }
    }
    
    static class OverdueAction extends AbstractAction {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		JIDLogic.setCommand("overdue");
    		logger.debug("*********exeCmd(inside Action): Overdue");
    		Task[] task = JIDLogic.executeCommand("OVERDUE");
    		
    		logger.debug(task[0].toString());
    		
    		UIController.showTopPopUpMsg(task.length + " task(s) overdue.");
    		ExpandJPanel.updateJTableWithTasks(task);
    	}
    }
    
    static class RedoAction extends AbstractAction {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		JIDLogic.setCommand("redo");
    		logger.debug("******exeCmd(inside Action: Redo");
    		Task[] task = JIDLogic.executeCommand("redo");
    		
        	if(task == null)
        		MainJFrame.showPopup("REDO unsuccessfully!");
        	else {
        		MainJFrame.showPopup("REDO: "+task[0].getName());
            	ExpandJPanel.updateJTable();
        	}
    		
    		UIController.refresh();
    	}
    }
    
    static class ListAction extends AbstractAction {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		UIController.refresh();
    		UIController.expandFrame();
    	}
    }
    
    static class ExpandAction extends AbstractAction {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		if(UIController.isFrameExpand())
    			UIController.contractFrame();
    		else
    			UIController.expandFrame();
    	}
    }
    
    static class HelpAction extends AbstractAction {
    	public void actionPerformed(ActionEvent e) {
    		
    	}
    }
}
