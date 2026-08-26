package ro.oneerp.integration.mcp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

        //Verify Headers - Content Type
        String contentType = t.getRequestHeaders().getFirst("Content-Type");
        if (contentType == null || contentType.isBlank()){
            t.sendResponseHeaders(415, -1);
            t.close();
            return;
        }
        String mediaType = contentType.split(";",2)[0].trim();

        if (!mediaType.equalsIgnoreCase("application/json")) {
            t.sendResponseHeaders(415, -1);
            t.close();
            return;
        }

        //Verify Headers - Accept
        String acceptHeader = t.getRequestHeaders().getFirst("Accept");
        if (acceptHeader == null || acceptHeader.isBlank()){
            t.sendResponseHeaders(406, -1);
            t.close();
            return;
        }

        String[] acceptHeaderItems = acceptHeader.split(",");
        boolean existsAppJson = false;
        boolean existsEventStream = false;

        for (String acceptHeaderItem : acceptHeaderItems) {
            acceptHeaderItem = acceptHeaderItem.trim();
            if (acceptHeaderItem.equalsIgnoreCase("application/json")){
                existsAppJson = true;
            }
            if (acceptHeaderItem.equalsIgnoreCase("text/event-stream")){
                existsEventStream = true;
            }
        }

        if (!existsAppJson || !existsEventStream){
            t.sendResponseHeaders(406, -1);
            t.close();
            return;
        }

        //Verify Headers - MCP-Protocol-Version: 2026-07-28
        String mcpProtocolVersionHeader = t.getRequestHeaders().getFirst("MCP-Protocol-Version");
        if (mcpProtocolVersionHeader == null || mcpProtocolVersionHeader.isBlank()){
            t.sendResponseHeaders(400, -1);
            t.close();
            return;
        }
        if (!mcpProtocolVersionHeader.equals("2026-07-28")) {
            t.sendResponseHeaders(400, -1);
            t.close();
            return;
        }

        byte[] reqBodyBytes;
        try (InputStream is = t.getRequestBody()){
            reqBodyBytes = is.readAllBytes();
        }

        String reqBody = new String(reqBodyBytes, StandardCharsets.UTF_8);

        //verify for empty body
        if (reqBody.isBlank()) {
            t.sendResponseHeaders(400, -1);
            t.close();
            return;
        }

        //All OK
        t.sendResponseHeaders(501, -1);
        t.close();
    }
}
