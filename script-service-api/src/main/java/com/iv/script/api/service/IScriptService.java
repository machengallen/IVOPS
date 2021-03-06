package com.iv.script.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.iv.common.response.ResponseDto;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.TemporaryScriptDto;

public interface IScriptService {

	/**
	 * 存储/编辑临时文件
	 * 
	 * @param inputStream
	 * @return
	 */
	@RequestMapping(value = "/temp/write", method = RequestMethod.POST)
	int tempWrite(@RequestParam("fileName") String fileName, @RequestParam("type") String type,
			@RequestParam("content") byte[] content, @RequestParam("scriptId") Integer scriptId);

	/**
	 * 获取临时文件流
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/temp/read", method = RequestMethod.GET)
	ResponseEntity<byte[]> tempRead(@RequestParam("scriptId") int scriptId);

	/**
	 * 获取正式脚本库文件流
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/official/read", method = RequestMethod.GET)
	ResponseEntity<byte[]> officialRead(@RequestParam("scriptId") int scriptId);

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
	
	/**
	 * 删除临时文件
	 * 
	 * @param scriptId
	 * @return
	 */
	@RequestMapping(value = "/delete/temporaryScript", method = RequestMethod.GET)
	ResponseDto deleteTemporaryScript(@RequestParam("scriptId") int scriptId);		

}
