package chatapp;


import java.io.Serializable;
import java.time.*;
import java.io.File;

public class MsgPacket implements Serializable {
    private String username;
    private String recepient;
    private String msg;
    LocalDateTime dateTime;
    private File file= null;

    MsgPacket(){}
    MsgPacket(String username, String msg, String recepient) {
        this.username = username;
        this.msg = msg;
        this.recepient = recepient;
        dateTime = LocalDateTime.now();
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

    public String getRecepient() {
        return recepient;
    }

    public String getMsg() {
        return msg;
    }

    public LocalDateTime getDateTime(){
        return dateTime;
    }


    // setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void getDateTime(LocalDateTime dt){
        dateTime = dt;
    }


    @Override
    public String toString() {
        return String.format("%s says %s to %s", username , msg , recepient);
    }
}
