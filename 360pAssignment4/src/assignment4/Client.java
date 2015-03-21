package assignment4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
	

public class Client {
	private String id;
	private ArrayList<InetSocketAddress> server_list;		//list of server objects in order by proximity
	
	public Client(){
		this.id = "";
		//this.ipAddress = null;
	}
	
	public class InetSocketAddress extends SocketAddress{
		private InetAddress ipAddress;
		private int port;
		
		public InetSocketAddress(InetAddress ip, int port){
			this.ipAddress = ip;
			this.port = port;
		}
	}
	
	public void processFirstCommand(String client){
		this.id = client;
	}
	
	public void storeServers(String address){
		String[] values = address.split(":");
		try {
			InetAddress ipAdd = InetAddress.getByName(values[0]);
			int port = Integer.parseInt(values[1]);
			server_list.add(new InetSocketAddress(ipAdd, port));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void processCommands(String input){
		String[] command = input.split("\\s+");
		String book_id = command[0];
		String request = command[1];
		
		if(book_id.equals("sleep")){
			try {
				Thread.sleep(Integer.parseInt(request));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else{
			Socket socket = new Socket();
			Boolean connected = false;
			int index = 0;
			
			while(!connected){								 	//attempt to connect to closest server
				InetSocketAddress s = server_list.get(index);

				try {
					//socket = new Socket(s.ipAddress, s.port);
					socket.connect(s, 100); 					// tries to connects with a timeout of 100 milliseconds
					if(socket.isConnected()){
						connected = true;
						sendTCP(book_id, request, socket);
					}		
				} catch (IOException e) {
					//e.printStackTrace();				 
				}
				
				index = (index + 1) % server_list.size();		//loop index to next server in list
			}
		}
	}
	
	
	public void sendTCP(String book_id, String request, Socket socket){
		String command = id + " " + book_id + " " + request;
			try {
				//socket = new Socket(this.ipAddress, port);
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println(command);
				System.out.println(in.nextLine());
				
				socket.close();			//close socket 
				in.close();				//close scanner
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String argv[]) throws Exception{
		
		Client client = new Client();
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		String[] input_split = input.split("\\s+");		// <client id><# of servers present>
		
		client.processFirstCommand(input_split[0]);
		
		int servers = Integer.parseInt(input_split[1]);
		
		for (int i=1; i <= servers; i++){
			client.storeServers(in.nextLine());
		}
		
		while(true){
		//while(in.hasNextLine()){
			try{
				client.processCommands(in.nextLine());
			}catch(NoSuchElementException e){
				break;
			}
		}
		
	}
	
}

