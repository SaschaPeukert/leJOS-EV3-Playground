package ifsr.lego.sorter.advanced;

import lejos.hardware.Button;

public class SortingStarter {

    public static void main(String[] args){
    	Thread thread = new AdancedColorSorter();
    	thread.start();
    	
    	while(thread.isAlive()){
    		if(Button.ESCAPE.isDown()){
    			thread.interrupt(); 
    			return;
    		}
    		
    	}
    	
    }
}