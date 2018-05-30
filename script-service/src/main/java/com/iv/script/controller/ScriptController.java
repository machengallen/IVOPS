package com.iv.script.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iv.common.enumeration.ItemType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.script.api.constant.ErrorMsg;
import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.api.dto.TextUploadDto;
import com.iv.script.service.ScriptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "脚本库管理系列接口")
public class ScriptController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptController.class);
	@Autowired
	private ScriptService docLibraryService;

	/**
	 * 上传脚本文件
	 * 
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "上传脚本文件", notes = "87100")
	@RequestMapping(value = "/upload/file", method = RequestMethod.POST)
	public ResponseDto fileUpload(@RequestParam(required = true) MultipartFile file,
			@RequestParam(required = true) String alias, @RequestParam(required = true) ItemType itemType,
			HttpServletRequest request) {
		ResponseDto responseDto = new ResponseDto();
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return docLibraryService.fileUpload(file, userId, alias, itemType);// 文件上传
		} catch (Exception e) {
			LOGGER.error("上传脚本失败", e);
			responseDto.setErrorMsg(ErrorMsg.UPLOAD_FAILED);
			return responseDto;
		}
	}

	@ApiOperation(value = "上传（或更新）内容文本", notes = "87101")
	@RequestMapping(value = "/upload/text", method = RequestMethod.POST)
	public ResponseDto textUpload(@RequestBody TextUploadDto dto, HttpServletRequest request) {
		ResponseDto responseDto = new ResponseDto();
		try {

			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			if (null != dto.getId()) {
				return docLibraryService.textUpdate(dto.getId(), dto.getText(), dto.getType(), dto.getAlias(), userId,
						dto.getItemType());
			}
			return docLibraryService.textUpload(dto.getText(), dto.getType(), dto.getAlias(), userId,
					dto.getItemType());// 文本上传
		} catch (Exception e) {
			LOGGER.error("上传脚本失败", e);
			responseDto.setErrorMsg(ErrorMsg.UPLOAD_FAILED);
			return responseDto;
		}
	}

	/**
	 * 获取文件列表
	 * 
	 * @param
	 * @return
	 */
	@ApiOperation(value = "获取文件列表", notes = "87102")
	@RequestMapping(value = "get/list", method = RequestMethod.POST)
	public ResponseDto fileList(@RequestBody ScriptQueryDto query) {
		ResponseDto responseDto = new ResponseDto();
		try {
			responseDto.setData(docLibraryService.fileList(query));
			responseDto.setErrorMsg(ErrorMsg.OK);
			return responseDto;
		} catch (Exception e) {
			LOGGER.error("获取文件列表失败", e);
			responseDto.setErrorMsg(ErrorMsg.LIST_GET_FAILED);
			return responseDto;
		}
	}

	/**
	 * 获取文件
	 * 
	 * @param id
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "文件下载", notes = "87103")
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseDto download(@RequestParam int id, HttpServletResponse response) {
		ResponseDto responseDto = new ResponseDto();
		try {
			return docLibraryService.download(id, response);
		} catch (Exception e) {
			LOGGER.error("下载文件失败", e);
			responseDto.setErrorMsg(ErrorMsg.DOWNLOAD_FAILED);
			return responseDto;
		}
	}

	@ApiOperation(value = "文件编辑", notes = "87105")
	@RequestMapping(value = "/editor", method = RequestMethod.GET)
	public ResponseEntity<byte[]> editor(@RequestParam int id) {

		try {
			return docLibraryService.editor(id);
		} catch (Exception e) {
			LOGGER.error("下载文件失败", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param id
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "文件删除", notes = "87104")
	@RequestMapping(value = "/del", method = RequestMethod.POST)
	public ResponseDto fileDelete(@RequestBody List<Integer> ids) {
		ResponseDto responseDto = new ResponseDto();
		try {
			return docLibraryService.fileDelete(ids);
		} catch (Exception e) {
			LOGGER.error("删除文件失败", e);
			responseDto.setErrorMsg(ErrorMsg.DELETE_FAILED);
			return responseDto;
		}
	}
}
