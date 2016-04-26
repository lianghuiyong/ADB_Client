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
			
			Runtime.getRuntime().exec("adb -s e3a06c65 remount");
			final Process process = Runtime.getRuntime().exec("adb -s e3a06c65 pull /storage/emulated/0/DCIM/Camera/  E:\\");
			readProcessOutput(process);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//adb�׽��ִ���
	private void init_adb_socket() {
		// TODO ��ʼ��ADB �˿ں�
		try {
			Runtime.getRuntime().exec("adb -s e3a06c65 remount");
			Runtime.getRuntime().exec("adb -s e3a06c65 forward tcp:11180 tcp:17786");
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
	
	//���շ��������͹���������
	private void loopReadStr() {
		final char[] tempbuffer = new char[4000];
		
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // �����̳߳ص�һ�̷߳�ʽ����ֹ��ɱ��
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (socket != null) {
					
						int numReadBytes  = in.read(tempbuffer,0,tempbuffer.length);
						String str_Recv = new String(tempbuffer, 0, numReadBytes);
						System.out.println(str_Recv);
					} 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					try {
						System.out.println("�������ѹر�");
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	
	//�������ݵ�������
	private void loopSendStr() {
		ExecutorService weatherPool = Executors.newSingleThreadExecutor(); // �����̳߳ص�һ�̷߳�ʽ����ֹ��ɱ��
		weatherPool.execute(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("TakePicture", "10");
				while(socket != null){	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.write(jsonObject.toString()+ i++);
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
					
				}
			}
		});
	}
	/**
     * ��ӡ�������
     *
     * @param process ����
     */
    private static void readProcessOutput(final Process process) {
        // �����̵���������� System.out �д�ӡ�����̵Ĵ�������� System.err �д�ӡ
        readProcess(process.getInputStream(), System.out);
        readProcess(process.getErrorStream(), System.err);
    }

    // ��ȡ������
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
