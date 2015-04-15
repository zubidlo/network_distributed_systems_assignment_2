package assignment_2.helperClasses;

import assignment_2.interfaces.Client;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static javax.swing.UIManager.*;

/**
 * JFrame skeleton with theme changing menu
 * Created by martin on 28/03/2015.
 */
public class FrameSkeleton extends JFrame {

    public final JTextField messageField;
    public final StyleContext context;
    public final StyledDocument chatRoomDocument, usersOnlineDocument;
    public final JTextPane chatPane, onlinePane;
    public final Style userNameStyle, textStyle;

    private static final Color BACKGROUND_COLOR = Color.white;
    private static final Border BORDER = new LineBorder(Color.lightGray);

    public FrameSkeleton(String title) {
        super(title);
        messageField = new JTextField();
        context = new StyleContext();
        chatRoomDocument = new DefaultStyledDocument(context);
        usersOnlineDocument = new DefaultStyledDocument(context);
        userNameStyle = context.addStyle("Username", null);
        textStyle = context.addStyle("Text", null);
        setStyles();
        chatPane = new JTextPane(chatRoomDocument);
        onlinePane = new JTextPane(usersOnlineDocument);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(themesMenu());
        buildContentPane();
        setJMenuBar(menuBar);
        setTheme("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(600, 400);
        setResizable(false);
        setVisible(true);
    }

    private void setStyles() {
        userNameStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
        userNameStyle.addAttribute(StyleConstants.FontFamily, "serif");
        userNameStyle.addAttribute(StyleConstants.Bold, new Boolean(true));
        userNameStyle.addAttribute(StyleConstants.Italic, new Boolean(true));

        textStyle.addAttribute(StyleConstants.FontSize, new Integer(14));
        textStyle.addAttribute(StyleConstants.FontFamily, "arial");
        textStyle.addAttribute(StyleConstants.Bold, new Boolean(false));
    }

    private void setTheme(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
            SwingUtilities.updateComponentTreeUI(FrameSkeleton.this);
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

        onlinePane.setEditable(false);
        onlinePane.setBorder(BORDER);
        JScrollPane usersOnlineScrollPane = new JScrollPane(onlinePane,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        usersOnlineScrollPane.setBounds(437, 18, 156, 300);
        usersOnlineScrollPane.setBorder(BORDER);
        getContentPane().add(usersOnlineScrollPane);
    }

    private void setAndPlaceMessageField() {
        messageField.setBounds(85, 320, 508, 26);
        messageField.setBorder(BORDER);
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
                SwingUtilities.updateComponentTreeUI(FrameSkeleton.this);
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

    public void addToChatPane(Color userColor, String username, String text) {
        userNameStyle.addAttribute(StyleConstants.Foreground, userColor);
        try {
            chatRoomDocument.insertString(0, String.format("%s%n", text), textStyle);
            chatRoomDocument.insertString(0, String.format("[%s]:", username), userNameStyle);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void refillUsersOnlinePane(List<Client> onlineClients) {
        try {
            usersOnlineDocument.remove(0, usersOnlineDocument.getLength());
            for(Client c : onlineClients) {
                userNameStyle.addAttribute(StyleConstants.Foreground, c.getColor());
                usersOnlineDocument.insertString(0, String.format("%s%n", c.getUserName()), userNameStyle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color randColor() {
        return new Color(rand(), rand(), rand());
    }

    public static int rand() {
        int color = (int) Math.round(256 * Math.random());
        return color > 230 ? 230 : color;
    }

    public static void main(String[] args) {
        new FrameSkeleton("frame skeleton");
    }
}
