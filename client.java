import java.io.DataInputStream;
import java.io.DataOutputStream;
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
        return username + " : " + msg;
    }
}

public class client {
    public static void main(String[] args) {
        int port = 3000;
        Scanner sc = new Scanner(System.in);
        String userName, msg;
        userName="hitansh";
        // System.out.println("Enter your username.");
        // userName = sc.nextLine();

        System.out.println("socket");
        try (
                Socket socket = new Socket("localhost", port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    
            socket.setKeepAlive(true);

            System.out.println("Socket closed"+socket.isClosed());
            // System.out.println((String) in.readObject());
            out.writeObject(userName);
            out.flush();
            System.out.println("sent username" + userName);
            System.out.println("Server: " + (String)in.readObject());

            while (true) {
                // if (in.read() == -1) {
                // System.out.println("Server closed");

                // } else {
                // System.out.println("Server: " + in.readUTF());
                // }
                System.out.println("Enter your Msg.");
                msg = sc.nextLine();
                MsgPacket packet = new MsgPacket(userName, msg, "yash");
                out.writeObject(packet);
                System.out.println("bef while");
                System.out.println("Reading from server");
                System.out.println((String)in.readObject());

            }
        } catch (Exception e) {
            System.err.println("Error in Client" + e);
        } finally {
            sc.close();
        }
    }
}
