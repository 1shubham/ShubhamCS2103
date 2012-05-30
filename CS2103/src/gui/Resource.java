package gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Resource {
	/*public final Icon bigLogo = new ImageIcon( getClass().getResource("logo.png"));
	public final Icon exitImg = new ImageIcon( getClass().getResource("exit.png"));
	public final Icon exitOn = new ImageIcon( getClass().getResource("exitOn.png"));
	public final Icon trayLogo = new ImageIcon( getClass().getResource("trayLogo.png"));
	public final Image trayImage = iconToImage(trayLogo);
	*/
	/*
	public final Icon down = new ImageIcon( getClass().getResource("down.png"));
	public final Icon downOn = new ImageIcon( getClass().getResource("downOn.png"));
	public final Icon downPress = new ImageIcon( getClass().getResource("downPress.png"));
	*/
    public static ImageIcon bigLogo = createImageIcon("images/logo.png","");
	public static ImageIcon exitImg = createImageIcon("images/exit.png","");
	public static ImageIcon exitOn = createImageIcon("Images/exitOn.png","");
	public static ImageIcon trayLogo = createImageIcon("Images/trayLogo.png","");
	public static Image trayImage = iconToImage(trayLogo); 
	
	public static ImageIcon down = createImageIcon("Images/down.png","");
	public static ImageIcon downOn = createImageIcon("Images/downOn.png","");
    public static ImageIcon downPress = createImageIcon("Images/downPress.png","");
	
	static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon)icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = 
              GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
	
    protected static ImageIcon createImageIcon(String path,
            String description) {
		java.net.URL imgURL = Resource.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
    }
    
}
