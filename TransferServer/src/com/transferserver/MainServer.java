package com.transferserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

/*
 * server:
write small files (less 1MB)
rotate port for data retrieve
send to client port for data transfer
remove hardcoded path
set timeout for GetFiles ServerSocket open

client:
get port from server
open socket only after port number retrieved
 */


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

				InputStreamReader reader = new InputStreamReader(is);
				
				BufferedReader breader = new BufferedReader(reader);
				
				StringTokenizer response=new StringTokenizer(breader.readLine());
				
				String filesizestr = response.nextToken();
				
				String boolstr = response.nextToken();
				
				String filePath = response.nextToken();
				
				long filesize = Long.valueOf(filesizestr);
				
				boolean indicator = boolstr.equals("1");
				
				Runnable runnable = new GetFiles(filesize,filePath,indicator,port-1);
				
				(new Thread(runnable)).start();

				sock.close();

			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
