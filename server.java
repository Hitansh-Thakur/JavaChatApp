import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ClientThread extends Thread {
    // private int port = 3000;
    static Map<String, ObjectOutputStream> clients = new HashMap<>();

    // static ServerSocket ServerSoc;
    Socket ClientSoc;
    String Username;
    ObjectOutputStream clientOut;
    ObjectInputStream clientIn;

    ClientThread(Socket ClientSoc, ObjectOutputStream out, ObjectInputStream in, String Username) {
        try {
            this.ClientSoc = ClientSoc;
            clientOut = out;
            clientOut.flush();
            clientIn = in;
            this.Username = Username;
            clients.put(Username, out);
        } catch (Exception e) {
            System.err.println("Error in constructor : " + e);
        }
    }

    @Override
    public void run() {
        try {
            // synchronized (ClientSoc) {
            // clientOut = new ObjectOutputStream(ClientSoc.getOutputStream());
            // clientOut.flush();
            // clientIn = new ObjectInputStream(ClientSoc.getInputStream());
            // }
            // System.out.println("in run meth\nSocket closed: " + ClientSoc.isClosed());
            // Wait
            // Deserialize the transmitted obj from Bytes to Obj of type MsgPacket.
            MsgPacket msg = (MsgPacket) clientIn.readObject();
            System.out.println(msg);
            clientOut.writeObject("Message sent");
            while (true) {
                System.out.println("REcipent from hashmap: " +
                        clients.get(msg.getRecepient()));
                System.out.println(clients.entrySet());
                msg = (MsgPacket) clientIn.readObject();
                if (clients.containsKey(msg.getRecepient())) {
                    ObjectOutputStream recipientout = clients.get(msg.getRecepient());
                    recipientout.writeObject(msg.getMsg());
                } else {
                    clientOut.writeObject("Invalid Recipient");
                }
                System.out.println(msg);
            }

        } catch (StreamCorruptedException e) {
            System.err
                    .println("Error in Client thread: " + "\nCause: " + e.getCause() + "\nMessage: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Server : Error in Thread run " + e);
        }
    }
}

class ConnectionHandler {
    Socket Client;
    ObjectOutputStream out;
    ObjectInputStream in;

    ConnectionHandler(Socket Client) {
        this.Client = Client;
    }

    public void connect() {

        try {
            out = new ObjectOutputStream(Client.getOutputStream());
            in = new ObjectInputStream(Client.getInputStream());
            out.flush();

            String username = (String) in.readObject();
            out.writeObject(username + " Connected!");
            System.out.println(username + " Connected!");
            Client.setKeepAlive(true);
            ClientThread ct = new ClientThread(Client, out, in, username);
            ct.start();
        } catch (IOException e) {
            System.err.println("Error reading username from socket: " + e.getMessage());
            // continue; // Skip to the next iteration if username cannot be read
        } catch (Exception e) {
            System.err.println("Error starting ClientThread thread: " + e.getMessage());

        }

    }
}

public class server {
    public static void main(String[] args) {
        // ArrayList<ClientThread> clients = new ArrayList<>();
        try (ServerSocket ServerSoc = new ServerSocket(3000);) {
            System.out.println("Server Listening on port 3000.");
            while (true) {
                Socket Client = ServerSoc.accept();
                // System.out.println("Client Connected!");
                Client.setKeepAlive(true);
                ConnectionHandler ch = new ConnectionHandler(Client);
                ch.connect();
            }
        } catch (Exception e) {
            System.out.println("Error in Main" + e);
        }

    }
}
