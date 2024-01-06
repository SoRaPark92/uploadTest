package com.jinhwan.sampletest.util;

import java.text.DecimalFormat;

public class StringUtil {

	/**
	 * null , ""값 체크
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.equals("")) {
			return true;
		}  
		return false;
	}
	
	/**
	 * 주민등록번호 포메팅
	 * @param s
	 * @return
	 */
	public static String regNoFormmat (String s) {
		if (s.length() != 13) {
			return s;
		}
		
		return s.substring(0,6) + "-" + s.substring(6,13);
	}
	/**
	 * 금액 콤마 처리
	 * @return
	 */
	public static String amountFormat(long s) {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return formatter.format(s);
	}
	
	public static long amountLongForString(String s) {
		s = s.replace(",", "");
	
		return (long) Math.floor(Double.parseDouble(s)) ;
	}
}
