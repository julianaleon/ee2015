package assignment1;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;
import java.util.concurrent.Callable;

public class PSearch implements Callable<Integer>{
	
	private int[] Array;
	private int x;
	private int start;
	private int end;
	
	public PSearch(int[] A, int x, int start, int end) {
		this.Array = A;
		this.x= x;
		this.start = start;
		this.end= end;
	}

	@Override
	public Integer call() throws Exception {
		for(int i=start; i <= end; i++){
			if(Array[i] == x){
				return i;
			}
		}
		return -1;
	}
	
	public static int parallelSearch(int x, int[] A, int numThreads){
		//Get ExecutorService from Executors utility class, thread pool size is numThreads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		 //create list to hold the Future object associated with Callable
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		int div = A.length/numThreads;
		int start;
		int end;
		int result = -1;
		
		//Callable tasks to be executed by thread pool
		for(int i=0; i < numThreads; i++){
			start=i*div ;
			end=(i+1)*div - 1;
			if(i==numThreads-1){
				end= end+A.length%numThreads ;
			}
			//WorkerThread t= new WorkerThread(A, x, start, end);
			Callable<Integer> t = new PSearch(A, x, start, end);
			Future<Integer> future = executor.submit(t);
			list.add(future);
		}

		for(Future<Integer> f: list){
			try{
				int index = f.get();
				if(index != -1){
					result = index;
				}
			}
			catch(InterruptedException  e){
				e.printStackTrace();
			}
			catch(ExecutionException ex){
				ex.printStackTrace();
			}
			
		}
		
		executor.shutdown();
		return result;
		}
}

