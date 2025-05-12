
import java.io.Serializable;

public class MsgPacket implements Serializable {
    private String username;
    private String recepient;
    private String msg;

    MsgPacket(String username, String msg, String recepient) {
        this.username = username;
        this.msg = msg;
        this.recepient = recepient;
    }

    String getUsername() {
        return username;
    }

    String getMsg() {
        return msg;
    }

    String getRecepient() {
        return recepient;
    }

    @Override
    public String toString() {
        return String.format("%s says %s to %s", username , msg , recepient);
    }
}
