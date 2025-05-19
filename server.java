import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ClientThread extends Thread {
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
            MsgPacket msg;
            while (true) {
                // Deserialize the transmitted obj from Bytes to Obj of type MsgPacket.
                msg = (MsgPacket) clientIn.readObject();
                System.out.println(msg);
                if (clients.containsKey(msg.getRecepient())) {
                    ObjectOutputStream recipientout = clients.get(msg.getRecepient());
                    recipientout.writeObject(msg.getUsername() + " : " + msg.getMsg());
                    if (msg.getFile() != null) {
                        FileInputStream fin = new FileInputStream(msg.getFile());
                        BufferedInputStream bin = new BufferedInputStream(fin);
                        File saveFile = new File("received/received_" + msg.getMsg());
                        FileOutputStream fout = new FileOutputStream(saveFile);
                        int i;
                        while ((i = bin.read()) != -1) {
                            // System.err.print((char) i);
                            fout.write(i);
                        }
                        recipientout.writeObject(msg.getMsg() + " file Downloaded.");
                    }
                } else {
                    clientOut.writeObject("Invalid Recipient");
                }
                System.out.println(msg);
            }

        } catch (StreamCorruptedException e) {
            System.err
                    .printf("Error in Client thread: %s \nCause: %s\nMessage: %s", e, e.getCause(), e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Error starting ClientThread thread: " + e.getMessage());

        }

    }
}

public class server {
    static final int port = 3000;

    public static void main(String[] args) {
        try (ServerSocket ServerSoc = new ServerSocket(port);) {
            System.out.printf("Server Listening on port %d.\n", port);
            while (true) {
                Socket Client = ServerSoc.accept();
                Client.setKeepAlive(true);
                ConnectionHandler ch = new ConnectionHandler(Client);
                ch.connect();
            }
        } catch (Exception e) {
            System.out.println("Error in Main" + e);
        }
    }
}
