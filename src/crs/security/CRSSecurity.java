package crs.security;

/**
 * @author Christopher Harris
 */
public class CRSSecurity {

	// The length of the key required by the user to use the program
	public static final int KEY_LENGTH = 256;
	
	/**
	 * Generates a key of a specified length
	 * @param crypto The Crypto algorithm to use
	 * @param length The length of the key to generate
	 * @return a valid key
	 */
	public static String generateKey(Crypto crypto, int length) {
		String key = "";
		
		for (int i = 0; i < length - 1; i++) {
			key += (char)(66 + (int)(Math.random() * 24.0));
		}
		
		for (int i = 66; i < 90; i++) {
			String prime_key = key + (char)i;
			if (validateDecryptedKey(crypto, prime_key, length)) {
				return crypto.encrypt(prime_key);
			}
		}
		
		return generateKey(crypto, length);
	}
	
	/**
	 * Validates a particular key for a given Crypto algorithm and a specified length
	 * @param crypto The Crypto algorithm to use
	 * @param key The key provided
	 * @param length The required length of the key
	 * @return True if the key is a valid key, false if the key is invalid
	 */
	public static boolean validateKey(Crypto crypto, String key, int length) {
		if (key.length() != length) return false;
		
		String decrypted = crypto.decrypt(key);
		
		int sum = 0;
		for (int i = 0; i < decrypted.length() - 1; i++, sum += decrypted.charAt(i));

		return isPrime(sum);
	}
	
	/**
	 * Validates a particular key for a given Crypto algorithm and a specified length
	 * This method assumes that the key has already been decrypted
	 * @param crypto The Crypto algorithm to use
	 * @param key The key provided
	 * @param length The required length of the key
	 * @return True if the key is a valid key, false if the key is invalid
	 */
	public static boolean validateDecryptedKey(Crypto c, String decrypted, int length) {
		if (decrypted.length() != length) return false;
		
		int sum = 0;
		for (int i = 0; i < decrypted.length() - 1; i++, sum += decrypted.charAt(i));

		return isPrime(sum);
	}
	
	/**
	 * Validates a particular key for a given Crypto algorithm and a specified length
	 * @param crypto The Crypto algorithm to use
	 * @param key The key provided
	 * @param length The required length of the key
	 * @return True if the key is a valid key, false if the key is invalid
	 */
	public static boolean isPrime(int i) {
		if (i <= 1) return false;
		
		for (int j = 2; j <= i / 2; j++) {
			if ((i % j) == 0) return false;
		}
		
		return true;
	}
	
}