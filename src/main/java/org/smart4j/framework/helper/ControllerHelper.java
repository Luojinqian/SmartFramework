package org.smart4j.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;

/**
 * 控制器助手类
 * 
 * @author 鑫哲
 * @Node 处理器映射器
 */
public class ControllerHelper {

	/**
	 * 用于存放请求与处理器的映射关系(简称 Action Map)
	 */
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();
	
	static {
		// 获取所有Controller类
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
		if (CollectionUtil.isNotEmpty(controllerClassSet)) {
			// 获取所有Action方法
			for (Class<?> controllerClass : controllerClassSet) {
				Method[] methods = controllerClass.getDeclaredMethods();
				if (ArrayUtil.isNotEmpty(methods)) {
					for (Method method : methods) {
						if (method.isAnnotationPresent(Action.class)) {
							// 获取URL
							Action action = method.getAnnotation(Action.class);
							String mapping = action.value();
							// 验证URL规则
							if (mapping.matches("\\w+:/\\w*")) {
								String[] array = mapping.split(":");
								if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
									// 获取请求方法与请求路径
									String requestMethod = array[0];
									String requestPath = array[1];
									// 构造Request
									Request request = new Request(requestMethod, requestPath);
									// 构造Handler
									Handler handler = new Handler(controllerClass, method);
									// 初始化ACTION_MAP
									ACTION_MAP.put(request, handler);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取Handler
	 */
	public static Handler getHandler(String requestMethod, String requestPath) {
		Request request = new Request(requestMethod, requestPath);
		Handler handler = ACTION_MAP.get(request);
		return handler;
	}
	
}
