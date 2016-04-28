package com.lenovo.adb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

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
		adb_Client.loopReadStr();
		adb_Client.loopSendStr();
	}
	
	private void adb_pull_to_PC(){
		try {
			
			Runtime.getRuntime().exec("adb -s WAWJMHNDON remount");
			final Process process = Runtime.getRuntime().exec("adb -s WAWJMHNDON pull /storage/emulated/0/DCIM/Camera/  E:\\");
			readProcessOutput(process);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//adb套接字传输
	private void init_adb_socket() {
		// TODO 初始化ADB 端口号
		try {
			
			Runtime.getRuntime().exec("adb -s WAWJMHNDON remount");			
			Runtime.getRuntime().exec("adb -s WAWJMHNDON forward tcp:11180 tcp:17786");

			Thread.sleep(1000);
			
			socket = new Socket(InetAddress.getByName("127.0.0.1"), 11180);
			System.out.println("C: Connecting...");
			if(socket != null){
				System.out.println("C: RECEIVE");
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			}
			
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//接收服务器发送过来的数据
	private void loopReadStr() {
		final char[] tempbuffer = new char[4000];
		
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // 采用线程池单一线程方式，防止被杀死
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (socket != null) {
					
						int numReadBytes  = in.read(tempbuffer);
						String str_Recv = new String(tempbuffer, 0, numReadBytes);
						System.out.println(str_Recv);
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	
	//发送数据到服务器
	private void loopSendStr() {
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // 采用线程池单一线程方式，防止被杀死
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {			
				while(socket != null){	
					try {
						JSONObject jsonObject1 = new JSONObject();
						jsonObject1.put("Fun", "CARD");
						out.write(jsonObject1.toString());
						out.flush();
						Thread.sleep(10000);
						
						JSONObject jsonObject2 = new JSONObject();
						jsonObject2.put("Fun", "FINGER");
						out.write(jsonObject2.toString());
						out.flush();
						Thread.sleep(10000);
					
						JSONObject jsonObject3 = new JSONObject();
						jsonObject3.put("Fun", "RECOGNIZE");
						out.write(jsonObject3.toString());
						out.flush();
						Thread.sleep(10000);
					
						JSONObject jsonObject4 = new JSONObject();
						jsonObject4.put("Fun", "AVATAR");
						out.write(jsonObject4.toString());
						out.flush();
						Thread.sleep(10000);
					
						JSONObject jsonObject5 = new JSONObject();
						jsonObject5.put("Fun", "A4");
						out.write(jsonObject5.toString());
						out.flush();
						Thread.sleep(10000);
					
						JSONObject jsonObject6 = new JSONObject();
						jsonObject6.put("Fun", "MATTER");
						out.write(jsonObject6.toString());
						out.flush();
						Thread.sleep(10000);
					
					} catch (InterruptedException |IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						try {
							socket.close();
							socket = null;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
						}
						
					} 
				}
			}
		});
	}
	/**
     * 打印进程输出
     *
     * @param process 进程
     */
    private static void readProcessOutput(final Process process) {
        // 将进程的正常输出在 System.out 中打印，进程的错误输出在 System.err 中打印
        readProcess(process.getInputStream(), System.out);
        readProcess(process.getErrorStream(), System.err);
    }

    // 读取输入流
    private static void readProcess(InputStream inputStream, PrintStream out) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
