package gui;

import java.awt.SystemTray;



import data.Task;

public class Reminder {
	
	SystemTray tray;
	int timeLeft;
	Task task;

	public Reminder(SystemTray tray) {
		this.tray = tray;
		
		getLatestTask();
		// swingworker
		// check every second for new task coming.
	}

	private void getLatestTask() {
		//JIDLogic.setCommand("find");
		//task = JIDLogic.executeCommand("find *.*")[0];
	}

}
