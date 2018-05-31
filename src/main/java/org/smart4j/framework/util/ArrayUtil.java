package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
 * @author 鑫哲
 *
 */
public final class ArrayUtil {
	
	private ArrayUtil() {
		// TODO
	}
	
	/**
	 * 判断数组是否为空
	 */
	public static boolean isEmpty(Object[] array) {
		return ArrayUtils.isEmpty(array);
	}
	
	/**
	 * 判断数组是否非空
	 */
	public static boolean isNotEmpty(Object[] array) {
		return !isEmpty(array);
	}
	
}
