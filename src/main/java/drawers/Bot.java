package drawers;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main bot class contains the logic for incoming and reply message.
 * @author harshit
 * @author nishant.pathak
 * @version 2.0.0
 */
public class Bot implements MqttCallback {

    private static Bot bot;
    MqttAsyncClient mqttAsyncClient;
    private MessageSubscriber messageSubscriber;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public String name;
    public String pwd;

    private Bot(MessageSubscriber messageSubscriber, String name, String pwd) throws MqttException {
        this.messageSubscriber = messageSubscriber;
        this.name = name;
        this.pwd = pwd;
        initializeConnection();
    }

    public static synchronized Bot getBot(MessageSubscriber messageSubscriber, String name, String pwd) throws MqttException {
        if (bot == null) {
            bot = new Bot(messageSubscriber, name, pwd);
        }
        return bot;
    }

    /**
     * Building the connection with stream resumption.
     * Stream resumption allows for quick reconnect for flaky networks.
     * Listening for incoming message and dispatching to message subscriber using thread pool of size 5.
     * @see  <a href="http://xmpp.org/extensions/xep-0198.html">http://xmpp.org/extensions/xep-0198.html</a>
     * @throws MqttException
     */
    public void initializeConnection() throws MqttException {
        final String clientId = "DefaultId";
        mqttAsyncClient = new MqttAsyncClient("tcp://52.76.99.168:80", clientId);
        mqttAsyncClient.setCallback(this);
        mqttAsyncClient.connect(new MqttConnectOptions(), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Successfully connected");
                try {
                    mqttAsyncClient.subscribe(clientId + "/o/m", 1);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     *
     * @param message
     * Generates the message from subscriber and propogate to {@link #sendMessage(String, String)}.
     * Also check for error conditions.
     */
    private void replyMessage(String topic, MqttMessage message) {
        try {
            MqttChatMessage chatMessage = MqttChatMessage.Companion.fromString(String.valueOf(message.getPayload()));
            String replyMessage = messageSubscriber.generateReply(chatMessage.getMessage());
            sendMessage(chatMessage.getSenderUid(), replyMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param uid
     * @param reply
     * Publish the actual message.
     */
    private void sendMessage(String uid, String reply) {
        try {
            MqttChatMessage chatMessage = new MqttChatMessage(UUID.randomUUID().toString(), reply,
                    mqttAsyncClient.getClientId(),ChatConstant.TEXT.toString() , false);
            MqttMessage message = new MqttMessage(MqttChatMessage.Companion.toJson(chatMessage).getBytes());
            message.setQos(1);
            mqttAsyncClient.publish(uid + "/o/m", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            mqttAsyncClient.disconnectForcibly();
            mqttAsyncClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("message arrived");
        executorService.submit(() -> replyMessage(topic, message));

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("delivery complete");

    }
}
