package httpd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpReasonPhrases {
    public static String getByStatusCode(int statusCode) {
        return MAP.getOrDefault(statusCode, "");
    }

    private static final Map<Integer, String> MAP;

    static {
        Map<Integer, String> m = new HashMap<>();

        m.put(100, "Continue");
        m.put(101, "Switching Protocols");
        m.put(102, "Processing");                 // WebDAV
        m.put(103, "Early Hins");                 // (optional; pre-HTTP/2 hint)

        m.put(200, "OK");
        m.put(201, "Created");
        m.put(202, "Accepted");
        m.put(203, "Non-Authoritative Information");
        m.put(204, "No Content");
        m.put(205, "Reset Content");
        m.put(206, "Partial Content");
        m.put(207, "Multi-Status");               // WebDAV
        m.put(208, "Already Reported");           // WebDAV
        m.put(226, "IM Used");

        m.put(300, "Multiple Choices");
        m.put(301, "Moved Permanently");
        m.put(302, "Found");
        m.put(303, "See Other");
        m.put(304, "Not Modified");
        m.put(305, "Use Proxy");
        m.put(307, "Temporary Redirect");
        m.put(308, "Permanent Redirect");

        m.put(400, "Bad Request");
        m.put(401, "Unauthorized");
        m.put(402, "Payment Required");
        m.put(403, "Forbidden");
        m.put(404, "Not Found");
        m.put(405, "Method Not Allowed");
        m.put(406, "Not Acceptable");
        m.put(407, "Proxy Authentication Required");
        m.put(408, "Request Timeout");
        m.put(409, "Conflict");
        m.put(410, "Gone");
        m.put(411, "Length Required");
        m.put(412, "Precondition Failed");
        m.put(413, "Payload Too Large");
        m.put(414, "URI Too Long");
        m.put(415, "Unsupported Media Type");
        m.put(416, "Range Not Satisfiable");
        m.put(417, "Expectation Failed");
        m.put(418, "I'm a teapot");
        m.put(421, "Misdirected Request");
        m.put(422, "Unprocessable Entity");
        m.put(423, "Locked");
        m.put(424, "Failed Dependency");
        m.put(425, "Too Early");
        m.put(426, "Upgrade Required");
        m.put(428, "Precondition Required");
        m.put(429, "Too Many Requests");
        m.put(431, "Request Header Fields Too Large");
        m.put(451, "Unavailable For Legal Reasons");

        m.put(500, "Internal Server Error");
        m.put(501, "Not Implemented");
        m.put(502, "Bad Gateway");
        m.put(503, "Service Unavailable");
        m.put(504, "Gateway Timeout");
        m.put(505, "HTTP Version Not Supported");
        m.put(506, "Variant Also Negotiates");
        m.put(507, "Insufficient Storage");
        m.put(508, "Loop Detected");
        m.put(510, "Not Extended");
        m.put(511, "Network Authentication Required");

        MAP = Collections.unmodifiableMap(m);
    }
}
