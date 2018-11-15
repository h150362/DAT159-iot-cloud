package no.hvl.dat159.roomcontrol;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Controller implements Runnable {
	
	HeatPub heatpub;
	MQTTSubTemperature tempsub;
	public Controller(MQTTSubTemperature tempsub, HeatPub heatpub ) {
		this.tempsub = tempsub;
		this.heatpub = heatpub;
		//new MQTTSubTemperature(display);
	}
	
	String state = "";
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	private double temperature;
	double upperTemp = 22;
	double lowerTemp = 18;
	
	
	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	@Override
	public void run() {
		System.out.println("Controller running");	
		
		
		//Display display = new Display();
		//Controller controller = new Controller();
		
		
		
	
		try {
			
			for (int i = 1; i<=20; i++) {

				// receive from CloudMQTT
				
				if(tempsub.getTemperaturen() < lowerTemp) {
				
				heatpub.run("ON");
				
				} else if(tempsub.getTemperaturen() > upperTemp) {
				heatpub.run("OFF");
				
				}
				
				Thread.sleep(3000);
				
			}
				
				// determine whether to publish on CloudMQTT
			

		} catch (Exception ex) {
			System.out.println("Controller: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		System.out.println("Controller stopping");

	}
}



