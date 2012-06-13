package testParser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import parser.TimeParser;

public class testTimeParser {
	TimeParser testObj;
	String[] validTestMins;
	String[] invalidTestMins;
	String[] validTest12Hours;
	String[] invalidTest12Hours;
	String[] validTest24Hours;
	String[] invalidTest24Hours;
	String COLON =":";
	String DOT =".";
	String SPACE =" ";
	String[] AM;
	String[] PM;
	String[] HRS;
	String[] valid12Times;
	String[] invalid12Times;
	String[] valid24Times;
	String[] invalid24Times;
	
	@Before
	public void setUp() throws Exception {
		testObj = new TimeParser();
		
		validTestMins = new String[] {"00","01","12","59"};
		invalidTestMins = new String[] {"590","61","60",null};
		
		validTest12Hours = new String[] {"12","11","9","09","1"};
		invalidTest12Hours = new String[] {"13","20","0",null};
		
		validTest24Hours = new String[] {"00","0","1","01","9","10","19","20","23"};
		invalidTest24Hours = new String[] {"24","30","40",null};
		
		AM = new String[] {"am", "aM", "AM", "Am"};
		PM = new String[] {"pm", "pM", "PM", "Pm"};
		HRS = new String[] {"hours","HOURS","hrs","HRS","hour","HOURS"};
		
		String temp1,temp2,temp3,temp4;
		
		ArrayList<String> validTimes12List = new ArrayList<String>();
		for (int i=0; i<validTest12Hours.length; i++) {
			for (int k=0; k<AM.length; k++) {
				validTimes12List.add(validTest12Hours[i]+AM[k]);
				validTimes12List.add(validTest12Hours[i]+SPACE+AM[k]);
			}
			for (int k=0; k<PM.length; k++) {
				validTimes12List.add(validTest12Hours[i]+PM[k]);
				validTimes12List.add(validTest12Hours[i]+SPACE+PM[k]);
			}
			for (int j=0; j<validTestMins.length; j++) {
				temp1 = validTest12Hours[i]+COLON;
				temp2 = validTest12Hours[i]+DOT;
				
				for (int k=0; k<AM.length; k++) {
					validTimes12List.add(temp1+validTestMins[j]+AM[k]);
					validTimes12List.add(temp2+validTestMins[j]+AM[k]);
					validTimes12List.add(temp1+validTestMins[j]+SPACE+AM[k]);
					validTimes12List.add(temp2+validTestMins[j]+SPACE+AM[k]);
				}
				for (int k=0; k<PM.length; k++) {
					validTimes12List.add(temp1+validTestMins[j]+PM[k]);
					validTimes12List.add(temp2+validTestMins[j]+PM[k]);
					validTimes12List.add(temp1+validTestMins[j]+SPACE+PM[k]);
					validTimes12List.add(temp2+validTestMins[j]+SPACE+PM[k]);
				}
			}
		}
		valid12Times = validTimes12List.toArray(new String[validTimes12List.size()]);
		
		ArrayList<String> validTimes24List = new ArrayList<String>();
		for (int i=0; i<validTest24Hours.length; i++) {
			temp1 = validTest24Hours[i]+COLON;
			temp2 = validTest24Hours[i]+DOT;
			for (int j=0; j<validTestMins.length; j++) {
				validTimes24List.add(temp1+validTestMins[j]);
				validTimes24List.add(temp2+validTestMins[j]);
				for (int k=0; k<HRS.length; k++) {
					validTimes24List.add(temp1+validTestMins[j]+HRS[k]);
					validTimes24List.add(temp1+validTestMins[j]+SPACE+HRS[k]);
					validTimes24List.add(temp2+validTestMins[j]+HRS[k]);
					validTimes24List.add(temp2+validTestMins[j]+SPACE+HRS[k]);
				}
			}
		}
		valid24Times = validTimes24List.toArray(new String[validTimes24List.size()]);
		
		ArrayList<String> invalidTimes12List = new ArrayList<String>();
		for (int i=0; i<invalidTest12Hours.length; i++) {
			for (int k=0; k<AM.length; k++) {
				invalidTimes12List.add(invalidTest12Hours[i]+AM[k]);
				invalidTimes12List.add(invalidTest12Hours[i]+SPACE+AM[k]);
			}
			for (int k=0; k<PM.length; k++) {
				invalidTimes12List.add(invalidTest12Hours[i]+PM[k]);
				invalidTimes12List.add(invalidTest12Hours[i]+SPACE+PM[k]);
			}
			for (int j=0; j<invalidTestMins.length; j++) {
				temp1 = invalidTest12Hours[i]+COLON;
				temp2 = invalidTest12Hours[i]+DOT;
				
				for (int k=0; k<AM.length; k++) {
					invalidTimes12List.add(temp1+invalidTestMins[j]+AM[k]);
					invalidTimes12List.add(temp2+invalidTestMins[j]+AM[k]);
					invalidTimes12List.add(temp1+invalidTestMins[j]+SPACE+AM[k]);
					invalidTimes12List.add(temp2+invalidTestMins[j]+SPACE+AM[k]);
				}
				for (int k=0; k<PM.length; k++) {
					invalidTimes12List.add(temp1+invalidTestMins[j]+PM[k]);
					invalidTimes12List.add(temp2+invalidTestMins[j]+PM[k]);
					invalidTimes12List.add(temp1+invalidTestMins[j]+SPACE+PM[k]);
					invalidTimes12List.add(temp2+invalidTestMins[j]+SPACE+PM[k]);
				}
			}
		}
		invalid12Times = invalidTimes12List.toArray(new String[invalidTimes12List.size()]);
		
		ArrayList<String> invalidTimes24List = new ArrayList<String>();
		for (int i=0; i<invalidTest24Hours.length; i++) {
			temp1 = invalidTest24Hours[i]+COLON;
			temp2 = invalidTest24Hours[i]+DOT;
			for (int j=0; j<invalidTestMins.length; j++) {
				invalidTimes24List.add(temp1+invalidTestMins[j]);
				invalidTimes24List.add(temp2+invalidTestMins[j]);
				for (int k=0; k<HRS.length; k++) {
					invalidTimes24List.add(temp1+invalidTestMins[j]+HRS[k]);
					invalidTimes24List.add(temp1+invalidTestMins[j]+SPACE+HRS[k]);
					invalidTimes24List.add(temp2+invalidTestMins[j]+HRS[k]);
					invalidTimes24List.add(temp2+invalidTestMins[j]+SPACE+HRS[k]);
				}
			}
		}
		invalid24Times = invalidTimes24List.toArray(new String[invalidTimes24List.size()]);
	}

	@After
	public void tearDown() throws Exception {
	}
	/*
	@Test
	public final void testGetGeneralPattern() {
		
		final String TIME_12_PATTERN = "(1[012]|0?[1-9])([:.][0-5][0-9])?(\\s)?(?i)(am|pm)"; 
		final String TIME_24_PATTERN = "(2[0-3]|[01]?[0-9])[:.]([0-5][0-9])([ ]?(?i)(hours|hour|hrs|hr))?";
		final String GENERAL_TIME_PATTERN = "("+TIME_12_PATTERN+")|("+TIME_24_PATTERN+")";
		
		assertEquals(GENERAL_TIME_PATTERN,TimeParser.getGeneralPattern());
		fail("Not yet implemented"); // TODO
	}
	*/
	
	@Test
	public final void testSetStartTime() {
		for (int i=0; i<valid12Times.length; i++) {
			assertTrue (testObj.setStartTime(valid12Times[i]));
		}
		
		for (int i=0; i<invalid12Times.length; i++) {
			assertFalse (testObj.setStartTime(invalid12Times[i]));
		}
		
		for (int i=0; i<valid24Times.length; i++) {
			assertTrue (testObj.setStartTime(valid24Times[i]));
		}
		
		for (int i=0; i<invalid24Times.length; i++) {
			assertFalse (testObj.setStartTime(invalid24Times[i]));
		}
		
		assertFalse(testObj.setStartTime(null));
	}
	
	
	@Test
	public final void testSetEndTime() {
		for (int i=0; i<valid12Times.length; i++) {
			assertTrue (testObj.setEndTime(valid12Times[i]));
		}
		
		for (int i=0; i<invalid12Times.length; i++) {
			assertFalse (testObj.setEndTime(invalid12Times[i]));
		}
		
		for (int i=0; i<valid24Times.length; i++) {
			assertTrue (testObj.setEndTime(valid24Times[i]));
		}
		
		for (int i=0; i<invalid24Times.length; i++) {
			assertFalse (testObj.setEndTime(invalid24Times[i]));
		}
		
		assertFalse(testObj.setEndTime(null));
	}
	@Test
	public final void testGetStartTime() {
		int[] startTimeDefault = new int[] {-1,-1};
		int[] startTimeDummy = new int[] {23,59};
		assertArrayEquals (startTimeDefault, testObj.getStartTime());
		testObj.setStartTime("23:59");
		assertArrayEquals (startTimeDummy, testObj.getStartTime());
	}

	@Test
	public final void testGetEndTime() {
		int[] endTimeDefault = new int[] {-1,-1};
		int[] endTimeDummy = new int[] {23,59};
		assertArrayEquals (endTimeDefault, testObj.getEndTime());
		testObj.setEndTime("23:59");
		assertArrayEquals (endTimeDummy, testObj.getEndTime());
	}
	
	@Test
	public final void testIsValid () {
		assertTrue(testObj.isValid("12:00pm"));
	}

}
