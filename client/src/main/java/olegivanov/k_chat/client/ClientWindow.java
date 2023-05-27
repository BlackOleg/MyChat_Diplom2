package olegivanov.k_chat.client;

import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame  {
    private static final String IP_ADDR = "127.0.0.1";
    private static final int PORT = 8189;
    private static final int WITH = 600;
    private static final int HEIGHT = 400;
    private final JTextArea msgFrame = new JTextArea();
    private final JLabel userLabel = new JLabel("User name: ");
    private final JTextField nickName = new JTextField("Noname");
    private final JLabel textLabel = new JLabel("Input your message: ");
    private final JTextField inputMessage = new JTextField();

    public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() {
        new ClientWindow();
    }
});
    }
    private ClientWindow(){
setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // Стандартный выход/закрытие по крестику из окна
        setSize(WITH,HEIGHT); // задаем размеры размеры по-умодчанию
        setLocationByPlatform(true); // расположение задает Windows
        setAlwaysOnTop(true); // всегда  поверх экрана
        setTitle("Simple Messenger"); // подписываем окно сообщений
        setResizable(true); // может менять размер
        msgFrame.setLineWrap(true); // включение Wrap on
        msgFrame.setEditable(false); // не редактируемый
        add(msgFrame, BorderLayout.CENTER); // добавляем компоненты ..
        add(userLabel, BorderLayout.NORTH);
        add(nickName, BorderLayout.NORTH);
        add(inputMessage, BorderLayout.SOUTH);



        setVisible(true); // делаем видимым
    }
}
