package storagecontroller;

import data.Task;
import data.TaskHashMap;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
public class FileHandler 
{
	
	private Logger logger=Logger.getLogger("Abc");
	private static String fileName;
	public FileHandler(String name)
	{
		fileName=name;
	}
	public boolean writeToFile(TaskHashMap instance) 
	{
		try
		{
		BufferedOutputStream xmlOut=new BufferedOutputStream(new FileOutputStream(fileName));
		XMLEncoder writeToXml=new XMLEncoder(xmlOut);
		logger.debug(instance.getKeySet().size());
		for(String key: instance.getKeySet())
		{
			writeToXml.writeObject(instance.getTaskById(key));
		}
		writeToXml.close();
		return true;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found!");
			return false;
		}
	}
	
	
	
	public boolean readFromFile(TaskHashMap instance) 
	{
		try
		{
		BufferedInputStream xmlIn=new BufferedInputStream(new FileInputStream(fileName));
		XMLDecoder readFromXml=new XMLDecoder(xmlIn);
		
			Task obj;
			while((obj = (Task) readFromXml.readObject())!=null)
			{
				instance.addTaskById(obj);
			}
			readFromXml.close();
			logger.debug(instance.getKeySet().size());
			logger.debug("abc");
		}
		catch(FileNotFoundException e)
		{
			logger.debug("File Not Found!");
			return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			logger.debug("array out of bounds!");
			return false;
		}
		catch(NullPointerException e)
		{
			logger.debug("null pointer exception");
			return false;
		}
		return true;
	}
}
