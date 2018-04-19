/*
    Code written for cmpsc441 class
    by Professor. Mohan using the Jsch Library.
    This class provides the methods such as running shell commands on remote machines,
    copying data from one machine to another, etc.

*/
//package cloud;
import java.lang.ProcessBuilder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class AWSShell {
	public static void copyFile(String SourceDIR, String DestinationDIR,
			String strHostName, String strUserName, String strPassword) {
		String SFTPHOST = strHostName;
		int SFTPPORT = 22;
		String SFTPUSER = strUserName;
		String SFTPPASS = strPassword;
		String SFTPWORKINGDIR = DestinationDIR;
		String FILETOTRANSFER = SourceDIR;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPort(22);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			File f = new File(FILETOTRANSFER);
			channelSftp.put(new FileInputStream(f), f.getName());
			channel.disconnect();
			session.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public static String executeShellCommands(String hostName, String strUserName,
			String strPassword, List<String> commands) {
		String strLogMessages = "";
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(strUserName, hostName, 22);
			session.setPassword(strPassword);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			Channel channel = session.openChannel("shell");// only shell
			OutputStream inputstream_for_the_channel = channel
					.getOutputStream();
			PrintStream shellStream = new PrintStream(
					inputstream_for_the_channel, true);
			channel.connect();
			for (String command : commands) {
				shellStream.println(command);
				shellStream.flush();
			}

			shellStream.close();

			InputStream outputstream_from_the_channel = channel
					.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					outputstream_from_the_channel));
			String line;

			// while ((line = br.readLine()) != null) {
			// 	strLogMessages = strLogMessages + line + "\n";
			// 	//System.out.println(strLogMessages);
			// }
			Thread.sleep(20000);

			do {

			} while (!channel.isEOF());

			outputstream_from_the_channel.close();
			br.close();
			session.disconnect();
			channel.disconnect();

			return strLogMessages;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strLogMessages;
	}

	public static String executeCommandsInEC2( String pemFileLocation, String strHostName, String strUserName,
			ArrayList<String> commands){
		String strLogMessages = "";
	    try {
			JSch jsch = new JSch();
	    	jsch.addIdentity(pemFileLocation);
			Session session = jsch.getSession(strUserName, strHostName, 22);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		    Channel channel=session.openChannel("shell");//only shell
	        OutputStream inputstream_for_the_channel = channel.getOutputStream();
	        PrintStream shellStream = new PrintStream(inputstream_for_the_channel, true);
	        channel.connect();
	        for(String command: commands) {
	            shellStream.println(command);
	            shellStream.flush();
	        }
	        shellStream.close();
	        InputStream outputstream_from_the_channel = channel.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
	        String line;
	        // while ((line = br.readLine()) != null){
	        // 		strLogMessages = strLogMessages + line+"\n";
	        // }
					Thread.sleep(20000);
					channel.disconnect();
	        outputstream_from_the_channel.close();
	        br.close();
	        session.disconnect();
	        channel.disconnect();
	        return strLogMessages;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return strLogMessages;
	}

	public static void placeFiles(String pemFileLocation, String strHostName, String strUserName){
		try {

			//ProcessBuilder procs = new ProcessBuilder(pro).start();
		JSch jsch = new JSch();
		jsch.addIdentity(pemFileLocation);
		Session session = jsch.getSession(strUserName, strHostName, 22);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
			Channel channel=session.openChannel("sftp");//only shell
				OutputStream inputstream_for_the_channel = channel.getOutputStream();
				PrintStream shellStream = new PrintStream(inputstream_for_the_channel, true);
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;
				sftp.put("DogsData.zip", "/home/ubuntu/DogsData.zip");
				sftp.put("runSetup.sh", "/home/ubuntu/runSetup.sh");
				shellStream.close();
				InputStream outputstream_from_the_channel = channel.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
				String line;
				do {
				} while(!channel.isEOF());
				outputstream_from_the_channel.close();
				br.close();
				session.disconnect();
				channel.disconnect();
		} catch (Exception e) {
				e.printStackTrace();
		}
	}

}
