package olegivanov.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Connection implements Runnable {
    private final Socket socket;
    private Thread rxThread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final ConnectionListener eventListener;

    public Connection(ConnectionListener eventListener, String ipAddress, int port) throws IOException{
        this(eventListener, new Socket(ipAddress,port));
    }
    public Connection(ConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

    }

    public synchronized void sendMsg(String msg) {
        try {
            out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
            disconnect();
        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
        }
    }
    @Override
    public String toString() {
        return "Guest with TCP: " + socket.getInetAddress()+ " port: " + socket.getPort();
    }

    @Override
    public void run() {
        try {
            eventListener.onConnectionReady(Connection.this);
            while (rxThread.isAlive()) {
                eventListener.onReceiveString(Connection.this, in.readLine());
            }

        } catch (IOException e) {
            eventListener.onException(Connection.this, e);
        } finally {
            eventListener.onDisconnect(Connection.this);
        }
    }

    public void go() {
        rxThread = new Thread(Connection.this);
        rxThread.start();
    }
}
