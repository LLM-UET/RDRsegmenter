package httpd;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Connection Object.
 *
 * Handles requests made by a client socket on behalf of the server by
 * sending appropriate response. Implements the Runnable interface to
 * allows the Connection object to run inside of a thread.
 *
 * @author Maurice Harris 1000882916
 * @author Vu Tung Lam
 *
 */
public class Connection implements Runnable {
    /**
     * Ran at the start of the runnable Connection object's execution inside of a thread
     */
    @Override
    public void run() {
        try {
            runSafe();
            // Close the client connection
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                this.connectionSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    // The socket used to connect to the server
    private Socket connectionSocket;
    // A HashMap to store the keys/values inside of the client request
    private HashMap<String, String> requestHeaders;
    // A HashMap that contains keys that need to be redirected to the given value
    private HashMap<String, String> redirect;
    // Others
    private String requestMethod;
    private String requestResourcePath;
    private String requestProtocol;
    private String requestBody;
    private RequestHandler requestHandler;

    /**
     * Creates a Connection object
     *
     * @param connectionSocket The socket the client used to connect to the server
     */
    public Connection(Socket connectionSocket, RequestHandler requestHandler) {

        this.connectionSocket = connectionSocket;
        this.requestHandler = requestHandler;

        this.requestHeaders = new HashMap<>();
        this.requestMethod = "";
        this.requestResourcePath = "";
        this.requestBody = "";
        this.requestProtocol = "";
        this.redirect = new HashMap<>();

        // Add key/value pairs to the redirect HashMap
        // The key represents the URL value used by the user and the value is the URL value to redirect to
        // redirect.put("/", "/index.html");
        // redirect.put("/index.htm", "/index.html");
        // redirect.put("/index", "/index.html");
    }

    private void parseRequest() throws IOException {

        // Connect a BufferedReader to the client socket's input stream and read it its request data
        BufferedReader connectionReader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

        // Read in the top client request line. ex: GET /index.html HTTP/1.1
        String requestLine = connectionReader.readLine();

        // If the request line exists we begin to parse the request, otherwise it is not a proper request
        // and we do nothing
        if (requestLine != null) {

            // The top line of a client request is formatted differently from the rest of the request so
            // we get the values of the top line of the client request first
            String[] requestLineParams = requestLine.split(" ");

            // Extract the relevant information from the top line of the request. Method, Resource URL, and Protocol
            String requestMethod = requestLineParams[0];
            String requestResource = requestLineParams[1];
            String requestProtocol = requestLineParams[2];

            // Add the Method, Resource, and Protocol to the request HashMap
            this.requestMethod = requestMethod;
            this.requestResourcePath = requestResource;
            this.requestProtocol = requestProtocol;
            if (!"HTTP/1.1".equals(this.requestProtocol)) {
                throw new IOException("Only HTTP/1.1 is supported; got " + this.requestProtocol);
            }

            // Read the next line of the client request header
            String headerLine = connectionReader.readLine();

            // While the request header still has lines to read we continue reading and
            // storing the values of each request field into the request HashMap
            while (!headerLine.isEmpty()) {

                // Splits the request field into its key and value pair
                String[] requestParams = headerLine.split(":", 2);

                // Put the request field key and value into the request HashMap
                requestHeaders.put(requestParams[0], requestParams[1].replaceFirst(" +", ""));

                // Read the next header line of the request
                headerLine = connectionReader.readLine();
            }

            // Also read body if any. Currently only text content is supported.
            String contentLengthHeader = requestHeaders.get("Content-Length");
            int contentLength = contentLengthHeader == null ? 0 : Integer.parseInt(contentLengthHeader);
            if (contentLength == 0) {
                this.requestBody = "";
            } else {
                char[] bodyChars = new char[contentLength];
                int read = connectionReader.read(bodyChars, 0, contentLength);
                this.requestBody = new String(bodyChars, 0, read);
            }
        }
    }

    private void runSafe() throws IOException {
        try {
            this.parseRequest();
        } catch (IOException e) {
            this.sendExceptionResponse(e, 400);
            return;
        }

        if (redirect.get(requestResourcePath) != null) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Location", redirect.get(requestResourcePath));
            Response response = new Response(301, "text/plain", "Moved Permanently", headers);
            this.sendResponse(response);
            return;
        }

        Response r = this.callRequestHandler(requestMethod, requestResourcePath, requestBody);
        this.sendResponse(r);
    }

    private Response callRequestHandler(String method, String resourcePath, String body) {
        System.err.println("Handling request: " + method + " " + resourcePath + " with body length " + body.length()); // + " and content:\n" + body);
        try {
            return this.requestHandler.handleRequest(method, resourcePath, body);
        } catch (Exception e) {
            return new Response(500, "text/plain", e.getMessage());
        }
    }

    private void sendResponse(Response response) throws IOException {
        // Prepare body as UTF-8 bytes
        byte[] bodyBytes = response.getContent().getBytes("UTF-8");

        // Send the status line and headers
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("HTTP/1.1 ")
            .append(response.getStatusCode())
            .append(" ")
            .append(response.getReasonPhrase())
            .append("\r\n");
        headerBuilder.append("Content-Type: ")
            .append(response.getContentType())
            .append("; charset=utf-8\r\n");
        headerBuilder.append("Content-Length: ")
            .append(bodyBytes.length)
            .append("\r\n");

        // Add any additional headers
        for (String headerKey : response.getHeaders().keySet()) {
            headerBuilder.append(headerKey)
                .append(": ")
                .append(response.getHeaders().get(headerKey))
                .append("\r\n");
        }

        // End headers
        headerBuilder.append("\r\n");

        OutputStream outStream = connectionSocket.getOutputStream();
        // Write headers as UTF-8 bytes
        outStream.write(headerBuilder.toString().getBytes("UTF-8"));
        // Write body bytes
        outStream.write(bodyBytes);

        outStream.flush();
        outStream.close();
    }

    private void sendExceptionResponse(Exception exception, int defaultStatusCode) throws IOException {
        long ticketId = System.currentTimeMillis();
        System.err.println("++++++++++++++++++++ Ticket ID: " + ticketId);
        System.err.println("Stack trace:");
        exception.printStackTrace();
        System.err.println("-------------------- End of stack trace for ticket ID: " + ticketId);

        Map<String, String> headers = new HashMap<>();
        headers.put("Ticket-ID", Long.toString(ticketId));

        int statusCode;
        if (exception instanceof HttpException) {
            statusCode = ((HttpException) exception).getStatusCode();
        } else {
            statusCode = defaultStatusCode;
        }

        sendResponse(
            new Response(statusCode, "text/plain", exception.getMessage(), headers)
        );
    }
}
