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
	 * 临时文件存储
	 * @param fileName
	 * @param type
	 * @param inputStream
	 * @return 
	 * @throws IOException 
	 */
	public int tempWrite(String fileName, String type, byte[] content) throws IOException {		
		TemporaryScriptEntity temporaryScriptInfo = toSaveTempScript(fileName, type);
		//存储临时文件流
		File file = new File(temporaryPath + temporaryScriptInfo.getName());
		//File file = new File(temporaryPath);
		if (!file.getParentFile().exists()) {
			 file.getParentFile().mkdir();  
		}
		file.createNewFile();			
		try {			
			/*int index;  
		    byte[] bytes = new byte[1024]; */ 
		    FileOutputStream downloadFile = new FileOutputStream(file); 
		   /* while ((index = inputStream.read(bytes)) != -1) {  
		        downloadFile.write(bytes, 0, index);  
		        downloadFile.flush();  
		    }  */
		    downloadFile.write(content);
		    downloadFile.close();  
		   // inputStream.close();  
	        return temporaryScriptInfo.getId();
		} catch (IllegalStateException | IOException e) {
			// 回滚数据库文件信息
			LOGGER.error("临时文件保存失败", e);
			temporaryScriptDao.delById(temporaryScriptInfo.getId());
			return 0;
		}
		
	
	}
	
	/**
	 * 保存临时文件信息
	 * @param fileName
	 * @param type
	 * @return
	 */
	private TemporaryScriptEntity toSaveTempScript(String fileName, String type) {
		//临时文件信息存储
		TemporaryScriptEntity TemporaryScriptInfo = new TemporaryScriptEntity();		
		TemporaryScriptInfo.setType(type);
		long creaDate = System.currentTimeMillis();
		TemporaryScriptInfo.setCreDate(creaDate);
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
		TemporaryScriptInfo.setName(name);
		if(StringUtils.isEmpty(fileName)) {
			TemporaryScriptInfo.setAlias(name);
		}else {
			TemporaryScriptInfo.setAlias(fileName);
		}		
		TemporaryScriptEntity temporaryScriptInfo = temporaryScriptDao.saveOrUpdate(TemporaryScriptInfo);
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
}
