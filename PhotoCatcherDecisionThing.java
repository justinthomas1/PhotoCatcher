import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.nio.file.*;
import java.text.SimpleDateFormat;

public class PhotoCatcherDecisionThing extends JPanel{

	private static JFrame jf;
	private static int JFwidth;
	private static int JFheight;
	private static ArrayList<Picture> listOfPictures;
	private static ArrayList<String> listOfDeletedPictures;
	private static boolean searchTypeBoolean;
	private static int algorithmTypeInt;
	private static int rgbGraceValueAmount;
	private static double percentSimilarityAmount;
	private static boolean hardCompare;

	private static JLabel picture1Label;
	private static JLabel picture2Label;
	private static JTextArea pic1Filepath;
	private static JTextArea pic2Filepath;
	private static Picture pic1OnLabel;
	private static Picture pic2OnLabel;
	
	private static double image1FractionNumber = 1.0;
	private static double image2FractionNumber = 1.0;
	
	private static int imageMaxWidth;
	private static int imageMaxHeight;
	
	private static boolean leftImageDeleteButtonPressed = false;
	private static boolean rightImageDeleteButtonPressed = false;
	private static boolean cancelButtonPressed = false;
	
	
	private static Thread thready;
	
	
	public static void instantiateJFrameAndStuffInIt(int JFwidth, int JFheight, ArrayList<Picture> listOfPictures, boolean searchTypeBoolean, int algorithmTypeInt, int rgbGraceValueAmount, double percentSimilarityAmount, boolean hardCompare, Image icon){
		jf = new JFrame();
		PhotoCatcherDecisionThing decisionThing = new PhotoCatcherDecisionThing(JFwidth, JFheight, listOfPictures, searchTypeBoolean, algorithmTypeInt, rgbGraceValueAmount, (double) percentSimilarityAmount, hardCompare);
		jf.add(decisionThing);
		jf.pack();
		jf.setSize(JFwidth,JFheight);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("Photo Catcher");
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		//Set the icon
		try{
			jf.setIconImage(icon);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		jf.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				updateValuesAndPictureSizes();
			}
		});
	}
	
	public PhotoCatcherDecisionThing(int JFwidth, int JFheight, ArrayList<Picture> listOfPictures, boolean searchTypeBoolean, int algorithmTypeInt, int rgbGraceValueAmount, double percentSimilarityAmount, boolean hardCompare){
		this.JFwidth = JFwidth;
		this.JFheight = JFheight;
		this.listOfPictures = listOfPictures;
		this.searchTypeBoolean = searchTypeBoolean;
		this.algorithmTypeInt = algorithmTypeInt;
		this.rgbGraceValueAmount = rgbGraceValueAmount;
		this.percentSimilarityAmount = percentSimilarityAmount;
		this.hardCompare = hardCompare;
		
		imageMaxWidth = JFwidth/2;
		imageMaxHeight = JFheight/2;
		
		/* System.out.println("Checking the correct values entered:");
		System.out.println("searchTypeBoolean:" + searchTypeBoolean);
		System.out.println("algorithmTypeInt:" + algorithmTypeInt);
		System.out.println("rgbGraceValueAmount:" + rgbGraceValueAmount);
		System.out.println("percentSimilarityAmount:" + percentSimilarityAmount);
		System.out.println("hardCompare:" + hardCompare); */
		
		setLayout(new BorderLayout());
		
		JPanel picsAndLabels = new JPanel();
		picsAndLabels.setLayout(new GridBagLayout());
		GridBagConstraints picsAndLabelsConstraints = new GridBagConstraints();
		
		
		picture1Label = new JLabel("", SwingConstants.CENTER);
		picture1Label.setMinimumSize(new Dimension(imageMaxWidth, imageMaxHeight));
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 2;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 0;
		picsAndLabelsConstraints.gridy = 0;
		picsAndLabels.add(picture1Label, picsAndLabelsConstraints);
		
		
		
		picture2Label = new JLabel("", SwingConstants.CENTER);
		picture2Label.setMinimumSize(new Dimension(imageMaxWidth, imageMaxHeight));
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 2;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 2;
		picsAndLabelsConstraints.gridy = 0;
		picsAndLabels.add(picture2Label, picsAndLabelsConstraints);
		
		
		
		
		
		
		
		pic1Filepath = new JTextArea(3, 20);
		pic1Filepath.setWrapStyleWord(true);
		pic1Filepath.setLineWrap(true);
		pic1Filepath.setOpaque(false);
		pic1Filepath.setEditable(false);
		pic1Filepath.setFocusable(false);
		pic1Filepath.setBackground(UIManager.getColor("Label.background"));
		pic1Filepath.setFont(UIManager.getFont("Label.font"));
		pic1Filepath.setBorder(UIManager.getBorder("Label.border"));

		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 1;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 0;
		picsAndLabelsConstraints.gridy = 2;
		picsAndLabels.add(pic1Filepath, picsAndLabelsConstraints);
		
		
		
		pic2Filepath = new JTextArea(3, 20);
		pic2Filepath.setWrapStyleWord(true);
		pic2Filepath.setLineWrap(true);
		pic2Filepath.setOpaque(false);
		pic2Filepath.setEditable(false);
		pic2Filepath.setFocusable(false);
		pic2Filepath.setBackground(UIManager.getColor("Label.background"));
		pic2Filepath.setFont(UIManager.getFont("Label.font"));
		pic2Filepath.setBorder(UIManager.getBorder("Label.border"));
		
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 1;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 2;
		picsAndLabelsConstraints.gridy = 2;
		picsAndLabels.add(pic2Filepath, picsAndLabelsConstraints);
		
		
		JPanel buttonsAtBottom = new JPanel();
		buttonsAtBottom.setLayout(new GridLayout(0,3));
		
		JButton button1 = new JButton("Delete Left Picture");
		DeleteLeftPictureButtonListener deleteLeftPictureButtonListener = new DeleteLeftPictureButtonListener();
		button1.addActionListener(deleteLeftPictureButtonListener);
		buttonsAtBottom.add(button1);
		
		JButton button2 = new JButton("Delete Right Picture");
		DeleteRightPictureButtonListener deleteRightPictureButtonListener = new DeleteRightPictureButtonListener();
		button2.addActionListener(deleteRightPictureButtonListener);
		buttonsAtBottom.add(button2);
		
		JButton button3 = new JButton("Skip");
		SkipButtonListener skipButtonListener = new SkipButtonListener();
		button3.addActionListener(skipButtonListener);
		buttonsAtBottom.add(button3);
		
		
		add(picsAndLabels, BorderLayout.CENTER);
		add(buttonsAtBottom, BorderLayout.SOUTH);
		
		listOfDeletedPictures = new ArrayList<String>();
		
		try{
			catchThePhotoDuplicates();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	private class DeleteLeftPictureButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				leftImageDeleteButtonPressed = true;
				
				// Resume
                synchronized(thready){
                    thready.notify();
                }
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private class DeleteRightPictureButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				rightImageDeleteButtonPressed = true;
				
				// Resume
                synchronized(thready){
                    thready.notify();
                }
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private class SkipButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
				cancelButtonPressed = true;
				
				// Resume
                synchronized(thready){
                    thready.notify();
                }
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	public static void updateValuesAndPictureSizes(){
		JFwidth = jf.getWidth();
		JFheight = jf.getHeight();
		
		imageMaxWidth = JFwidth/2;
		imageMaxHeight = JFheight/2;
		
		updateThePictures();
	}
	
	public void catchThePhotoDuplicates(){
		
		Runnable runnable = new Runnable(){
            @Override
            public void run() {
				for(int i=0; i<listOfPictures.size(); i++){
					//Important: This is the actual code for comparing the different files to each other.
					pic1OnLabel = listOfPictures.get(i);
					for(int j=1+i; j<listOfPictures.size(); j++){
						pic2OnLabel = listOfPictures.get(j);
						
						if(pic1OnLabel.isEqualTo(pic2OnLabel, searchTypeBoolean, algorithmTypeInt, rgbGraceValueAmount, (double) percentSimilarityAmount, hardCompare)){
							
							updateThePictures();
							
							while(!leftImageDeleteButtonPressed && !rightImageDeleteButtonPressed && !cancelButtonPressed){
								synchronized(thready){
									try{
										thready.wait();
									}
									catch (InterruptedException e) {
									}
								}
							}
							
							if(leftImageDeleteButtonPressed){
								//Removes the element from the arraylist, then decrements the i value (that way we won't skip over an arraylist element) and appropriately restarts the j value.
								listOfDeletedPictures.add(listOfPictures.get(i).getFilepath());
								listOfPictures.get(i).getFile().delete();
								listOfPictures.remove(i);
								i--;
								leftImageDeleteButtonPressed = false;
								break;
							}
							else if(rightImageDeleteButtonPressed){
								//Removes the element from the arraylist, then decrements the j value (that way we won't skip over an arraylist element).
								listOfDeletedPictures.add(listOfPictures.get(j).getFilepath());
								listOfPictures.get(j).getFile().delete();
								listOfPictures.remove(j);
								j--;
								rightImageDeleteButtonPressed = false;
							}
							else if(cancelButtonPressed){
								cancelButtonPressed = false;
							}
						}
					}
				}
				JOptionPane.showMessageDialog(new JFrame(),("Comparing done! Have a nice day!"));
				
				//Make an output log.
				String pattern = "MM-dd-yyyy HH-mm";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				String date = simpleDateFormat.format(new Date());
				
				try{
					FileWriter writer= new FileWriter(new File(date + ".txt"));
					writer.write("Deleted Files " + date + ":");
					writer.write(System.getProperty("line.separator"));
					
					if(listOfDeletedPictures.size()==0){
						writer.write("There doesn't appear to be anything here...");
					}
					else{
						for(int i=0; i<listOfDeletedPictures.size(); i++){
							writer.write(listOfDeletedPictures.get(i));
							writer.write(System.getProperty("line.separator"));
						}
					}		
					
					writer.close();
				}
				catch(Exception e){
					
				}
				
				//Fin
				System.exit(0);
            }
        };
        thready = new Thread(runnable);
        thready.start();
	}
	
	public static void updateThePictures(){
		try{
			//Find out how small to make the picture so it still fits in the GUI.
			boolean imageTooBig = true;
			
			image1FractionNumber = 1.0;
			
			while(imageTooBig){
				if((int) ((double) pic1OnLabel.getHeight()*(1/image1FractionNumber)) <= imageMaxHeight){
					if((int) ((double) pic1OnLabel.getWidth()*(1/image1FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image1FractionNumber+=0.1;
			}
			
			imageTooBig = true;
			
			image2FractionNumber = 1.0;
			
			while(imageTooBig){
				if((int) ((double) pic2OnLabel.getHeight()*(1/image2FractionNumber)) <= imageMaxHeight){
					if((int) ((double) pic2OnLabel.getWidth()*(1/image2FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image2FractionNumber+=0.1;
			}
			
			Image picture1Resized = pic1OnLabel.getImage().getScaledInstance((int) ((double) pic1OnLabel.getWidth()*(1/image1FractionNumber)), (int) ((double) pic1OnLabel.getHeight()*(1/image1FractionNumber)), Image.SCALE_SMOOTH);
			Image picture2Resized = pic2OnLabel.getImage().getScaledInstance((int) ((double) pic2OnLabel.getWidth()*(1/image2FractionNumber)), (int) ((double) pic2OnLabel.getHeight()*(1/image2FractionNumber)), Image.SCALE_SMOOTH);
			
			picture1Label.setIcon(new ImageIcon(picture1Resized));
			picture2Label.setIcon(new ImageIcon(picture2Resized));
			pic1Filepath.setText(pic1OnLabel.getFilepath());
			pic2Filepath.setText(pic2OnLabel.getFilepath());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
