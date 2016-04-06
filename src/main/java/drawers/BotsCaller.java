package drawers;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.TimeUnit;

/**
 * Created by nishant.pathak on 06/04/16.
 */
public class BotsCaller {
    public static void main(String [] args) {
        MessageSubscriber subs = new Subscriber();
        Bot bot = null;
        try {
            bot = Bot.getBot(subs, "uname", "pwd");
            bot.getExecutorService().awaitTermination(1000000000000L, TimeUnit.DAYS);
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
