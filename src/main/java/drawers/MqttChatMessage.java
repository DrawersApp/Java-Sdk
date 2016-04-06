package drawers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nishant.pathak on 06/04/16.
 */
public class MqttChatMessage {

    @SerializedName("i")
    private String messageId;
    @SerializedName("m")
    private String message;
    @SerializedName("s")
    private String senderUid;

    @SerializedName("c")
    private ChatConstant.ChatType chatType;
    @SerializedName("d")
    private boolean deliveryReceipt;

    public String getSenderUid() {
        return senderUid;
    }

    public String getMessage() {
        return message;
    }

    public MqttChatMessage( String messageId, String message, String senderUid, ChatConstant.ChatType chatType, boolean deliveryReceipt) {
        this.messageId = messageId;
        this.message = message;
        this.senderUid = senderUid;
        this.chatType = chatType;
        this.deliveryReceipt = deliveryReceipt;
    }
    private static Gson gson = new Gson();

    public static MqttChatMessage fromString(String json) {
        return gson.fromJson(json, MqttChatMessage.class);
    }

    public static String toJson(MqttChatMessage chatMessage) {
        return gson.toJson(chatMessage);
    }
}
