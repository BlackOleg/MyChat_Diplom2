package olegivanov.k_chat.server;

import olegivanov.logger.Log;
import olegivanov.network.Config;
import olegivanov.network.Connection;
import olegivanov.network.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static olegivanov.logger.Log.getInstance;

public class ChatServer implements ConnectionListener {
    public static void main(String[] args) {
        new ChatServer().go();
    }

    private Log log = getInstance("server");
    private List<Connection> connections;
    private final int port;

    public void go() {
        connections = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("got a connection on port: " + port);
            log.logInsert("service", "Server started on port: " + port);
            while (true) {
                try {
                    new Connection(this, serverSocket.accept()).go();

                } catch (IOException e) {
                    System.out.println("TCPConnection Exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ChatServer() {
        Config.load("config.json");
        Config config = Config.getInstance();
        this.port = config.getPort();
    }

    @Override
    public synchronized void onConnectionReady(Connection connection) {
        connections.add(connection);
        sendAll("Client connected! WELCOME - " + connection);
    }

    @Override
    public synchronized void onReceiveString(Connection connection, String msg) {
//        if (msg.indexOf("exit") >= 0) {
//            System.out.println("Client wants to disconnect!");
//            connection.disconnect();
//        }
        sendAll(connection + ":- " + msg);
    }

    @Override
    public synchronized void onDisconnect(Connection connection) {
        connections.remove(connection);
        sendAll("Client disconnected! GOODBYE - " + connection);
    }

    @Override
    public synchronized void onException(Connection connection, Exception e) {
        System.out.println("TCPConnection Exception: " + e);
    }

    private void sendAll(String value) {
        log.logInsert("service", value);
        System.out.println(value);
        final int allI = connections.size();
        for (Connection connection : connections) {
            connection.sendMsg(value);
        }


    }

}
