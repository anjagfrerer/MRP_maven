package model;

import restserver.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        System.out.println("Server läuft auf http://localhost:8080");
    }
}