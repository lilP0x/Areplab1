package org.example;

import java.net.*;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = null;
        FileReader fileReader = null;        
        try {
            serverSocket = new ServerSocket(35000);
            fileReader = new FileReader();
            
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
                fileReader.handleRequest(serverSocket, clientSocket);

            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            } 
        }
        serverSocket.close();
    }

    private static String helloRestService(String path, String query) {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n"
                + "{\"name\":\"Jhon\", \"age\":30, \"car\":null}";
    }
}
