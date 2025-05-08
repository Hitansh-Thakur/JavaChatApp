import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ConnectionHandler extends Thread {
    // private int port = 3000;
    static Map<String, DataOutputStream> clients = new HashMap<>();

    static ServerSocket ServerSoc;
    Socket ClientSoc;

    ConnectionHandler(Socket ClientSoc,String Username) {
        try {
            this.ClientSoc = ClientSoc;
            DataOutputStream clientout = new DataOutputStream(ClientSoc.getOutputStream());
            clients.put(Username,clientout);
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        try (
                DataOutputStream out = new DataOutputStream(ClientSoc.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(ClientSoc.getInputStream());) {

            // System.out.println("Client connected with " + Thread.currentThread().getName());
            out.writeUTF(getName());
            
            // Wait
            // Deserialize the trasmitted obj form Bytes to Obj of type MsgPacket.
            MsgPacket msg = (MsgPacket) in.readObject();
            System.out.println(msg.getUsername() + " connected!");
            // clients.put(msg.getUsername(), out);
            // out.writeUTF("msg received!");
            while (true) {
                System.out.println("REcipent from hashmap: " + clients.get(msg.getRecepient()));
                msg = (MsgPacket) in.readObject();
                if (clients.containsKey(msg.getRecepient())) {
                    DataOutputStream outTO = new DataOutputStream(clients.get(msg.getRecepient()));
                    outTO.writeUTF(msg.getMsg());
                    // outTO.close();
                } else {
                    out.writeUTF("Invalid Recipient");
                }
                System.out.println(msg);
            }
        } catch (Exception e) {
            System.err.println("Server : Error in Thread run " + e);
        }
    }
}

public class server {
    public static void main(String[] args) {
        // ArrayList<ConnectionHandler> clients = new ArrayList<>();
        try (ServerSocket ServerSoc = new ServerSocket(3000);) {
            System.out.println("Server Listening on port 3000.");
            while (true) {
                Socket Client = ServerSoc.accept();
                System.out.println("Client Connected!");
                DataInputStream in = new DataInputStream(Client.getInputStream());
                String username = in.readUTF();
            
                System.out.println(username + "Connected!");
                ConnectionHandler ch = new ConnectionHandler(Client,username);
                // clients.add(ch);
                ch.start();

            }
        } catch (Exception e) {
            System.err.println("Error in creating server Socket." + e);

        }

    }
}
