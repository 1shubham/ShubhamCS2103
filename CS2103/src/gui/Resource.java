package gui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Resource {
	
    public final static ImageIcon bigLogo = createImageIcon("images/logo.png","");
	public final static ImageIcon exitImg = createImageIcon("images/exit_.png","");
	public final static ImageIcon exitOn = createImageIcon("Images/exit_On.png","");
	public final static ImageIcon minimizeImg = createImageIcon("images/min_.png", "");
	public final static ImageIcon minimizeImgOn = createImageIcon("Images/min_on.png", "");
	public final static ImageIcon helpImg = createImageIcon("images/help_.png","");
	public final static ImageIcon helpImgOn = createImageIcon("Images/help_on.png","");
	public final static ImageIcon trayLogo = createImageIcon("Images/trayLogo.png","");
	
	public final static ImageIcon topPopUpBG = createImageIcon("Images/topPopUpBG.png", "");
	
	public final static ImageIcon backgroundLogo = createImageIcon("Images/smallBG.png","");
	
	public final static ImageIcon down = createImageIcon("Images/down.png","");
	public final static ImageIcon downOn = createImageIcon("Images/downOn.png","");
    public final static ImageIcon downPress = createImageIcon("Images/downPress.png","");
    
    public final static ImageIcon up = createImageIcon("Images/up.png", "");
    public final static ImageIcon upOn = createImageIcon("Images/upOn.png", "");
    public final static ImageIcon upPress = createImageIcon("Images/upPress.png", "");
    
    public final static ImageIcon loginBG = createImageIcon("Images/logInBG2.png", "");
    public final static ImageIcon largeBG = createImageIcon("Images/largeBG2.png", "");
    public final static ImageIcon smallBG = createImageIcon("Images/smallBG2.png", "");
    public final static ImageIcon alarmBG = createImageIcon("Images/reminderBG.png", "");
    public final static ImageIcon helpBG = createImageIcon("Images/help.png", "");
    
	public final static Image trayImage = iconToImage(trayLogo); 
	public final static Image backgroundImage = iconToImage(backgroundLogo);
	public final static Image loginBGImage = iconToImage(loginBG);
	
	public final static ImageIcon starImportant = createImageIcon("Images/star_important.png","");
	public final static ImageIcon starUnimportant = createImageIcon("Images/star_unimportant.png","");
	
	public final static URL alarmSoundURL = Resource.class.getResource("alarm.wav");
	//public final static BufferedImage backgroundBuffered = BufferedImageBuilder.bufferImage(backgroundImage);
	
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
