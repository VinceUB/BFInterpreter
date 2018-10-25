package io.github.vkb24312.BFInterpreter.ArgumentParsing;

import java.util.ArrayList;
import java.util.Arrays;

public class Argument {
    private String main;
    private String desc;
    private ArrayList<String> aliases;
    public String getFull(){
        return main;
    }
    public String getShort(){
        try {
            return getAlias(0);
        } catch (IndexOutOfBoundsException ignored){
            return null;
        }
    }
    public String getDescription(){
        return desc;
    }
    public ArrayList<String> getAliases(){
        return aliases;
    }
    public String getAlias(int index){
        return getAliases().get(index);
    }

    public Argument(String main, String description, String... aliases){
        this.main = main;
        this.aliases = new ArrayList<>(Arrays.asList(aliases));
        desc = description;
    }
}