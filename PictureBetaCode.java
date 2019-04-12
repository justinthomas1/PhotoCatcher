import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

public class Picture{

	private static BufferedImage image;
	
	public static void main(String[] args){
	//public Picture(String filepath){
		Scanner input = new Scanner(System.in);
		System.out.println("Please input the name of the file you'd like me to find.");
		String filepath = input.nextLine();
		
		try{
			image = ImageIO.read(new File(filepath));
			System.out.println("File found. Info: ");
			
			System.out.println(image.getHeight());
			System.out.println(image.getWidth());
			//System.out.println(image.getType()); - Unlikely to be helpful.
			//System.out.println(image.) - Verify how to get a filehash!
		}
		catch(Exception e){
			
		}
	}
	
	public boolean isEqualTo(Picture picToCompareAgainst){
		//Three different algorithms.
		//1. Check every pixel.
		//2. Check half the pixels.
		//3. Check file hashes.
		return false;
	}

}