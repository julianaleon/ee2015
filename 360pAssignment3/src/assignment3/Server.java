package assignment3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket tSocket;
	private DatagramSocket uSocket;
	ExecutorService threadpool;
	ArrayList<String> book_list;
	
	public Server(){
		
	}
	
	public void processCommand(String input){
		String[] setup = input.split(" ");
	      
	    int total_books = Integer.parseInt(setup[0]);
	      
	    book_list = new ArrayList<String>(total_books+1);
	      
	    for(int i =0; i <= total_books; i++){
	    	book_list.set(i,"Available");
	    }
	    
	    int uPort = Integer.parseInt(setup[1]);
		int tPort = Integer.parseInt(setup[2]);	
		
		try {
			tSocket = new ServerSocket(tPort);
			System.out.println("Listening for TCP on : " + tPort);
			uSocket = new DatagramSocket(uPort);
			System.out.println("Listening for UDP on : " + uPort);
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public class udpListener implements Runnable{
		int size = 1024;
		Server myServer; 
		int port;
		public udpListener(){}
		
		synchronized void sendUDP(){}
		
		@Override
		public void run(){
			try{
				while(true){
					byte[] buf  = new byte[size];
					DatagramPacket data = new DatagramPacket(buf, buf.length);
					uSocket.receive(data);
					
					String r = new String (data.getData());				//returns <clientid> <bookid> <request>
					String[] request = r.split(" ");
					String output = Server.this.checkBook(request[0], request[1], request[2]);
					byte[] output_buf = output.getBytes();	
					
					DatagramPacket packet = new DatagramPacket (output_buf, output_buf.length, data.getAddress(), data.getPort());
					uSocket.send(packet);	
				}
			}
			catch(Exception e){
				
			}
		} 
	}
	
	public class tcpListener implements Runnable{
		public tcpListener(){};
		
		@Override
		public void run() {
			Socket socket;
			try {
				while ( (socket = tSocket.accept()) != null) {
					threadpool.submit(new tcpHandler(socket));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}	
	}
	
	public class tcpHandler implements Runnable{
		Socket socket;
		public tcpHandler(Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try{
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				String r = in.nextLine();				//returns <clientid> <bookid> <request>
				String[] request = r.split(" ");
				
				String output = Server.this.checkBook(request[0], request[1], request[2]);
				out.println(output);
				out.flush();
				out.close();
			    socket.close();
			    in.close(); 		//added, shouldnt we close scanner?
			}
			catch(IOException e){
				
			}
		}
	}
	
	public synchronized String checkBook(String client_id , String book_num, String request){
		int bookNumber = Integer.parseInt(book_num.substring(1));
		String returnMessage;
		String status = book_list.get(bookNumber);
		
		if (request.equals("reserve")){ 					//check if book is available
			if (status.equals("Available")){
				returnMessage = client_id + book_num;
				book_list.set(bookNumber, client_id);		//checkout book to client
			}
			else{											//not available, request fails
				returnMessage = "fail" + client_id + book_num;
			}
		}
		else{									// return book
			if(status.equals(client_id)){
				returnMessage = "free" + client_id + book_num;
				book_list.set(bookNumber, "Available");			
			}
			else{
				returnMessage = "fail" + client_id + book_num;
			}
		}
		return returnMessage;
	}
	
	
	public void listener(){
		udpListener udpListen = new udpListener();
		threadpool = Executors.newCachedThreadPool();
		threadpool.submit(udpListen);
		tcpListener tcpListen = new tcpListener();
		threadpool.submit(tcpListen);
		while (true);
	}
	
	public static void main(String argv[]){
	     
		Server server = new Server();  
	    Scanner in = new Scanner(System.in);
	    String input = in.nextLine();
	    server.processCommand(input);
	    server.listener();
	}	
		
}
