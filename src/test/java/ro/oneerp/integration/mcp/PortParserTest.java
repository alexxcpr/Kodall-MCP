package ro.oneerp.integration.mcp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortParserTest {

    @Test
    void acceptsNormalPort () {
        // Arrange
        String testPort = "8000";

        // Act
        int actualPort = PortParser.parsePort(testPort);

        // Assert
        assertEquals(8000, actualPort);
    }

    @Test
    void rejectsMissingPort () {
        PortConfigurationException exception = assertThrows(
                PortConfigurationException.class,
                () -> PortParser.parsePort(null)
        );

        assertEquals(PortErrorCode.MISSING, exception.getErrorCode());
    }

    @Test
    void rejectsBlankPort () {
        PortConfigurationException exception = assertThrows(
                PortConfigurationException.class,
                () -> PortParser.parsePort("     ")
        );

        assertEquals(PortErrorCode.BLANK, exception.getErrorCode());
    }

    @Test
    void rejectsNotNumericPort () {
        PortConfigurationException exception = assertThrows(
          PortConfigurationException.class,
          () -> PortParser.parsePort("08abc")
        );

        assertEquals(PortErrorCode.NOT_NUMERIC, exception.getErrorCode());
    }

    @Test
    void rejectsOutOfRangePortLowerInterval () {
        PortConfigurationException exception = assertThrows(
          PortConfigurationException.class,
          () -> PortParser.parsePort("0")
        );

        assertEquals(PortErrorCode.OUT_OF_RANGE, exception.getErrorCode());
    }

    @Test
    void rejectsOutOfRangePortHigherInterval () {
        PortConfigurationException exception = assertThrows(
                PortConfigurationException.class,
                () -> PortParser.parsePort("65536")
        );

        assertEquals(PortErrorCode.OUT_OF_RANGE, exception.getErrorCode());
    }
}
