package io.github.vkb24312.BFInterpreter;

import io.github.vkb24312.BFInterpreter.GUI.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String... args) {
        if(args.length>0){
            if(Arrays.asList(args).contains("-help") || Arrays.asList(args).contains("/help") || Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("/h")){
                System.out.println(help());
            }else if(Arrays.asList(args).contains("-c") || Arrays.asList(args).contains("-console") || Arrays.asList(args).contains("/c") || Arrays.asList(args).contains("/console")){
                /*if(Arrays.asList(args).contains("-f")){
                    ConsoleRun(
                            new File(
                                    args[
                                            Arrays.asList(args).indexOf("-f")+1
                                    ]
                            )
                    );
                } if(Arrays.asList(args).contains("/f")){
                    ConsoleRun(
                            new File(
                                    args[
                                            Arrays.asList(args).indexOf("/f")+1
                                            ]
                            )
                    );
                } else if(Arrays.asList(args).contains("-file")){
                    ConsoleRun(
                            new File(
                                    args[
                                            Arrays.asList(args).indexOf("-file")+1
                                    ]
                            )
                    );
                } else if(Arrays.asList(args).contains("/file")){
                    ConsoleRun(
                            new File(
                                    args[
                                            Arrays.asList(args).indexOf("/file")+1
                                            ]
                            )
                    );
                }*/
                ConsoleRun();
            } else {
                GUIRun();
            }

        } else if(GraphicsEnvironment.isHeadless()){
            ConsoleRun();

        } else {
            GUIRun();
        }
    }

    private static void ConsoleRun(){
        ConsoleRun(null);
    }

    private static void GUIRun(){
        JFrame frame = new JFrame("Brainfuck interpreter");
        frame.setSize(500, 500);
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

    private static void ConsoleRun(File f){
        if(f!=null && f.exists()){
            try {
                String code = new String(Files.readAllBytes(f.toPath()));
                new Interpreter(code, System.out, System.in, System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(f!=null && !f.exists()){
            System.out.println("File " + f.getAbsolutePath() + " does not exist. Now opening non-file thing");
        } else if(f==null){
            System.out.println("Please enter your code, ending with \"END\" (no quotes) on separate line");
            Scanner s = new Scanner(System.in);
            s.useDelimiter("\nEND");

            StringBuilder code = new StringBuilder();
            String next;
            do {
                next = s.next();
                code.append(next);
            } while (next.contains("\nEND"));

            new Interpreter(code.toString(), System.out, System.in, System.err).run();
        }
    }

    private static String help() {
        File source = null;
        try {
            source = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String description = "Just a brainfuck compiler made in java";

        String command = "java " + Main.class.getCanonicalName();
        if(source==null) {
            command = "java -jar *jarfile*";
        } else if(source.getName().split("\\.")[source.getName().split("\\.").length-1].equals("jar")) {
            command = "java -jar " + source.getName();
        }

        HashMap<String, String> options = new HashMap<>();
        options.put("-help", "Displays the help message");
        options.put("-console", "Runs in non-gui mode");
        options.put("-file", "Uses file (specified after) as input (Planned feature)");

        StringBuilder message = new StringBuilder("");

        message.append(description);
        message.append("\nUsage: ").append(command).append(" [OPTIONS]\nOptions:");

        options.forEach((k, v) -> message.append("\n\t").append(k).append("\t").append(v));

        return message.toString();
    }
}
