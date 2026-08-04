package ro.oneerp.integration.mcp;

class PortParser {
    static int parsePort(String rawPort) {
        if (rawPort == null){
            throw new PortConfigurationException(PortErrorCode.MISSING, "Portul pentru One MCP nu este configurat");
        }

        if (rawPort.isBlank()){
            throw new PortConfigurationException(PortErrorCode.BLANK, "Portul pentru One MCP nu este completat (blank)");
        }

        String normalizedPort = rawPort.trim();

        int intPort;

        try {
            intPort = Integer.parseInt(normalizedPort);
        } catch (NumberFormatException e) {
            throw new PortConfigurationException(PortErrorCode.NOT_NUMERIC, "Portul introdus nu poate fi convertit in int");
        }

        if (intPort <= 0 || intPort > 65535){
            throw new PortConfigurationException(PortErrorCode.OUT_OF_RANGE, "Portul introdus nu este valid");
        }

        return intPort;
    }
}
