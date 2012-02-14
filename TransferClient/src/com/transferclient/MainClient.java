package com.transferclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {

	public static void errorExit() {
		System.out
				.print("Usage : 'java MainClient <target_host> <target_port>  <file path> [<target path>]' ");
		System.exit(1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 3 && args.length < 5)
			errorExit();

		String targetHost = args[0];// check array

		int port = Integer.valueOf(args[1]);

		if (port < 0 && port > 65536)
			errorExit();

		String filePath = args[2];

		String targetPath = "";

		if (args.length > 3)
			targetPath = args[3];

		Socket welcomeSocket;

		try {

			welcomeSocket = new Socket(targetHost, port);

			System.out.println("welcomeSocket created");

			int block_size = 1048576;

			File fileToSend = new File(filePath);
			OutputStream socketStream = welcomeSocket.getOutputStream();
			InputStream is = welcomeSocket.getInputStream();
			OutputStreamWriter socketStream_writer = new OutputStreamWriter(
					socketStream);
			BufferedWriter buferedWriter = new BufferedWriter(
					socketStream_writer);
			if (targetPath.isEmpty())
				buferedWriter.write(String.format("%d %s\r",
						fileToSend.length(), fileToSend.getName()));
			else
				buferedWriter.write(String.format("%d %s\r",
						fileToSend.length(), targetPath));
			buferedWriter.flush();

			InputStreamReader reader = new InputStreamReader(is);

			BufferedReader breader = new BufferedReader(reader);

			String str = breader.readLine();

			int writePort = Integer.valueOf(str);

			buferedWriter.close();
			welcomeSocket.close();

			Socket sockWrite = new Socket(targetHost, writePort);

			byte[] byteBuffer = new byte[block_size];
			FileInputStream fileIS = new FileInputStream(fileToSend);
			OutputStream WriteSocketStream = sockWrite.getOutputStream();

			int nextByte = 0;
			do {
				nextByte = fileIS.read(byteBuffer);
				if (nextByte >= 1)
					WriteSocketStream.write(byteBuffer, 0, nextByte);
				else
					break;
				WriteSocketStream.flush();
			} while (true);

			sockWrite.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
