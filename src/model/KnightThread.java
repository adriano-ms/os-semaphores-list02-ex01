package model;

import java.util.concurrent.Semaphore;

import view.Main;

public class KnightThread extends Thread {
	
	private int minSpeed;
	private int maxSpeed;
	private int progress;
	private Semaphore doorMutex;
	private Semaphore torchMutex;
	private Semaphore rockMutex;
	
	public KnightThread() {
		super();
	}
	
	public KnightThread(Semaphore doorMutex, Semaphore torchMutex, Semaphore rockMutex) {
		this.minSpeed = 2;
		this.maxSpeed = 4;
		this.progress = 0;
		this.doorMutex = doorMutex;
		this.torchMutex = torchMutex;
		this.rockMutex = rockMutex;
	}

	@Override
	public void run() {
		try {
			while(progress < Main.TOTAL_ROAD) {
				if(progress >= Main.TORCH_POSITION && Main.torchAmount > 0) 
					takeTorch();
				if(progress >= Main.ROCK_POSITION && Main.rockAmount > 0)
					takeRock();
				move();
			}
			
			enterDoor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void move() throws InterruptedException {
		int movement = rollMovement();
		progress += movement;
		System.out.println(this + " moved " + movement + "m" + " (" + progress + "m)");
		sleep(50);
	}
	
	private int rollMovement() {
		return (int) (Math.random() * (maxSpeed - minSpeed + 1) + minSpeed);
	}
	
	private void takeTorch() throws InterruptedException {
		torchMutex.acquire();
		if(Main.torchAmount > 0) {
			System.out.println(this + " take a torch");
			Main.torchAmount--;
			minSpeed += 2;
			maxSpeed += 2;
		}
		torchMutex.release();
	}
	
	private void takeRock() throws InterruptedException {
		rockMutex.acquire();
		if(Main.rockAmount > 0) {
			System.out.println(this + " take a rock");
			Main.rockAmount--;
			minSpeed += 2;
			maxSpeed += 2;
		}
		rockMutex.release();
	}
	
	private void enterDoor() throws InterruptedException {
		doorMutex.acquire();
		int rolledDoor = 0;
		do {
			rolledDoor = (int) (Math.random() * Main.doors.length);
		}while(!(Main.doors[rolledDoor] == null));
		Main.doors[rolledDoor] = this;
		doorMutex.release();
		
		if(Main.doorsSafeties[rolledDoor])
			System.out.println(this + " exit alive");
		else
			System.out.println(this + " died");
			
	}
	
	@Override
	public String toString() {
		return this.getName().replace("Thread-", "Knight ");
	}

}
