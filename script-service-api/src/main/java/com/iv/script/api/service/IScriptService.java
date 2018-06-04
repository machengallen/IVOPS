package com.iv.script.api.service;

import java.io.InputStream;

public interface IScriptService {

	/**
	 * 存储临时文件
	 * @param inputStream
	 * @return
	 */
	int tempWrite(String fileName, InputStream inputStream);
	
	/**
	 * 获取临时文件流
	 * @param scriptId
	 * @return
	 */
	InputStream tempRead(int scriptId);
	
	/**
	 * 获取正式脚本库文件流
	 * @param scriptId
	 * @return
	 */
	InputStream officialRead(int scriptId);
	
	
}
