package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import java.io.*;

public class ProgramInput extends JTextField {

    InputStream in = new InputStream() {
        int mark = 0;
        int index = 0;

        @Override
        public int read() throws IOException {
            index++;
            return getText().charAt(index-1);
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
        public synchronized void reset() throws IOException {
            index = mark;
        }
    };
}
