import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class GUI extends JTextPane {
    public static ArrayList<String> lines = new ArrayList<String>();
    public static ArrayList<Color> linesColor = new ArrayList<Color>();
    JFrame frame = new JFrame();
    JPanel panel = new JPanel(new BorderLayout());
    JLayeredPane layeredPane = new JLayeredPane();
    JTextAreaA errorPane = new JTextAreaA();

    JTextAreaA inventoryPane = new JTextAreaA();

    JTextAreaA textPaneHView = new JTextAreaA();
    JTextAreaA textPaneHView2 = new JTextAreaA();

    JTextAreaA textPaneYView = new JTextAreaA();
    JTextAreaA textPaneYView2 = new JTextAreaA();

    JTextAreaA textPaneXView = new JTextAreaA();
    JTextAreaA textPaneXView2 = new JTextAreaA();

    static int WIDTH = 1295;
    static int HEIGHT = 695;
    Font fontMed = new Font("Monospaced", Font.BOLD, 14);
    public GUI(){
        newTextPane(textPaneXView, fontMed, new Dimension(300, 250));
        newTextPane(textPaneXView2, fontMed, new Dimension(300, 250));

        newTextPane(textPaneYView, fontMed, new Dimension(200, 200));
        newTextPane(textPaneYView2, fontMed, new Dimension(200, 200));

        newTextPane(textPaneHView, fontMed, new Dimension(WIDTH*3/5 - 100, HEIGHT*6/7));
        newTextPane(textPaneHView2, fontMed, new Dimension(WIDTH*3/5 - 100, HEIGHT*6/7));

        newTextPane(errorPane, fontMed, new Dimension(WIDTH /5 - 35, HEIGHT*4/7));
        newTextPane(inventoryPane, fontMed, new Dimension(WIDTH /5 - 35, HEIGHT*4/7));
        layeredPane.add(textPaneHView, 100);
        layeredPane.add(textPaneHView2, 100);

        //layeredPane.add(textPaneXView, 101);
        //layeredPane.add(textPaneXView2, 101);

        //layeredPane.add(textPaneYView, 101);
        //layeredPane.add(textPaneYView2, 101);

        layeredPane.add(errorPane, 286);
        layeredPane.add(inventoryPane, 286);
        layeredPane.setBackground(Color.black);
        layeredPane.setPreferredSize(new Dimension((int) WIDTH, (int) HEIGHT));
        layeredPane.setFocusable(true);

        errorPane.setText("errorPane");
        inventoryPane.setText("invPane");
        Point p = new Point(10, 10);
        Point p2 = new Point(700, 0);
        Point p3 = new Point(700, 250);
        Point p4 = new Point(WIDTH*4/5 - 2, 19);
        Point p5 = new Point(700, 10);
        errorPane.setForeground(Color.white);
        errorPane.setLocation(p4);

        inventoryPane.setForeground(Color.white);
        inventoryPane.setLocation(p5);

        textPaneHView.setLocation(p);
        textPaneHView2.setLocation(p);
        textPaneXView.setLocation(p2);
        textPaneYView.setLocation(p3);
        textPaneXView2.setLocation(p2);
        textPaneYView2.setLocation(p3);

        textPaneYView.setText("hello");
        textPaneXView.setText("Hello");
        panel.add(layeredPane);
        setPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        frame.add(panel, BorderLayout.CENTER);
        setFrame();
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        textPaneHView.selectAll();
        panel.setBackground(Color.black);
        errorPane.setBorder(BorderFactory.createLineBorder(Color.white));
        inventoryPane.setBorder(BorderFactory.createLineBorder(Color.white));
        textPaneXView2.setBorder(BorderFactory.createLineBorder(Color.white));
        textPaneHView.setBorder(BorderFactory.createLineBorder(Color.white));
        textPaneHView2.setBorder(BorderFactory.createLineBorder(Color.white));
        addKeyListenerHere(panel);
        addKeyListenerHere(textPaneHView);
        addKeyListenerHere(textPaneHView2);
        addKeyListenerHere(layeredPane);
        addKeyListenerHere(errorPane);
        addKeyListenerHere(inventoryPane);
        panel.setBackground(Color.black);
        panel.setFocusable(true);

    }
    public void newTextPane(JTextAreaA infoPane, Font font, Dimension dim) {
        infoPane.setPreferredSize(dim);
        infoPane.setSize(dim);
        infoPane.setText("textPane");
        infoPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
        infoPane.setFont(font);
        infoPane.setOpaque(true);
        infoPane.setEditable(false);
        infoPane.setFocusable(false);
        infoPane.setBackground(Color.black);
        infoPane.setVisible(true);
    }
    public void setFrame() {
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setTitle("Space_Game");
        frame.pack();
        frame.setVisible(true);
        frame.setBackground(Color.black);
    }
    public void setPanel() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setFocusable(false);
        panel.setBackground(Color.BLACK);
    }
    public static boolean DOWN_HELD = false;
    public static boolean UP_HELD = false;
    public static boolean LEFT_HELD = false;
    public static boolean RIGHT_HELD = false;
    public void addKeyListenerHere(JComponent comp) {
        comp.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case (KeyEvent.VK_W) -> {
                        UP_HELD = true;
                    }
                    case (KeyEvent.VK_S) -> {
                        DOWN_HELD = true;
                    }
                    case (KeyEvent.VK_A) -> {
                        LEFT_HELD = true;
                    }
                    case (KeyEvent.VK_D) -> {
                        RIGHT_HELD = true;
                    }
                    case (KeyEvent.VK_G) -> {
                        MainLoop.input = "g";
                    }
                    case (KeyEvent.VK_SPACE) -> {
                        MainLoop.entities.add(new Entity("O", "bubble", MainLoop.playerX, MainLoop.playerY + 1, Color.white));
                    }
                    /*
                    case () -> {

                    }
                     */
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case (KeyEvent.VK_W) -> {
                        UP_HELD = false;
                    }
                    case (KeyEvent.VK_S) -> {
                        DOWN_HELD = false;
                    }
                    case (KeyEvent.VK_A) -> {
                        LEFT_HELD = false;
                    }
                    case (KeyEvent.VK_D) -> {
                        RIGHT_HELD = false;
                    }
                }
            }

        });

    }
    public void inventoryFieldUpdater(String string) { ;
        inventoryPane.setText(string);
    }
    public void errorFieldUpdater(String string, Color color) { ;
        lines.add(string);
        linesColor.add(color);
        errorPane.setText("");
        if (lines.size() > 12) {
            lines.remove(0);
        }
        if (linesColor.size() > 12) {
            linesColor.remove(0);
        }
        for (int x = lines.size() - 1; x > 0; x--) {
            errorPane.append(lines.get(x), linesColor.get(x));
            errorPane.append("\n", linesColor.get(x));
            errorPane.append("\n", linesColor.get(x));
        }
    }
}
