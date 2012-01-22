package com.transferserver;

import java.io.BufferedOutputStream;
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
	    int bytesRead;   
	    
		try {
			servsock = new ServerSocket(port);
			while (true) {			      

			      	Socket sock = servsock.accept();      			      	
			      	byte [] mybytearray  = new byte [(int) filesize];
				    InputStream is = sock.getInputStream();
				    String realPath;
				    if(indicator) realPath = name; 
				    else realPath = folder+"/"+name;
				    FileOutputStream fos = new FileOutputStream(realPath);
				    BufferedOutputStream bos = new BufferedOutputStream(fos);
				    bytesRead = is.read(mybytearray,0,mybytearray.length);				    
				    bos.write(mybytearray, 0 , bytesRead);
				    bos.flush();				    
				    bos.close();
				    sock.close();			      
			    }
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}

	

}
