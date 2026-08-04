package ro.oneerp.integration.mcp;

/*
* PORT_ONE_MCP variabina de mediu necesara
* */

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting ONE ERP MCP Server");
        System.out.println("Java version: " + System.getProperty("java.version"));

        String rawPortOneMcp = System.getenv("PORT_ONE_MCP");
        int portOneMcp = PortParser.parsePort(rawPortOneMcp);

        System.out.println(portOneMcp);
    }


}

