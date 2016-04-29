package com.github.vjgorla.refdata.example;

import java.util.List;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;

@SuppressWarnings("serial")
public class State extends AbstractRefDataValue {

    private static final RefDataType<State> TYPE = new RefDataType<State>(State.class);
    
    public static final State ACT = decode("ACT");
    public static final State NSW = decode("NSW");
    
    private State(String code) {
        super(TYPE, code);
    }
    
    public static State decode(String code) {
        return TYPE.decode(code);
    }
    
    public static List<State> values() {
        return TYPE.values(false);
    }
}
