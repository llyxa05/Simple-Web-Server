package ua.ilyxa05.Engine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import ua.ilyxa05.utils.Config;

public class Engine {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        System.out.println("Web server started on port 8080");

        while (true) {
            try (Socket socket = serverSocket.accept()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream();

                String request = in.readLine();
                if (request == null || request.isEmpty()) {
                    continue;
                }

                String[] parts = request.split(" ");
                if (parts.length != 3) {
                    continue;
                }

                String method = parts[0];
                String path = parts[1];

                if (method.equalsIgnoreCase("GET")) {
                	System.out.print("Request: " + path + "\n");
                    if (path.equals("/index.html") || path.equals("/")) {
                        File file = new File("public/index.html");
                        if (file.exists()) {
                            String response = "HTTP/1.1 200 OK\r\n";
                            response += "Content-Type: text/html\r\n";
                            response += "Content-Length: " + file.length() + "\r\n";
                            response += "\r\n";

                            out.write(response.getBytes());

                            FileInputStream fileIn = new FileInputStream(file);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fileIn.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                            fileIn.close();
                        }
                    } else if (path.equals("assets/css/style.css")) {
                        File file = new File("public/assets/css/style.css");
                        if (file.exists()) {
                            String response = "HTTP/1.1 200 OK\r\n";
                            response += "Content-Type: text/css\r\n";
                            response += "Content-Length: " + file.length() + "\r\n";
                            response += "\r\n";

                            out.write(response.getBytes());

                            FileInputStream fileIn = new FileInputStream(file);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fileIn.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                            fileIn.close();
                        }
                    } else {
                    	if(Config.Debug == true) {
                    		System.out.println();
                    	}
                        String response = "HTTP/1.1 404 Not Found\r\n";
                        response += "Content-Type: text/html\r\n";
                        response += "Content-Length: 0\r\n";
                        response += "\r\n";
                        out.write(response.getBytes());
                    }
                }
            }
        }
    }
}
