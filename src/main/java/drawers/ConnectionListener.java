package drawers;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Created by nishant.pathak on 06/04/16.
 */
public class ConnectionListener implements IMqttActionListener {

    private IMqttAsyncClient client;
    private String id;
    public ConnectionListener(IMqttAsyncClient asyncClient, String clientId) {
        client = asyncClient;
        id = clientId;
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        System.out.println("Successfully connected");
        try {
            client.publish("/aadjs/ajksdjadss/adsjkds", new MqttMessage());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        exception.printStackTrace();
    }
}