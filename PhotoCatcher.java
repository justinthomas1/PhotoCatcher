import java.util.*;
import java.io.*;

public class PhotoCatcher{
	
	private ArrayList<Picture> listOfPictures = new ArrayList<Picture>();
	
	public static void main(String[] args){
		//Alpha code - just making sure this stuff works!
		Scanner input = new Scanner(System.in);
		System.out.println("Please input the name of the file you'd like me to find.");
		String filepath = input.nextLine();
		
		Picture image1 = new Picture("Images/" + filepath);
		
		System.out.println("And another one, please!.");
		filepath = input.nextLine();
		
		Picture image2 = new Picture("Images/" + filepath);
		
		System.out.println("Are these two similar? Answer: " + image1.isEqualTo(image2));
		
		//Beta code - get all files in the current directory.
		//TODO!
		File currentDirectory = new File(".");
		File[] listOfFiles = currentDirectory.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			System.out.println("File: " + listOfFiles[i].getName());
		  } else if (listOfFiles[i].isDirectory()) {
			System.out.println("Directory: " + listOfFiles[i].getName());
		  }
		}
		
	}
}



/*
Source Log:

https://www.dyclassroom.com/image-processing-project/how-to-read-and-write-image-file-in-java
https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html
https://docs.oracle.com/javase/7/docs/api/java/io/File.html
https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder


*/