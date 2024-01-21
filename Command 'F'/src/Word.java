// Word Object Class

public class Word implements Comparable <Word>{
	
	// VARIABLES
	private String word;
	private int count;
	
	// METHODS
	
	// Description: constructor
	// Parameters: the words, the count
	// Return: none
	public Word (String word, int count) {
		this.word = word;
		this.count = count;
	}
	
	// Description: increments the count / frequency of the word
	// Parameters: none
	// Return: void
	public void increment () {
		count++;
	}
	
	// Description: getter method for the count / frequency
	// Parameters: none
	// Return: int
	public int getCount () {
		return count;
	}
	
	// Description: getter method for the word
	// Parameters: none
	// Return: string
	public String getWord () {
		return word;
	}
	
	// Description: toString method
	// Parameters: none
	// Return: string
	public String toString() {
		return String.format(word + " (" + count + ")");
	}
	
	// Description: compareTo method (natural sorting order)
	// Parameters: the Word w
	// Return: int
	public int compareTo(Word w) {
		return w.count - this.count;
		
	}
}
