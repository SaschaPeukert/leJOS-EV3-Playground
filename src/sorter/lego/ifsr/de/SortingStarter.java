package sorter.lego.ifsr.de;

import lejos.hardware.Button;
import lejos.hardware.Sound;

public class SortingStarter {

    public static void main(String[] args){
    	Thread t = new AdancedColorSorter();
    	t.start();
    	
    	//boolean running = true;
    	while(t.isAlive()){
    		if(Button.ESCAPE.isDown()){
    			t.interrupt(); // Should be handled with interrupts!
    			return;
    		}
    		
    	}
    	
    }
}