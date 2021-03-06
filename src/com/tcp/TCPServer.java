package com.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import com.tools.SupTools;

/**
 * 
 * TCPServer
 * 
 * @author Fengyuan Zhang
 * 
 */
public class TCPServer {
	/* the port used to accept tcp connection */
	private final static int TCP_PORT = 8888;
	/* the hashmap used to be operated */
	private static HashMap<String, String> map = new HashMap<String, String>();

	public static void main(String[] args) {
		ServerSocket mServer = null;// the tcp server
		Socket mSocket = null;// socket used to connect
		DataInputStream in = null;// the stream used to receive message
		DataOutputStream out = null;// the stream used to send message
		try {
			mServer = new ServerSocket(TCP_PORT);// listen on the port
			System.out.println("TCPServer start!");
			while (true) {
				mSocket = mServer.accept();// once connented show message and
											// initialize streams
				SupTools.showMeswithTime("connect to client success!");
				in = new DataInputStream(mSocket.getInputStream());
				out = new DataOutputStream(mSocket.getOutputStream());
//				out.writeUTF("hello");
				boolean flag = true;
				String str = null;
				String reply = null;
				while (flag) {
					str = in.readUTF();// receive and decode message
					if (str == null || str.equals("")) {// when empty message is
														// sent, terminate connection
						flag = false;
						reply = "connection terminated!";
					} else if (str.equals("bye")) {// when the client want to
													// close connection, notice it
						flag = false;
						reply = "connection terminated!";
						SupTools.showMeswithTime("Goodbye!client terminated!");
					} else {
						reply = SupTools.executeOrder(str, map);// execute the
																// order from client
						SupTools.showMeswithTime(str);
					}
					out.writeUTF(reply);//encode and send message
				}
				out.close();
				mSocket.close();
			}
		} catch (SocketTimeoutException e){
			System.out.println("Time out, no response.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}