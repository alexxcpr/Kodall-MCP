package ro.oneerp.integration.mcp;

class PortConfigurationException extends IllegalArgumentException {
    private final PortErrorCode errorCode;

    PortConfigurationException(PortErrorCode paramErrorCode, String message) {
        super(message);
        this.errorCode = paramErrorCode;
    }

    PortErrorCode getErrorCode () {
        return this.errorCode;
    }
}
