package com.awesome;
import java.net.*;
import java.io.*;
import javax.xml.bind.DatatypeConverter;

/*
 * USAGE: Short answer: See main() method - It currently contains a commented out example of how you would use this class to send an email.
 * 	Long answer: 
 * 		ShoeMail.sendMail( String[]{
 * 			Mail server address,
 * 			Mail server port,
 * 			Mail server username,
 * 			Mail server password,
 * 			Sender Email,
 * 			Recipient Email,
 * 			Message (including any desired email headers such as To:, From: and Subject: )
 * 		}, boolean debug )
 *  The debug true/false changes how much is printed to System.out - when true, the function will also take about 3x longer
 *  as it waits for a server response for each command before continuing, and prints the response as it goes.
 *  
 *  RETURNS: String "Sent" if email was sent correctly, otherwise "Not sent" and string containing SMTP server responses for debugging
 */

public class ShoeMail {

/*	public static void main(String a[]) {
		ShoeMail shoeMailObject = new ShoeMail();

		// Run sendMail, pass variables, including entire HTML email message with headers 
		System.out.println(shoeMailObject.sendMail(new String[]{
				"mail.andrewbeveridge.co.uk",
				"25",
				"gmail@andrewbeveridge.co.uk",
				"gmail",
				"gmail@andrewbeveridge.co.uk",
				"andrew.d.beveridge@gmail.com",
				
				"To: Andrew Beveridge <andrew.d.beveridge@gmail.com>\n" +
				"From: ShoeMail <gmail@andrewbeveridge.co.uk>\n" +
				"Subject: Your invoice for ShoeMail purchases\n" +
				"MIME-Version: 1.0\n" +
				"Content-Type: text/html; charset=ISO-8859-1\n" +
				"<html><body>" +
				"<h1>ShoeMail</h1>" +
				"<b>Dear Andrew,</b><br /><br />" +
				"We would like to thank you for your recent purchase of very expensive shoes at our shop.<br />" +
				"However, you didn't pay.<br />" +
				"We don't take people who don't pay lightly, since our shoes cost so much.<br />" +
				"Here is our final offer of an invoice for you to settle your debts.<br /><br />" +
				"If you do not reply, we will be forced to take appropriate action...<br />" +
				"<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />" +
				"<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />" +
				"<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />" +
				"...by appropriate action, we mean, we will kill you.<br /><br />" +
				"Have a nice day!<br /><br />" +
				"Kind Regards,<br />" +
				"ShoeMail" +
				"</body></html>"
		},false));
	}
*/ 
	
	public String sendMail(String[] vars, boolean debug) {
		// Extract variable data from array parameter
		String smtpServer = vars[0];
		int smtpPort = Integer.parseInt(vars[1]);
		String smtpUser = vars[2];
		String smtpPass = vars[3];
		String sender = vars[4];
		String recipient = vars[5];
		String messageBody = vars[6];

		try {
			// Open connection to specified smtp server on specified port
			Socket sock = new Socket(smtpServer, smtpPort);
			/* Set a timeout on the socket so readLine() doesn't hang our script; 
			 * Read large comment section in smtpCommand() function for a more detailed explanation of why this is necessary */
			sock.setSoTimeout(3000);
			// Get socket output stream, ready to send commands
			OutputStream os = sock.getOutputStream(); 
			// Get socket input stream
			InputStream is = sock.getInputStream(); 
			// Make it easier to read the input stream
			InputStreamReader isr = new InputStreamReader(is); 
			// Enable reading the input stream *one line at a time*
			BufferedReader br = new BufferedReader(isr); 

			if(debug) {
				// Print message to show we've made it this far without error and opened the socket
				System.out.println("SMTP Connection Opened (woo!):\n"); 
			}
			// Read message(s) sent by the server upon opening the connection (greeting)
			System.out.println(smtpCommand("",os,br,debug));
			// Send EHLO command to get server abilities and initialize
			System.out.println(smtpCommand("EHLO",os,br,debug));

			/*
			 *  Send AUTH PLAIN command to log into the server and allow us to send mail
			 *  AUTH PLAIN accepts username and password encoded as a string in the following format:
			 *  \0username\0password
			 *  Encoded as Base64.
			 */			
			String smtpPlainAuth = "\0"+smtpUser+"\0"+smtpPass;
			String smtpPlainAuthB64 = DatatypeConverter.printBase64Binary(smtpPlainAuth.getBytes());
			System.out.println(smtpCommand("AUTH PLAIN "+smtpPlainAuthB64,os,br,debug));

			// Tell the mail server we want to actually send mail, and who from
			System.out.println(smtpCommand("MAIL From: <"+sender+">",os,br,debug));
			// Set recipient email address
			System.out.println(smtpCommand("RCPT To: <"+recipient+">",os,br,debug));
			// Tell mail server what comes next is data for the actual message, not server commands
			System.out.println(smtpCommand("DATA",os,br,debug));
			
			// Send actual message
			System.out.println(smtpCommand(messageBody,os,br,debug));
			
			// Tell mail server we have finished sending a message, so -fingers crossed- it should send!
			String finalOutput = smtpCommand(".",os,br,true);			
			
			// All done, quit now. Can't leave the poor SMTP server hanging now, can we!
			System.out.println(smtpCommand("QUIT",os,br,debug));
			
			// Close I/O streams and socket nicely before returning
			os.close();
			is.close();
			sock.close();
			
			// Check final output for the reply from the server which shows the message was successfully sent
			if(finalOutput.matches("(?s).*OK id.*")) {
				// If message sent, return "sent"
				String returnString;
				if(debug) {
					returnString = "Sent: "+finalOutput;
				} else {
					returnString = "Sent";
				}
				return returnString;
			} else {
				// Otherwise return the entire server response string for further analysis
				return "Not sent: "+finalOutput;
			}
			
		} catch (UnknownHostException e) {
			//System.err.println("Couldn't connect to andrew's mail server.");
			//System.exit(1);
		} catch (SocketTimeoutException e) {
			//System.out.println("SMTP socket timed out, carrying on");
		} catch (IOException e) {
			//System.err.println("Couldn't get I/O for the mail server socket.");
			//System.exit(1);
		}
		
		return "Not sent: Unexpected error in ShoeMail";
	}

	public String smtpCommand(String command, OutputStream smtpOutputStream, BufferedReader smtpBufferedReader, boolean debug) {
		// Create empty string for results of command execution
		String commandResults = "";
		
		// Allow for this function to be used to read input from the server without any output first
		if(command.length()>0) {
			// Add line feed after command so mail server knows command is complete
			command += "\n";
			// Show command being executed in the results, formatted to show output
			commandResults+="-> "+command;
			
			try {
				// Send command to mail server, in US-ASCII format
				smtpOutputStream.write(command.getBytes("US-ASCII")); 
			} catch (UnsupportedEncodingException e) {
				System.err.println("SMTP output stream Unsupported Encoding Exception in smtpCommand function: "+e);
			} catch (IOException e) {
				System.err.println("SMTP output stream IO exception in smtpCommand function: "+e);
			}
		}
			
		if(debug) {
			// Create empty string to store each line returned by the mail server, as it is read
			String currentLine = "";
			try {
				// Read the next line from the mail server reply, as long as readLine is still active
				while((currentLine = smtpBufferedReader.readLine())!=null) {
					// Append the reply to the return string, formatted to show input 
					commandResults+="<- "+currentLine+"\n"; 
				}
			} catch (SocketTimeoutException e) {
				/* This exception deliberately does nothing; It is crucial to the operation of this function:
				 * The readLine() function of BufferedReader does not return until it receives a line feed character
				 * However, SMTP does not require a line feed character after every response
				 * Thus, after a command is executed, there is no way to detect when the mail server has finished
				 * sending it's response, other than to wait a few seconds and hope it has sent all it wants to.
				 * Earlier in the script we set a timeout of 3 seconds; therefore for every command we execute,
				 * we must wait 3 seconds until this exception is thrown before carrying on.
				 */
			} catch (IOException e) {
				System.err.println("SMTP buffered reader IO exception in smtpCommand function: "+e);
			}
			return commandResults;
		} else {
			return "";
		}
	}
	
}
