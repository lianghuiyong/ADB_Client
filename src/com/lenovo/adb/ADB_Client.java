package com.lenovo.adb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.stream.FileImageOutputStream;
import javax.xml.stream.events.StartDocument;

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
		adb_Client.init_adb_socket();
		adb_Client.loopSaveImg();;
		//adb_Client.show_data();
	}

	private void init_adb_socket() {
		// TODO ��ʼ��ADB �˿ں�
		try {
			Runtime.getRuntime().exec("adb forward tcp:12580 tcp:19986");
			Thread.sleep(3000);
			
			System.out.println("C: Connecting...");
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 12580);
			System.out.println("C: RECEIVE");
			if(socket != null){
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
	
	//���շ��������͹���������
	private void loopSaveImg() {
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // �����̳߳ص�һ�̷߳�ʽ����ֹ��ɱ��
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (in != null && in.readInt()!= 0) {
					//��ȡ�������ϵ�����
						int length = in.readInt();
						byte[] btRecv = new byte[length];
						byte[] btFlag = new byte[4];
						byte[] btImg = new byte[length -4];
						System.out.println("length = "+length);
						
						//��ȡbyte����
						//in.read(btRecv);
						
						
						//Array.Copy(,);
						
						
						//System.out.println("length = "+ length);	
						//System.out.println("btRecv = "+btRecv[1]);	
						//System.out.println("btRecv = "+btRecv[2]);	
						//System.out.println("btRecv = "+btRecv[3]);	
						
						//System.arraycopy(btRecv,0,btFlag,0,4);
						
						//System.out.println("flag = "+bytesToInt(btFlag));	  
						
						//byte[] imgData = new byte[bytesToInt(length)-8];    
						
						//��ȡͼƬ����
						//in.read(imgData,8,bytesToInt(length)-8);
						/*System.out.println("imgData = "+imgData.toString());
						
						FileImageOutputStream imageOutput;
						imageOutput = new FileImageOutputStream(new File("E:\\111.png"));
						imageOutput.write(imgData, 0, imgData.length);//��byteд��Ӳ��
						imageOutput.close();
						System.out.println("Make Picture success,Please find image in " + "E:\\");*/
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//��������
			}
		});
	}
	
	// ���ܰ�׿���͵�������ʾ�ڿ���̨
	private void show_data() {
		while (true) {
			String string;
			if ((string = readFromSocket(in)) != null) {
				System.out.println(string);
			}
		}
	}


	/* ��InputStream���ж����� */
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
		return msg;
	}
	
	public static int bytesToInt(byte[] bytes) {  
	    int number = bytes[0] & 0xFF;  
	   // "|="��λ��ֵ��  
	    number |= ((bytes[1] << 8) & 0xFF00);  
	    number |= ((bytes[2] << 16) & 0xFF0000);  
	    number |= ((bytes[3] << 24) & 0xFF000000);  
	    return number;  
	}  

	
}
