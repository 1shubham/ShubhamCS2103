package jotItDownLogic;

import storage.*;
import java.util.Date;

public class JotItDownLogic {
	
	public static boolean addTask(Storage tasks, String TaskDescription, boolean isClashedAllowed) {
		return true;
	}
	
	public static boolean deleteTask(Storage tasks, String TaskDescription) {
		return true;
	}
	
	public static boolean modify(Storage tasks, String TaskDescription) {
		return true;
	}
	
	/**find time suggestion
	 *
	 * @param tasks
	 * @return the best time slot
	 */
	public static Date findSuggestion(Storage tasks) {
		return null;
	}
	
	public static Storage search(Storage tasks, String s) {
		return null;
	}
	
	public static boolean setImportant(Storage tasks, String TaskDescription) {
		return true;
	}
	
	public static boolean setComplete (Storage tasks, String TaskDescription) {
		return true;
	}
	
	public static Storage listImportant( Storage tasks) {
		return null;
	}
	
	public static Task findRecent( Storage tasks) {
		return null;
	}
	/**This function run first each time we open the program.
	* Please help me think of the better function name.
	* load all tasks into Storage and update all the data e.g. add done to those that have already expired, (for V0.2, we may "happy belated birthday" )
	*/
	public static void startApp(Storage taks) {
		
	}
	
	/**
	 * for the logic API part to write back to file or anything that should be done when closing the application.
	 * 
	 */
	public static void closeApp() {
		
	}
}
