import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time24HourTester {

	private Pattern pattern;
	private Matcher matcher;
	private final String TIME24_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
	
	public Time24HourTester() {
		pattern = Pattern.compile(TIME24_PATTERN);
	}
	
	public boolean isValid (String time) {
		matcher = pattern.matcher(time);
		return matcher.matches();
	}
}

/*
Time format that match:

1. “01:00″, “02:00″, “13:00″,
2. “1:00″, “2:00″, “13:01″,
3. “23:59″,”15:00″
4. “00:00″,”0:00″

Time format doesn’t match:

1. “24:00″ – hour is out of range [0-23]
2. “12:60″ – minute is out of range [00-59]
3. “0:0″ – invalid format for minute, at least 2 digits
4. “13:1″ – invalid format for minute, at least 2 digits
5. “101:00″ – hour is out of range [0-23]
*/