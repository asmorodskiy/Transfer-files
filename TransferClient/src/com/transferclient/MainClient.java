package com.transferclient;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			boolean correct_params=true;
			
			if(args.length<3 && args.length<5) correct_params=false;
			
			String targetHost = args[0];
			
			int port = Integer.valueOf(args[1]);
			if(port<0 && port>65536 ) correct_params=false;
			
			String filePath=args[2];
			
			String targetPath="";
			
			if(args.length>3) targetPath=args[3];
			
			if(!correct_params)
			{
				System.out.print("Usage : 'java MainClient <target_host> <target_port>  <file path> [<target path>]' ");
				System.exit(1);
			}
		
		    Socket sock;
		    
			try {				
				sock = new Socket(targetHost,port);				
				
					// sendfile
			      File myFile = new File (filePath);
			      OutputStream os = sock.getOutputStream();
			      OutputStreamWriter os_writer = new OutputStreamWriter(os);
			      BufferedWriter bufr = new BufferedWriter(os_writer);
			      
			      Thread.sleep(1000);
			      if(targetPath.isEmpty()) bufr.write(String.format("%d|%d%s",myFile.length(),0,myFile.getName()));
			      else bufr.write(String.format("%d|%d%s",myFile.length(),1,targetPath));
			      
			      bufr.flush();
			      bufr.close();
			      sock.close();
			      Thread.sleep(1000);
			      
			      Socket sockWrite = new Socket(targetHost,port-1);
			      byte [] mybytearray  = new byte [(int)myFile.length()];
			      FileInputStream fis = new FileInputStream(myFile);
			      BufferedInputStream bis = new BufferedInputStream(fis);
			      bis.read(mybytearray,0,mybytearray.length);
			      OutputStream wos = sockWrite.getOutputStream();			      
			      wos.write(mybytearray,0,mybytearray.length);
			      wos.flush();
			      
			   	
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    	  

	}

}
