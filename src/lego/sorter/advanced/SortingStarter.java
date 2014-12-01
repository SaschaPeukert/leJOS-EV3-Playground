package lego.sorter.advanced;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.RegulatedMotor;

public class SortingStarter {
	
	private static RegulatedMotor bigMotor;
	private static RegulatedMotor smallMotor;
	private static EV3ColorSensor sensor;
	
	public static void main(String[] args){
    	
    	bigMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		smallMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		smallMotor.setSpeed(700);
		bigMotor.setSpeed(200);
		
		// get a port instance
		Port port = LocalEV3.get().getPort("S2");
		// Get an instance of the EV3 sensor
		sensor = new EV3ColorSensor(port);
    	
    	Thread mainThread = new AdvancedColorSorter(bigMotor, smallMotor, sensor);
    	mainThread.start();
    	
    	while(mainThread.isAlive()){
    		if(Button.ESCAPE.isDown()){
    			mainThread.interrupt();
    			break;
    		}
    		
    	}
    	cleanUp(); 
    }
    
	public static void cleanUp() {
		LCD.clear();
		try {
			bigMotor.close();
			smallMotor.close();
			sensor.close();
		} catch (Exception e) {

		}
	}
}