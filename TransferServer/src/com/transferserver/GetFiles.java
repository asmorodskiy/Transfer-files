package com.transferserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GetFiles implements Runnable {
	
	long filesize;
	String name;
	String folder="/home/antons";
	boolean indicator;
	int port;
	
	public GetFiles(long filesize,String name,boolean indicator,int port)
	{
		this.filesize=filesize;
		this.name=name;
		this.indicator=indicator;
		this.port=port;
	}

	public void run() {
		
		ServerSocket servsock;	    
	    int bytesRead = 0;   
	    
		try {
			servsock = new ServerSocket(port);
			int block_size = 1048576;
	      	Socket ReadSocket = servsock.accept();      			      	
	      	byte [] ReadArray  = new byte [block_size];
		    InputStream InStream = ReadSocket.getInputStream();
		    String realPath;
		    if(indicator) realPath = name; 
		    else realPath = folder+"/"+name;
		    FileOutputStream fos = new FileOutputStream(realPath);			    
		    if(filesize<block_size)
		    {
		    	InStream.read(ReadArray);				    
		    	fos.write(ReadArray,0,ReadArray.length);
		    }
		    else
		    {			    	
		    	do {
		    		bytesRead = InStream.read(ReadArray);
		    		if(bytesRead>=1) fos.write(ReadArray,0,bytesRead);
		    		else break;			    		
		    	}while(true);
		    }			    
		    fos.flush();				    
		    fos.close();
		    ReadSocket.close();
		    servsock.close();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
}
