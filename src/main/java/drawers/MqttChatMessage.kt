package drawers

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by nishant.pathak on 04/04/16.
 */

data class MqttChatMessage(@SerializedName("i") public val messageId: String,
                             @SerializedName("m") public val message: String,
                             @SerializedName("s") public val senderUid: String,
                             @SerializedName("c") public val chatType: String,
                             @SerializedName("d") public val deliveryReceipt: Boolean
                             ) {
    companion object {
        val gson: Gson = Gson()
        fun fromString(data: String): MqttChatMessage? {
            try {
                return gson.fromJson(data, MqttChatMessage::class.java)
            } catch(e: Exception) {
                throw e
            }
            return null
        }
        fun toJson(data: MqttChatMessage): String = gson.toJson(data)
    }
}

