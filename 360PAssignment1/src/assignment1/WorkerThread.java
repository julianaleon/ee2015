package assignment1;

import java.util.concurrent.Callable;

public class WorkerThread implements Callable<Integer> {

	private int[] Array;
	private int num;
	private int s;
	private int e;
			
	public WorkerThread(int[] A, int val, int start, int end){
		this.Array = A;
		this.num= val;
		this.s = start;
		this.e= end;
	}
	
	@Override
	public Integer call() throws Exception {
		for(int i=s; i <= e; i++){
			if(Array[i]==num){
				return i;
			}
		}
		return -1;
	}
}
