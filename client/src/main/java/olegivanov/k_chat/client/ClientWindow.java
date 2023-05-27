package olegivanov.k_chat.client;

import olegivanov.network.Connection;
import olegivanov.network.ConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, ConnectionListener {
    private static String ip_addr = "127.0.0.1";
    private static  int port= 8189;
    private static final int WITH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea msgFrame = new JTextArea();
    private final JLabel userLabel = new JLabel("User name: ");
    private static JTextField nickName = new JTextField();
    private final JLabel textLabel = new JLabel("Input your message: ");
    private final JTextField inputMessage = new JTextField();
    private static Connection connection;

    public static void main(String[] args) {
        InputParameters();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Стандартный выход/закрытие по крестику из окна
        setSize(WITH, HEIGHT); // задаем размеры размеры по-умодчанию
        setLocationByPlatform(true); // расположение задает Windows
        setAlwaysOnTop(true); // всегда  поверх экрана
        setTitle("Simple Messenger - with user: " + nickName.getText()); // подписываем окно сообщений
        setResizable(true); // может менять размер
        msgFrame.setLineWrap(true); // включение Wrap on
        msgFrame.setEditable(false); // не редактируемый

        //add(userLabel,BorderLayout.NORTH);
        //add(nickName,BorderLayout.AFTER_LINE_ENDS);
        add(msgFrame, BorderLayout.CENTER); // добавляем компоненты ..
        add(textLabel, BorderLayout.SOUTH);
        inputMessage.addActionListener(this); // слушаем нажатие энтер
        add(inputMessage, BorderLayout.AFTER_LAST_LINE);

        setVisible(true); // делаем видимым
        try {
            connection = new Connection(this,ip_addr,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void InputParameters() {
        JTextField addressInput = new JTextField("127.0.0.1",15);
        JTextField portInput = new JTextField("8189",10);
        JTextField nickInput = new JTextField("Noname",15);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Server address: "));
        myPanel.add(addressInput );
        myPanel.add(Box.createHorizontalStrut(15)); //  spacer
        myPanel.add(new JLabel("Port:"));
        myPanel.add(portInput);
        myPanel.add(Box.createHorizontalStrut(15)); //  spacer
        myPanel.add(new JLabel("Nick Name:"));
        myPanel.add(nickInput);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter Server address, port & your nick name", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && addressInput.getText().equals("")
                                            && portInput.getText().equals("")
                                            && nickInput.getText().equals("")) {
            ip_addr = addressInput.getText();
            port = Integer.parseInt(portInput.getText());
            nickName = nickInput;

        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void onConnectionReady(Connection connection) {

    }

    @Override
    public void onReceiveString(Connection connection, String msg) {

    }

    @Override
    public void onDisconnect(Connection connection) {

    }

    @Override
    public void onException(Connection connection, Exception e) {

    }
}
