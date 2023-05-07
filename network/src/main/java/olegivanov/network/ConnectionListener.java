package olegivanov.network;

public interface ConnectionListener {
    void onConnectionReady(Connection connection);
    void onReceiveString(Connection connection, String msg);
    void onDisconnect(Connection connection);
    void onException(Connection connection, Exception e);
}
