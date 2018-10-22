package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;

public class ProgramOutput extends JTextPane {
    StyleContext context = new StyleContext();
    Style output = context.addStyle("out", null);
    Style error = context.addStyle("err", null);

    OutputStream out = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            try {
                getStyledDocument().insertString(getStyledDocument().getLength(), Character.toString((char) b), output);
            } catch (BadLocationException e) {
                throw new IOException(e.getCause());
            }
        }
    };

    OutputStream err = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
            try {
                getStyledDocument().insertString(getStyledDocument().getLength(), Character.toString((char) b), error);
            } catch (BadLocationException e) {
                throw new IOException(e.getCause());
            }
        }
    };

    public ProgramOutput(){
        StyleConstants.setForeground(error, Color.RED);
    }
}
