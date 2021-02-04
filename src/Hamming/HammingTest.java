package Hamming;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HammingTest {
	@Test
	public void testPow2() {
		int[] pows = new int[20];
		for(int i = 0; i < 20; ++i) {
			pows[i] = (int) Math.pow(2, i);
		}
		for(Integer i : pows) {
			assertTrue(Hamming.isPowerOfTwo(i));
		}
	}

	@Test
	public void testPrepare() {
		char[] un = new char[]{'0','1','0','0', '1', '1', '1'};
		char[] p = Hamming.prepareMessage(un);

		char[] expected = new char[]{'C', 'C', '1', 'C', '1', '1', '0', 'C', '0', '1', '0'};

		assertArrayEquals(expected, p);
	}

	@Test
	public void testGetKthBit() {
		assertEquals(0, Hamming.getKthBit(8,0));
		assertEquals(1, Hamming.getKthBit(1,0));
		assertEquals(0, Hamming.getKthBit(2,0));
	}

	@Test
	public void testEncodeHamming() {
		String expected = "010:1010010";
		assertEquals(expected, Hamming.encodeHamming("1010"));

		expected = "001:1010101";
		assertEquals(expected, Hamming.encodeHamming("1011"));

		expected = "0000:1100000100";
		assertEquals(expected, Hamming.encodeHamming("110001"));

		expected = "0101:110001011001";
		assertEquals(expected, Hamming.encodeHamming("11001010"));
	}
}