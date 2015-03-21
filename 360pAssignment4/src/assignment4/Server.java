package assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket tSocket;
	ExecutorService threadpool;
	ArrayList<String> book_list;
	private InetAddress ipAddress;
	private int commandsCompleted = 0;
	private int k = 0;
	private int delta = 0;
	
	public Server(){
		book_list = new ArrayList<String>();
		book_list.add("invalid");
	}
	
	public void createLibrary(int total_books){
		
		for(int i=0; i <= total_books; i++){
	    	book_list.add("Available");
	    }
		
		//System.out.println("total books : " + total_books);
	}
	
	public void createServer(String input){					   // input is <ipAddress>:<portNumber>
		String[] input_split = input.split(":");
	      
	    try {
			ipAddress = InetAddress.getByName(input_split[0]); 
			//TODO: need to compare this to the local host and see if server running
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} 
	    int port = Integer.parseInt(input_split[1]);	
		
		try {
			tSocket = new ServerSocket(port);
			//System.out.println("Listening for TCP on port: " + tPort);
			
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void setCrash(String input){
		String[] command = input.split("\\s+");			// <crash> <k> <delta>
		if(command[0].equals("crash")){
			k = Integer.parseInt(command[1]);
			delta = Integer.parseInt(command[2]);
		}
	}
	
	public void crashServer(){		
			if(k >= commandsCompleted)	{			// causes crash
				//TODO: lose data
				try {
					Thread.sleep(delta);			//sever unresponsive for delta milliseconds
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				commandsCompleted = 0; 			//reset counter
				k = 0;
				//TODO: regain data
			
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
				PrintWriter pw = new PrintWriter(socket.getOutputStream());
				String r = in.nextLine();				//returns <clientid> <bookid> <request>
				String[] request = r.split("\\s+");
				
				String output = Server.this.checkBook(request[0], request[1], request[2]);
				pw.println(output);
				pw.flush();
				pw.close();
			    socket.close();

				commandsCompleted += 1; 	// increment commands completed by server
				crashServer();			 	// Check if sever should crash
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public synchronized String checkBook(String client_id , String book_num, String request){
		int bookNumber = Integer.parseInt(book_num.substring(1));
		String returnMessage;
		String status = book_list.get(bookNumber);
		
		if (request.equals("reserve")){ 					//check if book is available
			if (status.equals("Available")){
				returnMessage = client_id + " " + book_num;
				book_list.set(bookNumber, client_id);		//checkout book to client
			}
			else{											//not available, request fails
				returnMessage = "fail" + " " + client_id + " " + book_num;
			}
		}
		else{									// return book
			if(status.equals(client_id)){
				returnMessage = "free" + " " + client_id + " " + book_num;
				book_list.set(bookNumber, "Available");			
			}
			else{
				returnMessage = "fail" + " " + client_id + " " + book_num;
			}
		}
		return returnMessage;
	}
	
	
	public void listener(){
		threadpool = Executors.newCachedThreadPool();
		tcpListener tcpListen = new tcpListener();
		threadpool.submit(tcpListen);
		while (true);
	}
	
	public static void main(String argv[]){
	     
		Server server = new Server();  
	    Scanner in = new Scanner(System.in);
	    String input = in.nextLine();
	    
		String[] commandPieces = input.split("\\s+");
	    
		int server_id = Integer.parseInt(commandPieces[1]);
		int server_instances = Integer.parseInt(commandPieces[2]);
	    int total_books = Integer.parseInt(commandPieces[3]);
	    server.createLibrary(total_books);
	    
	    for (int i = 1; 1 <= server_instances; i++){
	    	input = in.nextLine();
	    	server.createServer(input);
	    }
	    
	    while(in.hasNextLine()){
	    	server.setCrash(in.nextLine());
	    	server.crashServer();
	    }
		//System.out.println("Input to process:" + input);
	    server.listener();
	}	
		
}
