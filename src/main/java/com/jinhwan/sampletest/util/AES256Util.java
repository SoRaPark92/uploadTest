package com.jinhwan.sampletest.util;


import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256Util {
	//키를 고정값으로 쓴 이유는 userId단위로 체크할 경우 중복되는 주민등록번호로 가입 할가능성이 존재함
	private static final String secretKey = "JinHwan92ApiTest"; 
	
	/**
	 * AES256 Enc
	 * @param str
	 * @param secretKey
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Encode(String str){
		String enStr = null;;
		try {
			byte[] keyData = secretKey.getBytes();
			String iv = secretKey.substring(0,16);

			SecretKey secureKey = new SecretKeySpec(keyData, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));

			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			enStr = new String(Base64.getEncoder().encode(encrypted));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return enStr;
	}

	/**
	 * AES256 Dec
	 * @param str
	 * @param secretKey
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String AES_Decode(String str) {
		String deStr = null;
		try {

			byte[] keyData = secretKey.getBytes();
			String iv = secretKey.substring(0,16);
			
			SecretKey secureKey = new SecretKeySpec(keyData, "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes("UTF-8")));
	
			byte[] byteStr = Base64.getDecoder().decode(str.getBytes());
			
			deStr = new String(c.doFinal(byteStr),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return deStr;
	}
}