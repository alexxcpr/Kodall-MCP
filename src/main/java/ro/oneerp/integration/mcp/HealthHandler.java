package ro.oneerp.integration.mcp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class HealthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String reqMethod = t.getRequestMethod();
        if (!reqMethod.equals("GET")){
            t.getResponseHeaders().set("Allow", "GET");
            t.sendResponseHeaders(405, -1);
            t.close();
            return;
        }

        t.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");

        String response = "OK";
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

        t.sendResponseHeaders(200, responseBytes.length);

        try (OutputStream os = t.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
