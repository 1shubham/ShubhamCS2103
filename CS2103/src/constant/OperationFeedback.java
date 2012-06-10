package constant;

public enum OperationFeedback {
	
	VALID,
	START_DATE_TIME_LESS_THAN_CURRENT_DATE_TIME,
	END_DATE_TIME_LESS_THAN_CURRENT_DATE_TIME,
	START_DATE_TIME_MORE_THAN_END_DATE_TIME,
	INVALID_DATE_TIME,
	RECURRING_TIMES_EXCEEDED,
	INVALID_DATE,
	INVALID_TIME,
	INVALID_TASK_DETAILS,
	INVALID_LABEL,
	NOT_FOUND,
	
	INVALID_INCORRECTLOGIN, //incorrect username or password
	INVALID_NOINTERNET; //not connect to internet
	
	
	private String msg;
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}
}
