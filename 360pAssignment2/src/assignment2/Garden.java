package assignment2;

import java.util.concurrent.locks.*;

public class Garden implements GardenCounts{
	// Implement the following function
	// You don’t need to worry about exceptions
	// The constructor takes the MAX argument
	final ReentrantLock shovel = new ReentrantLock();
	//final Condition shovel_taken = lock.newCondition();
	final Condition can_dig = shovel.newCondition();
	final Condition can_plant = shovel.newCondition();
	final Condition can_fill = shovel.newCondition();
	
	private int newt = 0;
	private int ben = 0;
	private int mary = 0;
	private int max;
	
	public Garden(int MAX){
		this.max = MAX;
	}
	public void startDigging() throws InterruptedException{
		
			if(newt-mary == max){
				can_dig.await();
			}
			else{
				shovel.lock();
			}	
	}
	
	public void doneDigging(){	
		newt++;
		can_plant.signal();
		shovel.unlock();					//release shovel
	}
	
	public void startSeeding() throws InterruptedException{
		while(newt <= ben){
			can_plant.await();
		}
	}
	
	public void doneSeeding(){
		ben ++;
		can_fill.signal();
	}
	
	public void startFilling() throws InterruptedException{
		while(ben <= mary){
			can_fill.await();
		}
		shovel.lock();
	}	
	
	public void doneFilling(){
		mary ++;
		can_dig.signal();						   
		shovel.unlock();					//release shovel
	}
	
	@Override
	public int totalHolesDugByNewton() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int totalHolesSeededByBenjamin() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int totalHolesFilledByMary() {
		// TODO Auto-generated method stub
		return 0;
	}
}
