package Main;

import restserver.server.Server;

import java.io.IOException;

/**
 * Main class that starts the REST server.
 */
public class Main {
    /**
     * Starts the server and prints a message to the console.
     *
     * @param args not used
     * @throws IOException if the server cannot start
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        System.out.println("Server läuft auf http://localhost:8080");
    }
}