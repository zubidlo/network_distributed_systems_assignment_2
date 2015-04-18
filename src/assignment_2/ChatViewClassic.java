package assignment_2;

import assignment_2.interfaces.ChatView;
import assignment_2.interfaces.Client;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static javax.swing.UIManager.*;

/**
 * JFrame skeleton with theme changing menu
 * Created by martin on 28/03/2015.
 */
public class ChatViewClassic extends JFrame implements ChatView {

    public final JTextField messageField;
    private final StyledDocument chatRoomDocument, usersOnlineDocument;
    private final JTextPane chatPane, usersOnlinePane;
    private final Style userNameStyle, textStyle;
    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Border BORDER = new LineBorder(Color.lightGray);

    public ChatViewClassic(String title) {
        super(title);
        messageField = new JTextField();
        StyleContext styleContext = new StyleContext();
        chatRoomDocument = new DefaultStyledDocument(styleContext);
        usersOnlineDocument = new DefaultStyledDocument(styleContext);
        userNameStyle = styleContext.addStyle("Username", null);
        textStyle = styleContext.addStyle("Text", null);
        setStyles();
        chatPane = new JTextPane(chatRoomDocument);
        usersOnlinePane = new JTextPane(usersOnlineDocument);
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
        setVisible(true);
        messageField.requestFocus();
    }

    private void setStyles() {
        userNameStyle.addAttribute(StyleConstants.FontSize, 12);
        userNameStyle.addAttribute(StyleConstants.FontFamily, "Arial");
        userNameStyle.addAttribute(StyleConstants.Bold, true);
        userNameStyle.addAttribute(StyleConstants.Italic, true);

        textStyle.addAttribute(StyleConstants.FontSize, 14);
        textStyle.addAttribute(StyleConstants.FontFamily, "consolas");
        textStyle.addAttribute(StyleConstants.Bold, false);
    }

    private void insertLinesToChatPane(int i) {
        IntStream.range(1,i).forEach(j -> {
            try {
                chatRoomDocument.insertString(chatRoomDocument.getLength(), "\n", userNameStyle);
            } catch(BadLocationException e) { e.printStackTrace(); }
        });
    }

    private void setTheme(String theme) {
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

        JLabel newMessageLabel = new JLabel("New Message");
        newMessageLabel.setBounds(2, 326, 100, 15);
        getContentPane().add(newMessageLabel);

        Font font = new Font("Century", Font.PLAIN, 14);
        Color color = new Color(25, 71, 25);

        for(Component c :getContentPane().getComponents()) {
            if(c instanceof JLabel) {
                c.setFont(font);
                c.setForeground(color);
            }
        }
    }

    private void setAndPlaceTextPanes() {
        chatPane.setEditable(false);
        chatPane.setBorder(BORDER);
        JScrollPane chatScrollPane = new JScrollPane(chatPane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setBounds(2, 18, 434, 300);
        chatScrollPane.setBorder(BORDER);
        getContentPane().add(chatScrollPane);

        usersOnlinePane.setEditable(false);
        usersOnlinePane.setBorder(BORDER);
        JScrollPane usersOnlineScrollPane = new JScrollPane(usersOnlinePane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        usersOnlineScrollPane.setBounds(437, 18, 156, 300);
        usersOnlineScrollPane.setBorder(BORDER);
        getContentPane().add(usersOnlineScrollPane);
    }

    private void setAndPlaceMessageField() {
        messageField.setBounds(92, 320, 498, 26);
        messageField.setBorder(BORDER);
        messageField.setFont(new Font("consolas", Font.PLAIN, 14));
        messageField.setDocument(new JTextFieldLimiter(62));
        getContentPane().add(messageField);
    }

    private class LookAndFeelMenuItem extends JMenuItem implements ActionListener{

        private final LookAndFeelInfo lookAndFeelInfo;

        private LookAndFeelMenuItem(final LookAndFeelInfo lookAndFeelInfo) {
            super(lookAndFeelInfo.getName());
            this.lookAndFeelInfo = lookAndFeelInfo;
            addActionListener(this);
            System.out.println("theme: " + lookAndFeelInfo.getClassName());
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
        Arrays.asList(getInstalledLookAndFeels())
                .forEach(i -> menuLookAndFeel.add(new LookAndFeelMenuItem(i)));
        return menuLookAndFeel;
    }

    @Override
    public void postMessage(Line line) {
        userNameStyle.addAttribute(StyleConstants.Foreground, line.getColor());
        try {
            chatPane.setCaretPosition(chatRoomDocument.getLength());
            chatPane.insertIcon(line.getIcon());
            chatRoomDocument.insertString(chatRoomDocument.getLength(), String.format("[%s]>  ", line.getName()), userNameStyle);
            chatRoomDocument.insertString(chatRoomDocument.getLength(), String.format("%s%n", line.getText()), textStyle);
            chatPane.setCaretPosition(chatRoomDocument.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateConnectedUsers(List<Client> onlineClients) {
        try {
            usersOnlineDocument.remove(0, usersOnlineDocument.getLength());
            for(Client c : onlineClients) {
                userNameStyle.addAttribute(StyleConstants.Foreground, c.getColor());
                usersOnlineDocument.insertString(0, String.format("%s%n", c.getUserName()), userNameStyle);
                usersOnlinePane.insertIcon(c.getIcon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color randColor() {
        return new Color(rand(), rand(), rand());
    }

    private static int rand() {
        return (int) Math.round(200 * Math.random());
    }

    private static void printInstalledFonts() {
        Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        new ChatViewClassic("frame skeleton");
        printInstalledFonts();
    }

    /**
     * Document adjustment
     * Created by martin on 15/04/2015.
     */
    public class JTextFieldLimiter extends PlainDocument {

        private final int limit;

        public JTextFieldLimiter(final int limit) {
            super();
            this.limit = limit;
        }

        public void insertString( int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
