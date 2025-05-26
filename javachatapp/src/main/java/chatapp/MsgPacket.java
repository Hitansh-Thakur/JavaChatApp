package chatapp;


import java.io.Serializable;
import java.io.File;

public class MsgPacket implements Serializable {
    private String username;
    private String recepient;
    private String msg;
    private File file= null;

    MsgPacket(){}
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

    // getters
    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }

    public String getRecepient() {
        return recepient;
    }

    // setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }


    @Override
    public String toString() {
        return String.format("%s says %s to %s", username , msg , recepient);
    }
}
