package org.smart4j.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 编码与解码操作工具类
 * 
 * @author 鑫哲
 *
 */
public final class CodecUtil {

	private CodecUtil() {
		// TODO
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);
	
	/**
	 * 将URL编码
	 */
	public static String encodeURL(String source) {
		String target;
		try {
			target = URLEncoder.encode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("encode url failure", e);
			throw new RuntimeException(e);
		}
		return target;
	}
	
	/**
	 * 将URL解码
	 */
	public static String decodeURL(String source) {
		String target;
		try {
			target = URLDecoder.decode(source, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("decode url failure", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将字符串 Base64 编码
	 */
	public static String encodeBASE64(String str) {
		String target;
		try {
			target = Base64.encodeBase64URLSafeString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("编码出错！", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将字符串 Base64 解码
	 */
	public static String decodeBASE64(String str) {
		String target;
		try {
			target = new String(Base64.decodeBase64(str), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("解码出错！", e);
			throw new RuntimeException(e);
		}
		return target;
	}

	/**
	 * 将字符串 MD5 加密
	 */
	public static String encryptMD5(String str) {
		return DigestUtils.md5Hex(str);
	}

	/**
	 * 将字符串 SHA 加密
	 */
	public static String encryptSHA(String str) {
		return DigestUtils.sha1Hex(str);
	}

	/**
	 * 创建随机数
	 */
	public static String createRandom(int count) {
		return RandomStringUtils.randomNumeric(count);
	}

	/**
	 * 获取 UUID（32位）
	 */
	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
