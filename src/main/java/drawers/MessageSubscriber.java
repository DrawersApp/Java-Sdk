package drawers;

import java.io.UnsupportedEncodingException;

/**
 * Created by harshit on 30/1/16.
 */
public interface MessageSubscriber {
    /**
     * @return String
     * This text is shown when {@link #generateReply(String)} message throws exception.
     */
    String getErrorDefaultText();

    /**
     *
     * @param message
     * @return String
     * This function takes the input message,
     * makes the computation and return the relevant reply.
     */
    String generateReply(String message) throws UnsupportedEncodingException;
}
