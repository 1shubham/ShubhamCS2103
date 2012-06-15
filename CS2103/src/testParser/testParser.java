package testParser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import parser.Parser;
import constant.OperationFeedback;

public class testParser {

	Parser testObj;
	
	String[] TIMES;
	String[] DATES;
	String[] DETAILS;
	String[] RECUR;
	String[] RECURTIMES;
	String[] LABELS;
	String[] SEARCH_CMD;
	String[] DATETIME_FORMATS;
	
	String STAR = "*";
	String SPACE = " ";
	String FROM = "from";
	String TO = "to";
	String AT = "at";
	String BY = "by";
	String ON = "on";
	
	@Before
	public void setUp() throws Exception {
		testObj = new Parser();
		TIMES = new String[] {"5pm","5:00pm","23:00"/*,"23.00","1am","12:30pm","0:00","3am","20:00"*/};
		DATES = new String[] {"4th jan","1st february"/*,"01/12/2013","31st-DEC-2013","today","tmr","tuesday","next tuesday","SUN","tomorrow"*/};
		RECUR = new String[] {"weekly","daily"/*,"yearly","monthly"*/};
		RECURTIMES = new String[] {"-2 times"/*,"-3times","-3","- 3"*/,""};
		LABELS = new String[] {"@work", "@work @play @study",""};
		DETAILS = new String[] {"go to play","'this is comment#1' some details 'comment 2' bla bla"};
		String temp1;
		
		ArrayList<String> dateTimeList = new ArrayList<String>();
		for (int j=0; j<DATES.length-1; j++) {
			dateTimeList.add(ON+" "+DATES[j]);
			dateTimeList.add(BY+" "+ON+" "+DATES[j]);
			//dateTimeList.add(DATES[j]);
			//dateTimeList.add(BY+" "+DATES[j]);
		}
		
		for (int i=0; i<TIMES.length-1; i++) {
			dateTimeList.add(BY+" "+TIMES[i]);
			dateTimeList.add(AT+" "+TIMES[i]);
			dateTimeList.add(TIMES[i]);
		}
		for (int i=0; i<TIMES.length-1; i++) {
			for (int j=0; j<DATES.length-1; j++) {
				
				//System.out.println("inside loop:"+i+j+": "+FROM+" "+ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]+" "+TIMES[i+1]);
				
				
				dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				//dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]+" "+TIMES[i+1]);
				
				//dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]);
				
				dateTimeList.add(FROM+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]+" "+ON+" "+DATES[j+1]);
				
				dateTimeList.add(FROM+" "+TIMES[i]+" "+ON+" "+DATES[j]+" "+TO+" "+TIMES[i+1]+" "+ON+" "+DATES[j+1]);
				dateTimeList.add(FROM+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(FROM+" "+TIMES[i]+" "+ON+" "+DATES[j]+" "+TO+" "+TIMES[i+1]);
				
				//dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]);
				dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]);
				
				//dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]+" "+TIMES[i+1]);
				dateTimeList.add(FROM+" "+ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+FROM+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				//dateTimeList.add(BY+" "+ON+" "+DATES[j]);
				dateTimeList.add(AT+" "+TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(BY+" "+TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(ON+" "+DATES[j]+" "+AT+" "+TIMES[i]);
				//dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]);
				dateTimeList.add(BY+" "+ON+" "+DATES[j]+" "+AT+" "+TIMES[i]);
				//dateTimeList.add(BY+" "+ON+" "+DATES[j]+" "+TIMES[i]);
				
				
				
				/*
				dateTimeList.add(ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]+" "+TIMES[i+1]);
				
				dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]);
				
				dateTimeList.add(TIMES[i]+" "+TO+" "+TIMES[i+1]+" "+ON+" "+DATES[j+1]);
				
				dateTimeList.add(TIMES[i]+" "+ON+" "+DATES[j]+" "+TO+" "+TIMES[i+1]+" "+ON+" "+DATES[j+1]);
				dateTimeList.add(TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(TIMES[i]+" "+ON+" "+DATES[j]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+ON+" "+DATES[j+1]);
				
				dateTimeList.add(ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]+" "+TIMES[i+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+TO+" "+ON+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(BY+" "+ON+" "+DATES[j]);
				dateTimeList.add(AT+" "+TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(BY+" "+TIMES[i]+" "+ON+" "+DATES[j]);
				dateTimeList.add(ON+" "+DATES[j]+" "+AT+" "+TIMES[i]);
				dateTimeList.add(ON+" "+DATES[j]+" "+TIMES[i]);
				dateTimeList.add(BY+" "+ON+" "+DATES[j]+" "+AT+" "+TIMES[i]);
				dateTimeList.add(BY+" "+ON+" "+DATES[j]+" "+TIMES[i]);
				
				
				
				
				dateTimeList.add(DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				dateTimeList.add(DATES[j]+" "+TIMES[i]+" "+TO+" "+DATES[j+1]+" "+TIMES[i+1]);
				
				dateTimeList.add(DATES[j]+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				dateTimeList.add(DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(DATES[j]+" "+TO+" "+DATES[j+1]);
				
				dateTimeList.add(TIMES[i]+" "+TO+" "+TIMES[i+1]+" "+DATES[j+1]);
				
				dateTimeList.add(TIMES[i]+" "+DATES[j]+" "+TO+" "+TIMES[i+1]+" "+DATES[j+1]);
				dateTimeList.add(TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(TIMES[i]+" "+DATES[j]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(DATES[j]+" "+TIMES[i]+" "+TO+" "+DATES[j+1]);
				dateTimeList.add(DATES[j]+" "+AT+" "+TIMES[i]+" "+TO+" "+DATES[j+1]);
				
				dateTimeList.add(DATES[j]+" "+TO+" "+DATES[j+1]+" "+TIMES[i+1]);
				dateTimeList.add(DATES[j]+" "+TO+" "+DATES[j+1]+" "+AT+" "+TIMES[i+1]);
				dateTimeList.add(DATES[j]+" "+TIMES[i]+" "+TO+" "+TIMES[i+1]);
				
				dateTimeList.add(BY+" "+DATES[j]);
				dateTimeList.add(AT+" "+TIMES[i]+" "+DATES[j]);
				dateTimeList.add(TIMES[i]+" "+DATES[j]);
				dateTimeList.add(BY+" "+TIMES[i]+" "+DATES[j]);
				dateTimeList.add(DATES[j]+" "+AT+" "+TIMES[i]);
				dateTimeList.add(DATES[j]+" "+TIMES[i]);
				dateTimeList.add(BY+" "+DATES[j]+" "+AT+" "+TIMES[i]);
				dateTimeList.add(BY+" "+DATES[j]+" "+TIMES[i]);
				*/
			}
		}
		DATETIME_FORMATS = dateTimeList.toArray(new String[dateTimeList.size()]);
		
		System.out.println("size of test date times: "+dateTimeList.size());
		//for (int i=0; i<DATETIME_FORMATS.length; i++)
			//System.out.println("date time formats being tested: "+DATETIME_FORMATS[i]);
		
		
		//for loops for just time/date
		// for search, no need to have task details
		
		ArrayList<String> cmdList = new ArrayList<String>();
		for(int i=0; i<DETAILS.length; i++){
			for(int a=0; a<DATETIME_FORMATS.length; a++) {
				cmdList.add(DETAILS[i]+" "+DATETIME_FORMATS[a]);
				for (int l=0; l<LABELS.length;l++) {
					cmdList.add(DETAILS[i]+" "+DATETIME_FORMATS[a]+" "+LABELS[l]);
				}
			}
			for (int j=0; j<RECUR.length; j++) {
				for (int k=0; k<RECURTIMES.length; k++) {
					for(int a=0; a<DATETIME_FORMATS.length; a++) {
						cmdList.add(DETAILS[i]+" "+DATETIME_FORMATS[a]+" "+RECUR[j]+" "+RECURTIMES[k]);
						for (int l=0; l<LABELS.length;l++) {
							cmdList.add(DETAILS[i]+" "+DATETIME_FORMATS[a]+" "+RECUR[j]+" "+RECURTIMES[k]+" "+LABELS[l]);
						}
					}
				}
			}
		}
		SEARCH_CMD = cmdList.toArray(new String[cmdList.size()]);
		
		System.out.println("---------------");
		System.out.println("---------------");
		System.out.println("size of test commands: "+SEARCH_CMD.length);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public final void testGetErrorCode() {
		testObj.parseForAdd("*cs2013 presentation from 4pm to 3pm weekly-100 @work");
		assertEquals (OperationFeedback.START_DATE_TIME_MORE_THAN_END_DATE_TIME, testObj.getErrorCode());
	}
	
	@Test
	public final void testFetchTaskId() {
		assertNotNull (testObj.fetchTaskId("$$__30-06-2012160000B__$$"));
		//fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFetchTaskIds() {
		assertNotNull (testObj.fetchTaskIds("$$__30-06-2012160000B__$$ $$__31-06-2012160000B__$$"));
		//fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testParseForSearch() {
		/*
		for (int i=0; i<SEARCH_CMD.length-800; i++)
			assertNotNull (testObj.parseForSearch(SEARCH_CMD[i]));
		assertNotNull (testObj.parseForSearch("'comments' details 'comments2' at 12am"));
		assertNotNull (testObj.parseForSearch("from 2pm to 4pm"));
		assertNotNull (testObj.parseForSearch("*'comments' details 'comments2' at 12am"));
		*/
	}
	
	@Test
	public final void testParseForAdd() {
		assertNull(testObj.parseForAdd("blabla"));

		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm 14th aug to 4pm 27th nov yearly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation by 4pm 31st oct yearly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation at 4pm 31st oct yearly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm to 4pm yearly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm 14th aug to 4pm 27th nov monthly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation by 4pm 31st oct monthly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation at 4pm 31st oct monthly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm to 4pm monthly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm 14th aug to 4pm 27th nov daily-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation by 4pm 31st oct daily-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation at 4pm 31st oct daily-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm to 4pm daily-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm 14th aug to 4pm 27th nov weekly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation by 4pm 31st oct weekly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation at 4pm 31st oct weekly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 2pm to 4pm weekly-13 @work"));
		assertNull(testObj.parseForAdd("*cs2013 presentation from 4pm to 2pm weekly-13 @work"));
		assertNotNull(testObj.parseForAdd("*cs2013 presentation from 1am to 2am weekly-13 @work"));
		assertNull(testObj.parseForAdd("project deadline by 1:00 today"));
		assertNotNull(testObj.parseForAdd("project deadline by 11pm"));
		assertNull(testObj.parseForAdd("drive to work at 12:30am today"));
		assertNotNull(testObj.parseForAdd("drive to work at 12:30pm today"));
		assertNull(testObj.parseForAdd("*cs2013 presentation from 1am to 2am weekly-100 @work"));
		
	}
	
	@Test
	public final void testFetchGCalDes() {
		assertNotNull (testObj.fetchGCalDes("<CMPT:false><IMPT:false><DEAD:false><RECUR:><RECURID:><LABEL:>"));
	}
	
	@Test
	public final void testValidateEmailAdd() {
		assertTrue(testObj.validateEmailAdd("1shubham@gmail.com"));
	}
	
}
