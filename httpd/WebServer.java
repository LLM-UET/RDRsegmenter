package httpd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer Object.
 *
 * WebServer represents a server that serves up web content through its
 * ServerSocket. Listens indefinitely for new client connections and creates
 * a new thread to handle client requests.
 *
 * @author Maurice Harris 1000882916
 * @author Vu Tung Lam
 *
 */
public class WebServer {
    private RequestHandler requestHandler;

    public WebServer(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    /**
     * Creates the ServerSocket and listens for client connections, creates a
     * separate thread to handle each client request.
     */
    public void run(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        try {
            System.err.println("Listening for connections on port " + port + " ...");

            // Listen for new client connections
            while(true) {
                // Accept new client connection
                Socket connectionSocket = serverSocket.accept();

                // Create new thread to handle client request
                Thread connectionThread = new Thread(new Connection(connectionSocket, requestHandler));

                // Start the connection thread
                connectionThread.start();
            }
        } finally {
            serverSocket.close();
        }
    }
}
