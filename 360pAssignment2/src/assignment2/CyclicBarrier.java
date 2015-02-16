package assignment2;

import java.util.concurrent.Semaphore;

public class CyclicBarrier {
	private int threads_waiting;
	public Semaphore isNotFull;
	public Semaphore lock;
	public int parties;
	private int arrival_index;

	public CyclicBarrier(int parties) {
		// Creates a new CyclicBarrier that will trip when
		// the given number of parties (threads) are waiting upon it
		this.parties = parties;
		//TODO: check if isNotFUll should be false or true
		isNotFull = new Semaphore(0, true);  //initially no permits available
		lock = new Semaphore(1, true);
		threads_waiting = 0;
		arrival_index = 0;
		
	}
	
	int await() throws InterruptedException {
		// Waits until all parties have invoked await on this barrier.
		// If the current thread is not the last to arrive then it is
		// disabled for thread scheduling purposes and lies dormant until
		// the last thread arrives.
		// Returns: the arrival index of the current thread, where index
		// (parties - 1) indicates the first to arrive and zero indicates
		// the last to arrive.
		
		lock.acquire();
		arrival_index = threads_waiting;			//CS, don't want two different threads to be assigned the same index
		threads_waiting ++;
		lock.release();
		
		if(threads_waiting != parties){
			isNotFull.acquire(); 					//No permits available, so disables thread and lies dormant
		} 
		
		if(threads_waiting == parties){				//last thread arrives
			isNotFull.release(parties-1);			//releases (parties-1) permits
			threads_waiting = 0;					//reset index
		} 
		
		return arrival_index;
	}
		
}
