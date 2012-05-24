import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time12HourTester {

	private Pattern pattern1;
	private Matcher matcher1;
	private final String TIME12_PATTERN_1 = "(1[012]|(0?[1-9]))([:.][0-5][0-9])?(\\s)?(?i)(am|pm)";
	//private final String TIME12_PATTERN_2 = "(1[012]|[1-9])(\\s)?(?i)(am|pm)";
	
	//update: the following are combined to one now
	//TIME12_PATTERN_1 checks for input of the format 12:00am / 12.00am
	//TIME12_PATTERN_1 checks for input of the format 12am
	
	public Time12HourTester() {
		pattern1 = Pattern.compile(TIME12_PATTERN_1);
		//pattern2 = Pattern.compile(TIME12_PATTERN_2);
	}
	
	public boolean isValid (String time) {
		matcher1 = pattern1.matcher(time);
		//matcher2 = pattern2.matcher(time);
		return (matcher1.matches());//|| matcher2.matches());
	}

}

/*
Time format that match:

1. “1:00am”, “1:00 am”,”1:00 AM” ,
2. “1:00pm”, “1:00 pm”, “1:00 PM”,
3. “12:50 pm”

- : or .
03.45 am

Time format doesn’t match:

1. “0:00 am” – hour is out of range [1-12]
2. “10:00 am” – only one white space is allow
3. “1:00″ – must end with am or pm
4. “23:00 am” -24-hour format is not allow
5. “1:61 pm” – minute is out of range [0-59]
6. “13:00 pm” – hour is out of range [1-12]
7. “001:50 pm” – invalid hour format
8. “10:99 am” – minute is out of range [0-59]
9. “01:00 pm” – 24-hour format is not allow
10. “1:00 bm” – must end with am or pm

*/
