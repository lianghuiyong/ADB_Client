package com.lenovo.adb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.stream.events.StartDocument;

public class ADB_Client {

	private static final String HOST = "localhost";
	private static Socket socket = null;
	private BufferedOutputStream out;
	private BufferedInputStream in;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ADB_Client adb_Client = new ADB_Client();
		adb_Client.init_adb_socket();
		adb_Client.loopSendServer();
		adb_Client.show_data();
	}

	private void init_adb_socket() {
		// TODO 初始化ADB 端口号
		try {
			// Runtime.getRuntime().exec("adb shell am broadcast -a
			// NotifyServiceStop");
			// Thread.sleep(3000);
			Runtime.getRuntime().exec("adb forward tcp:12580 tcp:12286");
			Thread.sleep(3000);
			// Runtime.getRuntime().exec("adb shell am broadcast -a
			// NotifyServiceStart");
			// Thread.sleep(3000);
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("C: Connecting...");
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 12580);
			System.out.println("C: RECEIVE");

			out = new BufferedOutputStream(socket.getOutputStream());
			in = new BufferedInputStream(socket.getInputStream());
		} catch (UnknownHostException e1) {
			System.out.println("TCP 331133" + "ERROR:" + e1.toString());
		} catch (Exception e2) {
			System.out.println("TCP 441144" + "ERROR:" + e2.toString());
		}

	}

	// 接受安卓发送的数据显示在控制台
	private void show_data() {
		while (true) {
			String string;
			if ((string = readFromSocket(in)) != null) {
				System.out.println(string);
			}
		}
	}

	//
	private void loopSendServer() {
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // 采用线程池单一线程方式，防止被杀死
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						for (int i = 0; i < 10; i++) {
							send(socket, "Hello ADB by client" + i);
							System.out.println("Hello ADB by client");
							Thread.sleep(2000);
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 发送数据到安卓服务器
	private void send(Socket server, String str) {
		try {
			if (server != null) {
				out.write(str.getBytes("utf-8"));
				out.flush();
			} else {
				System.out.println("client NULL");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 从InputStream流中读数据 */
	public static String readFromSocket(InputStream in) {
		int MAX_BUFFER_BYTES = 4000;
		String msg = "";
		byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
		try {
			int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
			msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");

			tempbuffer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Log.v(Service139.TAG, "msg=" + msg);
		return msg;
	}

}
