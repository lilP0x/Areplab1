package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

public class FileReader {

    String relativePath = "/main/resources";

    public FileReader(){

    }


    public void handleRequest(ServerSocket socket, Socket clientSocket) throws Exception{
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            boolean isFirstLine = true;
            String file = "";   
            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    file = inputLine.split(" ")[1];
                    isFirstLine = false;

                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }
            // Construye y procesa la ruta

            URI requestFile = new URI(file);
            if (requestFile.getPath().startsWith("/index")) {
                //outputLine = helloRestService(requestFile.getPath(), requestFile.getQuery());
                

            } 
            out.close();
            in.close();
            clientSocket.close();
        
    }

}
