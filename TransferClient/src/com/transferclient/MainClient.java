package com.transferclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
		
		    Socket welcomeSocket;
		    
			try {				
				
				  welcomeSocket = new Socket(targetHost,port);
				  
				  int block_size = 1048576;
				
					
			      File fileToSend = new File (filePath);
			      OutputStream socketStream = welcomeSocket.getOutputStream();
			      OutputStreamWriter socketStream_writer = new OutputStreamWriter(socketStream);
			      BufferedWriter buferedWriter = new BufferedWriter(socketStream_writer);
			      if(targetPath.isEmpty()) buferedWriter.write(String.format("%d %d %s",fileToSend.length(),0,fileToSend.getName()));
			      else buferedWriter.write(String.format("%d %d %s",fileToSend.length(),1,targetPath));			      
			      buferedWriter.flush();
			      buferedWriter.close();
			      welcomeSocket.close();
			      Thread.sleep(1000);
			      
			      Socket sockWrite = new Socket(targetHost,port-1);
			      
			      byte [] byteBuffer  = new byte [block_size];
			      FileInputStream fileIS = new FileInputStream(fileToSend);			      
			      OutputStream WriteSocketStream = sockWrite.getOutputStream();
			      OutputStreamWriter osw = new OutputStreamWriter(WriteSocketStream);
			      
			      if(fileToSend.length()<block_size)
			      {
			    	  fileIS.read(byteBuffer);
			    	  //osw.write("Test"); 
			    	  WriteSocketStream.write(byteBuffer,0,byteBuffer.length);			    	  
			      }
			      else
			      {
				      int nextByte = 0;
				      do {				    	  
				    	  nextByte=fileIS.read(byteBuffer);
				    	  if(nextByte>=1) WriteSocketStream.write(byteBuffer,0,nextByte);
				    	  else break;
				    	  WriteSocketStream.flush();				    	  
				      }while(true);
			      }
			      sockWrite.close();
			   	
			} catch (UnknownHostException e) {				
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    	  

	}

}
