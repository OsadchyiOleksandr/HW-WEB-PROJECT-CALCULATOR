package hillel.hw;

import java.io.*;
import java.net.*;

public class WebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server started on port 12345");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection from " + clientSocket.getInetAddress());

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            String[] requestParts = requestLine.split(" ");
            String path = requestParts[1];

            if (path.equals("/") || path.equals("/calculator.html")) {
                serveStaticFile(out, "src/main/resources/static/calculator.html", "text/html");
            } else if (path.equals("/styles.css")) {
                serveStaticFile(out, "src/main/resources/static/styles.css", "text/css");
            } else if (path.equals("/script.js")) {
                serveStaticFile(out, "src/main/resources/static/script.js", "application/javascript");
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/plain");
                out.println();
                out.println("File not found");
            }

            out.close();
            in.close();
            clientSocket.close();
        }
    }

    private static void serveStaticFile(PrintWriter out, String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println();
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
            fileReader.close();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/plain");
            out.println();
            out.println("File not found");
        }
    }
}
