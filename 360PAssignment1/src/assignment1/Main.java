package assignment1;

public class Main {
	public static void main(String args[]){
	     // Test Q1 implementation
	     // you do not need to write this code. 
	     // create and populate array A with values
	     //int A[] = {42,2,9,25,49}; 
			int A[] = {10,11,42,2,9,25,49,88};
	     // call PSort 
	     PSort.parallelSort(A, 0, A.length);
	     
	     for(int i:A){
	    	 System.out.print(i + " ");
	     }
	     // verify if A is sorted
	     // ... verification code --- written by us. 
	     // (you do not need to write this code). 

	     // Test Q2 implementation with 3 threads
	     //int result = PSearch.parallelSearch(20, A, 3);
	     // should return -1 as 20 is not in array
	     // ... verification code --- written by us. 
	     // (you do not need to write this code)
	   }
}
