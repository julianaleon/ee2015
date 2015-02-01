package assignment1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class PSearch{
	
	public static int parallelSearch(int x, int[] A, int numThreads) {
		ExecutorService pool = Executors.newFixedThreadPool(numThreads);
		
		int div = A.length/numThreads;
		int start;
		int end;
		
		for(int i=0; i < numThreads; i++){
			start=i*div ;
			end=(i+1)*div - 1;
			if(i==numThreads-1){
				end= end+A.length%numThreads ;
			}
			
			WorkerThread t= new WorkerThread(A, x, start, end);
			
		}

		
		
		return 0;
		}
}
