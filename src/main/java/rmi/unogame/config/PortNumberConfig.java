package rmi.unogame.config;

public enum PortNumberConfig {
    DISPATCHER_PORT_NUMBER(1099),
    SERVER_PORT_NUMBER(1200),
    DATABASE_PORT_NUMBER(1300),
    ;

    final private int portNumber;

    PortNumberConfig(int portNumber) {
        this.portNumber = portNumber;
    }

    public int getProperty() {
        return portNumber;
    }
}
