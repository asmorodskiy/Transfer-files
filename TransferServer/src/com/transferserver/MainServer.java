package com.transferserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

// put command in one string , use java.io.Writer / Reader
// make block reading , writing
// try to use Java NIO channels


public class MainServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length!=1)
		{
			System.out.println("Correct usage : 'MainServer <port>'");
			System.exit(1);
		}
			
		int port = Integer.valueOf(args[0]);

		ServerSocket servsock;

		try {
			servsock = new ServerSocket(port);
			while (true) {				

				Socket sock = servsock.accept();			

				InputStream is = sock.getInputStream();

				


				long filesize = dis.readLong();
				
				long filenamesize = dis.readLong();
				
				boolean indicator = dis.readBoolean();
				
				byte [] mybytearray  = new byte [(int) filenamesize];
				
				dis.read(mybytearray,0,(int)filenamesize);
				
				String name = new String(mybytearray);
				
				Runnable runnable = new GetFiles(filesize,name,indicator,port-1);
				
				(new Thread(runnable)).start();

				sock.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
