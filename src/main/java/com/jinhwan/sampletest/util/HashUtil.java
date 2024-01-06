package com.jinhwan.sampletest.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.codec.Hex;

public class HashUtil {
	/**
	 * Computes hash
	 * @param originalString
	 * @param Algorithm
	 * @return hashData - hexString
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception
	 */
	public static String hash(String originalString, String Algorithm) throws NoSuchAlgorithmException {
		String hashHexString = "";
		
		MessageDigest digest = MessageDigest.getInstance(Algorithm);
		byte[] hash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
		hashHexString = new String(Hex.encode(hash));
		
		return hashHexString;
	}
	
	/**
	 * Computes the MD5 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String md5(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "MD5");
	}
	
	/**
	 * Computes the SHA-1 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String sha1(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "SHA-1");
	}
	
	/**
	 * Computes the SHA-224 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String sha224(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "SHA-224");
	}
	
	/**
	 * Computes the SHA-256 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String sha256(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "SHA-256");
	}
	
	/**
	 * Computes the SHA-384 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String sha384(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "SHA-384");
	}
	
	/**
	 * Computes the SHA-512 hash
	 * @param originalString
	 * @return
	 * @throws Exception
	 */
	public static String sha512(String originalString) throws NoSuchAlgorithmException {
		return hash(originalString, "SHA-512");
	}
}