package ro.oneerp.integration.mcp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class McpHandler implements HttpHandler {
    @Override
    public void handle (HttpExchange t) throws IOException {
        //verify exact route
        if (!t.getRequestURI().getPath().equals("/mcp")){
            t.sendResponseHeaders(404, -1);
            t.close();
            return;
        }

        //only allow POST method
        String reqMethod = t.getRequestMethod();
        if (!reqMethod.equals("POST")){
            t.getResponseHeaders().set("Allow", "POST");
            t.sendResponseHeaders(405, -1);
            t.close();
            return;
        }

        byte[] reqBody;
        try (InputStream is = t.getRequestBody()){
            reqBody = is.readAllBytes();
        }

        //verify for empty body
        if (reqBody.length == 0) {
            t.sendResponseHeaders(400, -1);
            t.close();
            return;
        }

        t.sendResponseHeaders(501, -1);
        t.close();
    }
}
