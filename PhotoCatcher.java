import java.util.*;
import java.io.*;

public class PhotoCatcher{
	
	private static ArrayList<Picture> listOfPictures = new ArrayList<Picture>();
	private static String[] listOfPictureFiletypes = {"jpg","jpeg","png","tiff","gif","bmp"};
	
	public static void main(String[] args){
		//Beta code - get all files in the current directory (and recursively the subdirectories).
		File currentDirectory = new File(System.getProperty("user.dir"));
		searchAllDirectories(currentDirectory);
		
		clearFileDuplicates();
		
		catchThePhotoDuplicates();
		
		System.out.println("\n\n\nThe List:");
		System.out.println("Size: " + listOfPictures.size());
		for(int i=0; i<listOfPictures.size(); i++){
			System.out.println(listOfPictures.get(i).getFilePathAsString());
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
				
				//Verify that the file extension is one of the listed extensions for the class.
				for(int j=0; j<listOfPictureFiletypes.length; j++){
					if(filetype.toLowerCase().equals(listOfPictureFiletypes[j])){
						isAnImage = true;
						break;
					}
				}
				
				//If it is an image, proceed with adding it to the list.
				if(isAnImage){
					listOfPictures.add(new Picture(filepath));
				}
		  }
		  else if(listOfFiles[i].isDirectory()){
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
			String image1Path = listOfPictures.get(i).getFilePathAsString();
			for(int j=1+i; j<listOfPictures.size(); j++){
				String image2Path = listOfPictures.get(j).getFilePathAsString();
				
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
				
				if(picture1.isEqualTo(picture2, true, 2, 15, 100, false)){
					//Removes the element from the arraylist, then decrements the j value (that way we won't skip over an arraylist element).
					//System.out.println("First pic: " + picture1.getFilename());
					//System.out.println("Second pic: " + picture2.getFilename());
					//System.out.println("Removing " + listOfPictures.get(j).getFilename());
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
https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html

*/