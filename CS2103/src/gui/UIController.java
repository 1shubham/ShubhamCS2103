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

public class UIController {
	static MainJFrame mainJFrame = new MainJFrame();
	Reminder reminder;
	static SystemTray tray = SystemTray.getSystemTray();
	
	
	public UIController() {
		initializeTray();
		Reminder reminder = new Reminder(tray);
	}
	
	
	public static void main(String[] args) {
		//MainJFrame mainJFrame = new MainJFrame();
		new UIController();
		//initializeTray();
		//Reminder reminder = new Reminder(tray);
	}


	private static void initializeTray() {
		// TODO Auto-generated method stub
		tray = SystemTray.getSystemTray();
		Image img = Resource.trayImage;
		//Image img = Toolkit.getDefaultToolkit().getImage( "D:\\JAVAworkspace2\\GUITemp\\bin\\gui\\trayLogo.png");
		
		
		PopupMenu popup = new PopupMenu();
		
		MenuItem mItem1 = new MenuItem("Exit");
		mItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
				
			}
		});	
		
		MenuItem mItem2 = new MenuItem("Add from clipboard");
		mItem2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub]
				String input = getClipboard();
				if(!mainJFrame.isVisible())
					mainJFrame.showFrame();
				mainJFrame.setInputText("add "+input);
			}
		});
		
		
		popup.add(mItem2);
		popup.add(mItem1);
		
		TrayIcon trayIcon = new TrayIcon(img, "Tray Demo", popup);
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
						if(!mainJFrame.isVisible()) {
							mainJFrame.showFrame();
						}
		   			}
	    	       });
			}
		});
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
	
}
