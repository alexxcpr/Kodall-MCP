package ro.oneerp.integration.mcp;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class OneMcpHttpServer {
    private final HttpServer server;

    public OneMcpHttpServer(String ipAddress, int port) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(ipAddress, port);

        HttpServer server = HttpServer.create(socketAddress, 0);
        server.createContext("/health", new HealthHandler());
        server.setExecutor(null);

        this.server = server;
    }

    public void start() {
        this.server.start();
        System.out.println("MCP: HTTP server STARTED.");
    }

    public void stop(int delaySeconds) {
        this.server.stop(delaySeconds);
        System.out.println("MCP: HTTP server STOPPED.");
    }

    public int getMcpServerPort () {
        return this.server.getAddress().getPort();
    }
}
