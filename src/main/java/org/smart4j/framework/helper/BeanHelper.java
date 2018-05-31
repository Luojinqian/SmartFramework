package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectionUtil;

/**
 * Bean 助手类
 * @author 鑫哲
 * @Note 
 * 所有对象都是从当前线程类加载器获取的class通过反射创建实例，
 * 然后存入Bean Map中，所有的对象都是单例的。
 */
public final class BeanHelper {

	/**
	 * 定义 Bean 映射(用于存放 Bean 类与 Bean 实例的映射关系)
	 * @Note
	 * IOC容器
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static {
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for (Class<?> beanClass : beanClassSet) {
			Object obj = ReflectionUtil.newInstance(beanClass);
			BEAN_MAP.put(beanClass, obj);
		}
	}
	
	/**
	 * 获取 Bean 映射(IOC容器)
	 */
	public static Map<Class<?>, Object> getBeanMap() {
		return BEAN_MAP;
	}
	
	/**
	 * 获取 Bean 实例(IOC容器中的对象)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> cls) {
		if (!BEAN_MAP.containsKey(cls)) {
			throw new RuntimeException("can not get bean by class: " + cls);
		}
		return (T) BEAN_MAP.get(cls);
	}
	
}
