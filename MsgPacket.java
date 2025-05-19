
import java.io.Serializable;
import java.io.File;

public class MsgPacket implements Serializable {
    private String username;
    private String recepient;
    private String msg;
    private File file= null;

    MsgPacket(String username, String msg, String recepient) {
        this.username = username;
        this.msg = msg;
        this.recepient = recepient;
    }


    void SetFile(String fName){
        this.file= new File(fName);
    }
    public File getFile(){
        return this.file;
    }


    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }

    public String getRecepient() {
        return recepient;
    }

    @Override
    public String toString() {
        return String.format("%s says %s to %s", username , msg , recepient);
    }
}
