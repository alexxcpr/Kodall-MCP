package ro.oneerp.integration.mcp;

/*
* PORT_ONE_MCP variabina de mediu necesara
* */

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting Kodall MCP Server");
        System.out.println("Java version: " + System.getProperty("java.version"));

        String rawPortOneMcp = System.getenv("PORT_ONE_MCP");
        int portOneMcp = PortParser.parsePort(rawPortOneMcp);

        System.out.println(portOneMcp);

        //trebuie creat serverul, trimis portul ca parametru
        var mcpServer = new OneMcpHttpServer("127.0.0.1", portOneMcp);
        mcpServer.start();
    }


}

