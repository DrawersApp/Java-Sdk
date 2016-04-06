package drawers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.NonNull;

/**
 * Created by nishant.pathak on 06/04/16.
 */
public class MqttChatMessage {

    @NonNull
    @SerializedName("i")
    private String messageId;
    @NonNull @SerializedName("m")
    private String message;
    @NonNull @SerializedName("s")
    private String senderUid;

    @NonNull @SerializedName("c")
    private ChatConstant.ChatType chatType;
    @SerializedName("d")
    private boolean deliveryReceipt;

    public String getSenderUid() {
        return senderUid;
    }

    public String getMessage() {
        return message;
    }

    public MqttChatMessage(@NonNull String messageId, @NonNull String message, @NonNull String senderUid, @NonNull ChatConstant.ChatType chatType, boolean deliveryReceipt) {
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
