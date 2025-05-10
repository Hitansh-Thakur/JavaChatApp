import java.io.IOException;
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
                System.out.println("Reading from server: " +(String)in.readObject());
            // System.out.println();

        }catch(Exception e){
            System.err.println("Err in reading form server: " + e);
        }
    }
    public static void main(String[] args) {
        int port = 3000;
        Scanner sc = new Scanner(System.in);
        String userName, msg;
        // userName="hitansh";
        System.out.println("Enter your username.");
        userName = sc.nextLine();

        System.out.println("socket");
        try {
                Socket socket = new Socket("localhost", port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                    
            socket.setKeepAlive(true);

            System.out.println("Socket closed"+socket.isClosed());
            // System.out.println((String) in.readObject());
            out.writeObject(userName);
            out.flush(); 
            System.out.println("sent username" + userName);
            System.out.println("Server: " + (String)in.readObject());
            client c= new client();
            c.start();
            while (true) {
                System.out.println("Enter your Msg.");
                msg = sc.nextLine();
                MsgPacket packet = new MsgPacket(userName, msg, "yash");
                out.writeObject(packet);
            }
            
        } catch (Exception e) {
            System.err.println("Error in Client" + e);
        } finally {
            sc.close();
        }
    }
}
