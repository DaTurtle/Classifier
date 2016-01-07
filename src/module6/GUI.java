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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI {
	public static NaiveBayes classifier = new NaiveBayes(1.0, 2, 0.30);
	public static DocumentStore docs = classifier.getDocumentStore();

	private static void addComponentsToPane(final Container pane) {
		final JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		final JTextArea textContent = new JTextArea();
		textContent.setRows(10);
		textContent.setSize(400, 400);
		textContent.setLineWrap(true);
		textContent.setWrapStyleWord(true);
		textContent.setEditable(false);
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
		fileName.setEditable(false);
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

		final JLabel estimatedClass = new JLabel("Class: ");
		estimatedClass.setHorizontalAlignment(JLabel.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(estimatedClass, c);

		final JTextArea estimation = new JTextArea();
		estimation.setRows(1);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 1;
		c.gridx = 1;
		c.gridy = 4;
		pane.add(estimation, c);

		JButton addButton = new JButton("Add file to DocumentStore");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weightx = 1;
		c.gridx = 2;
		c.gridy = 4;
		pane.add(addButton, c);

		JLabel label1 = new JLabel("amount of condprobs");
		label1.setHorizontalAlignment(JLabel.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(label1, c);

		JLabel label2 = new JLabel("Smoothing");
		label2.setHorizontalAlignment(JLabel.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 5;
		pane.add(label2, c);

		JLabel label3 = new JLabel("Min Occurences");
		label3.setHorizontalAlignment(JLabel.CENTER);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0;
		c.gridx = 2;
		c.gridy = 5;
		pane.add(label3, c);

		final JSlider slider1 = new JSlider();
		slider1.setOrientation(JSlider.HORIZONTAL);
		slider1.setMinimum(0);
		slider1.setMaximum(1000);
		slider1.setValue(500);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 6;
		pane.add(slider1, c);

		final JSlider slider2 = new JSlider();
		slider2.setOrientation(JSlider.HORIZONTAL);
		slider2.setMinimum(1);
		slider2.setMaximum(10000);
		slider2.setValue(1000);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 6;
		pane.add(slider2, c);

		final JSlider slider3 = new JSlider();
		slider3.setOrientation(JSlider.HORIZONTAL);
		slider3.setMinimum(0);
		slider3.setMaximum(100);
		slider3.setValue(3);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 6;
		pane.add(slider3, c);

		slider1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				classifier.setAmountOfCondProbsToUse(((double) slider1
						.getValue()) / 1000);
			}
		});

		slider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				classifier.setSmoothing((double) (slider2.getValue()) / 1000);
			}
		});

		slider3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				classifier.setMinOccurrences(slider3.getValue());
			}
		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File[] file = chooser.getSelectedFiles();
				if (file != null) {
					if (estimation.getText().length() > 0) {
						try {
							for (File f : file) {
								docs.addDocument(
									Utils.readFile(f.getAbsolutePath()),
									estimation.getText());
							}
							fileName.setText("All files successfully added!");
						} catch (IOException e1) {
							fileName.setText("Error reading file(s)!");
						}
						chooser.setSelectedFile(null);
						textContent.setText("");
						estimation.setText("");
					} else {
						JOptionPane.showMessageDialog(pane,
								"Please fill in a class!",
								"Error: Missing class!",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(pane,
							"Please select a file!", "Error: Missing file!",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = chooser.showOpenDialog(pane);
				File[] file = null;
				BufferedReader in = null;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFiles();
					if (file.length == 1) {
						fileName.setText(file[0].getName());
					} else {
						fileName.setText("Multiple files");
					}
					textContent.setText("");
					if (file.length == 1) {
						try {
							in = new BufferedReader(new FileReader(file[0]));
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
					} else {
						textContent
								.setText("Unable to preview files. Multiple files are selected.");
					}
				}
			}
		});

		estimateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (docs.getVocab().length != 0) {
					if (chooser.getSelectedFiles() != null) {
						File[] file = chooser.getSelectedFiles();
						if (file.length == 1) {
						classifier.train();
							String estimated = "";
							try {
								estimated = classifier.estimate(Utils
										.readFile(file[0].toString()));
							} catch (IOException e1) {
								System.err
										.println("Error: File can not be read!");
								estimated = "ERROR";
								e1.printStackTrace();
							}
							String s = (String) JOptionPane.showInputDialog(
									pane, "The estimated class is \""
											+ estimated
											+ "\". What is the actual class?",
									"Estimation", JOptionPane.PLAIN_MESSAGE,
									null, null, estimated);
							estimation.setText(s);
						} else {
							JOptionPane.showMessageDialog(pane,
									"Please select only one file to estimate!",
									"Error: Too many files",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(pane,
								"Please select a file!", "Error: Missing file",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(pane,
							"First add a document to the Document Store!",
							"Error: Empty DocStore", JOptionPane.ERROR_MESSAGE);
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
