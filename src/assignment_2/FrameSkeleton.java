package assignment_2;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import static javax.swing.UIManager.*;

/**
 * JFrame skeleton with theme changing menu
 * Created by martin on 28/03/2015.
 */
public class FrameSkeleton extends JFrame {

    public FrameSkeleton(String title) {
        super(title);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(themesMenu());
        setJMenuBar(menuBar);
        setSize(400,400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(FrameSkeleton.this);
        } catch (Exception e) { e.printStackTrace(); }
        setVisible(true);
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

    public static void main(String[] args) throws RemoteException, NotBoundException {
        new FrameSkeleton("skeleton");
    }
}
