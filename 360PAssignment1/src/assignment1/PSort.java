package assignment1;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PSort implements Runnable {
	private int[] array;
	private int start;
	private int end;
	
	public PSort(int[] array, int start, int end){
		this.array= array;
		this.start= start;
		this.end = end;
	} 
	
	@Override
    public void run() {
		int pivot = array[start + ((end-start)/2)];
		PSort psort1 = new PSort(array, start, pivot);
		PSort psort2 = new PSort(array, pivot+1, end);

		ExecutorService pool = Executors.newCachedThreadPool();
		Future<?> runnableFuture1 = pool.submit(psort1);
		Future<?> runnableFuture2 = pool.submit(psort2);
		quickSort(array, start, end);
    }
	
	public static void parallelSort(int[] A, int begin, int end){
		if (A == null || end == 0){
			return;
		}
		
		//create cached thread pool
		ExecutorService cachedPool = Executors.newCachedThreadPool();
		PSort psort = new PSort(A, begin, end-1);
		Future<?> runnableFuture = cachedPool.submit(psort);
		//psort.quickSort(A, begin, end-1);
		cachedPool.shutdown(); // shutdown the pool
	}
		
	
	public void quickSort(int[] Array, int lower, int higher){
		int low = lower;
		int high = higher;
		int pivot = Array[low + ((high-low)/2)];
		
		while(low <= high){
			while (Array[low] < pivot){
				low ++;
			}
			while (Array[high] > pivot){
				high --;
			}
			if(low <= high){				//swap
				int temp = Array[low];
				Array[low] = Array[high];
				Array[high] = temp;
				low ++;
				high--;
			}
			
			if(lower < high){
				//runnableFuture.add(cachedPool.submit(new PSort(Array, lower, high)));
				quickSort(Array, lower, high);
			}
			if(low < higher){
				quickSort(Array, low, higher);
			}
			
		}
	}
	
}
