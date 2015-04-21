package assignment_2;

import assignment_2.HelperClasses.*;
import assignment_2.interfaces.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Chat View implementation
 * Created by martin on 28/03/2015.
 */
public class ChatViewClassic extends JFrame implements ChatView {

    private final JTextField messageField;
    private final StyledDocument chatRoomDocument, onlineUsersDocument;
    private final JTextPane chatPane, onlineUsersPane;
    private final Style userNameStyle, textStyle;
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Border BORDER = new LineBorder(Color.lightGray);

    public JTextField getMessageField() { return messageField; }

    /**
     * Creates Chat View
     * @param title text to window banner
     * @param onKeyENTER what happens when user hit ENTER
     * @param onEXIT what happens when user clicks on EXIT
     */
    public ChatViewClassic(final String title,
                           final VoidMethod onKeyENTER,
                           final VoidMethod onEXIT) {
        super(title);
        messageField = new JTextField();
        StyleContext styleContext = new StyleContext();
        chatRoomDocument = new DefaultStyledDocument(styleContext);
        onlineUsersDocument = new DefaultStyledDocument(styleContext);
        userNameStyle = styleContext.addStyle("Username", null);
        textStyle = styleContext.addStyle("Text", null);
        setStyles();
        chatPane = new JTextPane(chatRoomDocument);
        onlineUsersPane = new JTextPane(onlineUsersDocument);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(themesMenu());
        buildContentPane();
        getContentPane().setBackground(new Color(235, 235, 235));
        setJMenuBar(menuBar);
        setTheme("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 400);
        setResizable(false);
        insertLinesToChatPane(19);

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) onKeyENTER.run();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onEXIT.run();
            }
        });

        setVisible(true);
        messageField.requestFocus();
    }

    private void setStyles() {
        userNameStyle.addAttribute(StyleConstants.FontSize, 14);
        userNameStyle.addAttribute(StyleConstants.FontFamily, "Hobo Std");
        userNameStyle.addAttribute(StyleConstants.Italic, true);

        textStyle.addAttribute(StyleConstants.Foreground, Color.darkGray);
        textStyle.addAttribute(StyleConstants.FontSize, 14);
        textStyle.addAttribute(StyleConstants.FontFamily, "consolas");
        textStyle.addAttribute(StyleConstants.Bold, false);
    }

    private void setAndPlaceMessageField() {
        messageField.setBounds(2, 320, 589, 26);
        messageField.setBorder(BORDER);
        messageField.setFont(new Font("consolas", Font.PLAIN, 14));
        messageField.setForeground(Color.darkGray);
        messageField.setDocument(new JTextFieldLimiter(73));
        getContentPane().add(messageField);
    }

    private void setAndPlaceTextPanes() {
        makeChatPane();
        makeOnlineUsersPane();
    }

    private void makeChatPane() {
        chatPane.setEditable(false);
        chatPane.setBorder(BORDER);
        JScrollPane chatScrollPane = new JScrollPane(chatPane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setBounds(2, 18, 434, 300);
        chatScrollPane.setBorder(BORDER);
        getContentPane().add(chatScrollPane);
    }

    private void makeOnlineUsersPane() {
        onlineUsersPane.setEditable(false);
        onlineUsersPane.setBorder(BORDER);
        JScrollPane usersOnlineScrollPane = new JScrollPane(onlineUsersPane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        usersOnlineScrollPane.setBounds(437, 18, 156, 300);
        usersOnlineScrollPane.setBorder(BORDER);
        getContentPane().add(usersOnlineScrollPane);
    }

    private void insertLinesToChatPane(final int i) {
        IntStream.range(1,i).forEach(j -> {
            try {
                chatRoomDocument.insertString(chatRoomDocument.getLength(), "\n", userNameStyle);
            } catch(BadLocationException e) { e.printStackTrace(); }
        });
    }

    private void setTheme(final String theme) {
        try {
            UIManager.setLookAndFeel(theme);
            SwingUtilities.updateComponentTreeUI(ChatViewClassic.this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void buildContentPane() {
        getContentPane().setBackground(Color.white);
        getContentPane().setLayout(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setAndPlaceLabels();
        setAndPlaceTextPanes();
        setAndPlaceMessageField();
    }

    private void setAndPlaceLabels() {
        JLabel chatRoomLabel = new JLabel("Chat Room");
        chatRoomLabel.setBounds(2, 2, 100, 15);
        getContentPane().add(chatRoomLabel);

        JLabel usersOnlineLabel = new JLabel("Users Online");
        usersOnlineLabel.setBounds(437, 2, 100, 15);
        getContentPane().add(usersOnlineLabel);

        Font font = new Font("Century", Font.PLAIN, 14);
        Color color = new Color(25, 71, 25);

        for(Component c :getContentPane().getComponents()) {
            if(c instanceof JLabel) {
                c.setFont(font);
                c.setForeground(color);
            }
        }
    }

    private class LookAndFeelMenuItem extends JMenuItem implements ActionListener{

        private final UIManager.LookAndFeelInfo lookAndFeelInfo;

        private LookAndFeelMenuItem(final UIManager.LookAndFeelInfo lookAndFeelInfo) {
            super(lookAndFeelInfo.getName());
            this.lookAndFeelInfo = lookAndFeelInfo;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                LookAndFeelMenuItem menuItem = ((LookAndFeelMenuItem) event.getSource());
                String className = menuItem.lookAndFeelInfo.getClassName();
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(ChatViewClassic.this);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private JMenu themesMenu() {
        JMenu menuLookAndFeel = new JMenu("Themes");
        Arrays.asList(UIManager.getInstalledLookAndFeels())
                .forEach(i -> menuLookAndFeel.add(new LookAndFeelMenuItem(i)));
        return menuLookAndFeel;
    }

    private static final String messageArrived = "button.wav";

    @Override
    public void print(final Line chatLine) {
        Sounds.playSound(messageArrived);
        userNameStyle.addAttribute(StyleConstants.Foreground, chatLine.getColor());
        try {
            chatPane.setCaretPosition(chatRoomDocument.getLength());
            chatPane.insertIcon(chatLine.getIcon());

            chatRoomDocument.insertString(
                    chatRoomDocument.getLength(),
                    String.format("[%s]>  ", chatLine.getName()),
                    userNameStyle);

            chatRoomDocument.insertString(
                    chatRoomDocument.getLength(),
                    String.format("%s%n", chatLine.getText()),
                    textStyle);

            chatPane.setCaretPosition(chatRoomDocument.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(final List<Client> onlineClients) {
        try {
            onlineUsersDocument.remove(0, onlineUsersDocument.getLength());
            for(Client c : onlineClients) {
                userNameStyle.addAttribute(StyleConstants.Foreground, c.getColor());

                onlineUsersDocument.insertString(
                        0,
                        String.format("%s%n", c.getUserName()),
                        userNameStyle);

                onlineUsersPane.insertIcon(c.getIcon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * text field limiter
     * Created by martin on 15/04/2015.
     */
    public class JTextFieldLimiter extends PlainDocument {

        private final int limit;

        /**
         * Limits number of characters entered to text field
         * @param limit max of characters
         */
        public JTextFieldLimiter(final int limit) {
            super();
            this.limit = limit;
        }

        public void insertString(final int offset, final String str, final AttributeSet attr)
                throws BadLocationException {
            if (str == null) return;
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    public static void main(String[] args) {
        new ChatViewClassic(
                "Chat View",
                () -> System.out.println("Enter is working"),
                () -> System.exit(0)
        );
    }
}
