package gui.mainWindow.extended;

import gui.Resource;
import gui.mainWindow.MainJFrame;

import java.awt.Color;
import java.awt.Point;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * showing user manual
 * @author Ramon
 *
 */
public class HelpFrame extends JFrame{
	
	/**
	 * this class constructor
	 */
	private HelpFrame() {
		initComponents();
	}

	/**
	 * initialize everything in JFrame
	 */
	private void initComponents() {
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		this.setFocusableWindowState(false);
		
        jLayeredPane = new JLayeredPane();
        bgLabel = new JLabel();
        exitLabel = new JLabel();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setIconImages(null);
        
        /*
        exitLabel.setIcon(Resource.exitImg);
        exitLabel.setBounds(165, 5, 20, 20);
        exitLabel.addMouseListener(new ExitListener());
        jLayeredPane.add(exitLabel, JLayeredPane.DEFAULT_LAYER);
        */
        
        bgLabel.setIcon(Resource.helpBG);
        bgLabel.setBounds(0, 0, 200, 300);
        jLayeredPane.add(bgLabel, JLayeredPane.DEFAULT_LAYER);
        
        this.add(jLayeredPane);
        this.setSize(200,300);
	}
	
	private static JLayeredPane jLayeredPane;
	private static JLabel bgLabel;
	private static JLabel exitLabel;
	private static HelpFrame helpFrame;
	private static boolean shown;
	
	public static void main(String[] args) {
		HelpFrame.initialize();
		HelpFrame.showHelp();
	}
	
	/**
	 * set position of this frame
	 * @param x x-coordinate of the screen
	 * @param y y-coordinate of the screen
	 */
	public static void setPosition(int x, int y){
		helpFrame.setLocation(x, y);
	}
	
	/**
	 * check whether it is shown or not
	 * @return true if shown
	 */
	public static boolean isShown() {
		return shown;
	}
	
	/**
	 * make help window visible
	 */
	public static void showHelp() {
		helpFrame.setVisible(true);
		helpFrame.toFront();
		shown = true;
	}
	
	/**
	 * make help window invisible
	 */
	public static void hideHelp() {
		helpFrame.setVisible(false);
		shown = false;
	}
	
	/**
	 * make help window invisible temporalily
	 */
	public static void hideHelpTempolarily() {
		helpFrame.setVisible(false);
	}
	
	/**
	 * initialize help frame
	 */
	public static void initialize() {
		if(helpFrame== null)
			helpFrame = new HelpFrame();
	}
	
	/**
	 * set position according to mainJFrame direction.
	 */
	public static void setPosition() {
		helpFrame.setLocation(MainJFrame.currentLocation.x + 410, MainJFrame.currentLocation.y-10);
	}
	
	/**
	 * 
	 * @return current location
	 */
	public static Point getPosition() {
		return helpFrame.getLocation();
	}
	
	/**
	 * MouseListener for exit button
	 * @author Ramon
	 *
	 */
	class ExitListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			HelpFrame.this.setVisible(false);
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			exitLabel.setIcon(Resource.exitOn);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			exitLabel.setIcon(Resource.exitImg);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
	
	/**
	 * toggle help frame to be shown/ hidden
	 */
	public static void toggleShown() {
		if(isShown())
			hideHelp();
		else
			showHelp();
	}
}
