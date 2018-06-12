package org.smart4j.framework.bean;

/**
 * 封装表单参数
 * 
 * @author 鑫哲
 *
 */
public class FormParam {
	
	private String fieldName;
	private Object fieldValue;
	
	public FormParam(String fieldName, Object fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}
	
}
