package httpd;

@FunctionalInterface
public interface RequestHandler {
    public Response handleRequest(String method, String resourcePath, String body) throws Exception;
}
