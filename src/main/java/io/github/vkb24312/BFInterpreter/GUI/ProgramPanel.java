package io.github.vkb24312.BFInterpreter.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ProgramPanel extends JPanel {
    JScrollPane scrollPane;

    ProgramInput inputPane;
    public ProgramOutput outputPane;

    public PrintStream out;
    public PrintStream err;
    public InputStream in;

    public ProgramPanel() {
        inputPane = new ProgramInput();
        outputPane = new ProgramOutput();

        out = new PrintStream(outputPane.out);
        err = new PrintStream(outputPane.err);
        in = inputPane.in;

        setLayout(new BorderLayout());

        add(inputPane, BorderLayout.PAGE_START);
        scrollPane = new JScrollPane(outputPane);
        scrollPane.setViewportView(outputPane);
        add(scrollPane, BorderLayout.CENTER);

        outputPane.setText("Once you press run, the output will appear here. Input can be entered above");
        outputPane.setEditable(false);
    }
}
