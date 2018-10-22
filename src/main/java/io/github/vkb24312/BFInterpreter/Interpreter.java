package io.github.vkb24312.BFInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

public class Interpreter {
    String code;
    int index = 0;
    char codeAt(int index){
        return code.charAt(index);
    }

    PrintStream out;
    PrintStream err;
    InputStream in;

    byte[] cells = new byte[30000];
    int pointer = 0;


    void add(){
        if((cells[pointer]&0xff) >= 255){
            cells[pointer] = 0;
        } else {
            cells[pointer]++;
        }
    }

    void subtract(){
        if((cells[pointer&0xff]) <= 0){
            cells[pointer] = -1;
        } else {
            cells[pointer]--;
        }
    }

    void pointerUp(){
        if(pointer>=cells.length){
            pointer = 0;
        } else {
            pointer++;
        }
    }

    void pointerDown(){
        if(pointer<=0){
            pointer = cells.length;
        } else {
            pointer--;
        }
    }

    void prompt(){
        try{
            cells[pointer] = ((byte) in.read());
        } catch (IOException e){
            e.printStackTrace(err);
        }
    }

    void print(){
        out.print((char) (cells[pointer]&0xff));

        if(out.checkError()){
            err.println("There was an error in output");
        }
    }

    void openLoop(){
        if(cells[pointer]==0){
            index = findPair(index);
        }
    }

    void closeLoop(){
        if(cells[pointer]!=0)
        index = findPair(index);
    }


    public void run(){
        for (index = 0; index < code.toCharArray().length; index++) {
            switch(codeAt(index)){
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
    }

    public Interpreter(String code, PrintStream output, InputStream input, PrintStream error){
        this.code = code;
        out = output;
        in = input;
        err = error;
    }

    int findPair(int index){
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
            while(codeAt(index)!='[' || indent!=0){
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
