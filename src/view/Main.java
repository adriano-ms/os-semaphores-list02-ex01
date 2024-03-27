package view;

import java.util.concurrent.Semaphore;

import model.KnightThread;

public class Main {
	
	public static final int KNIGHTS_AMOUNT = 4;
	public static final int TOTAL_ROAD = 2000;
	public static final int TORCH_POSITION = 500;
	public static final int ROCK_POSITION = 1500;
	
	public static int torchAmount = 1;
	public static int rockAmount = 1;
	public static boolean[] doorsSafeties = new boolean[KNIGHTS_AMOUNT];
	public static KnightThread[] doors = new KnightThread[KNIGHTS_AMOUNT];

	public static void main(String[] args) {
		
		rollSafeDoor();
		
		Semaphore doorMutex = new Semaphore(1);
		Semaphore torchMutex = new Semaphore(1);
		Semaphore rockMutex = new Semaphore(1);
		KnightThread[] knights = new KnightThread[KNIGHTS_AMOUNT];

		for(int i = 0; i < KNIGHTS_AMOUNT; i++)
			knights[i] = new KnightThread(doorMutex, torchMutex, rockMutex);
		
		for(int i = 0; i < KNIGHTS_AMOUNT; i++)
			knights[i].start();
	}
	
	public static void rollSafeDoor() {
		doorsSafeties[(int) (Math.random() * KNIGHTS_AMOUNT)] = true;
	}

}
