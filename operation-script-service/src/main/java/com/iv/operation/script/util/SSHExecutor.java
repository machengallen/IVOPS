package com.iv.operation.script.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * ssh执行工具类
 * 
 * @author macheng 
 * 2018年5月24日 
 * operation-script-service
 * 
 */
public class SSHExecutor {

	//private static final Logger LOGGER = LoggerFactory.getLogger(SSHExecutor.class);
	private Session session;

	public SSHExecutor(Session session) throws JSchException {
		super();
		this.session = session;
		if(!this.session.isConnected()) {
			this.session.connect(Constant.SESSION_TIMEOUT);
		}
	}

	public SSHExecutor() {
		super();
	}

	/**
	 * 交互式shell命令执行
	 * @param cmd
	 * @param outputFileName
	 * @return
	 * @throws JSchException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String shell(String cmd, String outputFileName) throws JSchException, IOException, InterruptedException {
		Channel channel = this.session.openChannel("shell");
		PipedInputStream pipeIn = new PipedInputStream();
		PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
		FileOutputStream fileOut = new FileOutputStream(outputFileName, true);
		channel.setInputStream(pipeIn);
		channel.setOutputStream(fileOut);
		channel.connect(Constant.CHANNEL_TIMEOUT);

		pipeOut.write(cmd.getBytes());
		Thread.sleep(100);
		pipeOut.close();
		pipeIn.close();
		fileOut.close();
		channel.disconnect();
		
		return null;
	}

	/**
	 * 单条shell命令执行
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 * @throws InterruptedException
	 */
	public String exec(String cmd) throws IOException, JSchException, InterruptedException{
		ChannelExec channelExec = null;
		StringBuffer buf = null;
		
		
		// read方式读取，ChannelShell模式下会阻塞
		/*reader = new BufferedReader(new InputStreamReader(in, Charset.forName(charset)));
		String buf = null;
		while ((buf = reader.readLine()) != null) {
		    System.out.println(buf);
		}*/
		try {
			//BufferedReader reader = null;
			channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand(cmd);
			channelExec.setInputStream(null);
			channelExec.setErrStream(System.err);
			InputStream in = channelExec.getInputStream();
			channelExec.connect();
			int res = -1;
			buf = new StringBuffer(1024);
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					buf.append(new String(tmp, 0, i));
				}
				if (channelExec.isClosed()) {
					res = channelExec.getExitStatus();
					System.out.println(String.format("SSH channel " + "Exit-status: %d", res));
					break;
				}
				TimeUnit.MILLISECONDS.sleep(100);
			} 
		} finally {
			/*try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
			if(null != channelExec) {
				channelExec.disconnect();
			}
			//session.disconnect();
		}
		return buf == null ? null : buf.toString();
	}
	
	/**
	 * sftp进行文件上传
	 * @param session
	 * @param uploadFileName
	 * @throws JSchException 
	 * @throws SftpException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public void sftpPut(Session session, String uploadFileName, InputStream fileStream) throws JSchException, SftpException, IOException {  
		ChannelSftp sftp = null;  
          

        //列出指定的文件列表,可自定义保存位置 
        /*Vector v = sftp.ls("/");  
        for(int i=0; i < v.size(); i++){  
            System.out.println(v.get(i));  
        }*/

        //要实现下载，对换以下流就可以
        OutputStream outstream = null;  
        //InputStream instream = new FileInputStream(new File(filePath));

        try {
			//创建sftp通信通道  
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect(Constant.CHANNEL_TIMEOUT);
			//进入服务器指定的文件夹  
			sftp.cd("/root");
			outstream = sftp.put(uploadFileName);
			byte b[] = new byte[1024];
			int n;
			while ((n = fileStream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}
			outstream.flush();
		} finally {
			if(null != outstream) {
				outstream.close();  
			}
			if(null != fileStream) {
				fileStream.close();  
			}
			if(null != sftp) {
				sftp.disconnect();  
			}
		}
    }
	
	/**
	 * 文件删除
	 * @param session
	 * @param uploadFileName
	 * @throws JSchException 
	 * @throws SftpException 
	 */
	public void sftpRm(Session session, String uploadFileName) throws JSchException, SftpException {
		ChannelSftp sftp = null;
		
		try {
			sftp = (ChannelSftp)session.openChannel("sftp");
			sftp.connect(Constant.CHANNEL_TIMEOUT);
			sftp.chmod(755, "/root/" + uploadFileName);
			sftp.rm("/root/" + uploadFileName);
			
		} finally {
			if(null != sftp) {
				sftp.disconnect();  
			}
		}
	}

	public Session getSession() {
		return this.session;
	}

	public void disconnect() {
		getSession().disconnect();
	}

}
