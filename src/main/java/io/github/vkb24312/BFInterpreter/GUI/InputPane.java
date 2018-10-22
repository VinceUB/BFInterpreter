package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputPane extends JTextPane {
    private final static String defaultText = "Enter your code here";

    StyleContext context = new StyleContext();
    Style ghostText = context.addStyle("ghost", null);

    public InputPane() {
        StyleConstants.setForeground(ghostText, Color.GRAY);

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
                    setCharacterAttributes(context.getStyle("default"), true);
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
