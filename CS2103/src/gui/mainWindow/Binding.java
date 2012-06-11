package gui.mainWindow;


import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import logic.JIDLogic;

import org.apache.log4j.*;

/**
 * for making hotkey
 * @author Ramon
 *
 */
public class Binding {
	
	private static Logger logger=Logger.getLogger(JIDLogic.class);
	
	InputMap inputMap;
	ActionMap actionMap;
	JFrame jFrame;
	
	/**
	 * constructor
	 * @param jframe the focused window
	 * @param inputMap inputMap from that window
	 * @param actionMap actionMap from that window
	 */
	Binding(JFrame jframe, InputMap inputMap, ActionMap actionMap) {
		this.jFrame = jFrame;
		this.inputMap = inputMap;
		this.actionMap = actionMap;
		addKeyBinding();
	}
	
	/**
	 * mapping hot keys
	 */
	private void addKeyBinding() {
	       KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
	        inputMap.put(key, "undo");
	        actionMap.put("undo", new Action.UndoAction());


	        key = KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK);
	        inputMap.put(key, "completed");
	        actionMap.put("completed", new Action.CompletedAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK);
	        inputMap.put(key, "delete");
	        actionMap.put("delete", new Action.DeleteAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
	        inputMap.put(key, "delete");
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK);
	        inputMap.put(key, "important");
	        actionMap.put("important", new Action.ImportantAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK);
	        inputMap.put(key, "exit");
	        actionMap.put("exit", new Action.ExitAction());
	
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK);
	        inputMap.put(key, "expand");
	        actionMap.put("expand", new Action.ExpandAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
	        inputMap.put(key, "redo");
	        actionMap.put("redo", new Action.RedoAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK);
	        inputMap.put(key, "list");
	        actionMap.put("list", new Action.ListAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
	        inputMap.put(key, "overdue");
	        actionMap.put("overdue", new Action.OverdueAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK);
	        inputMap.put(key, "gCalendar");
	        actionMap.put("gCalendar", new Action.GCalendarAction());
	        
	        key = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
	        inputMap.put(key, "help");
	        actionMap.put("help", new Action.HelpAction());
	}
	

}
