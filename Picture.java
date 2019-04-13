import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Picture{

	private File file;
	private BufferedImage image;
	
	private int width;
	private int height;
	private String filename;
	
	//public static void main(String[] args){
	public Picture(String filepath){
		try{
			file = new File(filepath);
			image = ImageIO.read(file);
			
			height = image.getHeight();
			width = image.getWidth();
			filename = file.getName();
			
			System.out.println("File " + filename + " found.");
		}
		catch(Exception e){
			System.out.println("Error: Image at " + filepath + " couldn't be opened.");
		}
	}
	
	public String getFilePath(){
		return file.getAbsolutePath().toString();
	}
	
	public File getFile(){
		return file;
	}
	
	public String getFilename(){
		return filename;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public boolean isEqualTo(Picture picToCompareAgainst, boolean checkSimilar, int algorithmNumber){
		//First, some stress checks
		int picOneHeight = height;
		int picOneWidth = width;
		
		int picTwoHeight = picToCompareAgainst.getHeight();
		int picTwoWidth = picToCompareAgainst.getWidth();
		
		String picOneName = filename;
		String picTwoName = picToCompareAgainst.getFilename();
		
		if(picOneHeight != picTwoHeight){
			return false;
		}
		if(picOneWidth != picTwoWidth){
			return false;
		}
		
		/* //This one should be toggleable.
		if(!picOneName.equals(picTwoName)){
			return false;
		} */
		
		
		//Three different algorithms.
		//1. Check every pixel.
		//2. Check half the pixels.
		//3. Check file hashes.
		
		BufferedImage imageOne = image;
		BufferedImage imageTwo = picToCompareAgainst.getImage();
		
		
		if(!checkSimilar){
			//Check every pixel (least efficient but most accurate)
			for(int i=0; i<width; i++){
				for(int j=0; j<height; j++){
					if(imageOne.getRGB(i,j) != imageTwo.getRGB(i,j)){
						return false;
					}
				}
			}
			//Check half the pixels (less accurate but more efficient)
			//Check file hashes
		}
		//Now do the same but for similar files!
		else{
			//TODO
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return true;
	}

}