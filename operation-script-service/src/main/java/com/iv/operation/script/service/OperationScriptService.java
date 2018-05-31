package com.iv.operation.script.service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iv.operation.script.dao.impl.OperationScriptDaoImpl;
import com.iv.operation.script.dto.OperationScriptDto;
import com.iv.operation.script.util.SSHAccount;
import com.iv.operation.script.util.SSHExecutor;
import com.iv.operation.script.util.SSHSessionFactory;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service
public class OperationScriptService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationScriptService.class);

	@Autowired
	private OperationScriptDaoImpl scriptOperationDao;
	
	public void singleScript(OperationScriptDto dto) {
		
	}
	
	public void singleScript(MultipartFile file, OperationScriptDto dto) throws Exception {
		InputStream inputStream = file.getInputStream();
		String oriName = file.getOriginalFilename();
		String contextType = file.getContentType();
		String name = file.getName();
		SSHAccount account = new SSHAccount(dto.getAccount(), dto.getPassword(), dto.getHostIp());
		sftpUpload(account, inputStream);
	}
	/**
	 * shell单指令执行
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	public String sshCmd(String cmd) throws IOException, JSchException, InterruptedException {
		SSHAccount account = new SSHAccount("root", "1234qwer", "10.10.31.21");
		Session session = SSHSessionFactory.getSession(account);
		SSHExecutor executor = null;
		String result = null;
		try {
			executor = new SSHExecutor(session);
			result = executor.exec(cmd);
		} catch (JSchException e) {
			LOGGER.error("获取ssh session失败", e);
		} finally {
			if(null != executor) {
				executor.disconnect();
			}
		}
		return result;
	}
	
	/**
	 * 文件上传
	 * @throws Exception
	 */
	public void sftpUpload(SSHAccount account, InputStream fileStream) throws Exception{
		Session session = SSHSessionFactory.getSession(account);
		SSHExecutor executor = null;
		try {
			executor = new SSHExecutor(session);
			executor.sftp(session, "ivops_operation.py", fileStream);
		} catch (JSchException e) {
			LOGGER.error("获取ssh session失败", e);
		} finally {
			if(null != executor) {
				executor.disconnect();
			}
		}
		
	}
}
