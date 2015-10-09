package com.joysoft.andutils.common.data;

import com.joysoft.andutils.lg.Lg;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA1 {

	private static final String MAC_NAME = "HmacSHA1";  
    private static final String ENCODING = "UTF-8";  
    
	/*
	 * 展示了一个生成指定算法密钥的过程 初始化HMAC密钥 
	 * @return 
	 * @throws Exception
	 * 
	  public static String initMacKey() throws Exception {
	  //得到一个 指定算法密钥的密钥生成器
	  KeyGenerator KeyGenerator keyGenerator =KeyGenerator.getInstance(MAC_NAME); 
	  //生成一个密钥
	  SecretKey secretKey =keyGenerator.generateKey();
	  return null;
	  }
	 */
    
    /** 
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名 
     * @param encryptText 被签名的字符串 
     * @param encryptKey  密钥 
     * @return 
     * @throws Exception 
     */  
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception   
    {         
    	byte[] data=encryptKey.getBytes(ENCODING);
    	//根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME); 
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME); 
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);  
        
        byte[] text = encryptText.getBytes(ENCODING);  
        //完成 Mac 操作 
        return mac.doFinal(text); 
        
    } 
    
    public static String HmacSHA1Encrypt(TreeMap<String, String> encyptMap,String encryptKey){
    	try{
    		StringBuffer appent = new StringBuffer();
    		
    		int length = encyptMap.size();
       	 for (Map.Entry<String, String> entry : encyptMap.entrySet()) {
       		 	length--;
       		 	appent.append(entry.getKey());
       		 	appent.append('=');
//       		 	appent.append(entry.getValue() == null ? "" : new String(HmacSHA1Encrypt(entry.getValue(), encryptKey)));
       		 	appent.append(entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue()));
       		 	if(length > 0)
       		 		appent.append('&');
          }
       	 Lg.e("====  appent:" + appent.toString());
//       	 String token = new String(HmacSHA1Encrypt(appent.toString(), encryptKey),"UTF-8");
       	 String token = encodeHexStr(HmacSHA1Encrypt(appent.toString(), encryptKey));
       	 return token; 
    	}catch(Exception e){
    		Lg.e(e.toString());
    	}
    	
    	return "";
    }
    

    	/**
    	 * 用于建立十六进制字符的输出的小写字符数组
    	 */
    	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5',
    			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    	/**
    	 * 用于建立十六进制字符的输出的大写字符数组
    	 */
    	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5',
    			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    	/**
    	 * 将字节数组转换为十六进制字符数组
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @return 十六进制char[]
    	 */
    	public static char[] encodeHex(byte[] data) {
    		return encodeHex(data, true);
    	}

    	/**
    	 * 将字节数组转换为十六进制字符数组
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @param toLowerCase
    	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
    	 * @return 十六进制char[]
    	 */
    	public static char[] encodeHex(byte[] data, boolean toLowerCase) {
    		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    	}

    	/**
    	 * 将字节数组转换为十六进制字符数组
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @param toDigits
    	 *            用于控制输出的char[]
    	 * @return 十六进制char[]
    	 */
    	protected static char[] encodeHex(byte[] data, char[] toDigits) {
    		int l = data.length;
    		char[] out = new char[l << 1];
    		// two characters form the hex value.
    		for (int i = 0, j = 0; i < l; i++) {
    			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
    			out[j++] = toDigits[0x0F & data[i]];
    		}
    		return out;
    	}

    	/**
    	 * 将字节数组转换为十六进制字符串
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @return 十六进制String
    	 */
    	public static String encodeHexStr(byte[] data) {
    		return encodeHexStr(data, true);
    	}

    	/**
    	 * 将字节数组转换为十六进制字符串
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @param toLowerCase
    	 *            <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式
    	 * @return 十六进制String
    	 */
    	public static String encodeHexStr(byte[] data, boolean toLowerCase) {
    		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    	}

    	/**
    	 * 将字节数组转换为十六进制字符串
    	 * 
    	 * @param data
    	 *            byte[]
    	 * @param toDigits
    	 *            用于控制输出的char[]
    	 * @return 十六进制String
    	 */
    	protected static String encodeHexStr(byte[] data, char[] toDigits) {
    		return new String(encodeHex(data, toDigits));
    	}

    	/**
    	 * 将十六进制字符数组转换为字节数组
    	 * 
    	 * @param data
    	 *            十六进制char[]
    	 * @return byte[]
    	 * @throws RuntimeException
    	 *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
    	 */
    	public static byte[] decodeHex(char[] data) {

    		int len = data.length;

    		if ((len & 0x01) != 0) {
    			throw new RuntimeException("Odd number of characters.");
    		}

    		byte[] out = new byte[len >> 1];

    		// two characters form the hex value.
    		for (int i = 0, j = 0; j < len; i++) {
    			int f = toDigit(data[j], j) << 4;
    			j++;
    			f = f | toDigit(data[j], j);
    			j++;
    			out[i] = (byte) (f & 0xFF);
    		}

    		return out;
    	}

    	/**
    	 * 将十六进制字符转换成一个整数
    	 * 
    	 * @param ch
    	 *            十六进制char
    	 * @param index
    	 *            十六进制字符在字符数组中的位置
    	 * @return 一个整数
    	 * @throws RuntimeException
    	 *             当ch不是一个合法的十六进制字符时，抛出运行时异常
    	 */
    	protected static int toDigit(char ch, int index) {
    		int digit = Character.digit(ch, 16);
    		if (digit == -1) {
    			throw new RuntimeException("Illegal hexadecimal character " + ch
    					+ " at index " + index);
    		}
    		return digit;
    	}


}


