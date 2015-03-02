package assignment3;

public monitor class LinkedList {
	private Node head;
	int max;
	int list_size = 0;
	
	public LinkedList(int max){
		head = null;
		this.max = max;
	}
	
	public boolean isEmpty(){
		return (head == null);
	}
	
	public int insert(String name, int pr){
		if ( search(name)== -1 ){			// check if name already exists in list,
			return -1;						// returns -1 if present
		}
		
		waituntil(list_size < max);
		/*while(list_size >= max){			//check if list is full		
			//no_op							//block if full
		}*/
		
		Node newNode = new Node(name, pr);
		Node prev = null;
		Node current = head;
		int index = 0;
	
		while(current != null && pr > current.priority){
			prev = current;
			current = current.next;
			++index;
		}
		if(prev == null){
			newNode.next = head;
			head = newNode;
		}
		else{
			prev.next = newNode;
			newNode.next = current;
		}
		++list_size;
		return index;
	}
	
	public String remove(){
		
		waituntil(list_size > 0);
		/*while (list_size == 0){
			// no_op
		}*/
		
		/*Node prev = null;
		Node current = head;
		Node temp = current;
		current = current.next;
		head = current;*/
		
		Node temp = head;
		head = head.next;
		
		--list_size;
		return temp.name;
	}
	
	public int search(String name){
		Node prev = null;
		Node current = head;
		int index = 0;
		while(current != null){
			if(current.name == name){
				return index;
			}
			prev = current;
			current = current.next;
			++index;
		}
		
		return -1;
	}
}
