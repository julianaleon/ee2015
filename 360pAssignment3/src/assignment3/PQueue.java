package assignment3;

public class PQueue {

	private LinkedList list;
	int position;
	String first;

	// Creates a Priority queue with maximum allowed size as m
	public PQueue(int m){
		list = new LinkedList(m);
	}
	
	// Inserts the name with its priority in the PQueue.
	// It returns -1 if the name is already present in the list.
	// Otherwise, returns the current position in the list where the name was inserted.
	// This method blocks when the list is full.
	public int insert(String name, int priority){
		
		position = list.insert(name, priority);
		return position;
	}
	
	// Returns the position of the name in the list.
	// If the name is not found it returns -1;
	public int search(String name){
		position = list.search(name);
		return position;
	}
	
	// Returns the name with the highest priority in the list.
	// If the list is empty, then the method blocks.
	// The name is deleted from the list.
	public String getFirst(){
		first = list.remove();
		return first;
	}
	
	
}
