package assignment3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
	

public class Client {
	private InetAddress ipAddress;
	private String id;
	
	public Client(){
		this.id = "c";
		this.ipAddress = null;
	}
	
	public void processFirstCommand(String input){
		String[] setup = input.split(" ");
		this.id = id + setup[0];
		
		try {
			this.ipAddress = InetAddress.getByName(setup[1]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
	}
	
	public void processCommands(String input){
		String[] setup = input.split(" ");
		String book_id = setup[0];
		String request = setup[1];
		
		if(book_id.equals("sleep")){
			try {
				Thread.sleep(Integer.parseInt(request));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		int port = Integer.parseInt(setup[2]);
		String protocol = setup[3];
		
		if(protocol.equals("T")){
			connectTCP(book_id, request, port);
		}
		else if(protocol.equals("U")){
			connectUDP(book_id, request, port);
		}
	}
	
	
	public void connectUDP(String book_id, String request, int port){
		DatagramSocket socket;
		DatagramPacket sendPacket;
		DatagramPacket receivePacket;
		String command = id + " " + book_id + " " + request;
		
		try {
			socket = new DatagramSocket();
			byte[] buf = command.getBytes();
			sendPacket = new DatagramPacket(buf, buf.length, ipAddress, port);
			socket.send(sendPacket);
			receivePacket = new DatagramPacket(buf, buf.length);
			socket.receive(receivePacket);
			
			String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println(received);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectTCP(String book_id, String request, int port){
		Socket socket;
		String command = id + " " + book_id + " " + request;
		try {
			socket = new Socket(this.ipAddress, port);
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(command);
			System.out.println(in.nextLine());
			
			socket.close();			//close socket 
			in.close();				//close scanner
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String argv[]) throws Exception{
		
		Client client = new Client();
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		client.processFirstCommand(input);
		
		while(in.hasNextLine()){
				client.processCommands(in.nextLine());
		}
		
	}
	
}
