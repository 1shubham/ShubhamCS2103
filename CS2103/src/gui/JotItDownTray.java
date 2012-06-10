package gui;


import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import data.Task;

import logic.JIDLogic;


/**
 * This class is for managing the tray
 * @author Ramon
 *
 */
public class JotItDownTray {
	static SystemTray tray = SystemTray.getSystemTray();
	PopupMenu popup;
	Image trayImg;
	TrayIcon trayIcon;
	
	/**
	 * constructor
	 */
	JotItDownTray() {
		getSystemTray();
		addPopupMenu();
		addTrayIcon();
		showText("JotItDown!", "JID is running!");
	}
	
	/**
	 * initialize tray from system tray
	 */
	private void getSystemTray() {
		tray = SystemTray.getSystemTray();
		trayImg = Resource.trayImage;

	}

	/**
	 * add menu to tray
	 */
	private void addPopupMenu() {
		popup = new PopupMenu();
		
		MenuItem mItem1 = new MenuItem("Exit");
		mItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				JIDLogic.JIDLogic_close();
				System.exit(0);
			}
		});	
		
		MenuItem mItem2 = new MenuItem("Add from clipboard");
		mItem2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addTaskFromTray();
			}
		});
		
		
		popup.add(mItem2);
		popup.add(mItem1);

	}

	/**
	 * add icon to tray with its action
	 */
	private void addTrayIcon() {
		trayIcon = new TrayIcon(trayImg, "Jot It Down!", popup);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println("Problem loading Tray icon");
		}
		
		trayIcon.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//mainJFrame.setVisible(true);
			    SwingUtilities.invokeLater(
	    	       new Runnable() {
					@Override
					public void run() {
						if(!UIController.isWindowVisible())
							UIController.showWindow();
		   			}
	    	       });
			}
		});
	}
	
	private void addTaskFromTray() {
		String input = UIController.getClipboard();
		
		JIDLogic.setCommand("add");
		Task[] tasks = JIDLogic.executeCommand("add "+ input);
		
		if(tasks == null)
			showText("Error!", "invalid input format");
		else {
			showText("Successfully added!", tasks[0].toString());
			UIController.refresh();
		}
	}
	
	/**
	 * 
	 * @return SystemTray tray
	 */
	public SystemTray getTray() {
		return tray;
	}
	
	/**
	 * show message at the system tray
	 * @param caption header
	 * @param text description
	 */
	public void showText(String caption, String text) {
		trayIcon.displayMessage(caption, text, MessageType.NONE);
	}
	
}
