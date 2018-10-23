package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class InputPane extends JTextPane {
    private final static String defaultText = "Enter your code here";

    private StyleContext context = new StyleContext();
    private Style ghostText = context.addStyle("ghost", null);
    private Style code = context.getStyle("default");

    public InputPane() {
        StyleConstants.setForeground(ghostText, Color.GRAY);
        try {
            setFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getClassLoader().getResourceAsStream("DejaVuSansMono.ttf")).deriveFont(12.0f));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            setText("\nThere seems to have been a problem loading the font. Perhaps you messed about in the jar file?\n" +
                    " If you haven't deleted or modified anything in the jarfile, please contact me on github (https://github.com/vkb24312/BFInterpreter/issues) \n");
            e.printStackTrace(new PrintStream(new OutputStream() {
                @Override
                public void write(int b){
                    setText(getText() + (char) b);
                }
            }));
            e.printStackTrace();
        }

        try {
            getStyledDocument().insertString(0, defaultText, ghostText);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(getText().equals(defaultText)){
                    setText("");
                    setCharacterAttributes(code, true);
                } else if(getText().isEmpty()){
                    try {
                        getStyledDocument().insertString(0, defaultText, ghostText);
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                    setCaretPosition(0);
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(getText().equals(defaultText)){
                    setCaretPosition(0);
                }
            }
        });

        addCaretListener(cl -> {
            if(getText().equals(defaultText)){
                setCaretPosition(0);
            }
        });
    }
}
