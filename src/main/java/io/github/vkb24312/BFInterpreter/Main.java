package io.github.vkb24312.BFInterpreter;

import io.github.vkb24312.BFInterpreter.GUI.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    public static void main(String... args) throws IOException {
        JFrame frame = new JFrame("Brainfuck interpreter");
        frame.setSize(500, 750);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JScrollPane codeScrollPane = new JScrollPane();
        InputPane codePane = new InputPane();
        ProgramPanel programPane = new ProgramPanel();

        codeScrollPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()/2));
        programPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()-(codeScrollPane.getPreferredSize().height)));

        JButton run = new JButton("Run code");

        codeScrollPane.setViewportView(codePane);
        mainPanel.add(codeScrollPane, BorderLayout.PAGE_START);
        mainPanel.add(run, BorderLayout.CENTER);
        mainPanel.add(programPane, BorderLayout.PAGE_END);

        frame.pack();
        frame.setVisible(true);

        run.addActionListener((ActionEvent l) -> {
            try{
                programPane.outputPane.setText("");
                programPane.in.reset();

                new Interpreter(codePane.getText(), programPane.out, programPane.in, programPane.err).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
