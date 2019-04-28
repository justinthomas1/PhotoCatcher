import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

public class PhotoCatcherDecisionThing extends JPanel{

	public PhotoCatcherDecisionThing(){
		
		setLayout(new BorderLayout());
		
		JPanel picsAndLabels = new JPanel();
		picsAndLabels.setLayout(new GridBagLayout());
		GridBagConstraints picsAndLabelsConstraints = new GridBagConstraints();
		
		
		JLabel pic1 = new JLabel("", SwingConstants.CENTER);
		picsAndLabelsConstraints.fill = GridBagConstraints.BOTH;
		picsAndLabelsConstraints.gridwidth = 2;
		picsAndLabelsConstraints.gridheight = 2;
		picsAndLabelsConstraints.weightx = 1.0;
		picsAndLabelsConstraints.gridx = 0;
		picsAndLabelsConstraints.gridy = 0;
		picsAndLabels.add(pic1, picsAndLabelsConstraints);
		
		
		
		JLabel pic2 = new JLabel("", SwingConstants.CENTER);
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
		
		//Need to resize these somehow
		pic1.setIcon(new ImageIcon("E:/Files/College/Senior Year/Spring/EDGE Project/Images 1/Doggo.jpg"));
		pic2.setIcon(new ImageIcon("E:/Files/College/Senior Year/Spring/EDGE Project/Images 1/Doggo.jpg"));
	}

}