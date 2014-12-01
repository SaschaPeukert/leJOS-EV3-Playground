package lego.sorter.simple;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * The main class to run for project-setup 2.1
 */
public class SimpleColorSorter {

	private static RegulatedMotor bigMotor;
	private static RegulatedMotor smallMotor;
	private static int currentPosition;
	private static int[] items;

	public static void main(String[] args) {

		LCD.clear();

		bigMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		smallMotor = new EV3MediumRegulatedMotor(MotorPort.B);
		smallMotor.setSpeed(700);
		bigMotor.setSpeed(200);
		currentPosition = 0;

		items = new int[8];
		for (int i=0;i<8;i++) {
			items[i] = -1;
		}

		// get a port instance
		Port port = LocalEV3.get().getPort("S2");

		// Get an instance of the EV3 sensor
		EV3ColorSensor sensor = new EV3ColorSensor(port);

		LCD.drawString("Next plz. Or press Enter.", 0, 4);

		int count = 0;
		while (count < 8) {

			switch (sensor.getColorID()) {
			case Color.BLUE:
				items[count] = 0;
				count++;
				waitAndShow("Blue");
				break;

			case Color.GREEN:
				items[count] = 1;
				count++;
				waitAndShow("Green");
				break;

			case Color.YELLOW:
				items[count] = 2;
				count++;
				waitAndShow("Yellow");
				break;

			case Color.RED:
				items[count] = 3;
				count++;
				waitAndShow("Red");
				break;
			default:

			}

			if (Button.ENTER.isDown()) {
				// Go with < 8 items
				break;
			}

		}

		for (int p : items) {

			if (p != -1) {
				deploy(p);
				count--;
			}

		}

		// Back To Start
		driveTo(0);

		// Cleanup
		smallMotor.close();
		bigMotor.close();
		sensor.close();

	}

	private static void driveTo(int pos) {

		bigMotor.rotate(180 * (pos - currentPosition));
		Delay.msDelay(500);
		currentPosition = pos;

	}

	private static void waitAndShow(String s) {

		LCD.clear();
		LCD.drawString(s, 0, 4);
		Delay.msDelay(3000);
		LCD.clear();
		LCD.drawString("Next plz. Or press Enter.", 0, 4);
	}

	private static void popOut() {

		// Change in the original schematics:
		// http://robotsquare.com/wp-content/uploads/2013/10/45544_colorsorter.pdf
		// Rotated the black part in picture 61.3 about 180 degrees

		smallMotor.rotate(-180);
		Delay.msDelay(200);
		smallMotor.rotate(180);
	}
	
	private static void deploy(int pos){
		driveTo(pos);
		popOut();
	}

}
