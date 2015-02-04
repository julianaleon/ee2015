package assignment1;

public class Main {
	public static void main(String args[]){
	     // Test Q1 implementation
	     // you do not need to write this code. 
	     // create and populate array A with values
	    // int A[] = {42,2,9,25,49}; 
		int A[] = {1,5,7,2,8,3,4,9,6,20,15,100,18,12};
	     // call PSort 
	     PSort.parallelSort(A, 0, A.length);
	     
	     for(int i:A){
	    	 System.out.print(i + " ");
	     }
	     // verify if A is sorted
	     // ... verification code --- written by us. 
	     // (you do not need to write this code). 

	     // Test Q2 implementation with 3 threads
	     int lookup = 8;
	     int result = PSearch.parallelSearch(lookup, A, 14);
	     System.out.println("\n" +"index is of " +lookup+ " is: " + result);
	     // should return -1 as 20 is not in array
	     // ... verification code --- written by us. 
	     // (you do not need to write this code)
	   }
}
