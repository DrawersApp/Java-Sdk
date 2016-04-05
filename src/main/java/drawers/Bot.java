package drawers;

import drawers.impl.DrawersCryptoEngineImpl;
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
    private ExecutorService executorService = Executors.newWorkStealingPool();

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

    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Building the connection with stream resumption.
     * Stream resumption allows for quick reconnect for flaky networks.
     * Listening for incoming message and dispatching to message subscriber using thread pool of size 5.
     * @see  <a href="http://xmpp.org/extensions/xep-0198.html">http://xmpp.org/extensions/xep-0198.html</a>
     * @throws MqttException
     */


    final private String clientId = "09676880-82c2-4e85-9d69-a979f4bf5ebe";

    public void initializeConnection() throws MqttException {
        mqttAsyncClient = new MqttAsyncClient("tcp://mqtt-lb.sandwitch.in:80", clientId);
        mqttAsyncClient.setCallback(Bot.this);
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(false);
        connectOptions.setConnectionTimeout(15000);
        connectOptions.setKeepAliveInterval(120);
        mqttAsyncClient.connect(connectOptions, null, new ConnectionListener(mqttAsyncClient, clientId));
    }


    // TODO: implement session based encryption decryption
    public static final byte[] KEY = new byte[] { 0x44, 0x52, 0x41, 0x57,
            0x45, 0x52, 0x53, 0x52, 0x45, 0x57, 0x41, 0x52, 0x44, 0x00, 0x00,
            0x00 };


    /**
     *
     * @param message
     * Generates the message from subscriber and propogate to {@link #sendMessage(String, String)}.
     * Also check for error conditions.
     */

    private void replyMessage(String topic, MqttMessage message) {
        try {
            DrawersCryptoEngineImpl cryptoEngine = new DrawersCryptoEngineImpl();
            String decryptedMessage = new String(cryptoEngine.aesDecrypt(KEY, null, message.getPayload()));
            MqttChatMessage chatMessage = MqttChatMessage.Companion.fromString(String.valueOf(decryptedMessage));
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

            DrawersCryptoEngineImpl cryptoEngine = new DrawersCryptoEngineImpl();
            String encryptedMessage = new String(cryptoEngine.aesEncrypt(KEY, null, MqttChatMessage.Companion.toJson(chatMessage).getBytes()));

            MqttMessage message = new MqttMessage(encryptedMessage.getBytes());
            message.setQos(1);
            message.setRetained(false);
            mqttAsyncClient.publish(uid + NAMESPACE, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String NAMESPACE = "/o/m";

    static boolean tryAgain = true;

    private final Object syncObj = new Object();
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection Lost");
        cause.printStackTrace();

        synchronized (syncObj) {
            while (tryAgain) {
                try {
                    mqttAsyncClient.connect(null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            System.out.println("connected successfully");
                            tryAgain = false;
                            try {
                                mqttAsyncClient.subscribe(clientId + Bot.NAMESPACE, 1);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            tryAgain = true;
                            System.out.println("failed");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(10 * 1000l);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            tryAgain = true;
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