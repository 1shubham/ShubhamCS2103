import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdTester {
	
	private Pattern pattern1;
	private Matcher matcher1;
	private final String CMD_REGEX = "(?i)((Add)|(Delete)|(Modify)|(Complete)|(Search)|(Archive))";
	//testing against case insensitive input
	
	public CmdTester () {
		pattern1 = Pattern.compile(CMD_REGEX);
	}
	
	public boolean isValid (String s) {
		matcher1 = pattern1.matcher(s);
		return (matcher1.matches());
	}
}
