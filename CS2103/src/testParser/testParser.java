package testParser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class testParser {

	String[] TIMES;
	String[] DATES;
	String[] DETAILS;
	String[] RECUR;
	String[] RECURTIMES;
	String[] LABELS;
	
	String STAR = "*";
	String SPACE = " ";
	String FROM = "from";
	String TO = "to";
	String AT = "at";
	String BY = "by";
	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetErrorCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFetchTaskId() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFetchTaskIds() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testParseForSearch() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testParseForAdd() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFetchGCalDes() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testValidateEmailAdd() {
		fail("Not yet implemented"); // TODO
	}

}
