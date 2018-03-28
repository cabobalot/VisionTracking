package model.util;

public class RollingTimer {
	private double responsivness;
	private long lastTimeTaken;
	private double average;
	private long startTime;
	
	private boolean isTiming = false;
	
	public RollingTimer(double responsivness) {
		this.responsivness = responsivness;
		startTime = System.currentTimeMillis();
		average = 0;
		
	}
	
	public void startTimer() {
		isTiming = true;
		startTime = System.currentTimeMillis();
	}
	
	public long stopTimer(){
		if (isTiming) {
			lastTimeTaken = System.currentTimeMillis() - startTime;
			isTiming = false;
			commitToAverage(lastTimeTaken);
			return lastTimeTaken;
		}
		else throw new IllegalStateException();
		
	}
	
	public void cancelTimer() {
		isTiming = false;
	}
	
	public void commitToAverage(long ms) {
		average = average*(1.0-responsivness) + ((double)ms*responsivness);
	}
	
	public long getLastTimeTaken() {
		return lastTimeTaken;
	}
	
	public double getAverage() {
		return average;
	}
	
	public double getOpsPerSecond() {
		return (1000/average);
	}
	public double getLastOpsPerSecond() {
		return (1000/lastTimeTaken);
	}
}
