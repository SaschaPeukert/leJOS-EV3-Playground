package lego.sorter.advanced.waver;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

import lego.sorter.advanced.AdvancedColorSorter;


/**
 * The main class to run for project-setup 2.3
 */
public class SortingStarter {
	
	private static RegulatedMotor bigMotor;
	private static RegulatedMotor smallMotor;
	private static EV3ColorSensor sensor;
	private static RegulatedMotor waveMotor;
	
    @SuppressWarnings("deprecation")
	public static void main(String[] args){
    	
    	bigMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		smallMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		smallMotor.setSpeed(700);
		bigMotor.setSpeed(200);
		
		waveMotor = new EV3LargeRegulatedMotor(MotorPort.D);
		waveMotor.setSpeed(100);
		
		// get a port instance
		Port port = LocalEV3.get().getPort("S2");
		// Get an instance of the EV3 sensor
		sensor = new EV3ColorSensor(port);
    	
    	Thread mainThread = new AdvancedColorSorter(bigMotor, smallMotor, sensor);
    	mainThread.start();
    	Thread waveThread = new Waver(waveMotor);
    	waveThread.start();
    	
    	while(mainThread.isAlive()){
    		if(Button.ESCAPE.isDown()){
    			mainThread.interrupt();
    			waveThread.interrupt();
    			Delay.msDelay(5000);
    			waveThread.stop();
    			break;
    		}
    		
    	}
    	if(waveThread.isAlive()){
    		waveThread.interrupt();
			Delay.msDelay(5000);
			waveThread.stop();
    	} 
    	cleanUp(); 
    }
    
	public static void cleanUp() {
		LCD.clear();
		try {
			bigMotor.close();
			smallMotor.close();
			sensor.close();
			waveMotor.close();
		} catch (Exception e) {

		}
	}
}