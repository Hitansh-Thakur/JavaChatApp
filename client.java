import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

class MsgPacket implements Serializable {
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
        return username + " says " + msg + " to " + recepient;
    }
}

public class client extends Thread{
    static ObjectInputStream in;
    @Override
    public void run(){
        try{
            while(true)
                System.out.println("\nReading from server: " +(String)in.readObject());
        }catch(Exception e){
            System.err.println("Err in reading form server: " + e);
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        int port = 3000;
        Scanner sc = new Scanner(System.in);
        String userName, msg,recepient="";
        // userName="hitansh";
        System.out.println("Enter your username.");
        userName = sc.nextLine();

        try {
                Socket socket = new Socket("localhost", port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                    
            socket.setKeepAlive(true);

            out.writeObject(userName);
            out.flush(); 
            System.out.println("Enter Recipient:");
            recepient = sc.nextLine();
            System.out.println("Server: " + (String)in.readObject());
            client c= new client();
            c.start();
            while (true) {
                System.out.println("Enter your Msg:");
                msg = sc.nextLine();
                MsgPacket packet = new MsgPacket(userName, msg, recepient);
                // send the serialized object to server
                out.writeObject(packet);
            }
            
        } catch (Exception e) {
            System.err.println("Error in Client" + e);
        } finally {
            sc.close();
        }
    }
}
