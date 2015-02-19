package assignment2;
import java.util.concurrent.locks.*;


public class FairReadWriteLock {
	ReadWriteLockLogger logger;
	private int readers = 0;
	private int writers = 0;
	private int write_request = 0;
	
	
	public synchronized void beginRead() throws InterruptedException{
		logger.logTryToRead(); 
		try{
			while(writers > 0 || write_request > 0){
				wait();
			}
		}
		catch (InterruptedException e){}
		
		readers ++;
		logger.logBeginRead(); 
	}
	
	public synchronized void endRead(){
		readers --;	
		notifyAll();
		
		logger.logEndRead(); 
	}
	
	public synchronized void beginWrite() throws InterruptedException{
		logger.logTryToWrite(); 
		
		if(readers > 0 || writers > 0){
			write_request ++;
			wait();
		}
		writers++;
		
		logger.logBeginWrite(); 
	}
	
	public synchronized void endWrite(){
		writers --;
		notifyAll();
		
		logger.logEndWrite();
	}
	
}
