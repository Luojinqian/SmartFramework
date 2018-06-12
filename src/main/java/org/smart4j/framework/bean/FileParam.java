package org.smart4j.framework.bean;

import java.io.InputStream;

/**
 * 封装上传文件参数
 * 
 * @author 鑫哲
 *
 */
public class FileParam {
	
	// 文件表单的字段名
	private String fieldName;
	// 上传文件名
	private String fileName;
	// 上传文件大小
	private long fileSize;
	// 上传文件类型
	private String contentType;
	// 上传文件的字节流
	private InputStream inputStream;
	
	public FileParam(String fieldName, String fileName, long fileSize, String contentType, InputStream inputStream) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
}
