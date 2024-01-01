import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private JTextArea chatArea;
    private JTextField messageField;
    private String userName;

    // Méthode principale pour démarrer le client
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Client().startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Méthode pour initialiser et démarrer le client
    public void startClient() throws IOException {
        // Créer une fenêtre pour le client
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Demander à l'utilisateur d'entrer son nom d'utilisateur
        userName = JOptionPane.showInputDialog(frame, "Enter your username:");

        // Établir une connexion avec le serveur
        socket = new Socket("localhost", 12345);
        out = new PrintWriter(socket.getOutputStream(), true);

        // Définir le titre de la fenêtre avec le nom d'utilisateur
        frame.setTitle("Chat Client - " + userName);

        // Configurer la zone de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.getContentPane().add(scrollPane);

        // Configurer le champ de message
        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        frame.getContentPane().add(messageField, "South");

        // Configurer le bouton d'envoi
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        frame.getContentPane().add(sendButton, "East");

        // Rendre la fenêtre visible
        frame.setVisible(true);

        // Démarrer un thread pour écouter les messages du serveur
        new Thread(new ServerListener(socket, chatArea)).start();

        // Envoyer le nom d'utilisateur au serveur
        out.println(userName);
    }

    // Méthode pour envoyer un message au serveur
    private void sendMessage() {
        String message = messageField.getText();
        out.println(message);
        messageField.setText("");
    }
}
