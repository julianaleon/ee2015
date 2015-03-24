package assignment4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private ServerSocket tSocket;
	ExecutorService threadpool;
	ArrayList<String> book_list;
	List<ServerInfo> server_list;
	List<Crashes> crash_list;
	private int commandsCompleted;
	private int k = 0;
	private int delta = 0;
	private int id;
	
	public Server(){
		book_list = new ArrayList<String>();
		server_list = new ArrayList<ServerInfo>();
		crash_list = new ArrayList<Crashes>();
		book_list.add("invalid");
		commandsCompleted = 0;
	}
	
	public void createLibrary(int total_books){
		
		for(int i=0; i <= total_books; i++){
	    	book_list.add("Available");
	    }
		
		//System.out.println("total books : " + total_books);
	}
	
	public void createServer(int index , String input){					   // input is <ipAddress>:<portNumber>
		String[] input_split = input.split(":");
	      
	    try {
	    	InetAddress ipAddress = InetAddress.getByName(input_split[0]);
			int port = Integer.parseInt(input_split[1]);
			
			server_list.add(new ServerInfo(index, port, ipAddress));
				
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	public void start(){
		 try {
		    	InetAddress ipAddress = server_list.get(id).getIp();

				if (ipAddress.isAnyLocalAddress() || ipAddress.isLoopbackAddress()){  			// checks if server is on local host
					tSocket = new ServerSocket(server_list.get(id).getPort());
				}		
			} catch (IOException e) {
				e.printStackTrace();
			} 

	}
	
	public void setCrashes(String input){
		String[] command = input.split("\\s+");			// <crash> <k> <delta>
		k = Integer.parseInt(command[1]);
		delta = Integer.parseInt(command[2]);
		crash_list.add(new Crashes(delta, k));
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
	
	//TODO: update this 
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
	    
		server.id = Integer.parseInt(commandPieces[1]);
		int server_instances = Integer.parseInt(commandPieces[2]);
	    int total_books = Integer.parseInt(commandPieces[3]);
	    
	    server.createLibrary(total_books);
	    
	    for (int i = 0; 1 < server_instances; i++){
	    	server.createServer(i, in.nextLine());
	    }
	    
	    while(in.hasNextLine()){
	    	server.setCrashes(in.nextLine());
	    	//TODO: look at crashServer.. 
	    	server.crashServer();
	    }
	    
	    server.start();
	    server.listener();
	}	
		
}
