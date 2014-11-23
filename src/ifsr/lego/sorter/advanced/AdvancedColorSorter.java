package ifsr.lego.sorter.advanced;

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

class AdancedColorSorter extends Thread {

	private RegulatedMotor bigMotor;
	private RegulatedMotor smallMotor;
	private EV3ColorSensor sensor;
	private int currentPosition;

	private boolean interruptCheck = false;

	
	@Override
	public void run() {
		// Do the Sorting
		
		while (!interruptCheck) {

			LCD.clear();

			bigMotor = new EV3LargeRegulatedMotor(MotorPort.A);
			smallMotor = new EV3MediumRegulatedMotor(MotorPort.B);
			smallMotor.setSpeed(700);
			bigMotor.setSpeed(200);
			currentPosition = 0;

			// get a port instance
			Port port = LocalEV3.get().getPort("S2");

			// Get an instance of the EV3 sensor
			sensor = new EV3ColorSensor(port);

			LCD.drawString("Press Enter to start", 0, 4);

			// Press Enter to Start
			while (Button.ENTER.isUp()) {
				// Necessary anymore?
			}

			LCD.clear();

			boolean stack_full = true;
			while (stack_full) {

				switch (sensor.getColorID()) {
				case Color.BLUE:
					LCD.clear();
					LCD.drawString("Blue", 0, 4);
					deploy(0);
					break;

				case Color.GREEN:
					LCD.clear();
					LCD.drawString("Green", 0, 4);
					deploy(1);
					break;

				case Color.YELLOW:
					LCD.clear();
					LCD.drawString("Yellow", 0, 4);
					deploy(2);
					break;

				case Color.RED:
					LCD.clear();
					LCD.drawString("Red", 0, 4);
					deploy(3);
					break;

				default:
					// Stack should be empty here
					// Back to start
					LCD.clear();
					LCD.drawString("Stack is empty", 0, 4);
					driveTo(0);
					stack_full = false;

					interrupt();
					break;
				}

			}
		}

	}

	private void driveTo(int pos) {

		bigMotor.rotate(180 * (pos - currentPosition));
		Delay.msDelay(500);
		currentPosition = pos;

	}

	private void popOut() {

		// Change in the original schematics:
		// http://robotsquare.com/wp-content/uploads/2013/10/45544_colorsorter.pdf
		// Rotated the black part in picture 61.3 about 180 degrees

		smallMotor.rotate(-180);
		Delay.msDelay(200);
		smallMotor.rotate(180);
		Delay.msDelay(100);

		// Shake the push-part back in place

		switch (currentPosition) {
		case 0:
			driveTo(1);
			break;
		case 3:
			driveTo(2);
			break;
		default:
			driveTo(currentPosition + 1);
			break;
		}

	}

	private void deploy(int pos) {
		driveTo(pos);
		popOut();
	}

	public void cleanUp() {
		LCD.clear();
		try {
			bigMotor.close();
			smallMotor.close();
			sensor.close();
		} catch (Exception e) {

		}
	}

	@Override
	public void interrupt() {
		interruptCheck = true;
		cleanUp();
	}

}