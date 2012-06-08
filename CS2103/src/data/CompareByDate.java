package data;

import java.util.Comparator;

import org.apache.log4j.Logger;
public class CompareByDate implements Comparator<Task>{

	private static Logger logger=Logger.getLogger(CompareByDate.class);
	@Override
	public int compare(Task o1, Task o2) {
		// TODO Auto-generated method stub
				
		if (o1.getStart()!=null && o2.getStart()!=null)
		{
			long diff = o1.getStart().getTimeMilli() - o2.getStart().getTimeMilli();
			if(o1.getName().contains("nirav"))
			{
				logger.warn("NIRAV GETTING COMPARED WITH "+o2.toString()+" "+diff);
				
			}
			if(diff < 0)
				return -1;
			else if(diff == 0)
				return 0;
			else
				return 1;
		
			
		}
		else if (o1.getStart()==null && o2.getStart()!=null)
		{
			if (o1.getEnd()!=null)
			{
				long diff = o1.getEnd().getTimeMilli() - o2.getStart().getTimeMilli();
				if(o1.getName().contains("nirav"))
				{
					logger.warn("NIRAV GETTING COMPARED WITH "+o2.toString()+" "+diff);
					
				}
				if(diff < 0)
					return -1;
				else if(diff == 0)
					return 0;
				else
					return 1;
			}
			
		}
		else if (o1.getStart()!=null && o2.getStart()==null)
		{
			if (o2.getEnd()!=null)
			{
				long diff = o1.getStart().getTimeMilli() - o2.getEnd().getTimeMilli();
				if(o1.getName().contains("nirav"))
				{
					logger.warn("NIRAV GETTING COMPARED WITH "+o2.toString()+" "+diff);
					
				}
				if(diff < 0)
					return -1;
				else if(diff == 0)
					return 0;
				else
					return 1;
			}
				
		}
		else
		{
			
			long diff = o1.getEnd().getTimeMilli() - o2.getEnd().getTimeMilli();
			if(o1.getName().contains("nirav"))
			{
				logger.warn("NIRAV GETTING COMPARED WITH "+o2.toString()+" "+diff);
			}
			if(diff < 0)
				return -1;
			else if(diff == 0)
				return 0;
			else
				return 1;
				
					
			}
		logger.fatal("going mad");
		return 0;


	}
	
}
