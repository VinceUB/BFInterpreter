package io.github.vkb24312.BFInterpreter;

import io.github.vkb24312.BFInterpreter.ArgumentParsing.ArgList;
import io.github.vkb24312.BFInterpreter.ArgumentParsing.Argument;
import io.github.vkb24312.BFInterpreter.GUI.InputPane;
import io.github.vkb24312.BFInterpreter.GUI.ProgramPanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main extends Object{
    private static ArgList arguments = new ArgList(Arrays.asList(
            new Argument("help", "Displays the help message", "h"),
            new Argument("console", "Runs in non-gui mode", "c"),
            new Argument("file", "Uses file (specified after) as input", "f")
    ));

    public static void main(String... args) throws IOException {
        if(args.length>0){
            parseArguments(args);
        } else if(GraphicsEnvironment.isHeadless()){
            ConsoleRun();
        } else {
            GUIRun();
        }
    }

    private static void GUIRun() throws IOException{
        GUIRun(null);
    }

    private static void GUIRun(File f) throws IOException {
        JFrame frame = new JFrame("Brainfuck interpreter");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JScrollPane codeScrollPane = new JScrollPane();
        InputPane codePane = new InputPane();
        ProgramPanel programPane = new ProgramPanel();

        if(f!=null && f.isFile()){
            String code = new String(Files.readAllBytes(f.toPath()));

            codePane.setText(null);
            try {
                codePane.getStyledDocument().insertString(0, code, codePane.code);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }

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

    private static void ConsoleRun(){
        ConsoleRun(null);
    }

    private static void ConsoleRun(File f){
        if(f!=null && f.exists()){
            try {
                String code = new String(Files.readAllBytes(f.toPath()));
                new Interpreter(code, System.out, System.in, System.err).run();
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

        StringBuilder message = new StringBuilder("");

        message.append(description);
        message.append("\nUsage: ").append(command).append(" [OPTIONS]\nOptions:");

        message.append("\n\t").append("Full key\tShortcut\tDescription");
        for (Argument arg : arguments) {
            message.append("\n\t-").append(arg.getFull()).append("\t-").append(arg.getShort()).append("\t-").append(arg.getDescription());
        }

        return message.toString();
    }

    private static void parseArguments(String... args) throws IOException{
        //region Turn arguments into HashMap
        HashMap<Argument, String> argMap = new HashMap<>(arguments.size());

        if(new File(args[0]).exists()) {
            argMap.put(arguments.getArgument("file"), args[0]);
        }

        for (int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-") || args[i].startsWith("/")){
                args[i] = args[i].substring(1);
                Argument arg = arguments.getArgument(args[i]);
                if(arg==null){
                    System.out.println("Argument " + args[i] + " does not exist\n" + help());
                    return;
                }
                try {
                    if (args[i + 1].startsWith("-") || args[i+1].startsWith("/")) {
                        argMap.put(arg, null);
                    } else {
                        argMap.put(arg, args[i + 1]);
                    }
                } catch(ArrayIndexOutOfBoundsException e){
                    argMap.put(arg, null);
                }
            }
        }
        //endregion

        if(argMap.containsKey(arguments.get("help"))){
            System.out.println(help());
            return;
        }

        boolean consoleMode = argMap.containsKey(arguments.get("console"));

        if(consoleMode){
            if(argMap.containsKey(arguments.get("file"))){
                ConsoleRun(new File(argMap.get(arguments.get("file"))));
            } else {
                ConsoleRun();
            }
        } else {
            if(argMap.containsKey(arguments.get("file"))){
                GUIRun(new File(argMap.get(arguments.get("file"))));
            } else {
                GUIRun();
            }
        }
    }
}

