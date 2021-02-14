package Hamming;

import java.util.ArrayList;

public abstract class Hamming {

	/**
	 * Method to encode a message into a Hamming word
	 * @param message The message to encode
	 * @return The control bits followed by the hamming word with the bits in it
	 */
	public static String encodeHamming(String message) {
		if(!message.matches("[01]+")) throw new IllegalArgumentException("The message must be a bits string");
		char[] prepared = prepareMessage(message.toCharArray());

		StringBuilder sb = new StringBuilder();

		char[] controlBits = getControlBits(prepared, prepared.length - message.length());
		setControlBits(prepared, controlBits);

		sb.append(String.valueOf(prepared));
		sb.append(":");
		sb.append(String.valueOf(controlBits));
		sb.reverse();

		return sb.toString();
	}

	/**
	 * Method that will change the control bits
	 * to what they need to be
	 * @param prepared the prepared char array of the message
	 * @param control Array with the control bits
	 */
	private static void setControlBits(char[] prepared, char[] control) {
		for(Character controlB : control) {
			for(int i = 0; i < prepared.length; ++i) {
				if (prepared[i] == 'C') {
					prepared[i] = controlB;
					break;
				}
			}
		}
	}

	/**
	 * Method that will calculate the control bits
	 * and return them into a char array
	 * @param prepared the prepared char array of the message
	 * @param nbControl the number of control bits
	 */
	private static char[] getControlBits(char[] prepared, int nbControl) {
		char[] control = new char[nbControl];
		int controlCnt = 0;
		int index = 0;
		for (char c : prepared) {
			if (c == 'C') {
				Character[] highBits = getHighBits(index++, prepared);
				control[controlCnt++] = getParity(highBits);
			}
		}
		return control;
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
	 * Method used to get the control bits that does not match the message
	 * @param messageArray the message in char array
	 * @param reversedMessage the essage reversed in char array
	 * @return an ArrayList of Integer representing the control bits that does not respect the parity
	 */
	static ArrayList<Integer> getWrongControlBits(char[] messageArray, char[] reversedMessage) {
		ArrayList<Integer> errors = new ArrayList<>();

		for(int i = 0; i < messageArray.length; ++i) reversedMessage[i] = messageArray[messageArray.length-i-1];

		int nbControl = 0;
		for(int i = 0; i < messageArray.length; ++i) if(isPowerOfTwo(i+1)) ++nbControl;

		for(int i = 0; i < nbControl; ++i) {
			ArrayList<Character> toControl = new ArrayList<>();
			for(int j = 0; j < reversedMessage.length; ++j) {
				if(getKthBit(j+1, i) == 1) toControl.add(reversedMessage[j]);
			}
			if(getParity(toControl.toArray(new Character[0])) != '0') errors.add(i);
		}

		return errors;
	}

	/**
	 * Method that will return the positions of the bits that may contain an error
	 * @param message The message hamming encoded
	 * @return an ArrayList with the positions of the bits that may be errors
	 */
	public static ArrayList<Integer> getErrors(String message) {
		ArrayList<Integer> posErrors = new ArrayList<>();
		char[] messageArray = message.toCharArray();
		char[] reversedMessage = new char[messageArray.length];
		ArrayList<Integer> wrongBits = getWrongControlBits(messageArray, reversedMessage);
		for(int i = 0; i < reversedMessage.length; ++i) {
			boolean isError = false;
			for(Integer bitPos : wrongBits) {
				if(getKthBit(i+1, bitPos) == 1) {
					isError = true;
				}else {
					isError = false;
					break;
				}
			}
			if(isError) posErrors.add(i);
		}
		return posErrors;
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
