import java.util.*;
import java.io.*;

public class PhotoCatcher{
	
	private static ArrayList<Picture> listOfPictures = new ArrayList<Picture>();
	private static String[] listOfPictureFiletypes = {"jpg","jpeg","png","tiff","gif","bmp"};
	
	public static void main(String[] args){
		/*System.out.println("Are these two similar? Answer: " + image1.isEqualTo(image2)); */
		
		//Beta code - get all files in the current directory.
		File currentDirectory = new File(System.getProperty("user.dir"));
		searchAllDirectories(currentDirectory);
		
		//Testing to see what happens when I add a different directory.
/* 		currentDirectory = new File("F:\\Until Flash Drive Fixes (Senior)\\Spring\\Capstone");
		searchAllDirectories(currentDirectory);
		
		currentDirectory = new File("F:\\Until Flash Drive Fixes (Senior)\\Spring");
		searchAllDirectories(currentDirectory); */
		
		System.out.println("First checkpoint");
		System.out.println("\n\n\nThe List:");
		System.out.println("Size: " + listOfPictures.size());
		for(int i=0; i<listOfPictures.size(); i++){
			//System.out.println(listOfPictures.get(i).getWidth());
			System.out.println(listOfPictures.get(i).getFilePath());
		}
		
		clearFileDuplicates();
		
		System.out.println("\n\n\nThe List:");
		System.out.println("Size: " + listOfPictures.size());
		for(int i=0; i<listOfPictures.size(); i++){
			System.out.println(listOfPictures.get(i).getFilePath());
		}
		
		catchThePhotoDuplicates();
		
		System.out.println("\n\n\nThe List:");
		System.out.println("Size: " + listOfPictures.size());
		for(int i=0; i<listOfPictures.size(); i++){
			System.out.println(listOfPictures.get(i).getFilePath());
		}
	}
	
	public static void searchAllDirectories(File directoryPath){
		//Start by passing the initial directory, then pass all directories to this afterwards.
		File[] listOfFiles = directoryPath.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++){
		  if(listOfFiles[i].isFile()){
				String filepath = listOfFiles[i].getAbsolutePath().toString();
				String filetype = stringToFileType(filepath);
				boolean isAnImage = false;
				
				System.out.println("Filepath: " + filepath);
				
				//Verify that the file extension is one of the listed extensions for the class.
				for(int j=0; j<listOfPictureFiletypes.length; j++){
					if(filetype.toLowerCase().equals(listOfPictureFiletypes[j])){
						isAnImage = true;
						break;
					}
				}
				
				//If it is an image, proceed with adding it to the list.
				if(isAnImage){
					System.out.println("File " + filepath + " is an image.");
					listOfPictures.add(new Picture(filepath));
					System.out.println(listOfPictures.get(0).getFilename());
					System.out.println(listOfPictures.size());
					
					//System.out.println(filenameWithoutFiletype);
					System.out.println("File: " + listOfFiles[i].getName());
				}
		  }
		  else if(listOfFiles[i].isDirectory()){
				System.out.println("-----------------Directory: " + listOfFiles[i].getName() + "---------------------------");
				searchAllDirectories(new File(listOfFiles[i].getAbsolutePath().toString()));
		  }
		}
	}
	
	public static String stringToFileType(String filenameWithFiletype){
		int locationOfPeriod = filenameWithFiletype.lastIndexOf('.');
		String filenameWithoutFiletype = filenameWithFiletype.substring(locationOfPeriod+1, filenameWithFiletype.length());
		
		return filenameWithoutFiletype;
	}
	
	public static void clearFileDuplicates(){
		//Important: this is only for clearing out occurences of the exact same file from the same directory.
		//This is not killing off the duplicate files. This is for times when the exact same thing, path and all, is being stored.
		//This will likely occur if the user can choose multiple directories. This ensures the same file isn't compared against itself, giving a false positive.
		for(int i=0; i<listOfPictures.size(); i++){
			String image1Path = listOfPictures.get(i).getFilePath();
			for(int j=1+i; j<listOfPictures.size(); j++){
				String image2Path = listOfPictures.get(j).getFilePath();
				
				if(image1Path.equals(image2Path)){
					//Removes the element from the arraylist, then decrements the j value (that way we won't skip over an arraylist element).
					listOfPictures.remove(j);
					j--;
				}
			}
		}
	}
	
	public static void catchThePhotoDuplicates(){
		//Important: This is the actual code for comparing the different files to each other.
		for(int i=0; i<listOfPictures.size(); i++){
			Picture picture1 = listOfPictures.get(i);
			for(int j=1+i; j<listOfPictures.size(); j++){
				Picture picture2 = listOfPictures.get(j);
				
				if(picture1.isEqualTo(picture2, false, 1)){
					//Removes the element from the arraylist, then decrements the j value (that way we won't skip over an arraylist element).
					listOfPictures.remove(j);
					j--;
				}
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

https://stackoverflow.com/questions/4871051/getting-the-current-working-directory-in-java
*/