package httpd;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vu Tung Lam
 */
public class Response {
    private int statusCode;
    private String contentType;
    private String content;
    private Map<String, String> headers;

    public Response(int statusCode, String contentType, String content) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.content = content;
        this.headers = new HashMap<>();
    }

    public Response(int statusCode, String contentType, String content, Map<String, String> headers) {
        this(statusCode, contentType, content);
        this.headers = new HashMap<>(headers);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public String getReasonPhrase() {
        return HttpReasonPhrases.getByStatusCode(statusCode);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
