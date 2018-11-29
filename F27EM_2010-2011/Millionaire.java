import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class Millionaire extends MIDlet implements CommandListener 
{
	Display display;
	Alert correctAlert, incorrectAlert;
	Form mainForm, winnerForm;
	Form technologyForm1, technologyForm2, technologyForm3, technologyForm4;
	Form natureForm1, natureForm2, natureForm3, natureForm4;
	Form foodForm1, foodForm2, foodForm3, foodForm4;

	public Millionaire()
	{
		display = Display.getDisplay(this);
		mainForm = new Form("Millionaire");
		winnerForm = new Form("You Have WON!");
		technologyForm1 = new Form("Technology Q1");
		technologyForm2 = new Form("Technology Q2");
		technologyForm3 = new Form("Technology Q3");
		technologyForm4 = new Form("Technology Q4");
		natureForm1 = new Form("Nature Q1");
		natureForm2 = new Form("Nature Q2");
		natureForm3 = new Form("Nature Q3");
		natureForm4 = new Form("Nature Q4");
		foodForm1 = new Form("Food Q1");
		foodForm2 = new Form("Food Q2");
		foodForm3 = new Form("Food Q3");
		foodForm4 = new Form("Food Q4");
	}

	public void startApp() 
	{
		correctAlert = new Alert("Correct Answer");
		incorrectAlert = new Alert("Incorrect Answer");

		mainForm.append("Hello and welcome to \n Who Wants To Be A Millionaire! \n The quiz game where EVERYBODY WINS. \n Select a topic from the menu.");
		mainForm.addCommand( new Command("Quit", 1, 1) );
		mainForm.addCommand( new Command(" ", 1, 2) );
		mainForm.addCommand( new Command("Technology", 1, 3) );
		mainForm.addCommand( new Command("Nature", 1, 4) );
		mainForm.addCommand( new Command("Food", 1, 5) );
		mainForm.setCommandListener(this);
		
		winnerForm.append("Well Done! You have just won £1 Million! Do you want to keep it, or donate it to charity?");
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.addCommand( new Command("Donate it", 2, 2) );
		winnerForm.setCommandListener(this);

		technologyForm1.append("What voltage do household appliances run on in the UK?");
		technologyForm1.addCommand( new Command("110V", 2, 1) );
		technologyForm1.addCommand( new Command("230V", 2, 2) );
		technologyForm1.setCommandListener(this);

		technologyForm2.append("What is a common term for a fault in a computer program?");
		technologyForm2.addCommand( new Command("Bug", 2, 2) );
		technologyForm2.addCommand( new Command("Glitch", 2, 2) );
		technologyForm2.setCommandListener(this);

		technologyForm3.append("Which acronym represents a newer technology?");
		technologyForm3.addCommand( new Command("LCD", 2, 1) );
		technologyForm3.addCommand( new Command("OLED", 2, 2) );
		technologyForm3.setCommandListener(this);

		technologyForm4.append("Which Operating System is better?");
		technologyForm4.addCommand( new Command("Mac OS X", 2, 1) );
		technologyForm4.addCommand( new Command("Windows", 2, 1) );
		technologyForm4.addCommand( new Command("BSD", 2, 1) );
		technologyForm4.addCommand( new Command("DOS", 2, 1) );
		technologyForm4.addCommand( new Command("iOS", 2, 1) );
		technologyForm4.addCommand( new Command("Linux", 2, 2) );
		technologyForm4.setCommandListener(this);

		natureForm1.append("Which family of rodents have several species which can glide through the air?");
		natureForm1.addCommand( new Command("Squirrel", 2, 1) );
		natureForm1.addCommand( new Command("Possum", 2, 2) );
		natureForm1.setCommandListener(this);

		natureForm2.append("What colour is the sea?");
		natureForm2.addCommand( new Command("Blue", 2, 2) );
		natureForm2.addCommand( new Command("Green", 2, 2) );
		natureForm2.setCommandListener(this);

		natureForm3.append("Trees produce a gas. What gas?");
		natureForm3.addCommand( new Command("Nitrogen", 2, 1) );
		natureForm3.addCommand( new Command("Oxygen", 2, 2) );
		natureForm3.setCommandListener(this);

		natureForm4.append("Which animal do humans share the most DNA with?");
		natureForm4.addCommand( new Command("Baboon", 2, 1) );
		natureForm4.addCommand( new Command("Chimp", 2, 2) );
		natureForm4.setCommandListener(this);

		foodForm1.append("What food is tastiest?");
		foodForm1.addCommand( new Command("Cheese", 2, 1) );
		foodForm1.addCommand( new Command("Chocolate", 2, 2) );
		foodForm1.setCommandListener(this);

		foodForm2.append("Bad health can be cause by excessive consumption of which of these commonly used flavour-enhancers?");
		foodForm2.addCommand( new Command("Salt", 2, 2) );
		foodForm2.addCommand( new Command("Pepper", 2, 2) );
		foodForm2.setCommandListener(this);

		foodForm3.append("Coffee or tea?");
		foodForm3.addCommand( new Command("Tea", 2, 1) );
		foodForm3.addCommand( new Command("Coffee", 2, 2) );
		foodForm3.setCommandListener(this);

		foodForm4.append("Mmmmmmmmmmm...");
		foodForm4.addCommand( new Command("No!", 2, 1) );
		foodForm4.addCommand( new Command("Ohhh yes!", 2, 2) );
		foodForm4.setCommandListener(this);

		display.setCurrent(mainForm);
	}

	public void pauseApp () {}

	public void destroyApp(boolean unconditional) {}

	public void commandAction(Command c, Displayable s) 
	{
		Displayable currentDisplay = display.getCurrent();
		switch ( c.getCommandType() )
		{
			case 1: // Command came from Main Form
				switch ( c.getPriority() )
				{
					case 1: // Quit Button, call destroyApp function
						notifyDestroyed();
						break;
					case 3:
						display.setCurrent(technologyForm1);
						break;
					case 4:
						display.setCurrent(natureForm1);
						break;
					case 5:
						display.setCurrent(foodForm1);
						break;
				}
				break;
			case 2: // Command came from Question Form
				switch ( c.getPriority() )
				{
					case 1: // Incorrect/Negative
						display.setCurrent(incorrectAlert,currentDisplay);
						break;
					case 2: // Correct/Positive
						if(currentDisplay == technologyForm1) display.setCurrent(correctAlert,technologyForm2);
						if(currentDisplay == technologyForm2) display.setCurrent(correctAlert,technologyForm3);
						if(currentDisplay == technologyForm3) display.setCurrent(correctAlert,technologyForm4);
						if(currentDisplay == technologyForm4) display.setCurrent(correctAlert,winnerForm);
						
						if(currentDisplay == natureForm1) display.setCurrent(correctAlert,natureForm2);
						if(currentDisplay == natureForm2) display.setCurrent(correctAlert,natureForm3);
						if(currentDisplay == natureForm3) display.setCurrent(correctAlert,natureForm4);
						if(currentDisplay == natureForm4) display.setCurrent(correctAlert,winnerForm);
						
						if(currentDisplay == foodForm1) display.setCurrent(correctAlert,foodForm2);
						if(currentDisplay == foodForm2) display.setCurrent(correctAlert,foodForm3);
						if(currentDisplay == foodForm3) display.setCurrent(correctAlert,foodForm4);
						if(currentDisplay == foodForm4) display.setCurrent(correctAlert,winnerForm);
						
						if(currentDisplay == winnerForm) display.setCurrent(correctAlert,mainForm);
						break;
				}
				break;
		}
	}
}
