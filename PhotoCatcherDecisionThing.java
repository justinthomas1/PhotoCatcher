import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

//Just for test code. Maybe.
import java.awt.image.BufferedImage;

public class PhotoCatcherDecisionThing extends JPanel{

	//catchThePhotoDuplicates();
	private int JFwidth;
	private int JFheight;
	private ArrayList<Picture> listOfPictures;
	private boolean searchTypeBoolean;
	private int algorithmTypeInt;
	private int rgbGraceValueAmount;
	private double percentSimilarityAmount;
	private boolean hardCompare;

	private double image1FractionNumber = 1.0;
	private double image2FractionNumber = 1.0;
	
	
	public PhotoCatcherDecisionThing(int JFwidth, int JFheight, ArrayList<Picture> listOfPictures, boolean searchTypeBoolean, int algorithmTypeInt, int rgbGraceValueAmount, double percentSimilarityAmount, boolean hardCompare){
		this.JFwidth = JFwidth;
		this.JFheight = JFheight;
		this.listOfPictures = listOfPictures;
		this.searchTypeBoolean = searchTypeBoolean;
		this.algorithmTypeInt = algorithmTypeInt;
		this.rgbGraceValueAmount = rgbGraceValueAmount;
		this.percentSimilarityAmount = percentSimilarityAmount;
		this.hardCompare = hardCompare;
		
		int imageMaxWidth = JFwidth/2;
		int imageMaxHeight = JFheight/2;
		
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
		
		
		JLabel pic1 = new JLabel("", SwingConstants.CENTER);
		pic1.setMinimumSize(new Dimension(imageMaxWidth, imageMaxHeight));
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 2;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 0;
		picsAndLabelsConstraints.gridy = 0;
		picsAndLabels.add(pic1, picsAndLabelsConstraints);
		
		
		
		JLabel pic2 = new JLabel("", SwingConstants.CENTER);
		pic2.setMinimumSize(new Dimension(JFwidth/3, JFheight/3));
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 2;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 2;
		picsAndLabelsConstraints.gridy = 0;
		picsAndLabels.add(pic2, picsAndLabelsConstraints);
		
		
		
		
		
		
		
		JLabel pic1Filepath = new JLabel("Pic1Filepath", SwingConstants.CENTER);
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 1;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 0;
		picsAndLabelsConstraints.gridy = 2;
		picsAndLabels.add(pic1Filepath, picsAndLabelsConstraints);
		
		
		
		JLabel pic2Filepath = new JLabel("Pic2Filepath", SwingConstants.CENTER);
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
		buttonsAtBottom.add(button1);
		JButton button2 = new JButton("Delete Right Picture");
		buttonsAtBottom.add(button2);
		JButton button3 = new JButton("Skip");
		buttonsAtBottom.add(button3);
		
		
		add(picsAndLabels, BorderLayout.CENTER);
		add(buttonsAtBottom, BorderLayout.SOUTH);
		
		try{
			
			//Test code
			Picture pic1picy = new Picture("E:/Files/College/Senior Year/Spring/EDGE Project/Totally not Shaggy Rogers.jpg");
			Picture pic2picy = new Picture("E:/Files/College/Senior Year/Spring/EDGE Project/Mustache Cat.jpg");
			
			
			//Find out how small to make the picture so it still fits in the GUI.
			boolean imageTooBig = true;
		
			while(imageTooBig){
				
				if((int) ((double) pic1picy.getHeight()*(1/image1FractionNumber)) <= imageMaxHeight){
					if((int) ((double) pic1picy.getWidth()*(1/image1FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image1FractionNumber+=0.1;
			}
			
			imageTooBig = true;
			
			while(imageTooBig){
				
				if((int) ((double) pic2picy.getHeight()*(1/image2FractionNumber)) <= imageMaxHeight){
					if((int) ((double) pic2picy.getWidth()*(1/image2FractionNumber)) <= imageMaxWidth){
						imageTooBig = false;
					}
				}
				
				image2FractionNumber+=0.1;
			}
		
			Image pic1Image = pic1picy.getImage().getScaledInstance((int) ((double) pic1picy.getWidth()*(1/image1FractionNumber)), (int) ((double) pic1picy.getHeight()*(1/image1FractionNumber)), Image.SCALE_SMOOTH);
			Image pic2Image = pic2picy.getImage().getScaledInstance((int) ((double) pic2picy.getWidth()*(1/image2FractionNumber)), (int) ((double) pic2picy.getHeight()*(1/image2FractionNumber)), Image.SCALE_SMOOTH);
			
			pic1.setIcon(new ImageIcon(pic1Image));
			pic2.setIcon(new ImageIcon(pic2Image));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void catchThePhotoDuplicates(){
		//Important: This is the actual code for comparing the different files to each other.
		for(int i=0; i<listOfPictures.size(); i++){
			Picture picture1 = listOfPictures.get(i);
			for(int j=1+i; j<listOfPictures.size(); j++){
				Picture picture2 = listOfPictures.get(j);
				
				if(picture1.isEqualTo(picture2, searchTypeBoolean, algorithmTypeInt, rgbGraceValueAmount, (double) percentSimilarityAmount, hardCompare)){
					//Removes the element from the arraylist, then decrements the j value (that way we won't skip over an arraylist element).
					listOfPictures.remove(j);
					j--;
				}
			}
		}
	}
	
	
}