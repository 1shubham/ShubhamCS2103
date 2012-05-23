import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time24HourTester {

	private Pattern pattern1, pattern2;
	private Matcher matcher1, matcher2;
	private final String TIME24_PATTERN_1 = "([01]?[0-9]|2[0-3])[:.]?[0-5][0-9]";
	//private final String TIME24_PATTERN_2 = "([01]?[0-9]|2[0-3])[0-5][0-9]";
	
	
	public Time24HourTester() {
		pattern1 = Pattern.compile(TIME24_PATTERN_1);
		//pattern2 = Pattern.compile(TIME24_PATTERN_2);
	}
	
	public boolean isValid (String time) {
		matcher1 = pattern1.matcher(time);
		//matcher2 = pattern2.matcher(time);
		return (matcher1.matches());
	}
}

/*
Time format that match:

1. “01:00″, “02:00″, “13:00″,
2. “1:00″, “2:00″, “13:01″,
3. “23:59″,”15:00″
4. “00:00″,”0:00″

-  : or .
- 1600

Time format doesn’t match:

1. “24:00″ – hour is out of range [0-23]
2. “12:60″ – minute is out of range [00-59]
3. “0:0″ – invalid format for minute, at least 2 digits
4. “13:1″ – invalid format for minute, at least 2 digits
5. “101:00″ – hour is out of range [0-23]
*/