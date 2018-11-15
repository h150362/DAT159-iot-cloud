package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPubTemperature implements Runnable {

	private String topic = "Temp";
	private int qos = 1;
	private String broker = "tcp://m20.cloudmqtt.com:14103";
	private String clientId = "MQTT_Temperature";
	private String username = "myhzvjvd";
	private String password = "q3oFHCQt1Mcr";

	private MqttClient publisherClient;

	TemperatureSensor sensor;

	public MQTTPubTemperature(TemperatureSensor sensor) {

		this.sensor = sensor;
	}

	void publish() throws MqttPersistenceException, MqttException, InterruptedException {

		for (int i = 0; i < 15; i++) {

			String temp = String.valueOf(sensor.read());

			System.out.println("Publishing message: " + temp);

			MqttMessage message = new MqttMessage(temp.getBytes());
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

	public void run() {

		try {

			System.out.println("Sensor publisher running");

			connect();

			publish();

			disconnect();

			System.out.println("Sensor publisher stopping");

		} catch (Exception ex) {
			System.out.println("Sensor publisher: " + ex.getMessage());
			ex.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws MqttPersistenceException, MqttException, InterruptedException {
		TemperatureSensor sensor = new TemperatureSensor(new Room(20));
		MQTTPubTemperature temp = new MQTTPubTemperature(sensor);
		temp.connect();
		temp.publish();
		
	}
}
