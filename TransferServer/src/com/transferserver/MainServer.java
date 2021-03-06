package com.transferserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class MainServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Correct usage : 'MainServer <port>'");
			System.exit(1);
		}

		int port = Integer.valueOf(args[0]);

		ServerSocket servsock;

		try {
			servsock = new ServerSocket(port);
			int portClient = port - 1;
			while (true) {

				Socket sock = servsock.accept();

				InputStream is = sock.getInputStream();

				OutputStream out = sock.getOutputStream();

				InputStreamReader reader = new InputStreamReader(is);

				BufferedReader breader = new BufferedReader(reader);

				StringTokenizer response = new StringTokenizer(
						breader.readLine());

				String filesizestr = response.nextToken();

				String filePath = response.nextToken();

				long filesize = Long.valueOf(filesizestr);

				Runnable runnable = new GetFiles(filesize, filePath, portClient);

				(new Thread(runnable)).start();

				OutputStreamWriter socketStream_writer = new OutputStreamWriter(
						out);
				BufferedWriter buferedWriter = new BufferedWriter(
						socketStream_writer);

				buferedWriter.write(String.valueOf(portClient) + "\r");
				buferedWriter.flush();
				buferedWriter.close();

				portClient--;

				sock.close();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
