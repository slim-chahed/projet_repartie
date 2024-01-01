import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    // Liste pour suivre les clients connectés
    private List<ClientHandler> clients = new ArrayList<>();
    // Liste pour stocker l'historique des messages du chat
    private List<String> messageHistory = new ArrayList<>();

    // Point d'entrée pour l'application serveur
    public static void main(String[] args) {
        // Créer une nouvelle instance du serveur et le démarrer
        new Server().startServer();
    }

    // Méthode pour démarrer le serveur et gérer les connexions entrantes
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Serveur démarré. En attente de clients...");

            // Écoute en continu des connexions de clients entrantes
            while (true) {
                // Accepter une nouvelle connexion client
                Socket socket = serverSocket.accept();
                System.out.println("Nouveau client connecté : " + socket);

                // Créer un nouveau ClientHandler pour le client connecté
                ClientHandler clientHandler = new ClientHandler(socket, this);
                // Ajouter le ClientHandler à la liste des clients connectés
                clients.add(clientHandler);
                // Démarrer un nouveau thread pour gérer la communication avec le client
                new Thread(clientHandler).start();

                // Envoyer l'historique des messages au nouveau client
                for (String message : messageHistory) {
                    clientHandler.sendMessage(message);
                }
            }
        } catch (IOException e) {
            // Afficher toutes les exceptions survenues pendant le fonctionnement du serveur
            e.printStackTrace();
        }
    }

    // Méthode pour diffuser un message à tous les clients connectés
    public void broadcastMessage(String message) {
        // Ajouter le message à l'historique du chat
        messageHistory.add(message);

        // Envoyer le message à tous les clients connectés
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Méthode pour supprimer un client de la liste des clients connectés
    // et diffuser un message indiquant son départ
    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastMessage(clientHandler.getUserName() + " a quitté le chat.");
    }

    // Méthode getter pour récupérer l'historique des messages du chat
    public List<String> getMessageHistory() {
        return messageHistory;
    }
}
