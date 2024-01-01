import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {
    private Socket socket;
    private PrintWriter out;
    private JTextArea chatArea;
    private JTextField messageField;
    private String userName;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Client().startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startClient() throws IOException {
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        userName = JOptionPane.showInputDialog(frame, "Enter your username:");

        socket = new Socket("localhost", 12345);
        out = new PrintWriter(socket.getOutputStream(), true);

        frame.setTitle("Chat Client - " + userName);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.getContentPane().add(scrollPane);

        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        frame.getContentPane().add(messageField, "South");

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        frame.getContentPane().add(sendButton, "East");

        frame.setVisible(true);

        new Thread(new ServerListener(socket, chatArea)).start();

        // Send the username to the server
        out.println(userName);

    }

    private void sendMessage() {
        String message = messageField.getText();
        out.println(message);
        messageField.setText("");
    }
}
