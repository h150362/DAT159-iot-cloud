package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class HeatPub implements Runnable {

	private String topic = "Heat";
	private int qos = 1;
	private String broker = "tcp://m20.cloudmqtt.com:14103";
	private String clientId = "HeatPub";
	private String username = "myhzvjvd";
	private String password = "q3oFHCQt1Mcr";
	
	private MqttClient publisherClient;
	private Controller controller;
	private String state = "";

	TemperatureSensor sensor;
/*
	public HeatPub(String state) {
		this.controller = controller;
		this.state = state;
		
			
	try {

		//System.out.println("Heat publisher running");

		connect();

		publish();

		disconnect();

		//System.out.println("CONSTRUCTOR: Heat publisher stopping");

	} catch (Exception ex) {
		System.out.println("Sensor publisher: " + ex.getMessage());
		ex.printStackTrace();
	}

} */

	void publish() throws MqttPersistenceException, MqttException, InterruptedException {
		
		if(state == "ON") {
			System.out.println("HEATPUB: Publishing STATE: " + state);
			MqttMessage message = new MqttMessage(state.getBytes());
			message.setQos(qos);

			publisherClient.publish(topic, message);

			Thread.sleep(5000);
			
		} if (state == "OFF") {
			
			System.out.println("HEATPUB: Publishing STATE: " + state);

			MqttMessage message = new MqttMessage(state.getBytes());
			message.setQos(qos);

			publisherClient.publish(topic, message);

			Thread.sleep(5000);
		}

	 }

	void connect() {

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			publisherClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(username);
			connOpts.setPassword(password.toCharArray());
			System.out.println("Connecting to broker: " + broker);
			publisherClient.connect(connOpts);
			System.out.println("Connected");

		} catch (MqttException e) {
			System.out.println("reason " + e.getReasonCode());
			System.out.println("msg " + e.getMessage());
			System.out.println("loc " + e.getLocalizedMessage());
			System.out.println("cause " + e.getCause());
			System.out.println("excep " + e);
			e.printStackTrace();
		}
	}

	private void disconnect() throws MqttException {

		publisherClient.disconnect();

	}

	public void run(String state) {
		this.state = state;
		try {
			//System.out.println("Heat publisher running");

			connect();

			publish();

			disconnect();

			//System.out.println("Heat publisher stopping");

		} catch (Exception ex) {
			System.out.println("Sensor publisher: " + ex.getMessage());
			ex.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws MqttPersistenceException, MqttException, InterruptedException {
		
		HeatPub heatPub = new HeatPub();
		heatPub.run("OFF");

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}



	
		
	
}
