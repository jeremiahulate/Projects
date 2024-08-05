package crs.security;

/**
 * 
 * Specifies two methods:
 * <ol>
 * 		<li><code>encrypt(str : String) -> String</code></li>
 * 		<li><code>decrypt(str : String) -> String</code></li>
 * </ol>
 * Which work to encrypt and decrypt a string based on an
 * algorithm programmed by whichever class uses the interface.
 * 
 * @author Christopher Harris
 * @version 1.0
 * 
 *
 */
public interface Crypto {
	
	/**
	 * 
	 * @param str - The <code>String</code> to encrypt
	 * @return an encrypted <code>String</code>
	 */
	public String encrypt(String str);
	
	/**
	 * 
	 * @param str - The <code>String</code> to decrypt
	 * @return the decrypted <code>String</code>
	 */
	public String decrypt(String str);
	
}