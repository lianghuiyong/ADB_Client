package com.lenovo.adb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.stream.events.StartDocument;

public class ADB_Client {

	private static final String HOST = "localhost";  
    private static final int PORT = 9888;  
    private static final int Server_Port = 9877;
    private Socket socket = null;  
    private static BufferedReader in = null;  
    private PrintWriter out = null;  
    
    public ADB_Client() {
		// TODO Auto-generated constructor stub
    	try {
    		
    		Runtime.getRuntime().exec("adb" + " forward tcp:"+PORT+" tcp:" + Server_Port);
			socket = new Socket(HOST, PORT);
		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
		    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
      
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ADB_Client adb_Client = new ADB_Client();
		
	    if (adb_Client != null) {
			while (true) {
				try {
					String string = in.readLine();
					if (string != null) {
						System.out.println(string);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//读取socket in数据流
	private static String readFromSocket(InputStream in){
		int MAX_BUFFER_BYTES = 4000;
		String msg = "";
		byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
		try{
			int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
			msg = new String(tempbuffer,0, numReadedBytes, "utf-8");
			tempbuffer = null;
		}catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
}
