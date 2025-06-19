package chatapp;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class login extends JFrame {

    login() {
        JFrame frame = new JFrame("Login");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeLoginComponents(panel);

        frame.setVisible(true);
    }

    private void placeLoginComponents(JPanel panel) {
        panel.setLayout(new GridLayout(3,2,5,5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel userNameLabel = new JLabel("Username:");
        JTextField userText = new JTextField(20);
        JLabel recepientLabel = new JLabel("Recipient:");
        JTextField recepientText = new JTextField(20);

        JButton loginButton = new JButton("Login");
        // loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(userNameLabel);
        panel.add(userText);
        panel.add(recepientLabel);
        panel.add(recepientText);
        panel.add(loginButton);

        loginButton.addActionListener((e) -> {
            String username = userText.getText();
            String recipent = new String(recepientText.getText());
            client c = new client(username, recipent);
            c.setVisible(true);
            this.dispose();
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            login frame = new login();
            frame.setVisible(true);
        });
    }
}

public class client extends JFrame {

    public client(String username, String recipient) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chat Client");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        JLabel headerLabel = new JLabel("WOWChat!");
        headerLabel.setFont(new Font("Source Sans Pro",Font.ITALIC + Font.BOLD,30));
        headerPanel.add(headerLabel);

        JLabel userNameLabel = new JLabel("Hello " + username + " !");
        JLabel recepientLabel = new JLabel("Recipient: " + recipient);

        headerPanel.add(userNameLabel);
        headerPanel.add(recepientLabel);
        add(headerPanel,BorderLayout.NORTH);
        
        // chats panel

        JPanel chatsPanel = new JPanel();
        chatsPanel.setLayout(new BoxLayout(chatsPanel, BoxLayout.Y_AXIS));
        // add(chatsPanel);

        JScrollPane jScrollPane = new JScrollPane(chatsPanel);
        chatsPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(jScrollPane, BorderLayout.CENTER);

        // Msg Box Panel
        JPanel MsgPanel = new JPanel();
        JTextField inputField = new JTextField(30);
        JButton sendBtn = new JButton("Send");
        MsgPanel.add(inputField);
        MsgPanel.add(sendBtn);
        add(MsgPanel, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            sendBtn.doClick();
        });

        sendBtn.addActionListener((e) -> {
            if(inputField.getText().trim() == "") return;
            JPanel msg = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            JLabel msgTxt = new JLabel(inputField.getText());
            msg.add(msgTxt);
            // Prevent msg panel from stretching vertically
            msg.setMaximumSize(new Dimension(Integer.MAX_VALUE, msg.getPreferredSize().height));
            chatsPanel.add(msg);
            chatsPanel.revalidate();
            chatsPanel.repaint();
            // autoscroll to bottom
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = jScrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        });
    }

}

class clientutil extends Thread {
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
            clientutil c = new clientutil();
            c.start();
            String choice;
            while (true) {
                System.out.println("Do you want to send file?Y/N :");
                choice = sc.nextLine();
                MsgPacket packet;
                if (choice.equals("y") || choice.equals("yes")) {
                    File dir = new File("./");
                    File[] files = dir.listFiles();
                    for (File f : files) {
                        System.out.println("- " + f);
                    }
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
            System.err.println("Error in Client: " + e);
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
