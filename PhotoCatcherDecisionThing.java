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

	//catchThePhotoDuplicates();
	private int JFwidth;
	private int JFheight;
	private ArrayList<Picture> listOfPictures;
	private ArrayList<String> listOfDeletedPictures;
	private boolean searchTypeBoolean;
	private int algorithmTypeInt;
	private int rgbGraceValueAmount;
	private double percentSimilarityAmount;
	private boolean hardCompare;

	private JLabel picture1Label;
	private JLabel picture2Label;
	private JTextArea pic1Filepath;
	private JTextArea pic2Filepath;
	
	private double image1FractionNumber = 1.0;
	private double image2FractionNumber = 1.0;
	
	int imageMaxWidth;
	int imageMaxHeight;
	
	private boolean leftImageDeleteButtonPressed = false;
	private boolean rightImageDeleteButtonPressed = false;
	private boolean cancelButtonPressed = false;
	
	
	private Thread thready;
	
	
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
		picture2Label.setMinimumSize(new Dimension(JFwidth/3, JFheight/3));
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
	
	
	public void catchThePhotoDuplicates(){
		
		Runnable runnable = new Runnable(){
            @Override
            public void run() {
				for(int i=0; i<listOfPictures.size(); i++){
					//Important: This is the actual code for comparing the different files to each other.
					Picture picture1 = listOfPictures.get(i);
					for(int j=1+i; j<listOfPictures.size(); j++){
						Picture picture2 = listOfPictures.get(j);
						
						if(picture1.isEqualTo(picture2, searchTypeBoolean, algorithmTypeInt, rgbGraceValueAmount, (double) percentSimilarityAmount, hardCompare)){
							
							updateThePictures(picture1, picture2);
							
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
	
	public void updateThePictures(Picture picture1, Picture picture2){
		try{
			//Find out how small to make the picture so it still fits in the GUI.
			boolean imageTooBig = true;
			
			image1FractionNumber = 1.0;
			
			while(imageTooBig){
				if((int) ((double) picture1.getHeight()*(1/image1FractionNumber)) <= imageMaxHeight){
					if((int) ((double) picture1.getWidth()*(1/image1FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image1FractionNumber+=0.1;
			}
			
			imageTooBig = true;
			
			image2FractionNumber = 1.0;
			
			while(imageTooBig){
				if((int) ((double) picture2.getHeight()*(1/image2FractionNumber)) <= imageMaxHeight){
					if((int) ((double) picture2.getWidth()*(1/image2FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image2FractionNumber+=0.1;
			}
			
			Image picture1Resized = picture1.getImage().getScaledInstance((int) ((double) picture1.getWidth()*(1/image1FractionNumber)), (int) ((double) picture1.getHeight()*(1/image1FractionNumber)), Image.SCALE_SMOOTH);
			Image picture2Resized = picture2.getImage().getScaledInstance((int) ((double) picture2.getWidth()*(1/image2FractionNumber)), (int) ((double) picture2.getHeight()*(1/image2FractionNumber)), Image.SCALE_SMOOTH);
			
			picture1Label.setIcon(new ImageIcon(picture1Resized));
			picture2Label.setIcon(new ImageIcon(picture2Resized));
			pic1Filepath.setText(picture1.getFilepath());
			pic2Filepath.setText(picture2.getFilepath());
		}
		catch(Exception e){
		}
	}
	
	
}
