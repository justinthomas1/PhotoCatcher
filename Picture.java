import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Picture{

	private File file;
	private BufferedImage image;
	
	
	//public static void main(String[] args){
	public Picture(String filepath){
		try{
			file = new File(filepath);
			image = ImageIO.read(file);
			System.out.println("File found. Info below:");
			
			System.out.println(image.getHeight());
			System.out.println(image.getWidth());
			System.out.println(file.getName());
		}
		catch(Exception e){
			System.out.println("Error: Image at " + filepath + " couldn't be opened.");
		}
	}
	
	public File getFile(){
		return file;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public boolean isEqualTo(Picture picToCompareAgainst){
		//First, some stress checks
		int picOneHeight = image.getHeight();
		int picOneWidth = image.getWidth();
		
		int picTwoHeight = picToCompareAgainst.getImage().getHeight();
		int picTwoWidth = picToCompareAgainst.getImage().getWidth();
		
		String picOneName = file.getName();
		String picTwoName = picToCompareAgainst.getFile().getName();
		
		if(picOneHeight != picTwoHeight){
			return false;
		}
		if(picOneWidth != picTwoWidth){
			return false;
		}
		if(!picOneName.equals(picTwoName)){
			return false;
		}
		
		//Three different algorithms.
		//1. Check every pixel.
		//2. Check half the pixels.
		//3. Check file hashes.
		return true;
	}

}