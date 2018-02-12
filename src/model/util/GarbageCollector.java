package model.util;

import java.util.concurrent.TimeUnit;

public class GarbageCollector extends Thread {

	boolean collect = false;

	public GarbageCollector() {
	}

	public void run() {
		while (!isInterrupted()) {
			if (collect) {
				long startTime = System.currentTimeMillis();
				System.gc();
				System.out.println("GC: " + (System.currentTimeMillis() - startTime));
				collect = false;
			}
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void collect() {
		this.collect = true;
	}

}
