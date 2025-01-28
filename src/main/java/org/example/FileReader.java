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
        serveFile(relativePath, out);

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

            InputStream fileStream = HttpServer.class.getClassLoader().getResourceAsStream("index.html");

            byte[] fileBytes = fileStream.readAllBytes();

            // Construir la respuesta HTTP
            String responseHeaders = "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: text/html\r\n" +
                                        "Content-Length: " + 
                                        fileBytes.length +
                                        "\r\n"+"\r\n";
            out.write(responseHeaders.getBytes(StandardCharsets.UTF_8));

            // Enviar el contenido del archivo como bytes
            out.write(fileBytes);
            out.flush();
        } 
        out.close();
        in.close();
        clientSocket.close();
        
    }


    private static void serveFile(String filePath, OutputStream output) throws IOException {
        // Cargar el archivo desde el classpath (resources)
        InputStream fileStream = HttpServer.class.getClassLoader().getResourceAsStream(filePath);
    
        if (fileStream == null) {
            // Si no se encuentra, enviar un 404
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("HTTP/1.1 404 Not Found");
            writer.println("Content-Type: text/plain");
            writer.println();
            writer.println("Archivo no encontrado.");
            return;
        }
    
        // Leer el archivo en bytes
        byte[] fileBytes = fileStream.readAllBytes();
    
        // Determinar el tipo de contenido
        String contentType = "image/png"; // Cambia según el tipo de archivo
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (filePath.endsWith(".gif")) {
            contentType = "image/gif";
        }
    
        // Enviar encabezados HTTP
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + fileBytes.length);
        writer.println(); // Línea en blanco para separar encabezados del cuerpo
    
        // Enviar el contenido del archivo
        output.write(fileBytes);
        output.flush();
    }
    

}
