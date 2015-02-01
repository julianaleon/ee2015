package assignment1;

public class PSort {
	public static void parallelSort(int[] A, int begin, int end){
	
		if (A == null || end == 0){
			return;
		}
		PSort psort = new PSort();
		psort.quickSort(A, begin, end-1);
	}
		
	private void quickSort(int[] Array, int lower, int higher){
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
			if(low <= high){
				int temp = Array[low];
				Array[low] = Array[high];
				Array[high] = temp;
				low ++;
				high--;
			}
			
			if(lower < high){
				quickSort(Array, lower, high);
			}
			if(low < higher){
				quickSort(Array, low, higher);
			}
		}
	}
	
}
