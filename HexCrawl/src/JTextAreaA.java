import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
/** subclass of JTextPane allowing color attributes */
public class JTextAreaA extends JTextPane
{
    private DefaultStyledDocument m_defaultStyledDocument=new DefaultStyledDocument();
    /** constructor */
    public JTextAreaA()
    {
        this.setDocument(m_defaultStyledDocument);
    }
    /** append text */
    public void append(String string,Color color)
    {
        try
        {
            SimpleAttributeSet attr=new SimpleAttributeSet();
            StyleConstants.setForeground(attr,color);
            m_defaultStyledDocument.insertString(m_defaultStyledDocument.getLength(),string,attr);
        }
        catch(Exception ignored)
        {

        }
    }
    public void append(String string,Color color,Color color2)
    {
        try
        {
            SimpleAttributeSet attr=new SimpleAttributeSet();
            StyleConstants.setForeground(attr,color);
            StyleConstants.setBackground(attr,color2);
            m_defaultStyledDocument.insertString(m_defaultStyledDocument.getLength(),string,attr);
        }
        catch(Exception ignored)
        {

        }
    }
    /** append text in default color */
    public void append(String string)
    {
        append(string,Color.white);
    }
}
