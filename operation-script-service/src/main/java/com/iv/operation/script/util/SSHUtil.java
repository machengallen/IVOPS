package com.iv.operation.script.util;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iv.operation.script.dto.OptResultDto;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ssh service层工具类
 * @author macheng
 * 2018年6月22日
 * operation-script-service
 * 
 */
public class SSHUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHUtil.class);

	/**
	 * shell单指令执行
	 * 
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	public static OptResultDto sshCmd(Session session, String cmd) {
		SSHExecutor executor = null;
		String result = null;
		try {
			executor = new SSHExecutor(session);
			result = executor.exec(cmd);
			return OptResultDto.build(true, result);
		} catch (JSchException e) {
			LOGGER.error("获取ssh session失败", e);
			return OptResultDto.build(false, "ssh连接失败");
		} catch (IOException e) {
			LOGGER.error("文件流读取失败", e);
			return OptResultDto.build(false, "文件流读取失败");
		} catch (InterruptedException e) {
			LOGGER.error("执行线程中断", e);
			return OptResultDto.build(false, "执行线程中断");
		} /*finally {
			if (null != executor) {
				executor.disconnect();
			}
		}*/
	}

	/**
	 * 删除脚本
	 * 
	 * @param session
	 * @param fileName
	 */
	public static void sftpDelete(Session session, String fileName) {
		SSHExecutor executor = null;
		try {
			executor = new SSHExecutor(session);
			executor.sftpRm(session, fileName);
		} catch (JSchException e) {
			LOGGER.error("获取ssh连接失败", e);
		} catch (SftpException e) {
			LOGGER.error("sftp操作失败", e);
		} /*finally {
			if (null != executor) {
				executor.disconnect();
			}
		}*/
	}

	/**
	 * 文件上传
	 * 
	 * @throws Exception
	 */
	public static OptResultDto sftpUpload(Session session, String fileName, InputStream fileStream) {
		SSHExecutor executor = null;
		try {
			executor = new SSHExecutor(session);
			executor.sftpPut(session, fileName, fileStream);
			return OptResultDto.build(true, null);
		} catch (JSchException e) {
			LOGGER.error("ssh连接失败", e);
			return OptResultDto.build(false, "ssh连接失败");
		} catch (SftpException e) {
			LOGGER.error("ssh连接失败", e);
			return OptResultDto.build(false, "sftp执行失败");
		} catch (IOException e) {
			LOGGER.error("文件流读取失败", e);
			return OptResultDto.build(false, "文件流读取失败");
		} /*finally {
			if (null != executor) {
				executor.disconnect();
			}
		}*/
	}
	
	/**
	 * 文件上传
	 * 
	 * @throws Exception
	 */
	public static OptResultDto sftpUpload(Session session, String fileName, byte[] fileBytes) {
		SSHExecutor executor = null;
		try {
			executor = new SSHExecutor(session);
			executor.sftpPut(session, fileName, fileBytes);
			return OptResultDto.build(true, null);
		} catch (JSchException e) {
			LOGGER.error("ssh连接失败", e);
			return OptResultDto.build(false, "ssh连接失败");
		} catch (SftpException e) {
			LOGGER.error("ssh连接失败", e);
			return OptResultDto.build(false, "sftp执行失败");
		} catch (IOException e) {
			LOGGER.error("文件流读取失败", e);
			return OptResultDto.build(false, "文件流读取失败");
		} /*finally {
			if (null != executor) {
				executor.disconnect();
			}
		}*/
	}
}
