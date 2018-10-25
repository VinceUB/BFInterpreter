package io.github.vkb24312.BFInterpreter.ArgumentParsing;

import java.util.ArrayList;
import java.util.Collection;

public class ArgList extends ArrayList<Argument> {
    public ArgList(Collection<Argument> c){
        super(c);
    }

    public Argument getArgument(String key){
        for(Argument a : this){
            if(a.getFull().equals(key)) return a;
            else if(a.getShort().equals(key)) return a;
            for(String alias : a.getAliases()){
                if(alias.equals(key)) return a;
            }
        }

        return null;
    }

    public Argument get(String key){
        return getArgument(key);
    }
}
