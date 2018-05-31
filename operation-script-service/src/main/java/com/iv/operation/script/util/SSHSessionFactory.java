package com.iv.operation.script.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * ssh 会话工厂
 * @author macheng
 * 2018年5月24日
 * operation-script-service
 * 
 */
public class SSHSessionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSHSessionFactory.class);
	
	public static Session getSession(SSHAccount account) {
		
		try {
			JSch jsch=new JSch();
	        Session session = jsch.getSession(account.getUserName(),account.getIp(),account.getPort());
	        session.setPassword(account.getPassWord());
	        UserInfo userInfo = new UserInfo() {
                @Override
                public String getPassphrase() {
                    return null;
                }
                @Override
                public String getPassword() {
                    return null;
                }
                @Override
                public boolean promptPassword(String s) {
                    return false;
                }
                @Override
                public boolean promptPassphrase(String s) {
                    return false;
                }
                @Override
                public boolean promptYesNo(String s) {
                    return true;
                }
                @Override
                public void showMessage(String s) {
                    System.out.println("showMessage:"+s);
                }
            };
	        session.setUserInfo(userInfo);
	        return session;
		} catch (JSchException e) {
			LOGGER.error("获取ssh session失败");
			return null;
		}
	}
	
	/**
	 * 获取指定类型的channel，支持常用的三种：shell，exec，sftp
	 * @param session
	 * @param type
	 * @return
	 * @throws JSchException
	 */
	public static Channel getChannel(Session session, String type) throws JSchException {
		if(type.equals("shell")){
			return session.openChannel("shell");
	    }
	    if(type.equals("exec")){
	    	return session.openChannel("exec");
	    }
	    if(type.equals("sftp")){
	    	return session.openChannel("sftp");
	    }
	    return null;
	}
}
