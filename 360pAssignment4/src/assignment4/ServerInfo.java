package assignment4;

import java.net.InetAddress;

public class ServerInfo {
	int port;
	int id;
	InetAddress ip;
	boolean running;
	
	public ServerInfo(int id, int port, InetAddress ip){
		this.id = id;
		this.port = port;
		this.ip = ip;
	}
	
	public void setRunning(boolean val){
		this.running = val;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public InetAddress getIp(){
		return ip;
	}
	
	public int getPort(){
		return port;
	}
	
}
