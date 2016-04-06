package drawers;

import java.util.Arrays;
import java.util.HashSet;

public class ChatConstant {
    public enum ChatType {
        FILE(0),
        IMAGE(1),
        VIDEO(2),
        TEXT(3),
        CONTACT(4),
        MAP(5),
        NOTIFICATION(6),
        CALL(7),
        QA(8);

        int pos;
        ChatType(int p) {
            pos = p;
        }

        public boolean isMediaMessage() {
            return pos <= 2;
        }

        public boolean isTextMessage() {
            return pos == 3;
        }
    }


    static HashSet<ChatType> mType = new HashSet<>(Arrays.asList(ChatType.values()));

    static public boolean validType(ChatType type) {
        return mType.contains(type);
    }

}
