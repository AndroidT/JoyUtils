package com.joysoft.andutils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * 对String进行MD5加密
	 * @param source
	 * @return
	 */
	public static String encode(String source) {
		try {
			MessageDigest sDigest = MessageDigest.getInstance("MD5");
			byte[] btyes = source.getBytes();
			byte[] encodedBytes = sDigest.digest(btyes);
			return hexString(encodedBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return source;
	}

	private static String hexString(byte[] source) {
		char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		if (source == null || source.length <= 0) {
			return "";
		}

		final int size = source.length;
		final char str[] = new char[size * 2];
		int index = 0;
		byte b;
		for (int i = 0; i < size; i++) {
			b = source[i];
			str[index++] = sHexDigits[b >>> 4 & 0xf];
			str[index++] = sHexDigits[b & 0xf];
		}
		return new String(str);
	}
}
