package gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import logic.JIDLogic;

public class UIController {
	static MainJFrame mainJFrame;
	Reminder reminder;
	static JotItDownTray JIDtray;
	
	public UIController() {
		TopPopUp.createTopPopUp();
		mainJFrame = new MainJFrame();
		
		Timer timer = new Timer(100, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				JIDtray = new JotItDownTray(mainJFrame);
				Reminder reminder = new Reminder(JIDtray.getTray());
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	
	public static void main(String[] args) {
		//MainJFrame mainJFrame = new MainJFrame();
		JIDLogic.JIDLogic_init();
		
		new UIController();
		//initializeTray();
		//Reminder reminder = new Reminder(tray);
	}
	
	public static String getClipboard() {
	    Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

	    try {
	        if (t != null && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	            String text = (String)t.getTransferData(DataFlavor.stringFlavor);
	            return text;
	        }
	    } catch (UnsupportedFlavorException e) {
	    } catch (IOException e) {
	    }
	    return null;
	}
	
	public static void showTopPopUpMsg(String str) {
		mainJFrame.showPopup(str);
	}
	
	public static void showTrayMsg(String caption, String text) {
		JIDtray.showText(caption, text);
	}
	
	public static void refresh() {
		ExpandJPanel.updateJTable();
	}
	
	public static boolean isFrameExpand() {
		return mainJFrame.isExpand();
	}
	
	public static void expandFrame() {
		mainJFrame.expandFrame();
	}
	
	public static void contractFrame() {
		mainJFrame.contractFrame();
	}
}
