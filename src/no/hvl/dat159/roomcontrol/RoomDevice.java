package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class RoomDevice {
	 Display display;
	 MQTTSubTemperature subtemp;
	 
	public static void main(String[] args) throws MqttException {
		
		
		MQTTSubTemperature subtemp = new MQTTSubTemperature(new Display());
		
		Room room = new Room(16);
		Heating heating = new Heating(room);
		TemperatureSensor sensor = new TemperatureSensor(room);
		Controller controller = new Controller(subtemp, new HeatPub());
		
		MQTTPubTemperature sensorpub = new MQTTPubTemperature(sensor);
		
		
		try {
			
			new HeatSub(heating);
			Thread temppublisher = new Thread(sensorpub);
			Thread contr = new Thread(controller);
			//Thread heatingSub = new Thread();
			
			//heatingSub.start();
			temppublisher.start();
			contr.start();
			
			temppublisher.join();
			contr.join();
			//heatingSub.join(millis);
		
			
			
		} catch (Exception ex) {
			
			System.out.println("RoomDevice: " + ex.getMessage());
			ex.printStackTrace();
		}
		


	}

}
