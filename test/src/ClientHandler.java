import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String userName;

    private Server server;
    private PrintWriter out;

    // Constructeur du gestionnaire de client
    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            // Initialiser le flux de sortie pour envoyer des messages au client
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir le nom d'utilisateur du client
    public String getUserName() {
        return userName;
    }

    // Méthode exécutée dans un thread pour gérer la communication avec le client
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            // Lire le nom d'utilisateur envoyé par le client
            this.userName = in.readLine();
            System.out.println(userName + " a rejoint.");

            // Informer les autres clients du nouveau utilisateur
            server.broadcastMessage(userName + " a rejoint.");

            String clientMessage;
            // Lire les messages du client et les diffuser à tous les autres clients
            while ((clientMessage = in.readLine()) != null) {
                System.out.println(userName + ": " + clientMessage);
                server.broadcastMessage(userName + ": " + clientMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Fermer la connexion du client
                socket.close();
                // Retirer le client de la liste des clients connectés et informer les autres clients de son départ
                server.removeClient(this);
                System.out.println("Client déconnecté : " + socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Méthode pour envoyer un message au client
    public void sendMessage(String message) {
        out.println(message);
    }
}
