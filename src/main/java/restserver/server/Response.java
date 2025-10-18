package restserver.server;

import com.sun.net.httpserver.HttpExchange;
import restserver.http.ContentType;
import restserver.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Represents an HTTP response to be sent to the client.
 * Stores the status code, content type, and response content.
 */
public class Response {
    private int status;
    private String contentType;
    private String content;

    public Response(HttpStatus httpStatus, ContentType contentType, String content) {
        this.status = httpStatus.code;
        this.contentType = contentType.type;
        this.content = content;
    }

    public void send(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add("Cache-Control", "nocache");
        httpExchange.getResponseHeaders().add("Content-Type", contentType);

        try (httpExchange) {
            byte[] responseBody = content.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(status, responseBody.length);
            httpExchange.getResponseBody().write(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}