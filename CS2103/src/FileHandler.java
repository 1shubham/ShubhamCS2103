package Storage;
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
	public boolean writeToFile(TaskHashMap instance) throws FileNotFoundException
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
	public void readFromFile(TaskHashMap instance) throws FileNotFoundException, ArrayIndexOutOfBoundsException
	{
		BufferedInputStream xmlIn=new BufferedInputStream(new FileInputStream(fileName));
		XMLDecoder readFromXml=new XMLDecoder(xmlIn);
		try
		{
			Task obj;
			while((obj=(Task)readFromXml.readObject())!=null)
			{
				instance.addTaskById(obj);
			}
			readFromXml.close();
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			
		}
		catch(NullPointerException e)
		{
			
		}
	}
}
