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

    @BeforeEach
    void setUp() throws Exception {
        // Arrange
        this.mcpServer = new OneMcpHttpServer("127.0.0.1", 0);
        this.mcpServer.start();

        this.healthUri = URI.create("http://127.0.0.1:" + this.mcpServer.getMcpServerPort() + "/health");

        this.client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        this.client.close();
        this.mcpServer.stop(0);
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
