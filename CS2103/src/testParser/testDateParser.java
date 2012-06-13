package testParser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import parser.DateParser;

public class testDateParser {
	DateParser testObj;
	int[] validDay;
	int[] invalidDay;
	int[] validMonDigit;
	int[] invalidMonDigit;
	String[] validMonText;
	String[] invalidMonText;
	int[] validYear;
	int[] invalidYear;
	String[] validTextDay;
	String[] invalidTextDay;
	String SLASH ="/";
	String DASH ="-";
	String COMMA =",";
	String SPACE =" ";
	String ST = "st";
	String ND = "ND";
	String RD = "rD";
	String TH = "Th";
	
	String[] validDates;
	String[] invalidDates;
	String[] specialValidDates;
	String[] specialInvalidDates;
	
	@Before
	public void setUp() throws Exception {
		testObj = new DateParser();
		validDay = new int[] {1,01,28,20/*,29,30,31*/};
		invalidDay = new int[] {32,34,90,0};
		validMonDigit = new int[] {1,2,3,4,5,6,7,8,9,10,11,12};
		invalidMonDigit = new int[] {13,0};
		validYear = new int[] {2099,2013};
		invalidYear = new int[] {1899,2100,232324,2332,23,0};
		validMonText = new String[] {"January","Jan","February","Feb","March","Mar","April","Apr","May","June","Jun","July","Jul","August","Aug","September","Sep","October","Oct","November","Nov","December","Dec"};
		invalidMonText = new String[] {"fsdfsd","jenau","oh you","sdfs"};
		validTextDay = new String[] {"monday","tuesday","sun","SUN","wed","Wed","weD","next monday","tmr","tomorrow","today"};
		invalidTextDay = new String[] {"vzc","tues","sxcvx","next today"};
		specialValidDates = new String[] {"29th feb 2012","29 february 2012"};
		specialInvalidDates = new String[] {"30st jan 2012","29th feb 2013","30th feb","31st feb","31 april 2012","31 jun 2012","31 november 2012","31 september 2012"};
		
		ArrayList<String> validDatesList = new ArrayList<String>();
		for (int i=0; i<validDay.length; i++) {
			for (int j=0; j<validMonDigit.length; j++) {
				validDatesList.add(validDay[i]+SLASH+validMonDigit[j]);
				validDatesList.add(validDay[i]+DASH+validMonDigit[j]);
				validDatesList.add(validDay[i]+SPACE+validMonDigit[j]);
				for (int k=0; k<validYear.length; k++) {
					validDatesList.add(validDay[i]+SLASH+validMonDigit[j]+SLASH+validYear[k]);
					validDatesList.add(validDay[i]+DASH+validMonDigit[j]+DASH+validYear[k]);
					validDatesList.add(validDay[i]+SPACE+validMonDigit[j]+SPACE+validYear[k]);
				}
			}
			for (int j=0; j<validMonText.length; j++) {
				validDatesList.add(validDay[i]+SLASH+validMonText[j]);
				validDatesList.add(validDay[i]+DASH+validMonText[j]);
				validDatesList.add(validDay[i]+SPACE+validMonText[j]);
				for (int k=0; k<validYear.length; k++) {
					validDatesList.add(validDay[i]+SLASH+validMonText[j]+SLASH+validYear[k]);
					validDatesList.add(validDay[i]+DASH+validMonText[j]+DASH+validYear[k]);
					validDatesList.add(validDay[i]+SPACE+validMonText[j]+SPACE+validYear[k]);
				}
			}
		}
		for (int i=0; i<validTextDay.length; i++) 
			validDatesList.add(validTextDay[i]);
		for (int i=0; i<specialValidDates.length; i++) 
			validDatesList.add(specialValidDates[i]);
		validDates = validDatesList.toArray(new String[validDatesList.size()]);
		
		
		
		ArrayList<String> invalidDatesList = new ArrayList<String>();
		for (int i=0; i<invalidDay.length; i++) {
			for (int j=0; j<invalidMonDigit.length; j++) {
				invalidDatesList.add(invalidDay[i]+SLASH+invalidMonDigit[j]);
				invalidDatesList.add(invalidDay[i]+DASH+invalidMonDigit[j]);
				invalidDatesList.add(invalidDay[i]+SPACE+invalidMonDigit[j]);
				for (int k=0; k<invalidYear.length; k++) {
					invalidDatesList.add(invalidDay[i]+SLASH+invalidMonDigit[j]+SLASH+invalidYear[k]);
					invalidDatesList.add(invalidDay[i]+DASH+invalidMonDigit[j]+DASH+invalidYear[k]);
					invalidDatesList.add(invalidDay[i]+SPACE+invalidMonDigit[j]+SPACE+invalidYear[k]);
				}
			}
			for (int j=0; j<invalidMonText.length; j++) {
				invalidDatesList.add(invalidDay[i]+SLASH+invalidMonText[j]);
				invalidDatesList.add(invalidDay[i]+DASH+invalidMonText[j]);
				invalidDatesList.add(invalidDay[i]+SPACE+invalidMonText[j]);
				for (int k=0; k<invalidYear.length; k++) {
					invalidDatesList.add(invalidDay[i]+SLASH+invalidMonText[j]+SLASH+invalidYear[k]);
					invalidDatesList.add(invalidDay[i]+DASH+invalidMonText[j]+DASH+invalidYear[k]);
					invalidDatesList.add(invalidDay[i]+SPACE+invalidMonText[j]+SPACE+invalidYear[k]);
				}
			}
		}
		for (int i=0; i<invalidTextDay.length; i++) 
			invalidDatesList.add(invalidTextDay[i]);
		for (int i=0; i<specialInvalidDates.length; i++) 
			invalidDatesList.add(specialInvalidDates[i]);
		invalidDates = invalidDatesList.toArray(new String[invalidDatesList.size()]);
	

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetStartDate() {
		int[]dummy = new int[] {-1,-1,-1};
		
		assertArrayEquals(dummy, testObj.getStartDate());
		dummy = new int[] {12,3,2012};
		testObj.setStartDate("12th march 2012");
		assertArrayEquals(dummy, testObj.getStartDate());
	}

	@Test
	public final void testGetEndDate() {
		int[]dummy = new int[] {-1,-1,-1};
		
		assertArrayEquals(dummy, testObj.getEndDate());
		dummy = new int[] {12,3,2012};
		testObj.setEndDate("12th march 2012");
		assertArrayEquals(dummy, testObj.getEndDate());
	}
	
	@Test
	public final void testSetStartDate() {
		for (int i=0; i<validDates.length; i++) 
			assertTrue (testObj.setStartDate(validDates[i]));
		
		for (int i=0; i<invalidDates.length; i++)
			assertFalse (testObj.setStartDate(invalidDates[i]));
		
		assertFalse (testObj.setStartDate(null));
		assertFalse (testObj.setStartDate(""));
	}
	
	@Test
	public final void testSetEndDate() {
		for (int i=0; i<validDates.length; i++)
			assertTrue (testObj.setEndDate(validDates[i]));
	
		for (int i=0; i<invalidDates.length; i++)
			assertFalse (testObj.setEndDate(invalidDates[i]));
		
		assertFalse (testObj.setEndDate(null));
		assertFalse (testObj.setEndDate(""));
	}

	@Test
	public final void testIsValidGeneral() {
		assertTrue(testObj.isValidGeneral("12th march"));
	}

}
