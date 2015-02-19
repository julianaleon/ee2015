package assignment2;

import java.util.concurrent.locks.*;

public class Garden implements GardenCounts{
	// Implement the following function
	// You don’t need to worry about exceptions
	// The constructor takes the MAX argument
	final ReentrantLock lock = new ReentrantLock();
	final Condition can_dig = lock.newCondition();
	final Condition can_plant = lock.newCondition();
	final Condition can_fill = lock.newCondition();
	
	private int newt = 0;
	private int ben = 0;
	private int mary = 0;
	private boolean shovel = true;
	private int max;
	
	public Garden(int MAX){
		this.max = MAX;
	}
	public void startDigging(){
		lock.lock();
		try{
			while(newt-mary == max || !shovel){
				try {
					can_dig.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			shovel = false;
		} finally {
				lock.unlock();
		}	
		
	}
	
	public void doneDigging(){	
		lock.lock();
		try{
			newt++;
			shovel = true;				//release shovel
			can_plant.signalAll();
		}
		finally{
			lock.unlock();
		}
	}
	
	public void startSeeding(){
		lock.lock();
		try{
			while(newt <= ben){
				try {
					can_plant.await();
				}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		finally{
			lock.unlock();
		}
	}
	
	public void doneSeeding(){
		lock.lock();
		try{
			ben ++;
			can_fill.signalAll();
		}
		finally{
			lock.unlock();
		}
	}
	
	public void startFilling(){
		lock.lock();
		try{
			while(ben <= mary || !shovel){
				try {
					can_fill.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			shovel = false;
		}
		finally{
			lock.unlock();
		}
	}	
	
	public void doneFilling(){
		lock.lock();
		try{
			mary ++;
			shovel = true;						//release shovel
			can_dig.signal();						   
		}
		finally{
			lock.unlock();
		}
	}
	
	
	@Override
	public int totalHolesDugByNewton() {
		return newt;
	}
	@Override
	public int totalHolesSeededByBenjamin() {
		return ben;
	}
	@Override
	public int totalHolesFilledByMary() {
		return mary;
	}
}
