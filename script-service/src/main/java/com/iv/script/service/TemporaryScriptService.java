package com.iv.script.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iv.common.response.ResponseDto;
import com.iv.script.api.constant.ErrorMsg;
import com.iv.script.api.dto.TemporaryScriptDto;
import com.iv.script.dao.TemporaryScriptDaoImpl;
import com.iv.script.entity.TemporaryScriptEntity;

@Service
public class TemporaryScriptService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryScriptService.class);
	@Value("${iv.script.temporaryPath}")
	private String temporaryPath;
	@Autowired
	private TemporaryScriptDaoImpl temporaryScriptDao;
	
	/**
	 * 查询临时库文件信息
	 * @param id
	 * @return
	 */
	public TemporaryScriptDto temporaryScriptInfoById(int id) {
		TemporaryScriptEntity temporaryScript = temporaryScriptDao.selectById(id);
		TemporaryScriptDto temporaryScriptDto = new TemporaryScriptDto();		
		BeanCopier copier = BeanCopier.create(TemporaryScriptEntity.class, TemporaryScriptDto.class, false);
		copier.copy(temporaryScript, temporaryScriptDto, null);
		return temporaryScriptDto;
	}
	
	/**
	 * 临时文件存储/编辑
	 * @param fileName
	 * @param type
	 * @param inputStream
	 * @return 
	 * @throws IOException 
	 */
	public int tempWrite(String fileName, String type, byte[] content, Integer scriptId) throws IOException {		
		TemporaryScriptEntity temporaryScriptInfo = toSaveTempScript(fileName, type, scriptId);
		//存储临时文件流
		File file = new File(temporaryPath + temporaryScriptInfo.getName());		
		if (!file.getParentFile().exists()) {
			 file.getParentFile().mkdir();  
		}
		file.createNewFile();			
		try {						
		    FileOutputStream downloadFile = new FileOutputStream(file); 		   
		    downloadFile.write(content);
		    downloadFile.close();  		  
	        return temporaryScriptInfo.getId();
		} catch (IllegalStateException | IOException e) {
			// 回滚数据库文件信息
			LOGGER.error("临时文件保存失败", e);
			temporaryScriptDao.delById(temporaryScriptInfo.getId());
			return 0;
		}
		
	
	}
	
	/**
	 * 保存/编辑临时文件信息
	 * @param fileName
	 * @param type
	 * @return
	 */
	private TemporaryScriptEntity toSaveTempScript(String fileName, String type, Integer scriptId) {
		TemporaryScriptEntity temporaryScriptInfo = null;
		if(null != scriptId) {
			//临时文件编辑
			temporaryScriptInfo = temporaryScriptDao.selectById(scriptId);
			//删除对应的临时文件
			try {
				File file = new File(temporaryPath + temporaryScriptInfo.getName());			
				if(file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				// TODO: handle exception
				LOGGER.error("临时文件删除失败");
			}
			
		}else {
			//临时文件信息存储
			temporaryScriptInfo = new TemporaryScriptEntity();
		}				
		temporaryScriptInfo.setType(type);
		long creaDate = System.currentTimeMillis();
		temporaryScriptInfo.setCreDate(creaDate);
		StringBuilder nameBuilder = new StringBuilder();		
		int radm = (int)(1+Math.random()*(100));
		if(StringUtils.isEmpty(fileName)) {
			nameBuilder.append(radm);
		}else {
			nameBuilder.append(fileName);
		}		
		nameBuilder.append("-");
		nameBuilder.append(creaDate);
		nameBuilder.append(".");
		nameBuilder.append(type);
		String name = nameBuilder.toString();
		temporaryScriptInfo.setName(name);
		if(StringUtils.isEmpty(fileName)) {
			temporaryScriptInfo.setAlias(name);
		}else {
			temporaryScriptInfo.setAlias(fileName);
		}		
		temporaryScriptInfo = temporaryScriptDao.saveOrUpdate(temporaryScriptInfo);
		return temporaryScriptInfo;
	}
	
	/**
	 * 获取临时文件流
	 * @param scriptId
	 * @return
	 * @throws IOException 
	 */
	public ResponseEntity<byte[]> tempRead(int scriptId) throws IOException {
		TemporaryScriptEntity docInfoEntity = temporaryScriptDao.selectById(scriptId);
		File file = new File(temporaryPath + docInfoEntity.getName());
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
	
	/**
	 * 删除临时文件
	 * @param scriptId
	 * @return
	 */
	public ResponseDto deleteTemporaryScript(int scriptId){
		TemporaryScriptEntity temporaryScriptInfo = temporaryScriptDao.selectById(scriptId);
		//删除对应的临时文件
		try {
			File file = new File(temporaryPath + temporaryScriptInfo.getName());			
			if(file.exists()) {
				file.delete();
			}
			temporaryScriptDao.delById(temporaryScriptInfo.getId());
			return ResponseDto.builder(ErrorMsg.OK);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("临时文件删除失败");
			return ResponseDto.builder(ErrorMsg.DELETE_TEMPORARY_SCRIPT_FAILED);
		}
	}
}
