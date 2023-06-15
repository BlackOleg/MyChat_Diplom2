package olegivanov.k_chat.client;

import olegivanov.logger.Log;
import olegivanov.network.Config;
import olegivanov.network.Connection;
import olegivanov.network.ConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static olegivanov.logger.Log.getInstance;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {
    private static String nick = "Noname";
    private static String ip_addr = "127.0.0.1";
    private static int port = 8189;
    private static final int WITH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea msgFrame = new JTextArea(22, 50);
    JPanel msgPanel = new JPanel();
    JPanel topPanel = new JPanel();
    JScrollPane scroller = new JScrollPane(msgFrame);
    private final JLabel userLabel = new JLabel("User name: ");
    private static JTextField nickName;
    private final JLabel textLabel = new JLabel("Input your message: ");
    private final JTextField inputMessage = new JTextField();
    private static Connection connection;
    private Log log = getInstance("client");

    public static void main(String[] args) {
        InputParameters();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow().go();
            }
        });
    }

    public void go() {
        try {
            connection = new Connection(this, ip_addr, port);
            connection.go();
        } catch (IOException e) {
            sendMessage("Connection Exception: " + e);
        }
    }

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Стандартный выход/закрытие по крестику из окна
        setSize(WITH, HEIGHT); // задаем размеры по-умолчанию
        setLocationByPlatform(true); // расположение задает Windows
        setAlwaysOnTop(true);
        setTitle("Simple Messenger - with user: " + nick); // подписываем окно сообщений
        setResizable(true); // может менять размер
        msgFrame.setLineWrap(true); // включение Wrap on
        msgFrame.setEditable(false); // не редактируемый
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        msgPanel.add(scroller);
        topPanel.add(userLabel);
        topPanel.add(nickName);
        add(topPanel, BorderLayout.NORTH);
        add(msgPanel, BorderLayout.CENTER); // добавляем компоненты ..
        //add(textLabel, BorderLayout.SOUTH);
        inputMessage.addActionListener(this); // слушаем нажатие энтер
        add(inputMessage, BorderLayout.SOUTH);
        setVisible(true); // делаем видимым
    }

    private static void InputParameters() {
        Config.load("config.json");
        Config config = Config.getInstance();
        ip_addr = config.getAddress();
        port = config.getPort();
        JTextField addressInput = new JTextField(ip_addr, 15);
        JTextField portInput = new JTextField(String.valueOf(port), 10);
        JTextField nickInput = new JTextField(nick, 15);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Server address: "));
        myPanel.add(addressInput);
        myPanel.add(Box.createHorizontalStrut(15)); //  spacer
        myPanel.add(new JLabel("Port:"));
        myPanel.add(portInput);
        myPanel.add(Box.createHorizontalStrut(15)); //  spacer
        myPanel.add(new JLabel("Nick Name:"));
        myPanel.add(nickInput);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please check Server address, port & input your nick name", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !addressInput.getText().equals("")
                && !portInput.getText().equals("")
                && !nickInput.getText().equals("")) {
            ip_addr = addressInput.getText();
            port = Integer.parseInt(portInput.getText());
            nick = nickInput.getText();
            nickName = new JTextField(nick);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String msg = inputMessage.getText();
        if (msg.equals("")) return;
        inputMessage.setText(null);
        connection.sendMsg(nick + " says: " + msg);

    }

    @Override
    public void onConnectionReady(Connection connection) {
        sendMessage("Connection ready");
    }

    @Override
    public void onReceiveString(Connection connection, String msg) {
        if (msg.indexOf("exit") >= 0) {
            System.out.println("Client wants to disconnect!");
            connection.disconnect();
        }
        sendMessage(msg);
    }

    @Override
    public void onDisconnect(Connection connection) {
        sendMessage("Connection closed");
    }

    @Override
    public void onException(Connection connection, Exception e) {
        sendMessage("Connection Exception: " + e);
    }

    private synchronized void sendMessage(String msg) {
        log.logInsert(nick, msg);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                msgFrame.append(msg + "\n");
                msgFrame.setCaretPosition(msgFrame.getDocument().getLength());

            }
        });

    }
}
