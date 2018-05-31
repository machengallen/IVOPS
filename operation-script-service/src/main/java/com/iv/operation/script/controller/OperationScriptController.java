package com.iv.operation.script.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iv.common.response.ResponseDto;
import com.iv.operation.script.dto.OperationScriptDto;
import com.iv.operation.script.service.OperationScriptService;
import com.iv.operation.script.util.ErrorMsg;
import com.jcraft.jsch.JSchException;

import io.swagger.annotations.Api;

/**
 * 脚本作业API
 * 
 * @author macheng 2018年5月25日 operation-script-service
 * 
 */
@RestController
@Api(description = "脚本作业相关接口")
public class OperationScriptController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptController.class);
	@Autowired
	private OperationScriptService service;

	@PostMapping("/script/single")
	public ResponseDto singleScript(@RequestParam(required = false) MultipartFile file,
			@RequestBody OperationScriptDto dto) {

		try {
			if(null == file) {
				service.singleScript(file, dto);
			}
			service.singleScript(dto);
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			LOGGER.error("单脚本任务执行失败", e);
			return ResponseDto.builder(ErrorMsg.EXEC_FAILED);
		}
	}

}
