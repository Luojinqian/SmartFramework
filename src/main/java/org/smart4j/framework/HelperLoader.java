package org.smart4j.framework;

import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;
import org.smart4j.framework.util.ClassUtil;

/**
 * 加载相应的Helper类
 * 
 * @author 鑫哲
 * @Node 初始化框架：我们创建了ClassHelper、BeanHelper、IocHelper、ControllerHelper
 *       四个Helper类需要通过一个入口程序来加载它们，实际上是加载它们的静态块。
 */
public final class HelperLoader {

	public static void init() {
		Class<?>[] classList = { 
				ClassHelper.class, 
				BeanHelper.class, 
				IocHelper.class, 
				ControllerHelper.class
				};
		for (Class<?> cls : classList) {
			ClassUtil.loadClass(cls.getName(), false);
		}
	}

}
