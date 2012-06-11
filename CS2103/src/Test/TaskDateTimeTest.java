package Test;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import data.TaskDateTime;
public class TaskDateTimeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetCurrentDateTime() {
		assertEquals("3:27 PM 03-06-2012",TaskDateTime.getCurrentDateTime().formattedToString());
	}

	@Test
	public final void testGetCurrentDate() {
		assertEquals("03-06-2012",TaskDateTime.getCurrentDate().formattedToString());
	}

	@Test
	public final void testIsEqual() {
		TaskDateTime first=new TaskDateTime(2015,4,3,16,0,0);
		TaskDateTime second=new TaskDateTime(2015,4,3,16,0,0);
		assertEquals(true,first.isEqual(second));
	}

	@Test
	public final void testCompareTo() {
		TaskDateTime first=new TaskDateTime(2015,4,3,16,0,0);
		TaskDateTime second=new TaskDateTime(2015,4,3,16,0,0);
		assertEquals(0,first.compareTo(second));
	}

	@Test
	public final void testFormattedToString() {
		TaskDateTime first=new TaskDateTime(2015,4,3,16,0,0);
		assertEquals("4:00 PM 03-04-2015",first.formattedToString());
	}

	@Test
	public final void testPresentableToString() {
		TaskDateTime first=new TaskDateTime(2015,4,3,16,0,0);
		assertEquals("03 Apr '15 4:00 PM",first.presentableToString());
	}

	@Test
	public final void testGenerateDateCode() {
		TaskDateTime first=new TaskDateTime();
		assertEquals("01-01-2000",first.generateDateCode());
	}

	@Test
	public final void testGenerateTimeCode() {
		TaskDateTime first = new TaskDateTime();
		assertEquals("000000",first.generateTimeCode());
	}
	@Test
	public final void get()
	{
		TaskDateTime first=new TaskDateTime(2015,4,3,23,0,0);
		assertEquals(23,first.get(Calendar.HOUR_OF_DAY));
	}

}