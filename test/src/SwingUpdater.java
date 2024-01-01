import javax.swing.JTextArea;

public class SwingUpdater implements Runnable {
    private JTextArea chatArea;
    private String message;

    // Constructeur de la classe SwingUpdater
    public SwingUpdater(JTextArea chatArea, String message) {
        this.chatArea = chatArea;
        this.message = message;
    }

    // Méthode exécutée dans un thread pour mettre à jour la zone de chat de l'interface utilisateur
    @Override
    public void run() {
        // Utiliser SwingUtilities.invokeLater pour mettre à jour l'interface graphique de manière thread-safe
        chatArea.append(message + "\n");
    }
}
