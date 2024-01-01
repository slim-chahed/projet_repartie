import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerListener implements Runnable {
    private Socket socket;
    private JTextArea chatArea;

    // Constructeur du listener du serveur
    public ServerListener(Socket socket, JTextArea chatArea) {
        this.socket = socket;
        this.chatArea = chatArea;
    }

    // Méthode exécutée dans un thread pour écouter les messages du serveur
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String serverMessage;

            // Lire et afficher les messages du serveur en continu
            while ((serverMessage = in.readLine()) != null) {
                updateChatArea(serverMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour la zone de chat de l'interface utilisateur
    private void updateChatArea(String message) {
        // Utiliser SwingUtilities.invokeLater pour mettre à jour l'interface graphique de manière thread-safe
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
        });
    }
}
