package Database;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Mailer {
	private static Boolean sendMail(String to, String subject, String content) {
		final String mailUsername = "sharingbookreader@gmail.com";
		final String mailPassword = "Seker7553..";

		Properties props = new Properties();
		props.setProperty("mail.store.protocol","imap");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		final PasswordAuthentication auth = new PasswordAuthentication(mailUsername, mailPassword);
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return auth;
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailUsername));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=utf-8");
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static Boolean sendPasswordRememberMail(String email, String password) {
		String content = "Your password is " + password;
		return Mailer.sendMail(email, "Password Remember", content);
	}

	public static Boolean sendRequestBookMail(String email, String bookName, String fromName) {
		String content = fromName + " has been requested the book " + bookName + " from you";
		return Mailer.sendMail(email, "Request New Book", content);
	}

	public static Boolean confirmRequestBookMail(String email, String bookName, String fromName) {
		String content = "Has been confirmed the book " + bookName + " by " + fromName;
		return Mailer.sendMail(email, "Confirm Your Book", content);
	}

	public static Boolean cancelRequestBookMail(String email, String bookName, String fromName) {
		String content = "Has been canceled the book " + bookName + " by " + fromName;
		return Mailer.sendMail(email, "Cancel Your Book", content);
	}

	public static Boolean sendBookMail(String email, String bookName, String fromName) {
		String content = "Has been sent the book " + bookName + " by " + fromName;
		return Mailer.sendMail(email, "Send Your Book", content);
	}

	public static Boolean deliverBookMail(String email, String bookName, String fromName) {
		String content = "Has been delivered the book " + bookName + " by " + fromName;
		return Mailer.sendMail(email, "Deliver Your Book", content);
	}
}
