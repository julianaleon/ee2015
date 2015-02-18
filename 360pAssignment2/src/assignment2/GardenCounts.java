package assignment2;
/** Your Garden class should implement this interface, and our scripts
 * will call these methods to test correctness under various criterion. */

public interface GardenCounts {

	 /*
	    * The following methods return the total number of holes dug, seeded or 
	    * filled by Newton, Benjamin or Mary at the time the methods' are 
	    * invoked on the garden class. 
	    * Important: Use locks to protect or atomic integers to ensure thread safe access. */

	   public int totalHolesDugByNewton(); 

	   public int totalHolesSeededByBenjamin(); 

	   public int totalHolesFilledByMary(); 
	
}
