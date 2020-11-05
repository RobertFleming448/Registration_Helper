package Main;

//Key Presses
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

//SMS Sending
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import java.util.Date;

import java.io.FileWriter;
import java.io.BufferedWriter;

public class RegistrationHelper 
{
	public static final String ACCOUNT_SID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";	//replace with account SID
	public static final String AUTH_TOKEN = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";		//replace with auth token
	public static int oldAmount=0;
	public static int newAmount=0;
	public static int savedAmount=0;
	public static int fatalErrorCount=0;
	public static boolean firstRun = true;
	public static ErrorCorrector errorCall= new ErrorCorrector();
	static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
	
	public static void main(String [] args) throws InterruptedException, AWTException, IOException
	{
		//sendText("test");		
		//errorCall.pageFind();
		
		
		System.out.println("Registration Helper starting in ");
		System.out.println("3 ");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("2");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("1");
		TimeUnit.SECONDS.sleep(1);
		
		while(true)
		{
			refreshPageAlt();
			capture();
			TimeUnit.SECONDS.sleep(1);
			newAmount=parseImg();
			calculate();
			TimeUnit.SECONDS.sleep(5);
			System.out.println();
		}
		
		
	}
	
	public static void calculate() throws InterruptedException, AWTException, IOException
	{
		append("NEW AMOUNT: " + newAmount);
		append("OLD AMOUNT: " + oldAmount);
		System.out.println("NEW AMOUNT: " + newAmount);
		System.out.println("OLD AMOUNT: " + oldAmount);
		if(firstRun)
		{
			append("System primed! " + new Date());
			System.out.println("System primed! " + new Date());
			oldAmount=newAmount;
			savedAmount=newAmount;
			firstRun=false;
		}
		else if(newAmount>1900 || newAmount<50)	//Math.abs(newAmount-oldAmount)>800
		{
			append("Hmm that's kinda fishy lets try to fix that. "+ new Date());
			System.out.println("Hmm that's kinda fishy lets try to fix that. "+ new Date());
			oldAmount=savedAmount;
			fatalErrorCount++;
			System.out.println("Concurrent Fatal Errors: " + fatalErrorCount);
			errorCheck();
		}
		else if(fatalErrorCount>0)
		{
			fatalErrorCount=0;
			//sendText("Encountered Error, but recovered");
			append("Encountered Error, but recovered "+ new Date());
			System.out.println("Encountered Error, but recovered " + new Date());
			//oldAmount=newAmount;
		}
		else if(newAmount>oldAmount && Math.abs(newAmount-oldAmount)>50)
		{
			fatalErrorCount=0;
			sendText("Class space may be open");
			append("Class space may be open " + new Date());
			System.out.println("Class space may be open " + new Date());
			oldAmount=newAmount;
			BufferedImage img = ImageIO.read(new File("screen-capture.png"));
			File outputfile = new File(formatter.format(new Date()) + "_opening.png");
			ImageIO.write(img, "png", outputfile);
		}
		else if(newAmount<oldAmount && Math.abs(newAmount-oldAmount)>50)
		{
			fatalErrorCount=0;
			sendText("A class may have closed");
			append("A class may have closed " + new Date());
			System.out.println("A class may have closed " + new Date());
			oldAmount=newAmount;
			BufferedImage img = ImageIO.read(new File("screen-capture.png"));
			File outputfile = new File(formatter.format(new Date()) + "_closing.png");
			ImageIO.write(img, "png", outputfile);
		}
		else
		{
			fatalErrorCount=0;
			System.out.println("Business as usual " + new Date());
			append("Business as usual " + new Date());
			oldAmount=newAmount;
		}
		
	}
	public static void errorCheck() throws InterruptedException, AWTException
	{
		if (fatalErrorCount>10)
		{
			sendText("System exceeded 10 fatal errors shutting down");
			System.out.println("System exceeded 10 fatal errors shutting down " + new Date());
			append("System exceeded 10 fatal errors shutting down " + new Date());
			System.exit(1);
		}
		/*
		Robot r = new Robot();
		
		
		r.keyPress(KeyEvent.VK_F5);
		r.keyRelease(KeyEvent.VK_F5);
		TimeUnit.SECONDS.sleep(2);
		r.keyPress(KeyEvent.VK_F5);
		r.keyRelease(KeyEvent.VK_F5);
		TimeUnit.SECONDS.sleep(2);
		r.keyPress(KeyEvent.VK_F5);
		r.keyRelease(KeyEvent.VK_F5);
		
		r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		r.keyPress(KeyEvent.VK_F5);
		r.keyRelease(KeyEvent.VK_F5);
		*/
		errorCall.pageFind();
		//oldAmount=0;
		//newAmount=0;
		//firstRun=true;
	}
	
	public static int parseImg() throws IOException
	{
		int greenCount=0;
		//Reading the image
	      BufferedImage img = ImageIO.read(new File("screen-capture.png"));
	      for (int y = 0; y < img.getHeight(); y++) 
	      {
	         for (int x = 0; x < img.getWidth(); x++) {
	            //Retrieving contents of a pixel
	            int pixel = img.getRGB(x,y);
	            //Creating a Color object from pixel value
	            Color color = new Color(pixel, true);
	            //Retrieving the R G B values
	            //int red = color.getRed();
	            //int green = color.getGreen();
	            int blue = color.getBlue();
	            //System.out.println(red);
	            //System.out.println(green);
	            //System.out.println(blue);
	            if (blue<50)
	            {
	            	greenCount++;
	            }
	         }
	      }
	      return greenCount;

	}
	
	
	public static void capture()
	{
        try 
        {
            Robot robot = new Robot();
 
            //Cropped photo works with 1080p screens 16:9 firefox 110% zoom
            /*
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            //System.out.println(screenSize.width);
            //System.out.println(screenSize.height);
            Rectangle captureRect = new Rectangle(1820, 1050 / 2, 20 , 400 );
            BufferedImage bufferedImage = robot.createScreenCapture(captureRect);
            File file = new File("screen-capture.png");	 
            boolean status = ImageIO.write(bufferedImage, "png", file);
            append("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            */
            
            //Fullscreen but cropped around class
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            //System.out.println(screenSize.width);
            //System.out.println(screenSize.height);
            Rectangle captureRect = new Rectangle(420, 1130 / 2, 581 , 448);		//alt screen dimensions: (420, 1130 / 2, 581 , 448);	//main screen size (420, 1150 / 2, 581 , 448 );
            BufferedImage bufferedImage = robot.createScreenCapture(captureRect);
            File file = new File("screen-capture.png");	 
            boolean status = ImageIO.write(bufferedImage, "png", file);
            append("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            
            
            //Fullscreen works with everything but less reliable
            /*
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            File file = new File("screen-capture.png");
            boolean status = ImageIO.write(bufferedImage, "png", file);
            append("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            */
 
        } 
        catch (AWTException | IOException ex) 
        {
            System.err.println(ex);
        }
    }
	
	public static void sendText(String text)
	{
	    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    Message message = Message.creator(new PhoneNumber("+00000000000"),		//Recipient phone number
	        new PhoneNumber("+00000000000"), 		//Twilio sender phone number
	        text).create();
	    
	    append(message.getSid());
	    System.out.println(message.getSid());
	}
	public static void append(String text)
	{
		 try {
	         String data = text;
	         File f1 = new File("logs\\log.txt");
	         
	         if(!f1.exists()) 
	         {
	            f1.createNewFile();
	         }

	         FileWriter fileWritter = new FileWriter("logs\\log.txt",true);
	         BufferedWriter bw = new BufferedWriter(fileWritter);
	         
	         bw.write(data);
	         bw.newLine();
	         bw.close();
	         
	      } catch(IOException e){
	         e.printStackTrace();
	      }
	}
	
	public static void refreshPage() throws InterruptedException, AWTException
	{
		Robot r = new Robot();
		
		
		TimeUnit.SECONDS.sleep(2);
		
		r.keyPress(KeyEvent.VK_F5);
		r.keyRelease(KeyEvent.VK_F5);
		
		TimeUnit.SECONDS.sleep(8);

		//first page
		r.keyPress(KeyEvent.VK_TAB);
	    r.keyRelease(KeyEvent.VK_TAB);
	    TimeUnit.SECONDS.sleep(1);
	    r.keyPress(KeyEvent.VK_TAB);
	    r.keyRelease(KeyEvent.VK_TAB);
	    TimeUnit.SECONDS.sleep(1);
	    r.keyPress(KeyEvent.VK_ENTER);
	    r.keyRelease(KeyEvent.VK_ENTER);
	    TimeUnit.SECONDS.sleep(8);
	    
	    //Second page
	    for(int i=1;i<15;i++)
	    {
	    	r.keyPress(KeyEvent.VK_TAB);
		    r.keyRelease(KeyEvent.VK_TAB);
	    }
	    TimeUnit.SECONDS.sleep(1);
	    r.keyPress(KeyEvent.VK_SPACE);
	    r.keyRelease(KeyEvent.VK_SPACE);
	    TimeUnit.SECONDS.sleep(1);
	    r.keyPress(KeyEvent.VK_TAB);
	    r.keyRelease(KeyEvent.VK_TAB);
	    r.keyPress(KeyEvent.VK_ENTER);
	    r.keyRelease(KeyEvent.VK_ENTER);
	    append("Page Refreshed");
	    System.out.println("Page Refreshed");
	    TimeUnit.SECONDS.sleep(8);
		
	}
	
	public static void refreshPageAlt() throws AWTException, InterruptedException
	{
		Robot robot = new Robot();
		
		TimeUnit.SECONDS.sleep(2);
		
		robot.keyPress(KeyEvent.VK_F5);
		robot.keyRelease(KeyEvent.VK_F5);
		
		TimeUnit.SECONDS.sleep(8);
		
		//first Page
		robot.mouseMove(700, 200);
		TimeUnit.SECONDS.sleep(1);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		TimeUnit.SECONDS.sleep(1);
		robot.mouseMove(1910, 300);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseMove(1910, 1000);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		TimeUnit.SECONDS.sleep(1);
		robot.mouseMove(700, 940);
		TimeUnit.SECONDS.sleep(1);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		TimeUnit.SECONDS.sleep(8);
		
		//Second Page
		robot.mouseMove(220, 450);		//alt screen size: (220, 450)	//main screen size(220, 470)
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		TimeUnit.SECONDS.sleep(1);
		robot.mouseMove(600, 480);		//alt screen size: (600, 480)	//main screen size(600, 500)
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		
		append("Page Refreshed");
	    System.out.println("Page Refreshed");
	    
	    TimeUnit.SECONDS.sleep(8);
	}
}

