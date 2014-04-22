package spreed;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Reads and parses an ASCII text document and provides random access to the 
 * words in the file.
 * 
 * @author Matthew Greene <mgreen85@uncc.edu>
 *
 */
public class WordSource {
	private String filename;
	private static String word;
	private static int speed;
	private int maxWordLength;
	private ArrayList<String> token = new ArrayList<>();
	
	/**
	 * Constructor
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public WordSource(String filename) throws FileNotFoundException{
		this.filename = filename;
		run();
	}
	
	/**
	 * Private method which takes in each word (token) of the document and places
	 * them into the ArrayList.
	 * 
	 * @throws FileNotFoundException
	 */
	private void run() throws FileNotFoundException{
		File file = new File(filename);
		Scanner reader = new Scanner(file);
		StringTokenizer t;
		
		while(reader.hasNext()){
			t = new StringTokenizer(reader.next(), " ");
			while(t.hasMoreTokens()){
				word = t.nextToken();
				if(word.length() > maxWordLength){
					maxWordLength = word.length();
				}
				token.add(word);
			}
		}
		reader.close();
	}
	
	/**
	 * Returns the 0-indexed word from the input file.
	 * 
	 * @param index
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public String getWord(int index) throws IndexOutOfBoundsException{
		return token.get(index);
	}
	
	/**
	 * Returns number of words in the document.
	 * 
	 * @return
	 */
	public int count(){
		return token.size();
	}
	
	/**
	 * Returns the delay speed for words per minute.
	 * 
	 * @param wpm
	 * @return
	 */
	public int wpm(float wpm){
		float delay = (60 / wpm) * 1000;
		return (int)delay;
	}
}
