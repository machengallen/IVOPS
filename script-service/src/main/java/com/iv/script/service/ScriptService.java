package com.iv.script.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.iv.common.enumeration.ItemType;
import com.iv.common.response.ResponseDto;
import com.iv.outer.dto.LocalAuthDto;
import com.iv.script.api.constant.ErrorMsg;
import com.iv.script.api.dto.ScriptQueryDto;
import com.iv.script.dao.AuthorDaoImpl;
import com.iv.script.dao.ScriptDaoImpl;
import com.iv.script.entity.AuthorEntity;
import com.iv.script.entity.ScriptEntity;
import com.iv.script.entity.ScriptPagingWrap;
import com.iv.script.feign.client.IUserServiceClient;
import com.iv.script.util.FileReaderUtil;


@Service
public class ScriptService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);
	@Autowired
	private ScriptDaoImpl docScriptDaoImpl;
	@Autowired
	private IUserServiceClient userServiceClient;
	@Autowired
	private AuthorDaoImpl authorDaoImpl;

	@Value("${iv.script.repositoryPath}")
	private String scriptRepositoryPath;

	@Transactional
	public ResponseDto fileUpload(MultipartFile multipartFile, int userId, String alias, ItemType itemType) {

		if (!multipartFile.isEmpty()) {
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String type = originalFilename.substring(index + 1);
			// 封装作者信息
			LocalAuthDto authDto = userServiceClient.selectLocalAuthById(userId);
			AuthorEntity author = authorDaoImpl.selectById(userId);
			if(null == author) {
				author = new AuthorEntity();
				author.setUserId(userId);
				author.setRealName(authDto.getRealName());
				author.setEmail(authDto.getEmail());
				author.setTel(authDto.getTel());
			}
			// 存储文件信息至db
			String name = upload2db(author, itemType, type, alias);
			// 存储文件至文件系统
			try {
				multipartFile.transferTo(new File(scriptRepositoryPath + name));
			} catch (IllegalStateException | IOException e) {
				// 回滚数据库文件信息
				LOGGER.error("文件保存失败", e);
				docScriptDaoImpl.delByName(name);
				return ResponseDto.builder(ErrorMsg.UPLOAD_FAILED);
			}

			return ResponseDto.builder(ErrorMsg.OK);

		} else {
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}
	}

	@Transactional
	public ResponseDto textUpload(String text, String type, String alias, int userId, ItemType itemType) {

		if (!StringUtils.isEmpty(text)) {
			// 封装作者信息
			LocalAuthDto authDto = userServiceClient.selectLocalAuthById(userId);
			AuthorEntity author = authorDaoImpl.selectById(userId);
			if(null == author) {
				author = new AuthorEntity();
				author.setUserId(userId);
				author.setRealName(authDto.getRealName());
				author.setEmail(authDto.getEmail());
				author.setTel(authDto.getTel());
			}
			// 存储文件信息至db
			String name = upload2db(author, itemType, type, alias);
			// 存储文件至文件系统
			FileWriter fileWriter = null;
			try {
				File file = new File(scriptRepositoryPath + name);
				file.createNewFile();
				fileWriter = new FileWriter(file, true);
				fileWriter.write(text);
				fileWriter.flush();
			} catch (IllegalStateException | IOException e) {
				// 回滚数据库文件信息
				LOGGER.error("文件保存失败", e);
				docScriptDaoImpl.delByName(name);
				return ResponseDto.builder(ErrorMsg.UPLOAD_FAILED);
			} finally {
				try {
					fileWriter.close();
				} catch (IOException e) {
					LOGGER.error("文件流关闭失败", e);
				}
			}

			return ResponseDto.builder(ErrorMsg.OK);
		} else {
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}
	}
	
	@Transactional
	public ResponseDto textUpdate(int scriptId, String text, String type, String alias, int modifierId, ItemType itemType) {
		if (StringUtils.isEmpty(text)) {
			// 文本内容为空
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EMPTY);
		}
		
		ScriptEntity scriptEntity = docScriptDaoImpl.selectById(scriptId);
		if(null == scriptEntity) {
			// 当前编辑脚本不存在
			return ResponseDto.builder(ErrorMsg.SCRIPT_NOT_EXIST);
		}
		// 封装作者信息
		LocalAuthDto authDto = userServiceClient.selectLocalAuthById(modifierId);
		AuthorEntity modifier = authorDaoImpl.selectById(modifierId);
		if(null == modifier) {
			modifier = new AuthorEntity();
			modifier.setUserId(modifierId);
			modifier.setRealName(authDto.getRealName());
			modifier.setEmail(authDto.getEmail());
			modifier.setTel(authDto.getTel());
		}
		// 更新文件信息至db
		scriptEntity.setAlias(alias);
		scriptEntity.setItemType(itemType);
		scriptEntity.setModDate(System.currentTimeMillis());
		scriptEntity.setModifier(modifier);
		scriptEntity.setType(type);
		docScriptDaoImpl.save(scriptEntity);
		// 存储文件至文件系统
		FileWriter fileWriter = null;
		try {
			File file = new File(scriptRepositoryPath + scriptEntity.getName());
			if(file.exists()) {
				//file.delete();
				fileWriter = new FileWriter(file, false);
				fileWriter.write(text);
				fileWriter.flush();
			}
		} catch (IllegalStateException | IOException e) {
			// 回滚数据库文件信息
			LOGGER.error("文件保存失败", e);
			docScriptDaoImpl.delByName(scriptEntity.getName());
			return ResponseDto.builder(ErrorMsg.UPDATE_FAILED);
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				LOGGER.error("文件流关闭失败", e);
			}
		}

		return ResponseDto.builder(ErrorMsg.OK);
	}

	private String upload2db(AuthorEntity author, ItemType itemType, String type, String alias) {
		File file = new File(scriptRepositoryPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		long creDate = System.currentTimeMillis();
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(itemType.name());
		nameBuilder.append("-");
		nameBuilder.append(creDate);
		nameBuilder.append(".");
		nameBuilder.append(type);
		String name = nameBuilder.toString();
		// 文件名重复校验
		/*
		 * if (!cover) { if (null != docScriptDaoImpl.selectByName(alias, type,
		 * tenantId)) { return ResponseDto.builder(ErrorMsg.FILE_EXIST); } }
		 */

		// 存储脚本文件信息
		ScriptEntity docInfoEntity = new ScriptEntity();
		docInfoEntity.setAlias(alias);
		docInfoEntity.setItemType(itemType);
		docInfoEntity.setName(name);
		docInfoEntity.setType(type);
		docInfoEntity.setCreDate(creDate);
		docInfoEntity.setCreator(author);
		docScriptDaoImpl.save(docInfoEntity);

		return name;

	}

	public ScriptPagingWrap fileList(ScriptQueryDto query) {

		// 脚本文件库信息保存在租户db
		List<AuthorEntity> auths = null;
		if(!StringUtils.isEmpty(query.getCreator())) {
			auths = authorDaoImpl.selectByRealName(query.getCreator());
			if(CollectionUtils.isEmpty(auths)) {
				ScriptPagingWrap dto = new ScriptPagingWrap();
				dto.setTotalCount(0);
				dto.setEntities(new ArrayList<>());
				return dto;
			}
		} 
		return docScriptDaoImpl.selectByCondition(query, auths);
	}

	public ResponseDto download(int id, HttpServletResponse response) {

		ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
		File file = new File(scriptRepositoryPath + docInfoEntity.getName());
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment;filename=" + docInfoEntity.getName());
		response.setCharacterEncoding("UTF-8");
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			LOGGER.error("IO异常", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					LOGGER.error("IO异常", e);
				}
			}
		}
		return null;
	}

	public ResponseEntity<byte[]> editor(int id) throws IOException{
		
		ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
		File file = new File(scriptRepositoryPath + docInfoEntity.getName());
	    byte[] body = null;
	    InputStream is = new FileInputStream(file);
	    body = new byte[is.available()];
	    is.read(body);
	    is.close();
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    headers.add("Content-Disposition", "attchement;filename=" + docInfoEntity.getName());
	    HttpStatus statusCode = HttpStatus.OK;
	    ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
	    return entity;
	}
	
	public ResponseDto fileDelete(List<Integer> ids) {

		List<Integer> failedIds = new ArrayList<Integer>();
		for (Integer id : ids) {
			
			ScriptEntity docInfoEntity = docScriptDaoImpl.selectById(id);
			if (null == docInfoEntity) {
				failedIds.add(id);
				continue;
			}
			
			if (FileReaderUtil.deleteFile(scriptRepositoryPath + docInfoEntity.getName())) {
				docScriptDaoImpl.delById(id);
			} else {
				failedIds.add(id);
			}
		}
		
		if(!CollectionUtils.isEmpty(failedIds)) {
			ResponseDto responseDto = new ResponseDto();
			responseDto.setErrorMsg(ErrorMsg.SCRIPT_DEL_PARTIAL_FAILED);
			responseDto.setData(failedIds);
			return responseDto;
		}else {
			return ResponseDto.builder(ErrorMsg.OK);
		}

	}

}
