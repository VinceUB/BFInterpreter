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
        if((cells[pointer&0xff]) <= 0){
            cells[pointer] = -1;
        } else {
            cells[pointer]--;
        }
    }
    private void pointerUp(){
        if(pointer>=cells.length){
            pointer = 0;
        } else {
            pointer++;
        }
    }
    private void pointerDown(){
        if(pointer<=0){
            pointer = cells.length;
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
            while(codeAt(index)!=']' || indent!=0){
                if(codeAt(index)=='[') indent++;
                else if(codeAt(index)==']' && indent!=0) indent--;
                index++;
            }
            return index;

        } else if(codeAt(index)==']'){
            int indent = 0;
            index--;
            while(index<0 || (codeAt(index)!='[' || indent!=0)){
                if(index<0) throw new RuntimeException("Hold on, the bracket at " + i + " isn't closed :/"); //FIXME: This thing throws RuntimeExceptions seemingly randomly. Probably has something to do with mishandled nesting
                if(codeAt(indent)=='[' && indent!=0) indent--;
                else if(codeAt(index)==']') indent++;
                index--;
            }
            return index;

        } else {
            throw new RuntimeException("Character " + codeAt(index) + " is not a bracket");
        }
    }
}
