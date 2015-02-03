package assignment1;

import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PSort implements Runnable {
	public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService executor = Executors.newFixedThreadPool(5000);
	
	private int[] array;
	private int start;
	private int end;
	private final int minPartion;
	
	public PSort(int minPartion, int[] array, int start, int end){
		this.array= array;
		this.start= start;
		this.end = end;
		this.minPartion = minPartion;
	} 
	
	@Override
    public void run() {
		quickSort(array, start, end);	
    }
	
	public static void parallelSort(int[] A, int begin, int end){
		
		//create cached thread pool
		//ExecutorService cachedPool = Executors.newCachedThreadPool();
		PSort psort = new PSort(A.length/PSort.MAX_THREADS, A, begin, end-1);
		psort.run();
		//Future<?> runnableFuture = cachedPool.submit(psort);
		//psort.quickSort(A, begin, end-1);
		//cachedPool.shutdown(); // shutdown the pool
	}
		
	public void swap(int[] Array, int x, int y){
		int temp = Array[x];
		Array[x] = Array[y];
		Array[y] = temp;
	}
	
	
	public void quickSort(int[] Array, int start, int end){
		int length = end - start +1;
		
		if(length <= 1){return;}
		
		//int pivot_index = medianOfThree(array, start, end);
		int pivot_index = start+(end-start)/2;
		int pivotValue= Array[pivot_index];
		
		swap(array, pivot_index, end);

        int storeIndex = start;
        for (int i = start; i < end; i++) {
            if (array[i] <= pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, end);

        if (length > minPartion) {

        	PSort quick = new PSort(minPartion, array, start, storeIndex - 1);
            Future<?> future = executor.submit(quick);
           // PSort quick2 = new PSort(minPartion, array, storeIndex + 1, end);
            quickSort(array, storeIndex + 1, end);
            //future = executor.submit(quick2);
            try {
                future.get();
                System.out.println("thread ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            quickSort(array, start, storeIndex - 1);
            quickSort(array, storeIndex + 1, end);
        }
			
	} 
	
}
