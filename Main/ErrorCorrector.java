package Main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.sun.glass.events.KeyEvent;

public class ErrorCorrector 
{
	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	public void pageFind() throws AWTException, InterruptedException
	{
		//Move to search bar
		 Robot robot = new Robot();
		 robot.mouseMove(300, 50);
		 
		 //left click
		 robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		 robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		 
		 //Control-A to select all
		 robot.keyPress(17);
		 robot.keyPress(KeyEvent.VK_A);
		 robot.keyRelease(17);
		 robot.keyRelease(KeyEvent.VK_A);
		 
		 //set clipboard to proper link
		 StringSelection stringSelection = new StringSelection("https://mynorthridge.csun.edu/psp/PANRPRD/EMPLOYEE/SA/c/NR_SSS_LAUNCH_MENU.NR_SSS_ENRL_CNTL_C.GBL");
		 clipboard.setContents(stringSelection, null);
		 
		 //paste link
		 robot.keyPress(17);
		 robot.keyPress(86);
		 robot.keyRelease(17);
		 robot.keyRelease(86);
		   
		 //enter
		 robot.keyPress(KeyEvent.VK_ENTER);
		 robot.keyRelease(KeyEvent.VK_ENTER);
		 
		 //move down to page
		 robot.mouseMove(300, 300);
		 TimeUnit.SECONDS.sleep(3);
		 
		 //refresh
		 robot.keyPress(KeyEvent.VK_F5);
		 robot.keyRelease(KeyEvent.VK_F5);
		 
	}
}
