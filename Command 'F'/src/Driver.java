// Name: Michelle Xu
// Description: This program finds the 20 most frequent words in a given book file
// Assumptions:
		// 1. Special strings such as 2.7a are broken up into separate words ("2" and "7a")
		// 2. Contractions are counted as one word
		// 3. Numbers are included as words

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.*;

public class Driver implements ActionListener  {
	
	// GLOBAL VARIABLES
	public static JFrame frame;
	public static JPanel myPanel;
	public static JLabel label;
	public static JButton addFileButton, exit;
	public static JComboBox <String> fileNames;
	public static JTextArea fileArea, outputArea;
	public static JScrollPane scrollPane;
	public static JTextField addNewFile;
	public static BufferedReader inFile;  
	public static String[] names = {"", "ALICE.TXT", "MOBY.TXT"};
	public static Map <String, String> fileMap = new HashMap <>();
	public static StringTokenizer st;
	public static String fileText = "";
	public static String line;
	public static ArrayList <Word> wordList;
	public static Set <String> conSet = new HashSet <String> ();
	public static Set <String> endSet = new HashSet <String> ();
	public static StringBuilder sb = new StringBuilder ();

	public Driver () throws IOException {
		
		// Create a new frame and panel
		frame = new JFrame ("Word Count Program");
		frame.setPreferredSize(new Dimension(1000, 600));
		myPanel = new JPanel();
		myPanel.setBackground (new Color(224, 221, 235));
		myPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		SpringLayout layout = new SpringLayout();
		myPanel.setLayout(layout);
		
		// Adding a new file
        addFileButton = new JButton ("Add File");
        addFileButton.setPreferredSize(new Dimension(100, 50));
        addFileButton.setActionCommand ("Add File");
        addFileButton.addActionListener(this);
        layout.putConstraint(SpringLayout.WEST, addFileButton, 175, SpringLayout.WEST, myPanel);
        layout.putConstraint(SpringLayout.NORTH, addFileButton, 20, SpringLayout.NORTH, myPanel);
        
        // Exiting the program
        exit = new JButton ("Exit");
        exit.setPreferredSize(new Dimension(50, 30));
        exit.setActionCommand ("Exit");
        exit.addActionListener(this);
        layout.putConstraint(SpringLayout.WEST, exit, 5, SpringLayout.WEST, myPanel);
        layout.putConstraint(SpringLayout.NORTH, exit, 5, SpringLayout.NORTH, myPanel);
        
        // Creating the drop down menu
        fileMap.put("ALICE.TXT", "ALICE.TXT");
        fileMap.put("MOBY.TXT", "MOBY.TXT");
		fileNames = new JComboBox <String> (names);
		fileNames.setPreferredSize(new Dimension(250, 50));
		fileNames.setSelectedIndex (0);
		fileNames.addActionListener (this);
		layout.putConstraint(SpringLayout.WEST, fileNames, 100, SpringLayout.WEST, myPanel);
        layout.putConstraint(SpringLayout.NORTH, fileNames, 70, SpringLayout.NORTH, myPanel);
        
        // Creating the output text area
        outputArea = new JTextArea ();
        outputArea.setPreferredSize(new Dimension(450, 500));
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        outputArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        layout.putConstraint(SpringLayout.WEST, outputArea, 475, SpringLayout.WEST, myPanel);
        layout.putConstraint(SpringLayout.NORTH, outputArea, 20, SpringLayout.NORTH, myPanel);
        
        // Creating the text file preview
        fileArea = new JTextArea ();
        fileArea.append(sb.toString());
        fileArea.setEditable(false);
        fileArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scrollPane = new JScrollPane(fileArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        int vericalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
        scrollPane.setHorizontalScrollBarPolicy(horizontalPolicy);
		scrollPane.setVerticalScrollBarPolicy(vericalPolicy);
        layout.putConstraint(SpringLayout.WEST, scrollPane, 20, SpringLayout.WEST, myPanel);
        layout.putConstraint(SpringLayout.NORTH, scrollPane, 120, SpringLayout.NORTH, myPanel);
        
        // Adding the components to the panel
        myPanel.add(exit);
        myPanel.add(outputArea);
        myPanel.add(scrollPane);
        myPanel.add(addFileButton);
        myPanel.add(fileNames);
        
        frame.add (myPanel);
        frame.pack();
        frame.setVisible(true);
	}

	// Description: the action performed
	// Parameters: the event
	// Return: void
	public void actionPerformed (ActionEvent event) {
		Object source = event.getSource();
		String eventName = event.getActionCommand();
		
		// If the user presses the "Add File" button
		if (source instanceof JButton) {
	        if (eventName.equals ("Add File")) {
	        	FileDialog openDialog = new FileDialog (frame, "Open a New File", FileDialog.LOAD);
				openDialog.setVisible (true);
				String fileName = openDialog.getFile ();
				String dir = openDialog.getDirectory ();
				
				// If the file chosen is not already in the drop-down menu
				if (dir != null && !fileMap.containsKey(fileName.toUpperCase())) {
					fileNames.addItem(fileName);
					fileMap.put(fileName, dir + fileName);
				}
	        }
	        
	        // Exiting the program
	        else if (eventName.equals("Exit")){
	        	System.exit(0);
	        }
	    } 
		
		// If the user presses an option from the drop-down menu
		else if (source instanceof JComboBox) {
			JComboBox comboBox = (JComboBox) event.getSource ();
			String file = (String) comboBox.getSelectedItem ();
			outputArea.setText("");
			wordList = new ArrayList <Word> ();
			
			// If a file is chosen from the drop-down menu
			if (file != "") {
				try {
					
					// Starting timer
			        long startTime = System.currentTimeMillis();
			        
			        // Find the frequencies of the words in the file
			        findFrequency (inFile, file, line, st, wordList, sb);
			        
			        // Stopping timer
					long stopTime = System.currentTimeMillis();
				    long elapsedTime = stopTime - startTime;
				    
				    // Setting the preview of the file
					fileArea.setText(sb.toString());
			        fileArea.setCaretPosition(0);
			        
			        // Setting the output text area 
			        outputArea.append("Total Time: " + elapsedTime + " Milliseconds\n\n20 Most Frequent Words!\n\n");
			        outputArea.append(String.format("%-40sFREQUENCY\n", "WORDS"));
			       
			        // Get the 20 more frequent words in the file from the arrayList
			        // Refer to the assumptions at the beginning of the class
			        for (int i = 0; i < 20; i++) {
			        	outputArea.append(String.format("%-10s%-30s" + wordList.get(i).getCount() + "\n", i + 1 + ")", wordList.get(i).getWord()));
			        }
				}
				catch (IOException e) {	
					System.out.println("Invalid.");
				}
			}
			
			// If no file is chosen
			else {
				fileArea.setText("");
			}
		}
	}

	
	// Description: this method finds the frequency of each word in the file and finds the 20 most frequent words
	// Parameters: the BufferedReader inFile, the file chosen, the line in the file, the StringTokenizer, the list of words, the StringBuilder
	// Return: void
	public static void findFrequency (BufferedReader inFile, String file, String line, StringTokenizer st, ArrayList <Word> wordList, StringBuilder sb) throws IOException {
		
		// VARIABLES
		Map <String, Word> wordCountMap = new HashMap <>();
		String token = "";
		ArrayList <String> words = new ArrayList <String> ();
		inFile = new BufferedReader (new FileReader (fileMap.get(file)));
		
		// RUN CODE
		// Reading each line in the file
		while ((line = inFile.readLine()) != null) {
			// Adding each line to the StringBuilder
			sb.append(line + "\n");	
			
			// Finding each punctuation
			st = new StringTokenizer (line.toLowerCase(), " ?,!/:.`\";()[]{}\'*—-<>&#@^|");
			int size = st.countTokens();
			
			// Adding the tokens into an ArrayList
			for (int i = 0; i < size; i++) {
				token = st.nextToken();
				
				// If the token is an "s," check the unique cases
				if (token.equals("s")) {
					if (i > 0 && conSet.contains(words.get(words.size() - 1))) {
						words.set(words.size() - 1, words.get(words.size() - 1) + "'s");
					}
				}
				
				// If the token is part of a contraction, keep it as one word by combining this token and the previous token
				else if (i > 0 && endSet.contains(token)) {
					words.set(words.size() - 1, words.get(words.size() - 1) + "'" + token);
				}
				
				// If the token is not a contraction, add the token to the ArrayList immediately
				else {	
					words.add(token);
				}
			}	
		}
		
		inFile.close();
		
		// Count the frequency of the word
		for (int i = 0; i < words.size(); i++) {
			
			// Increment the frequency of the word
			if (wordCountMap.containsKey(words.get(i))) {
				wordCountMap.get(words.get(i)).increment();
			}
			
			// Make a new Word
			else {
				wordCountMap.put(words.get(i), new Word (words.get(i), 1));
			}
		}
		
		// Place all the values in the wordCountMap into an arrayList
		for (Map.Entry<String, Word> entry : wordCountMap.entrySet()) {
		    wordList.add(entry.getValue());
		   
		}
		
		// Sort the arrayList of Words by frequency (natural sorting order)
		Collections.sort(wordList);	
	}
	
	// Description: this method adds the possible contractions (first word) to the set
	// Parameters: the set
	// Return: void
	public static void addContraction (Set <String> conSet) {
		conSet.add("there");
		conSet.add("she");
		conSet.add("he");
		conSet.add("it");	
	}

	// Description: this method adds the possible contractions (endings) to the set
	// Parameters: the set (ending)
	// Return: void
	public static void addContEnd (Set <String> endSet) {
		endSet.add("ve");
		endSet.add("t");
		endSet.add("ll");
		endSet.add("re");
		endSet.add("m");
		endSet.add("d");
	}
	
	// Description: the main
	public static void main (String[] args) throws IOException {
		addContEnd(endSet);
		addContraction(conSet);
		new Driver();
	}
}



