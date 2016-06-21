package com.github.vjgorla.refdata.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.github.vjgorla.refdata.AbstractRefDataValue;
import com.github.vjgorla.refdata.RefDataType;

@SuppressWarnings("serial")
public class Country extends AbstractRefDataValue {

    private static final RefDataType<Country> TYPE = new RefDataType<Country>(Country.class);
    
    public static final Country AUS = decode("AUS");
    public static final Country NZ = decode("NZ");
    
    private Country(String code) {
        super(TYPE, code);
    }
    
    public String getDialingCode() {
        return getAttribute("dialingCode");
    }
    
    public List<State> getStates() {
        List<State> states = new ArrayList<>();
        for (String stateCode : getAttributeAsList("states")) {
            states.add(State.decode(stateCode.trim()));
        }
        return states;
    }
    
    public static Country decode(String code) {
        return TYPE.decode(code);
    }
    
    public static List<Country> values() {
        return TYPE.values(false);
    }

    public static List<Country> effectiveValues() {
        return TYPE.values(true);
    }

    public static List<Country> sortedByName() {
        return TYPE.values(false, new Comparator<Country>() {
            @Override
            public int compare(Country c1, Country c2) {
                return c1.getDescription().compareTo(c2.getDescription());
            }
        });
    }
}
