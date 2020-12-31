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
    JTextAreaA textPaneHView = new JTextAreaA();
    JTextAreaA textPaneHView2 = new JTextAreaA();

    JTextAreaA textPaneYView = new JTextAreaA();
    JTextAreaA textPaneYView2 = new JTextAreaA();

    JTextAreaA textPaneXView = new JTextAreaA();
    JTextAreaA textPaneXView2 = new JTextAreaA();

    static int WIDTH = 1295;
    static int HEIGHT = 695;
    Font fontLar = new Font("Monospaced", Font.BOLD, 13);
    Font fontMed = new Font("Monospaced", Font.BOLD, 14);
    Font fontSma = new Font("Monospaced", Font.BOLD, 11);
    public static int fontSize = 12;

    public GUI(){
        newTextPane(textPaneXView, fontMed, new Dimension(300, 250));
        newTextPane(textPaneYView, fontMed, new Dimension(200, 200));
        newTextPane(textPaneXView2, fontMed, new Dimension(300, 250));
        newTextPane(textPaneYView2, fontMed, new Dimension(200, 200));
        newTextPane(textPaneHView, fontMed, new Dimension(WIDTH*3/5 - 100, HEIGHT*6/7));
        newTextPane(textPaneHView2, fontMed, new Dimension(WIDTH*3/5 - 100, HEIGHT*6/7));
        newTextPane(errorPane, fontLar, new Dimension(WIDTH /5 - 35, HEIGHT*4/7));
        layeredPane.add(textPaneHView, 100);
        errorPane.setText("errorPane");
        layeredPane.add(textPaneHView2, 100);
        layeredPane.add(errorPane, 286);
        layeredPane.add(textPaneXView, 101);
        layeredPane.add(textPaneYView, 101);
        layeredPane.add(textPaneXView2, 101);
        layeredPane.add(textPaneYView2, 101);
        layeredPane.setBackground(Color.black);
        layeredPane.setPreferredSize(new Dimension((int) WIDTH, (int) HEIGHT));
        layeredPane.setFocusable(true);
        Point p = new Point(10, 10);
        Point p2 = new Point(700, 0);
        Point p3 = new Point(700, 250);
        Point p4 = new Point(WIDTH*4/5 - 2, 19);
        errorPane.setForeground(Color.white);
        errorPane.setLocation(p4);
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
        textPaneXView2.setBorder(BorderFactory.createLineBorder(Color.white));
        textPaneHView.setBorder(BorderFactory.createLineBorder(Color.white));
        textPaneHView2.setBorder(BorderFactory.createLineBorder(Color.white));
        addKeyListenerHere(panel);
        addKeyListenerHere(textPaneHView);
        addKeyListenerHere(textPaneHView2);
        addKeyListenerHere(layeredPane);
        addKeyListenerHere(errorPane);
        panel.setBackground(Color.black);
        panel.setFocusable(true);

    }
    public void newTextPane(JTextAreaA infoPane, Font font, Dimension dim) {
        infoPane.setVisible(true);
        infoPane.setPreferredSize(dim);
        infoPane.setSize(dim);
        infoPane.setText("textPane");
        //infoPane.setBorder(BorderFactory.createLineBorder(Color.white));
        infoPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
        infoPane.setFont(font);
        infoPane.setOpaque(true);
        infoPane.setEditable(false);
        infoPane.setFocusable(false);
        infoPane.setBackground(Color.black);
        infoPane.setVisible(true);

        //infoPane.setBorder(BorderFactory.createLineBorder(Color.white));
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
    public void addKeyListenerHere(JComponent comp) {
        comp.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                int keyCode = e.getKeyCode();

                switch (keyCode) {
                    /*
                    case (KeyEvent.VK_F5) -> {
                        fontSize++;
                        try {
                            textPaneHView.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                            textPaneHView2.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                        } catch (Exception ignored) {
                            fontSize--;
                        }
                    }
                    case (KeyEvent.VK_F6) -> {
                        fontSize--;
                        try {
                            textPaneHView.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                            textPaneHView2.setFont(new Font("Monospaced", Font.BOLD, fontSize));
                        } catch (Exception ignored) {
                            fontSize++;
                        }
                    }

                     */
                    case (KeyEvent.VK_X) -> {
                        Game.takeDamage = true;
                    }
                    case (KeyEvent.VK_F1) -> {
                        Game.radx--;
                    }
                    case (KeyEvent.VK_F2) -> {
                        Game.radx++;
                    }
                    case (KeyEvent.VK_F3) -> {
                        Game.rady--;
                    }
                    case (KeyEvent.VK_F4) -> {
                        Game.rady++;
                    }
                    case (KeyEvent.VK_F5) -> {
                        Game.radz--;
                    }
                    case (KeyEvent.VK_F6) -> {
                        Game.radz++;
                    }
                    case (KeyEvent.VK_F7) -> {
                        Game.light--;
                    }
                    case (KeyEvent.VK_F8) -> {
                        Game.light++;
                    }
                    case (KeyEvent.VK_F9) -> {
                        Game.checkCollision = !Game.checkCollision;
                    }
                    case (KeyEvent.VK_W) -> {
                        Game.input = "up";
                    }
                    case (KeyEvent.VK_S) -> {
                        Game.input = "down";
                    }
                    case (KeyEvent.VK_A) -> {
                        Game.input = "left";
                    }
                    case (KeyEvent.VK_D) -> {
                        Game.input = "right";
                    }
                    case (KeyEvent.VK_F) -> {
                        Game.input = "descend";
                    }
                    case (KeyEvent.VK_R) -> {
                        Game.input = "ascend";
                    }
                    case (KeyEvent.VK_SPACE) -> {
                        Game.input = "grab";
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

                }
            }

        });
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
