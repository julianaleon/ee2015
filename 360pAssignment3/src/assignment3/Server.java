package assignment3;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private ServerSocket tSocket;
	private DatagramSocket uSocket;
	
	public class 
	
	public 
	
	public static void main(String argv[]) throws Exception{
	     
		Server server = new Server();  
	    Scanner in = new Scanner(System.in);
	    String input = in.nextLine();
	    String[] setup = input.split(" ");
	      
	    int total_books = Integer.parseInt(setup[0]);
	      
	    ArrayList<Integer> list = new ArrayList<Integer>(total_books+1);
	      
	    for(int i =0; i <= total_books; i++){
	    	list.set(i,-1);
	    }
	      
	    int uPort = Integer.parseInt(setup[1]);
		int tPort = Integer.parseInt(setup[2]);
	      
		uSocket = new DatagramSocket(uPort);
		tSocket = new ServerSocket(tPort);
		
		try{
			while(true){
				new UDPthread(uSocket.accept()).start();
				new TCPthread(tSocket.accpet()).start();
			}
		}finally{
			UDPthread.close();
			TCPthread.close();
		}
		
	     /* while(true) 
	      {
	         Socket connectionSocket = welcomeSocket.accept();

	         BufferedReader inFromClient =
	            new BufferedReader(
	            new InputStreamReader(
	            connectionSocket.getInputStream()));

	         DataOutputStream outToClient =
	            new DataOutputStream(
	            connectionSocket.getOutputStream());

	         clientSentence = inFromClient.readLine();

	         capitalizedSentence = clientSentence.toUpperCase() + '\n';

	         outToClient.writeBytes(capitalizedSentence);
	      }*/
	   }	
		
}
