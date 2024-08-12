package crs.security;

/**
 * 
 * <code>Cipher</code> uses a very simple substitution algorithm
 * to <code>encrypt</code> and <code>decrypt Strings</code>.
 * 
 * <p>
 * 		This is accomplished by subtracting <code>1</code> from 
 * 		each character in the <code>String</code> for encryption,
 * 		and adding <code>1</code> to each character in the <code>
 * 		String</code> for decryption.
 * </p>
 * 
 * @author Christopher Harris
 * @version 1.0
 *
 */
public class Cipher implements Crypto {
	
	@Override
	public String encrypt(String str) {
		// The string to return
		String result = "";
		
		// Convert `str` to a character array
		char[] chars = str.toCharArray();
		
		// For each char, replace it with the char
		// that is 1 less than the current char
		for (int i = 0; i < chars.length; i++) {
				chars[i] = (char) (chars[i] - 1);
			result += chars[i];
		}
		
		return result;
	}

	@Override
	public String decrypt(String str) {
		// The string to return
		String result = "";
		
		// Convert `str` to a character array
		char[] chars = str.toCharArray();
		
		// For each char replace it with the char
		// that is 1 more than the current char
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (chars[i] + 1);
			result += chars[i];
		}
		
		return result;
	}

}
