package com.lenovo.adb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.stream.FileImageOutputStream;
import javax.xml.stream.events.StartDocument;

import net.sf.json.JSONObject;

public class ADB_Client {

	private static final String HOST = "localhost";
	private static Socket socket = null;
	private DataOutputStream out;
	private DataInputStream in;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ADB_Client adb_Client = new ADB_Client();
		adb_Client.adb_pull_to_PC();
		//adb_Client.init_adb_socket();
		//adb_Client.loopSaveImg();;
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
			Runtime.getRuntime().exec("adb pull /DCIM/Camera/ D:\\");
			Runtime.getRuntime().exec("adb forward tcp:11180 tcp:17786");
			Thread.sleep(3000);
			
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 11180);
			System.out.println("C: Connecting...");
			if(socket != null){
				System.out.println("C: RECEIVE");
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
			}
			
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//二进制流接收服务器发送过来的数据
	private void loopSaveImg() {
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // 采用线程池单一线程方式，防止被杀死
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (true) {
					//获取服务器上的数据
						//int length = in.readInt();
						/*
						byte[] btFlag = new byte[4];
						byte[] btLength = new byte[4];
						byte[] btTemp = new byte[4096];
						
						//获取btLength数组
						in.read(btLength, 0, 4);
						System.out.println(bytesToInt(btLength));
						
						//获取btFlag数组
						in.read(btFlag, 0, 4);
						System.out.println(bytesToInt(btFlag));

						byte[] btImg = new byte[0];
						
						int len = 0;*/
						
						/*
						while (len < bytesToInt(btLength)) {
							len += in.read(btTemp);
							btImg = byteMerger(btImg, btTemp);
							System.out.println(len);
	                    }   */
						in.readInt();         	//Flag
						String fileName = in.readUTF();     //图片名         
						long fileLength = in.readLong();	//图片长度
                        
						FileOutputStream fos =new FileOutputStream(new File("E:\\" + fileName));
						
						byte[] tempBytes =new byte[1024];            
						int transLen =0;                  
						System.out.println("----开始接收文件<" + fileName +">,文件大小为<" + fileLength +">----");  
						while(transLen < fileLength){                    
							int read =0;    
						
							read = in.read(tempBytes);    
							
							//判断是否到了文件结尾
							String string = new String(tempBytes,"UTF-8");
							if (string.equals("EOF")) {
								break;
							}
							
							if(read == -1 )                
								break; 
							
							transLen += read;        
							//System.out.println("接收文件进度" +100 * transLen/fileLength +"%...");        
							fos.write(tempBytes,0, read);          
							fos.flush();                
						}                
						System.out.println("----接收文件<" + fileName +">成功-------");
						
						/*imageOutput = new FileImageOutputStream(new File("E:\\111.jpg"));
						imageOutput.write(btImg, 0, btImg.length);//将byte写入硬盘
						imageOutput.close();
						System.out.println("Make Picture success,Please find image in " + "E:\\");*/
						fos.close();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
