package no.hvl.dat159.roomcontrol.tests;

import no.hvl.dat159.roomcontrol.Display;
import no.hvl.dat159.roomcontrol.Heating;
import no.hvl.dat159.roomcontrol.MQTTSubTemperature;

public class SimpleController implements Runnable {

	Heating heater;
	double temperature;
	double upperTemp = 25;
	double lowerTemp = 15;
	
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public SimpleController(Heating heater) {
		this.heater = heater;
	}
	
	public void run() {
	
		System.out.println("Controller running");

		try {
			
			for (int i = 1; i<=6; i++) {
				
				
				// receive from CloudMQTT
				
				heater.write(true);
				
				Thread.sleep(5000);

				heater.write(false);

				Thread.sleep(5000);
				
				if(temperature < lowerTemp) {
					
				}
				
				// determine whether to publish on CloudMQTT
			}

		} catch (Exception ex) {
			System.out.println("Controller: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		System.out.println("Controller stopping");

	}
}
