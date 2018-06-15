package com.iv.script.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.iv.common.enumeration.ItemType;
import com.iv.common.response.ResponseDto;
import com.iv.common.util.spring.JWTUtil;
import com.iv.script.api.constant.ErrorMsg;
import com.iv.script.api.dto.ProcessDataDto;
import com.iv.script.api.dto.ScriptApproveReq;
import com.iv.script.api.dto.ScriptDto;
import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.iv.script.api.dto.TextUploadDto;
import com.iv.script.api.service.IScriptService;
import com.iv.script.service.ScriptService;
import com.iv.script.service.TemporaryScriptService;
import com.iv.script.util.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(description = "脚本库管理系列接口")
public class ScriptController implements IScriptService{

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptController.class);
	@Autowired
	private ScriptService docLibraryService;
	@Autowired
	private TemporaryScriptService temporaryScriptService;

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
			@RequestParam(required = true) String remark, HttpServletRequest request) {
		ResponseDto responseDto = new ResponseDto();
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return docLibraryService.fileUpload(file, userId, alias, itemType, remark);// 文件上传
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
						dto.getItemType(), dto.getRemark());
			}
			return docLibraryService.textUpload(dto.getText(), dto.getType(), dto.getAlias(), userId,
					dto.getItemType(), dto.getRemark());// 文本上传
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

	/*@ApiOperation(value = "文件编辑", notes = "87105")
	@RequestMapping(value = "/editor", method = RequestMethod.GET)
	public ResponseEntity<byte[]> editor(@RequestParam int id) {

		try {
			return docLibraryService.editor(id);
		} catch (Exception e) {
			LOGGER.error("下载文件失败", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}*/

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

	@Override
	@ApiOperation(value = "临时文件流存储")
	public int tempWrite(String fileName, String type, byte[] content) {
		// TODO Auto-generated method stub
		try {
			return temporaryScriptService.tempWrite(fileName, type, content);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("临时文件流存储失败");
			return 0;
		}
		
	}

	@Override
	@ApiOperation(value = "临时文件流获取")
	public ResponseEntity<byte[]> tempRead(int scriptId) {
		// TODO Auto-generated method stub
		try {
			return temporaryScriptService.tempRead(scriptId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("临时文件流获取失败");
			return null;
		}
		
	}

	@Override
	@ApiOperation(value = "正式文件流获取")
	public ResponseEntity<byte[]> officialRead(int scriptId) {
		// TODO Auto-generated method stub
		try {
			return docLibraryService.officialRead(scriptId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("正式文件流获取失败");
			return null;
		}
	}

	@Override
	@ApiOperation(value = "脚本库文件信息查询")
	public ScriptDto scriptInfoById(int scriptId) {
		// TODO Auto-generated method stub
		try {
			return docLibraryService.scriptInfoById(scriptId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("脚本库文件信息查询失败");			
		}
		return null;
	}

	@Override
	@ApiOperation(value = "临时脚本文件信息查询")
	public TemporaryScriptDto temporaryScriptInfoById(int scriptId) {
		// TODO Auto-generated method stub
		try {
			return temporaryScriptService.temporaryScriptInfoById(scriptId);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("临时库文件信息查询失败");			
		}
		return null;
	}
	
	@ApiOperation(value = "领域脚本量统计", notes = "87106")
	@RequestMapping(value = "/itemType/count", method = RequestMethod.GET)
	public ResponseDto itemTypeCount() {
		try {
			return ResponseDto.builder(ErrorMsg.OK, docLibraryService.itemTypeCount());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("领域脚本量统计失败");	
			return ResponseDto.builder(ErrorMsg.ITEMTYPE_SCRIPT_FAILED);
		}				
		
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "个人脚本量统计", notes = "87107")
	@RequestMapping(value = "/personalScript/count", method = RequestMethod.GET)
	public ResponseDto personalScriptCount(HttpServletRequest request) {
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return ResponseDto.builder(ErrorMsg.OK, docLibraryService.personalScriptCount(userId));
		} catch (Exception e) {
			// TODO: handle exception`
			LOGGER.info("个人脚本量统计失败");	
			return ResponseDto.builder(ErrorMsg.PERSONAL_SCRIPT_FAILED);
		}	
	}
	
	/**
	 * 提交脚本审批
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "提交脚本审批", notes = "87108")
	@RequestMapping(value = "/script/apply", method = RequestMethod.GET)
	public ResponseDto scriptApply(int scriptId,HttpServletRequest request) {
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			return docLibraryService.scriptApply(userId,scriptId);
		} catch (Exception e) {
			// TODO: handle exception`
			LOGGER.info("提交脚本审核失败");	
			return ResponseDto.builder(ErrorMsg.SCRIPT_APPLY_FAILED);
		}	
	}
	
	@ApiOperation(value = "脚本申请流审批", notes = Constant.STORE_TASKT_CODE)
	@PostMapping("/approve")
	public ResponseDto taskApprove(HttpServletRequest request,
			@RequestBody ScriptApproveReq dto) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			docLibraryService.taskApprove(userId, dto.getTaskId(), dto.isApproved(), dto.getRemark());
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("脚本审批失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.APPROVE_SCRIPT_FAILED);
			return response;
		}
	}
	
	@ApiOperation(value = "待我审批的脚本", notes = "87110")
	@GetMapping("/get/pending/store/script")
	public ResponseDto getUserTasksScriptStore(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto tasks = docLibraryService.getUserTasksScriptStore(userId, (curPage - 1) * items, items);
			response = new ResponseDto();
			response.setData(tasks);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("获取审批任务失败", e);
			response = new ResponseDto();
			response.setErrorMsg(com.iv.common.response.ErrorMsg.GET_DATA_FAILED);
			return response;
		} 
	}
	
	@ApiOperation(value = "我发起的脚本申请", notes = "87111")
	@GetMapping("/get/my/apply")
	public ResponseDto getMyScriptApply(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = docLibraryService.getMyScriptApply(userId, (curPage - 1) * items, items);
			response = new ResponseDto();
			response.setData(data);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("我的脚本申请列表查询失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.MY_SCRIPT_APPLY_FAILED);
			return response;
		}
	}
	
	@ApiOperation(value = "我已审批的脚本申请", notes = "87113")
	@GetMapping("/get/my/approved")
	public ResponseDto getMyApprovedScript(HttpServletRequest request,
			@RequestParam(value = "curPage", required = true) int curPage,
			@RequestParam(value = "items", required = true) int items) {
		ResponseDto response = null;
		try {
			int userId = JWTUtil.getJWtJson(request.getHeader("Authorization")).getInt("userId");
			ProcessDataDto data = docLibraryService.getMyApprovedScript(userId, (curPage - 1) * items, items);
			response = new ResponseDto();
			response.setData(data);
			response.setErrorMsg(ErrorMsg.OK);
			return response;
		} catch (Exception e) {
			LOGGER.error("我审批过的脚本申请列表查询失败", e);
			response = new ResponseDto();
			response.setErrorMsg(ErrorMsg.MY_APPLIED_SCRIPT_FAILED);
			return response;
		}
	}
	
	/**
	 * 查询脚本详细信息：脚本基础信息、脚本内容
	 * @param scriptId
	 * @return
	 */
	@ApiOperation(value = "脚本详情(脚本快速执行)", notes = "87112")
	@RequestMapping(value = "/script/detailInfo", method = RequestMethod.GET)
	public ResponseDto getScriptDetailInfo(int scriptId) {
		try {
			return ResponseDto.builder(ErrorMsg.OK, docLibraryService.getScriptDetailInfo(scriptId));
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("获取快速执行失败");
			return ResponseDto.builder(ErrorMsg.SELECT_SCRIPT_INFO_FAILED);
		}
	}
	
	
	/*public ResponseDto scriptSearch() {
		
	}*/
	
}
