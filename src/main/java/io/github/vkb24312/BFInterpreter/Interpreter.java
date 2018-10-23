package io.github.vkb24312.BFInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

class Interpreter {
    private String code;
    private int index = 0;
    private char codeAt(int index){
        return code.charAt(index);
    }

    private PrintStream out;
    private PrintStream err;
    private InputStream in;

    private byte[] cells = new byte[30000];
    private int pointer = 0;

    //region Actions? Methods? Commands? Dunno what to call them. They're just methods that are called for each symbol
    private void add(){
        if((cells[pointer]&0xff) >= 255){
            cells[pointer] = 0;
        } else {
            cells[pointer]++;
        }
    }
    private void subtract(){
        if((cells[pointer]&0xff) <= 0){
            cells[pointer] = -1;
        } else {
            cells[pointer]--;
        }
    }
    private void pointerUp(){
        if(pointer>=cells.length-1){
            pointer = 0;
        } else {
            pointer++;
        }
    }
    private void pointerDown(){
        if(pointer<=0){
            pointer = cells.length-1;
        } else {
            pointer--;
        }
    }
    private void prompt(){
        try{
            cells[pointer] = ((byte) in.read());
        } catch (IOException e){
            e.printStackTrace(err);
        }
    }
    private void print(){
        out.print((char) (cells[pointer]&0xff));

        if(out.checkError()){
            err.println("There was an error in output");
        }
    }
    private void openLoop(){
        if(cells[pointer]==0){
            index = findPair(index);
        }
    }
    private void closeLoop(){
        if(cells[pointer]!=0) {
            index = findPair(index);
        }
    }
    //endregion


    void run(){
        try {
            for (index = 0; index < code.toCharArray().length; index++) {
                //System.out.print(codeAt(index)); //To be used in debug. Comment out for efficiency
                switch (codeAt(index)) {
                    case '+':
                        add();
                        break;
                    case '-':
                        subtract();
                        break;
                    case '>':
                        pointerUp();
                        break;
                    case '<':
                        pointerDown();
                        break;
                    case '.':
                        print();
                        break;
                    case ',':
                        prompt();
                        break;
                    case '[':
                        openLoop();
                        break;
                    case ']':
                        closeLoop();
                        break;
                }
            }
        } catch (Exception e){
            e.printStackTrace(err);
        }
    }

    Interpreter(String code, PrintStream output, InputStream input, PrintStream error){
        this.code = code;
        out = output;
        in = input;
        err = error;
    }

    private int findPair(int index){
        int i = index;

        if(codeAt(index)=='['){

            int indent = 0;
            index++;
            while(index<code.length()){
                if(codeAt(index)=='['){
                    indent++;
                } else if(codeAt(index)==']') {
                    if (indent <= 0) {
                        return index;
                    } else {
                        indent--;
                    }
                }
                index++;
            }
            throw new BracketException("The bracket at position " + i + " needs to be closed");

        } else if(codeAt(index)==']'){
            int indent = 0;
            index--;
            while(index>=0){
                if(codeAt(index)=='['){
                    if(indent <= 0){
                        return index;
                    } else {
                        indent--;
                    }
                } else if(codeAt(index) == ']'){
                    indent++;
                }
                index--;
            }
            throw new BracketException("The bracket at postition " + i + " needs to be opened");

        } else {
            throw new IllegalArgumentException("Character " + codeAt(index) + " is not a bracket");
        }
    }
}

class BracketException extends RuntimeException{
    BracketException(String message){
        super(message);
    }
}