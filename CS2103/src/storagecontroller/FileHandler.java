package storagecontroller;
import data.DateTime;
import data.Task;
import data.TaskHashMap;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
public class FileHandler 
{
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
			while((obj=(Task)readFromXml.readObject())!=null)
			{
				instance.addTaskById(obj);
			}
			readFromXml.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File Not Found!");
			return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("array out of bounds!");
			return false;
		}
		catch(NullPointerException e)
		{
			System.out.println("null pointer exception");
			return false;
		}
		return true;
	}
}
