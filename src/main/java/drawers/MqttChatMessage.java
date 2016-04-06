package drawers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by nishant.pathak on 06/04/16.
 */
@Data
public class MqttChatMessage {

    @NonNull
    @SerializedName("i")
    public String messageId;
    @NonNull @SerializedName("m")
    public String message;
    @NonNull @SerializedName("s")
    public String senderUid;

    @NonNull @SerializedName("c")
    public ChatConstant.ChatType chatType;
    @SerializedName("d")
    boolean deliveryReceipt;

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
