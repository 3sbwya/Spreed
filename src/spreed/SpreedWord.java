package spreed;

/**
 * Utility functions to prepare words for speed reading.
 * 
 * @author Matthew Greene <mgreen85@uncc.edu>
 */
public class SpreedWord {
	
	/**
	 * Returns the 0-indexed pivot letter for the provided word.
	 * 
	 * @param word
	 * @return
	 */
	public static int getPivot(String word){
		int size = word.length();
		
		if(size == 1){
			return 0;
		}else if(size >= 2 && size <= 5){
			return 1;
		}else if(size >= 6 && size <= 9){
			return 2;
		}else if(size >= 10 && size <= 13){
			return 3;
		}else{
			return 4;
		}
	}
	
	/**
	 * Returns the (space-padded) String of the provided input length with the
     * input word centered at the pivot.
     * 
	 * @param word
	 * @param length
	 * @return
	 */
	public static String getAlignedWord(String word, int length){
		if(length % 2 == 0){
			length += 1;
		}
		StringBuilder alignedWord = new StringBuilder();
		int pivot = getPivot(word);
		int centerPoint = (int)Math.round(length / 2) + 1;
		int leftPadding = centerPoint - (pivot + 1);
		
		for(int i = 0; i < leftPadding; i++){
			alignedWord.append(" ");
		}
		
		alignedWord.append(word);
		
		//If word is greater than length, truncate word to fit length
		if(alignedWord.length() > length){
			alignedWord.delete(length, alignedWord.length());
		}
		
		int rightPadding = length - alignedWord.length();
		for(int i = 0; i < rightPadding; i++){
			alignedWord.append(" ");
		}
		
		return alignedWord.toString();
	}
	
	/**
	 * Returns the center (pivot) position.
	 * 
	 * @param word
	 * @return
	 */
	public static int getCenter(String word){
		return (int)Math.round(word.length()/2);
	}
	
	/**
	 * Returns the pause length for a given word. For WPM calculations, a "word"
     * is considered to be 5 characters. Additionally, add 1 for trailing comma
     * and 2 for trailing semicolon and period.
     * 
	 * @param word
	 * @return
	 */
	public static int getPauseLength(String word){
		int pause;
		if(word.length() <= 5){
			pause = 1;
		}else{
			pause = 2;
			if(word.charAt(word.length()-1) == ','){
				pause += 1;
			}else if(word.charAt(word.length()-1) == ';' || word.charAt(word.length()-1) == '.'){
				pause += 2;
			}
		}
		return pause;
	}
}
