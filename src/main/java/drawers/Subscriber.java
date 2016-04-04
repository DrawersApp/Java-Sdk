package drawers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


/**
 * Initializing the bot.
 */
public class Subscriber implements MessageSubscriber {

    private String errorDefaultText = "Something went wrong";

    @Override
    public String getErrorDefaultText() {
        return errorDefaultText;
    }

    // TODO - Change this method to do something meaningful.
    public String generateReply(String body) throws UnsupportedEncodingException {
        if (body == null) {
            return "Empty Message";
        }
        body = URLDecoder.decode(body, "UTF-8");
        return "Bot says:" + body;
    }
}
