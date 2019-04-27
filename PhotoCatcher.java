import java.util.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class PhotoCatcher extends JPanel{
	
	private static ArrayList<Picture> listOfPictures = new ArrayList<Picture>();
	private static String[] arrayOfPictureFiletypes = {"jpg","jpeg","png","tiff","gif","bmp"};
	private static ArrayList<String> listOfPictureFiletypes = new ArrayList<String>(Arrays.asList(arrayOfPictureFiletypes));
	
	private static ArrayList<File> listOfDirectories;
	
	
	private static JFrame jf;
	private static PhotoCatcher catcher;
	
	private JFileChooser jfc;
	
	private JPanel sideBar;
	private JPanel settingsPanel;
	
	private JTextArea dirList;
	private JTextArea extList;
	
	private JButton addExtension;
	private JButton removeExtension;
	
	
	private String[] searchTypeStrings = {"Duplicates","Similar"};
	private JComboBox<String> searchType;
	
	private String[] algorithmTypeStringsDuplicates = {"Full Picture","Half Picture","Filehash"};
	private String[] algorithmTypeStringsSimilar = {"Full Picture","Half Picture"};
	private String[][] algorithmTypeOptions = {algorithmTypeStringsDuplicates, algorithmTypeStringsSimilar};
	private int algorithmTypeInt = 0;
	private JComboBox<String> algorithmType;
	
	private JSlider rgbGraceValue;
	private JSlider percentSimilarity;
	
	private JCheckBox hardCompare;
	
	public static void main(String[] args){
		//Set up the GUI
		jf = new JFrame();
		
		catcher = new PhotoCatcher();
		jf.add(catcher);
		jf.pack();
		jf.setSize(900,600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("Photo Catcher");
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	
	public PhotoCatcher(){
		//At some point, set the icon?
		/* try{			
			Image image= new ImageIcon(getClass().getResource("/resources/Icon.png")).getImage();
			jf.setIconImage(image);
		}
		catch(Exception e){
			
		} */
		
		//Set the layout of the whole thing
		setLayout(new BorderLayout());
		
		//Create the sidebar - Make sure it goes from top to bottom!
		sideBar= new JPanel();
		sideBar.setLayout(new GridBagLayout());
		GridBagConstraints sideBarConstraints = new GridBagConstraints();
		sideBarConstraints.fill = GridBagConstraints.VERTICAL;
		
		//Adding the "Add a directory" button to the sidebar right at the top.
		//Much of this section was found on the Oracle tutorial "How to Use GridBagLayout"
		JButton addDirButton = new JButton("Add a directory");
		AddDirectoryListener dirListener = new AddDirectoryListener();
		addDirButton.addActionListener(dirListener);
		sideBarConstraints.fill = GridBagConstraints.HORIZONTAL;
		sideBarConstraints.gridheight = 1;
		sideBarConstraints.gridx = 0;
		sideBarConstraints.gridy = 0;
		sideBar.add(addDirButton, sideBarConstraints);
		
		//Directories list created and added to side bar below the button.
		dirList= new JTextArea("No directories selected.");
		dirList.setEditable(false);
		sideBarConstraints.fill = GridBagConstraints.BOTH;
		sideBarConstraints.ipady = 40;      //make this component tall
		sideBarConstraints.ipadx = 70;
		sideBarConstraints.weighty = 10.0; //Makes sure it takes up as much vertical free space as possible.
		sideBarConstraints.gridheight = 4;
		sideBarConstraints.gridx = 0;
		sideBarConstraints.gridy = 1;
		sideBar.add(dirList, sideBarConstraints);
		
		
		//Creates the JPanel for the settings.
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(1,0));
		
		
		//Create the extensions JPanel
		JPanel extensionsPanel = new JPanel();
		extensionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints extensionsConstraints = new GridBagConstraints();
		
		
		//Adds the "Extensions" title into the extensions panel.
		JLabel extensionsTitle = new JLabel("Extensions", SwingConstants.CENTER);
		extensionsConstraints.fill = GridBagConstraints.HORIZONTAL;
		extensionsConstraints.ipady = 1;
		extensionsConstraints.weightx = 1.0;
		extensionsConstraints.weighty = 0.0;
		extensionsConstraints.gridwidth = 2;
		extensionsConstraints.gridx = 0;
		extensionsConstraints.gridy = 0;
		extensionsPanel.add(extensionsTitle, extensionsConstraints);
		
		
		//Creates a Text Area for the listed extensions and adds that to the extensions JPanel.
		extList = new JTextArea("No extensions.");
		extList.setEditable(false);
		extensionsConstraints.fill = GridBagConstraints.BOTH;
		extensionsConstraints.ipady = 190;      //make this component tall
		extensionsConstraints.weightx = 1.0;
		extensionsConstraints.weighty = 20.0;
		extensionsConstraints.gridheight = 3;
		extensionsConstraints.gridwidth = 2;
		extensionsConstraints.gridx = 0;
		extensionsConstraints.gridy = 1;
		extensionsPanel.add(extList, extensionsConstraints);
		
		
		//Creates that dang JPanel that's especially for those little buttons on the bottom of the extensions
		//text box because apparently GridBagLayout just doesn't want to work on something like this.
		JPanel theStupidButtonPanelBecauseJavaSaysItDoesntWorkAnyOtherWayScrewJavaMan = new JPanel();
		theStupidButtonPanelBecauseJavaSaysItDoesntWorkAnyOtherWayScrewJavaMan.setLayout(new GridLayout(0,2));
		
		addExtension = new JButton("+");
		AddExtensionListener addExt = new AddExtensionListener();
		addExtension.addActionListener(addExt);
		
		removeExtension = new JButton("-");
		RemoveExtensionListener removeExt = new RemoveExtensionListener();
		removeExtension.addActionListener(removeExt);
		
		//Patchnote (4/26/19) - I may have found out what I did wrong. Yes Java, what /I/ did wrong.
		//Please come back. I didn't mean what I said. We can make awesome GUIs together using your admittedly
		//confusing and convoluted GridBagLayout, but still understanding how we did it the whole time.
		theStupidButtonPanelBecauseJavaSaysItDoesntWorkAnyOtherWayScrewJavaMan.add(addExtension);
		theStupidButtonPanelBecauseJavaSaysItDoesntWorkAnyOtherWayScrewJavaMan.add(removeExtension);
		
		//Adds the dang JPanel to the extensions panel.
		extensionsConstraints.fill = GridBagConstraints.HORIZONTAL;
		extensionsConstraints.gridwidth = 2;
		extensionsConstraints.ipady = 10;
		extensionsConstraints.weightx = 1.0;
		extensionsConstraints.weighty = 0.0;
		extensionsConstraints.gridx = 0;
		extensionsConstraints.gridy = 4;
		extensionsConstraints.anchor = GridBagConstraints.PAGE_END;
		extensionsPanel.add(theStupidButtonPanelBecauseJavaSaysItDoesntWorkAnyOtherWayScrewJavaMan, extensionsConstraints);
		
		
		
		
		
		
		
		
		
		
		//Create the extensions JPanel
		JPanel toggleSettingPanel = new JPanel();
		toggleSettingPanel.setLayout(new GridLayout(9,0));
		
		//Create the toggle settings JPanel
		toggleSettingPanel.add(new JLabel("Search Type"));
		searchType = new JComboBox<>(searchTypeStrings);
		ChangeSearchType changeSearchType = new ChangeSearchType();
		searchType.addChangeListener(changeSearchType);
		toggleSettingPanel.add(searchType);
		
		toggleSettingPanel.add(new JLabel("Algorithm"));
		algorithmType = new JComboBox<>(algorithmTypeOptions[algorithmTypeInt]);
		toggleSettingPanel.add(algorithmType);
		
		toggleSettingPanel.add(new JLabel("RGB Grace Value: 30"));
		rgbGraceValue = new JSlider(JSlider.HORIZONTAL, 0, 255, 30);
		//rgbGraceValue.addChangeListener();
		toggleSettingPanel.add(rgbGraceValue);
		
		
		toggleSettingPanel.add(new JLabel("Percent Similarity for Match: 60%"));
		percentSimilarity = new JSlider(JSlider.HORIZONTAL, 0, 100, 60);
		//percentSimilarity.addChangeListener();
		toggleSettingPanel.add(percentSimilarity);
		
		hardCompare = new JCheckBox("Hard Compare");
		//hardCompare.addItemListener();
		toggleSettingPanel.add(hardCompare);
		
		//If all else fails, I could always do two grid layouts: one that holds the Settings title up top, followed by the two settings planels in another grid layout.
		JPanel actualSettingsPanel = new JPanel();
		actualSettingsPanel.setLayout(new GridLayout(0,2));
		
		actualSettingsPanel.add(extensionsPanel);
		actualSettingsPanel.add(toggleSettingPanel);
		
		
		
		
		
		settingsPanel.add(actualSettingsPanel);
		
		
		
		//Creates a start button. Easy!
		JButton startButton = new JButton("Start");
		StartListener startListener = new StartListener();
		startButton.addActionListener(startListener);
		
		add(sideBar, BorderLayout.WEST);
		add(settingsPanel, BorderLayout.CENTER);
		add(startButton, BorderLayout.SOUTH);
		
		listOfDirectories = new ArrayList<File>();
		
		updateExtensions();
		updateDirectories();
	}
	
	public static void searchAndUpdate(){
		
		/* try{
			//File directory = new File(System.getProperty("user.dir"));
			//listOfDirectories.append(directory);
		}
		catch(Error e){
			
		} */
		
		
		
		
		
		//File currentDirectory = new File(System.getProperty("user.dir"));
		for(int i=0; i<listOfDirectories.size(); i++){
			searchAllDirectories(listOfDirectories.get(i));
		}
		
		clearFileDuplicates();
		catchThePhotoDuplicates();
		
		String listOfPicturesAsString = "";
		for(int i=0; i<listOfPictures.size(); i++){
			listOfPicturesAsString += (listOfPictures.get(i).getFilePathAsString() + "\n");
		}
		System.out.println(listOfPicturesAsString);
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
				for(int j=0; j<listOfPictureFiletypes.size(); j++){
					if(filetype.toLowerCase().equals(listOfPictureFiletypes.get(j))){
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
				
				if(picture1.isEqualTo(picture2, true, 2, 15, 50.0, false)){
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
	
	
	
	
	
	//Action listeners unite!
	//TODO: Make this happen!
	private class StartListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				//Start the algorithm!
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private class AddExtensionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				String newFiletype = JOptionPane.showInputDialog(new JFrame(), "Enter the file extension.", "Photo Catcher", JOptionPane.WARNING_MESSAGE);
				
				if(!newFiletype.equals("") && !newFiletype.equals("null") && newFiletype!=null){
					listOfPictureFiletypes.add(newFiletype);
				}
				
				updateExtensions();
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private class RemoveExtensionListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				listOfPictureFiletypes.remove(listOfPictureFiletypes.size() - 1);
				
				updateExtensions();
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private void updateExtensions(){
		String output = "";
		String newline = "\n";
		
		for(int i=0; i<listOfPictureFiletypes.size(); i++){
			output += listOfPictureFiletypes.get(i) + newline;
		}
		
		extList.setText(null);
		extList.append(output);
	}
	
	private void updateDirectories(){
		String output = "";
		String newline = "\n";
		
		for(int i=0; i<listOfDirectories.size(); i++){
			output += listOfDirectories.get(i) + newline;
		}
		
		dirList.setText(null);
		dirList.append(output);
	}
	
	//TODO: Make this happen!
	private class AddDirectoryListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				//JFC for this one. See PowerPointKnockOff.
				//Make sure not to accept a non-directory!
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	//TODO: Make this happen! It's for the dropdown changing.
	private class ChangeSearchType implements ChangeListener{
		
		@Override
		public void stateChanged(ChangeEvent e) {
			try{
				algorithmTypeInt = searchType.getSelectedIndex();
				algorithmType = new JComboBox<>(algorithmTypeOptions[algorithmTypeInt]);
			}
			catch(Exception ex){
				
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
https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
https://stackoverflow.com/questions/15455655/adding-buttons-below-jtextarea

https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
https://docs.oracle.com/javase/tutorial/uiswing/components/slider.html
https://stackoverflow.com/questions/18162985/found-raw-type-jcombobox
https://docs.oracle.com/javase/tutorial/uiswing/components/button.html
https://docs.oracle.com/javase/7/docs/api/javax/swing/event/ChangeListener.html
*/