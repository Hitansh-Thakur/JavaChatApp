import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class client extends Thread {
    static ObjectInputStream in;

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println((String) in.readObject());
            }
        } catch (SocketException e) {
            System.err.println("Server Socket Closed : " + e);
        } catch (Exception e) {
            System.err.println("Err in reading form server: " + e);
        }
    }

    public static void main(String[] args) {
        int port = 3000;
        Scanner sc = new Scanner(System.in);
        String userName, msg, recepient = "";
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
            System.out.println("Server: " + (String) in.readObject());
            client c = new client();
            c.start();
            String choice;
            while (true) {
                System.out.println("Do you want to send file?Y/N :");
                choice = sc.nextLine();
                MsgPacket packet;
                if (choice.equals("y") || choice.equals("yes")) {

                    System.out.println("Enter File Name:");
                    msg = sc.nextLine();

                    if (!new File(msg).exists()) {
                        System.out.println("File Does not exists.");
                        continue;
                    }
                    packet = new MsgPacket(userName, msg, recepient);
                    packet.SetFile(msg);
                } else {
                    System.out.println("Enter your Msg:");
                    msg = sc.nextLine();
                    packet = new MsgPacket(userName, msg, recepient);
                }
                // send the serialized object to server
                out.writeObject(packet);
            }

        } catch (Exception e) {
            System.err.println("Error in Client" + e);
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
