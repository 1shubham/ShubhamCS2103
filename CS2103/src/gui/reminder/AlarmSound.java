package gui.reminder;



import gui.Resource;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

/**
 * sound for alarm function
 * @author Ramon
 *
 */
public class AlarmSound
{
	static Clip clip;
	
	/**
	 * start /stop music
	 * @param value true = start, false = stop
	 */
	public static void music(boolean value)
	{
		if(clip == null)
			createMusic();
		
		if(value) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		}
		else 
			clip.stop();
	}
	
	/**
	 * initialize music
	 */
	private static void createMusic() {
		AudioInputStream audio;         
		try
		{
			clip=AudioSystem.getClip();
			
			audio=AudioSystem.getAudioInputStream(Resource.alarmSoundURL);
			clip.open(audio);

		}
		catch(IOException error)
		{
			System.out.println("File Not Found");
		}
		catch(UnsupportedAudioFileException e)
		{}
		catch(LineUnavailableException e2)
		{}
	}
}
