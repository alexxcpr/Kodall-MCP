package ro.oneerp.integration.mcp;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class OneMcpHttpServerTest {
    private OneMcpHttpServer mcpServer;
    private HttpClient client;
    private URI healthUri;
    private URI mcpUri;

    @BeforeEach
    void setUp() throws Exception {
        // Arrange
        String ipAddress = "127.0.0.1";
        this.mcpServer = new OneMcpHttpServer(ipAddress, 0);
        this.mcpServer.start();

        String protocol = "http://";
        this.healthUri = URI.create(protocol + ipAddress + ":" + this.mcpServer.getMcpServerPort() + "/health");
        this.mcpUri = URI.create(protocol + ipAddress + ":" + this.mcpServer.getMcpServerPort() + "/mcp");

        this.client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        this.client.close();
        this.mcpServer.stop(0);
    }

    @Test
    void mcpRejectsGet() throws Exception {
        // Arrange
        HttpRequest req = HttpRequest.newBuilder()
                .uri(this.mcpUri)
                .GET()
                .build();

        // Act
        HttpResponse<String> response = this.client.send(req, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(405, response.statusCode());
        assertEquals("", response.body());
        assertEquals(
                "POST",
                response.headers()
                        .firstValue("Allow")
                        .orElseThrow()
        );
    }

    @Test
    void mcpRejectsEmptyBody() throws Exception {
        // Arrange
        HttpRequest req = HttpRequest.newBuilder()
                .uri(mcpUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        // Act
        HttpResponse<String> response = this.client.send(req, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(400, response.statusCode());
        assertEquals("", response.body());
    }

    @Test
    void healthReturnsOk() throws Exception {
        // Arrange
        HttpRequest req = HttpRequest.newBuilder()
                .uri(this.healthUri)
                .GET()
                .build();

        // Act
        HttpResponse<String> response = this.client.send(req, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(200, response.statusCode());
        assertEquals("OK", response.body());
        assertEquals(
                "text/plain; charset=utf-8",
                response.headers()
                        .firstValue("Content-Type")
                        .orElseThrow()
        );
    }

    @Test
    void healthRejectsPost() throws Exception {
        // Arrange
        HttpRequest req = HttpRequest.newBuilder()
                .uri(this.healthUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        //Act
        HttpResponse<String> response = this.client.send(req, HttpResponse.BodyHandlers.ofString());

        //Assert
        assertEquals(405, response.statusCode());
        assertEquals("GET", response.headers().firstValue("Allow").orElseThrow());
        assertEquals("", response.body());
    }

    @Test
    void healthRejectInexistentRoute() throws Exception {
        // Arrange
        URI customUri = URI.create(this.healthUri + "/inexistent-route");
        HttpRequest req = HttpRequest.newBuilder()
                .uri(customUri)
                .GET()
                .build();

        // Act
        HttpResponse<String> response = this.client.send(req, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(404, response.statusCode());
        assertEquals("", response.body());
    }
}
