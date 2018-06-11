package com.iv.script.api.service;

import java.io.InputStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.TemporaryScriptDto;

public interface IScriptService {

	/**
	 * 存储临时文件
	 * 
	 * @param inputStream
	 * @return
	 */
	@RequestMapping(value = "/temp/write", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	int tempWrite(@RequestParam("fileName") String fileName, @RequestParam("type") String type,
			@RequestParam("fileStream") MultipartFile file);

	/**
	 * 获取临时文件流
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/temp/read", method = RequestMethod.GET)
	InputStream tempRead(@RequestParam("scriptId") int scriptId);

	/**
	 * 获取正式脚本库文件流
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/official/read", method = RequestMethod.GET)
	InputStream officialRead(@RequestParam("scriptId") int scriptId);

	/**
	 * 查询脚本库文件信息
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/script/info", method = RequestMethod.GET)
	ScriptDto scriptInfoById(@RequestParam("scriptId") int scriptId);

	/**
	 * 查询临时库脚本文件信息
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/temporaryScript/info", method = RequestMethod.GET)
	TemporaryScriptDto temporaryScriptInfoById(@RequestParam("scriptId") int scriptId);

}
