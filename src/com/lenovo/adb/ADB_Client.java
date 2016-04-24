package com.lenovo.adb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;

public class ADB_Client {

	private static Socket socket = null;
	private BufferedWriter out;
	private BufferedReader in;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ADB_Client adb_Client = new ADB_Client();
		//adb_Client.adb_pull_to_PC();
		adb_Client.init_adb_socket();
		adb_Client.loopReadStr();;
		//adb_Client.show_data();
	}
	
	private void adb_pull_to_PC(){
		try {
			
			Runtime.getRuntime().exec("adb -s e3a06c65 remount");
			Runtime.getRuntime().exec("adb -s e3a06c65 pull /storage/emulated/0/DCIM/Camera/  E:\\");
			System.out.println("adb -s e3a06c65 pull /storage/emulated/0/DCIM/Camera/ E:\\");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//adb套接字传输
	private void init_adb_socket() {
		// TODO 初始化ADB 端口号
		try {
			Runtime.getRuntime().exec("adb remount");
			Runtime.getRuntime().exec("adb forward tcp:11180 tcp:17786");
			Thread.sleep(3000);
			
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 11180);
			System.out.println("C: Connecting...");
			if(socket != null){
				System.out.println("C: RECEIVE");
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//二进制流接收服务器发送过来的数据
	private void loopReadStr() {
		final char[] tempbuffer = new char[4000];
		
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // 采用线程池单一线程方式，防止被杀死
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (socket != null) {
					
						int numReadedBytes  = in.read(tempbuffer,0,tempbuffer.length);
						String str_Recv = new String(tempbuffer, 0, numReadedBytes);
						System.out.println(str_Recv);
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					try {
						System.out.println("服务器已关闭");
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}
	

	
	public static int bytesToInt(byte[] bytes) {  
	    int number = bytes[0] & 0xFF;  
	   // "|="按位或赋值。  
	    number |= ((bytes[1] << 8) & 0xFF00);  
	    number |= ((bytes[2] << 16) & 0xFF0000);  
	    number |= ((bytes[3] << 24) & 0xFF000000);  
	    return number;  
	}  
	
    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
	
}
