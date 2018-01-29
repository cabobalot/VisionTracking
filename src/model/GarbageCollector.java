package model;

import java.util.concurrent.TimeUnit;

public class GarbageCollector extends Thread {

	public GarbageCollector() {
	}

	public void run() {
		System.gc();
	}
}
