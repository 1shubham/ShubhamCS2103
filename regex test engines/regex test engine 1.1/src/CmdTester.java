import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdTester {
	
	private Pattern pattern1;
	private Matcher matcher1;
	private final String CMD_TEST_STRING = "(?i)((Add)|(Delete)|(Modify)|(Complete)|(Search)|(Archive))";
	//testing against case insensitive input
	
	public CmdTester () {
		pattern1 = Pattern.compile(CMD_TEST_STRING);
	}
	
	public boolean isValid (String s) {
		matcher1 = pattern1.matcher(s);
		return (matcher1.matches());
	}
}
