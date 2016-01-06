package module6;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class GUI {

	/**
	 * TODO GUI bestand naar DocumentStore knop (gegeven de klasse) bestand
	 * estimaten -> dit geeft feedback op een of andere manier en dan dit
	 * bestand ook naar de documentstore sturen met een menselijk gecorrigeerde
	 * klasse. Knoppen om de smoothing amountOfCondProbsToUse en minOccurrences
	 * aan te passen is ook handig. Estimate knop heeft geen functionaliteit
	 * */

	private static void addComponentsToPane(final Container pane) {
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		final JTextArea textContent = new JTextArea();
		textContent.setRows(10);
		textContent.setSize(400, 400);
		textContent.setLineWrap(true);
		textContent.setWrapStyleWord(true);
		c.weightx = 0.5;
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		JScrollPane scroll = new JScrollPane(textContent,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.add(scroll, c);

		final JTextArea fileName = new JTextArea();
		fileName.setRows(1);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 3;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(fileName, c);

		JButton browseButton = new JButton("Browse...");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridx = 3;
		c.gridy = 1;
		pane.add(browseButton, c);

		JButton estimateButton = new JButton("Estimate");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 4;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(estimateButton, c);
		estimateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] possibilities = { "a", "b", "c" };
				String estimation = "a";
				String s = (String) JOptionPane.showInputDialog(pane,
						"The estimated class is " + estimation
								+ ". What is the actual class?", "Estimation",
						JOptionPane.PLAIN_MESSAGE, null, possibilities,
						possibilities[0]);
			}
		});

		final JFileChooser chooser = new JFileChooser();
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(pane);
				File file = null;
				BufferedReader in = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					fileName.setText(file.getName());
					textContent.setText("");
					try {
						in = new BufferedReader(new FileReader(file));
					} catch (FileNotFoundException fnfe) {
						// This shouldn't happen
					}
					String line;
					try {
						line = in.readLine();
						while (line != null) {
							textContent.append(line + "\n");
							line = in.readLine();
						}
					} catch (IOException ioe) {
						// TODO Auto-generated catch block
						ioe.printStackTrace();
					}
				}
			}
		});

	}

	public static void createAndShowGUI() {
		final JFrame frame = new JFrame("Interactive Learner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addComponentsToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
