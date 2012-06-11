package sendMail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SocialAuth extends Authenticator{
	private String FROM_ADDRESS;
	private String PASSWORD;
	public SocialAuth(String username,String password)
	{
		FROM_ADDRESS=username;
		PASSWORD=password;
	}
protected PasswordAuthentication getPasswordAuthentication(String FROM_ADDRESS,String pass)
{
	return new PasswordAuthentication(FROM_ADDRESS,pass);
}
}
