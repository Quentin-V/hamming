package Hamming;

import java.util.ArrayList;

public abstract class Hamming {

	/**
	 * Method to encore a message into a Hamming word
	 * @param message The message to encode
	 * @return The hamming word resulted of the encoding
	 */
	public static String encodeHamming(String message) {
		if(!message.matches("[01]+")) throw new IllegalArgumentException("The message must be a bits string");
		char[] prepared = prepareMessage(message.toCharArray());
		setControlBits(prepared);
		StringBuilder sb = new StringBuilder();
		for(int i = prepared.length-1; i >= 0; --i)  sb.append(prepared[i]);
		return sb.toString();
	}

	/**
	 * Method that will change the control bits
	 * to what they need to be
	 * @param prepared the prepared char array of the message
	 */
	private static void setControlBits(char[] prepared) {
		int index = 0;
		for(int i = 0; i < prepared.length; ++i) {
			if(prepared[i] == 'C') {
				Character[] highBits = getHighBits(index++, prepared);
				prepared[i] = getParity(highBits);
			}
		}
	}

	/**
	 * Method that will get the parity bit needed from a char array
	 * @param highBits the char array containing the bits
	 * @return The parity bit character needed to complete the array
	 */
	private static Character getParity(Character[] highBits) {
		char parity = '0';
		for(Character c : highBits)
			if(c == '1') parity = parity == '0' ? '1' : '0';
		return parity;
	}

	/**
	 * Method to get the bits to the positions where the index bit i is a 1
	 * @param i the index of the position wanted
	 * @param prepared The prepared char array
	 * @return An array with all the bits with a 1 on the position wanted
	 */
	private static Character[] getHighBits(int i, char[] prepared) {
		ArrayList<Character> highBits = new ArrayList<>();
		for(int j = 0; j < prepared.length; ++j) {
			if(prepared[j] != 'C' && getKthBit(j+1, i) == 1 ) {
				highBits.add(prepared[j]);
			}
		}
		return highBits.toArray(new Character[0]);
	}

	/**
	 * Method used to prepare the message
	 * @param unprepared the unprepared message
	 * @return the message prepared with the right size and undefined control bits
	 */
	static char[] prepareMessage(char[] unprepared) {
		char[] prepared = new char[unprepared.length + (int) Math.ceil(log2(unprepared.length)) + 1];
		int index = unprepared.length - 1;
		for(int i = 1; i <= prepared.length; ++i) {
			if(isPowerOfTwo(i)) {
				prepared[i-1] = 'C';
			}else {
				prepared[i-1] = unprepared[index--];
			}
		}
		return prepared;
	}

	/**
	 * Method used to get the log2 of a number
	 * @param n The number you want to know the log2 of
	 * @return The log2 of the number wanted
	 */
	static double log2(int n) {
		return Math.log(n)/Math.log(2);
	}

	/**
	 * Method used to know if an integer is a power of 2 or not
	 * @param n The number we want to know if it's a power of 2
	 * @return True if n is a power of 2 false otherwise
	 */
	static boolean isPowerOfTwo(int n) { return (n & n-1) == 0; }

	/**
	 * Method to get a bit from an integer at a position
	 * @param n The integer
	 * @param k The position of the bit
	 * @return The bit at position k in number n
	 */
	static int getKthBit(int n, int k) {
		return (n >> k) & 1;
	}

}
