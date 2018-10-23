package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import java.io.*;

public class ProgramInput extends JTextField {

    InputStream in = new InputStream() {
        int mark = 0;
        int index = 0;

        @Override
        public int read() {
            index++;
            try {
                return getText().charAt(index - 1);
            } catch (StringIndexOutOfBoundsException e){
                return '\u0000';
            }
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public synchronized void mark(int readlimit) {
            mark = readlimit;
        }

        @Override
        public synchronized void reset() {
            index = mark;
        }
    };
}
