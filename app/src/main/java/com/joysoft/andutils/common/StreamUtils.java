package com.joysoft.andutils.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamUtils {

	/**
	 * InputStream转换成String
	 */
	public static String inToStr(InputStream in) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
