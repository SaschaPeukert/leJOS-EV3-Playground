package ifsr.lego.sorter.advanced;

import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Waver extends Thread {
	
	private RegulatedMotor waveMotor;
	private boolean running = true;
	public Waver(RegulatedMotor motor) {
		this.waveMotor = motor;
	}

	@Override
	public void interrupt() {
		running = false;
	}

	@Override
	public void run() {
		
		while(running){
			wave();
		}
		
	}
	
	public void wave(){
		waveMotor.rotate(90);
		Delay.msDelay(10);
		waveMotor.rotate(-90);
	}

}
