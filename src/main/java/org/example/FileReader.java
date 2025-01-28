package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class FileReader {

    String relativePath = "resources/";

    public FileReader(){

    }

    public void handleRequest(ServerSocket socket, Socket clientSocket) throws Exception{
        OutputStream out = clientSocket.getOutputStream();
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
       
        String filePath = requestFile.getPath().substring(1); // Remueve el '/'
        serveFile(filePath, out);
        

        //if (requestFile.getPath().startsWith("/index")) {
          //  serveFile("index.html", out);
        //}

    
        out.close();
        in.close();
        clientSocket.close();
        
    }


    private static void serveFile(String filePath, OutputStream output) throws IOException {
        // Cargar el archivo desde el classpath
        boolean isError = false;
        InputStream fileStream = FileReader.class.getClassLoader().getResourceAsStream(filePath);
        
        if (fileStream == null) {
            
            fileStream = FileReader.class.getClassLoader().getResourceAsStream("400badrequest.html");
            isError = true;

        }
    
        byte[] fileBytes = fileStream.readAllBytes();
    
        // Determinar el tipo de contenido
        String contentType = "application/octet-stream"; 
    
        if (filePath.endsWith(".html")) {
            contentType = "text/html";
        } else if (filePath.endsWith(".png")) {
            contentType = "image/png";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filePath.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (filePath.endsWith(".css")) {
            contentType = "text/css";
        } else if (filePath.endsWith(".js")) {
            contentType = "application/javascript";
        }
    
        // Enviar encabezados HTTP
        PrintWriter writer = new PrintWriter(output, true);
        if (isError) {
            writer.println("HTTP/1.1 400 Bad Request");
            contentType = "text/html";
            System.err.println("si entra en el if");
        }else{
            writer.println("HTTP/1.1 200 OK");
            System.err.println("no esta entrando al if");
        }

        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + fileBytes.length);
        writer.println(); 
    
        // Enviar el contenido del archivo
        output.write(fileBytes);

        //System.out.println(fileBytes);
        output.flush();
    }
    
    

}
