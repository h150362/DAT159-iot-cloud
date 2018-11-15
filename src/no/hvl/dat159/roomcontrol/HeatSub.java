package no.hvl.dat159.roomcontrol;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import no.hvl.dat159.roomcontrol.tests.SimpleController;

public class HeatSub implements MqttCallback, Runnable {

	private String message;
	private Display display;
	SimpleController controller; 
	private Heating heating;

	public HeatSub(Heating heating) throws MqttException {
		controller = new SimpleController(heating);
		String topic = "Heat";
		int qos = 1; // 1 - This client will acknowledge to the Device Gateway that messages are
						// received
		String broker = "tcp://m20.cloudmqtt.com:14103";
		String clientId = "HeatSub";
		String username = "myhzvjvd";
		String password = "q3oFHCQt1Mcr";
		this.heating = heating;


		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());

		System.out.println("Connecting to broker: " + broker);

		MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
		client.setCallback(this);
		client.connect(connOpts);
		System.out.println("Connected");

		client.subscribe(topic, qos);
		System.out.println("Subscribed to message");

	}

	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost because: " + cause);
		System.exit(1);

	}

	/**
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String state = new String(message.getPayload());
		if(state.equals("ON")) {
		System.out.println("HEATINGSUB MESSAGE ARRIVED: ON");
		System.out.println();
		heating.write(true);	
		}
		if(state.equals("OFF")) {
		heating.write(false);
		System.out.println("HEATINGSUB MESSAGE ARRIVED: OFF");
		System.out.println();
		}

		this.setMessage(new String(message.getPayload()));
	}

	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static void main(String args[]) throws MqttException {

		

	}

	@Override
	public void run() {
		Heating heating = null;
		try {
			new HeatSub(heating);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
