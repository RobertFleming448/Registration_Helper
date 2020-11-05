package Main;

import java.io.File;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class GetPixels 
{
	int greenCount=0;
	public int count()throws Exception {

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
}