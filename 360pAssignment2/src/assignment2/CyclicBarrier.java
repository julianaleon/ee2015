package assignment2;

import java.util.concurrent.Semaphore;

public class CyclicBarrier {
	private int threads_waiting;
	public Semaphore isNotFull;
	public int parties;

	public CyclicBarrier(int parties) {
		// Creates a new CyclicBarrier that will trip when
		// the given number of parties (threads) are waiting upon it
		this.parties = parties;
		isNotFull = new Semaphore(0, false);
		threads_waiting = 0;
		
	}
	
	int await() throws InterruptedException {
		// Waits until all parties have invoked await on this barrier.
		// If the current thread is not the last to arrive then it is
		// disabled for thread scheduling purposes and lies dormant until
		// the last thread arrives.
		// Returns: the arrival index of the current thread, where index
		// (parties - 1) indicates the first to arrive and zero indicates
		// the last to arrive.
		
		//TODO: properly set index
		int arrival_index = 0;
		
		
		/*if(arrival_index != parties){
			threads_waiting ++;
			//disable thread and wait
		} 
		
		if(arrival_index == parties){				//last thread arrives
			isNotFull.release(parties-1);
			threads_waiting = 0;					//reset counter
		} */
		
		return arrival_index;
	}
		
}
