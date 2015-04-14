package assignment_2.helperClasses;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import static javax.swing.UIManager.*;

/**
 * JFrame skeleton with theme changing menu
 * Created by martin on 28/03/2015.
 */
public class FrameSkeleton extends JFrame {

    public final JTextArea chatTextArea;
    public final JTextField messageField;
    public final JButton sendButton;
    public final JButton disconnectButton;
    public final JLabel nameLabel;
    private static final Color COLOR = new Color(255, 255, 170);
    private static final Border BORDER = new EmptyBorder(0, 0, 0, 0);

    public FrameSkeleton(String title) {
        super(title);
        chatTextArea = new JTextArea(10, 50);
        messageField = new JTextField(40);
        sendButton = new JButton("Send");
        disconnectButton = new JButton("Disconnect");
        nameLabel = new JLabel();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(themesMenu());
        buildContentPane();
        setJMenuBar(menuBar);
        setTheme("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void setTheme(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
            SwingUtilities.updateComponentTreeUI(FrameSkeleton.this);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void buildContentPane() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getNorthPanel(), BorderLayout.NORTH);
        getContentPane().add(getCenterPanel(), BorderLayout.CENTER);
        getContentPane().add(getSouthPanel(), BorderLayout.SOUTH);
        pack();
    }

    private JPanel getNorthPanel() {
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.white);
        disconnectButton.setBackground(COLOR);
        northPanel.add(nameLabel);
        northPanel.add(disconnectButton);
        return northPanel;
    }

    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.white);
        chatTextArea.setEditable(false);
        chatTextArea.setBorder(BORDER);
        chatTextArea.setBackground(COLOR);
        JScrollPane scrollPane = new JScrollPane(chatTextArea);
        scrollPane.setBorder(BORDER);
        centerPanel.add(scrollPane);
        return centerPanel;
    }

    private JPanel getSouthPanel(){
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.white);
        JLabel messageLabel = new JLabel("Message:");
        messageField.setBorder(BORDER);
        messageField.setBackground(COLOR);
        sendButton.setBackground(COLOR);
        southPanel.add(messageLabel);
        southPanel.add(messageField);
        southPanel.add(sendButton);
        return southPanel;
    }

    private class LookAndFeelMenuItem extends JMenuItem implements ActionListener{

        private final LookAndFeelInfo lookAndFeelInfo;

        private LookAndFeelMenuItem(final LookAndFeelInfo lookAndFeelInfo) {
            super(lookAndFeelInfo.getName());
            this.lookAndFeelInfo = lookAndFeelInfo;
            addActionListener(this);
            //System.out.println("theme: " + lookAndFeelInfo.getClassName());
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                LookAndFeelMenuItem menuItem = ((LookAndFeelMenuItem) event.getSource());
                String className = menuItem.lookAndFeelInfo.getClassName();
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(FrameSkeleton.this);
                //pack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JMenu themesMenu() {
        JMenu menuLookAndFeel = new JMenu("Themes");
        Arrays.asList(getInstalledLookAndFeels())
                .forEach(i -> menuLookAndFeel.add(new LookAndFeelMenuItem(i)));
        return menuLookAndFeel;
    }
}
