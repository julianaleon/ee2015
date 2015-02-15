package assignment2;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteLockLogger {
	private int reqNo = 0;
	  private HashMap<Thread, Integer> mapThreadReqNo =
	      new HashMap<Thread, Integer>();
	  public void logTryToRead() {
	    System.out.println("[" + reqNo + "] " + "Thread " + Thread.currentThread().getId() + " requests to read");
	    mapThreadReqNo.put(Thread.currentThread(), reqNo++);
	  }

	  public void logBeginRead() {
	    int myReqNo = mapThreadReqNo.get(Thread.currentThread());
	    System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " starts to read");
	  }

	  public void logEndRead() {
	    int myReqNo = mapThreadReqNo.get(Thread.currentThread());
	    System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " ends reading");
	  }

	  public void logTryToWrite() {
	    System.out.println("[" + reqNo + "] " + "Thread " + Thread.currentThread().getId() + " requests to write");
	    mapThreadReqNo.put(Thread.currentThread(), reqNo++);
	  }

	  public void logBeginWrite() {
	    int myReqNo = mapThreadReqNo.get(Thread.currentThread());
	    System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " starts to write");
	  }

	  public void logEndWrite() {
	    int myReqNo = mapThreadReqNo.get(Thread.currentThread());
	    System.out.println("[" + myReqNo + "] " + "Thread " + Thread.currentThread().getId() + " ends writing");
	  }
}
