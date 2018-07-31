package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

/**
 * 依赖注入助手类
 * @author 鑫哲
 *
 */
public class IocHelper {

	static {
		// 获取 IOC 容器：所有Bean类与Bean实例之间的映射关系
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if (CollectionUtil.isNotEmpty(beanMap)) {
			// 遍历Bean Map
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				// 从Bean Map中获取 Bean类与 Bean实例
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				// 获取 Bean类定义的所有成员变量 (简称 Bean Field)
				Field[] beanFields = beanClass.getDeclaredFields();
				if (ArrayUtil.isNotEmpty(beanFields)) {
					// 遍历 Bean Field
					for (Field beanField : beanFields) {
						// 判断当前 Bean Field 是否带有Inject注解
						if (beanField.isAnnotationPresent(Inject.class)) {
							// 在 Bean Map中获取Bean Field对应的实例
							Class<?> beanFieldClass = beanField.getType();
							Object beanFieldInstance = beanMap.get(beanFieldClass);
							if (beanFieldInstance != null) {
								// 通过反射初始化 BeanField的值
								ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
							}
						}
					}
				}
			}
		}
	}
}
