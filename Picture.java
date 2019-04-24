import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

//Used for the Hash finder.
import java.security.*;
import java.nio.*;
import java.nio.file.*;
import javax.xml.bind.*;

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
	
	public String getFilePathAsString(){
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
	
	//Code here is from the StackOverflow "Getting a File's MD5 Checksum in Java" post, response by user "assylias". I chose to use this instead of a 3rd party library so as to keep with the base Java libraries.
	public String getMD5Hash(){
		String actualHash = "";
		
		try{
			byte[] b = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			byte[] hash = MessageDigest.getInstance("MD5").digest(b);
			actualHash = DatatypeConverter.printHexBinary(hash);
		}
		catch(Exception e){
			
		}
		
		return actualHash;
	}
	
	
	/*
	isEqualTo
	----------------------------------------------------------------------------------
	
	Used to compare pictures with each other. Contains many algorithms to do so.
	
	Important note: the way this method works, it tries to find any way possible to prove them dissimilar. If it can't do so, the pictures are declared similar.
	
	
	Variables:
	
	picToCompareAgainst: The picture this picture is comparing to.
	checkSimilar: Whether you want to check if they're the same or similar.
	algorithmNumber: Whether to use the "compare all pixels" (1), "compare half pixels" (2), or "compare file hashes" (3)
	similarityNumber: The amount that a similar picture's RGB value can be and still be considered "similar". Lower means more precise similarities.
	acceptableSimilarity: The amount (1-100) that the picture should have pixels similar for the two pictures to be considered similar.
	hardCompare: Whether to stop comparing the pictures if the two have even one dissimilar pixel. Use this if acceptableSimilarity is turned off.
	*/
	public boolean isEqualTo(Picture picToCompareAgainst, boolean checkSimilar, int algorithmNumber, int similarityNumber, double acceptableSimilarity, boolean hardCompare){
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
			if(algorithmNumber==1){
				//Check every pixel (least efficient but most accurate)
				for(int i=0; i<width; i++){
					for(int j=0; j<height; j++){
						if(imageOne.getRGB(i,j) != imageTwo.getRGB(i,j)){
							return false;
						}
					}
				}
			}
			else if(algorithmNumber==2){
				//Check half the pixels (less accurate but more efficient)
				
				//The for loop will essentially not scan a certain amount of pixels,
				//that being equivalent to the height divided by the width of the picture.
				//This amount gets added on again with each loop iteration.
				
				//This way, with each iteration, you'll scan slightly less pixels
				//than the last, creating a sort of triangular scan of the picture. This
				//is a much more efficient method, though it sacrifices accuracy.
				int widthAmountToDiscount = height/width;
				int widthToDiscount = 0;
				for(int i=0; i<width; i++){
					for(int j=0; j<height-widthToDiscount; j++){
						if(imageOne.getRGB(i,j) != imageTwo.getRGB(i,j)){
							return false;
						}
					}
					widthToDiscount+=widthAmountToDiscount;
				}
			}
			else if(algorithmNumber==3){
				//Check file hashes
				return this.getMD5Hash().equals(picToCompareAgainst.getMD5Hash());
			}
		}
		//Now do the same but for similar files!
		else{
			if(algorithmNumber==1){
				//Check every pixel (least efficient but most accurate)
				int similarityCount = height*width;
				for(int i=0; i<width; i++){
					for(int j=0; j<height; j++){
						Color pic1Color = new Color(imageOne.getRGB(i,j));
						Color pic2Color = new Color(imageTwo.getRGB(i,j));
						
						//The second picture's pixel value should be somewhere in the range of +/- the similarityNumber.
						if(!(pic1Color.getRed()+similarityNumber >= pic2Color.getRed() && pic1Color.getRed()-similarityNumber <= pic2Color.getRed())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getGreen()+similarityNumber >= pic2Color.getGreen() && pic1Color.getGreen()-similarityNumber <= pic2Color.getGreen())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getBlue()+similarityNumber >= pic2Color.getBlue() && pic1Color.getBlue()-similarityNumber <= pic2Color.getBlue())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getAlpha()+similarityNumber >= pic2Color.getAlpha() && pic1Color.getAlpha()-similarityNumber <= pic2Color.getAlpha())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
					}
				}
				System.out.println("Here's a thing: " + this.getFilename() + " vs " + picToCompareAgainst.getFilename() + ": " +  (100 * ((int) ((double) similarityCount)/(double) (height*width))));
				if(100 * ((int) ((double) similarityCount)/(double) (height*width)) < acceptableSimilarity){
					return false;
				}
			}
			else if(algorithmNumber==2){
				//Check half the pixels (less accurate but more efficient). See above for a better explanation.
				int similarityCount = height*width;
				
				//System.out.println(similarityCount);
				
				int widthAmountToDiscount = height/width;
				int widthToDiscount = 0;
				for(int i=0; i<width; i++){
					for(int j=0; j<height-widthToDiscount; j++){
						Color pic1Color = new Color(imageOne.getRGB(i,j));
						Color pic2Color = new Color(imageTwo.getRGB(i,j));
						
						//The second picture's pixel value should be somewhere in the range of +/- the similarityNumber.
						if(!(pic1Color.getRed()+similarityNumber >= pic2Color.getRed() && pic1Color.getRed()-similarityNumber <= pic2Color.getRed())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getGreen()+similarityNumber >= pic2Color.getGreen() && pic1Color.getGreen()-similarityNumber <= pic2Color.getGreen())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getBlue()+similarityNumber >= pic2Color.getBlue() && pic1Color.getBlue()-similarityNumber <= pic2Color.getBlue())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
						else if(!(pic1Color.getAlpha()+similarityNumber >= pic2Color.getAlpha() && pic1Color.getAlpha()-similarityNumber <= pic2Color.getAlpha())){
							if(hardCompare){
								return false;
							}
							else{
								similarityCount--;
							}
						}
					}
					widthToDiscount+=widthAmountToDiscount;
				}
				System.out.println("Here's a thing: " + this.getFilename() + " vs " + picToCompareAgainst.getFilename() + ": " +  (100 * ((int) ((double) similarityCount)/(double) (height*width))));
				if(100 * ((int) ((double) similarityCount)/(double) (height*width)) < acceptableSimilarity){
					return false;
				}
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		return true;
	}

}