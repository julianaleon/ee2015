package assignment1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PSort implements Runnable {
	//create thread pool
	ExecutorService executor = Executors.newCachedThreadPool();
	
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
		
		int length = end - start +1;
		
		if(length <= 1){return;}
		
		int pivot_index = start+(end-start)/2;
		int pivotValue= array[pivot_index];
		
		swap(array, pivot_index, end);

        int storeIndex = start;
        for (int i = start; i < end; i++) {
            if (array[i] <= pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, end);
        
		//split array into 2 new threads
		PSort psort1 = new PSort(array, start, storeIndex - 1);
		PSort psort2 = new PSort(array, storeIndex + 1, end);
		Future<?> f1 = executor.submit(psort1);
		Future<?> f2 = executor.submit(psort2);
		
			try{
				f1.get();
				f2.get();
			}
			catch(InterruptedException  e){
				e.printStackTrace();
			}
			catch(ExecutionException ex){
				ex.printStackTrace();
			}
			
    }
	
	public void swap(int[] Array, int x, int y){
		int temp = Array[x];
		Array[x] = Array[y];
		Array[y] = temp;
	}
	
	public static void parallelSort(int[] A, int begin, int end){
		
		PSort psort = new PSort(A, begin, end-1);
		Future<?> future = psort.executor.submit(psort);
		try{
			future.get();
		}
		catch(InterruptedException  e){
			e.printStackTrace();
		}
		catch(ExecutionException ex){
			ex.printStackTrace();
		}
		psort.executor.shutdown(); // shutdown the thread pool
	}
		
	
}