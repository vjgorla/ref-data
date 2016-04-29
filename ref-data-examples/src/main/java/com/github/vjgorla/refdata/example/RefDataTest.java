package com.github.vjgorla.refdata.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefDataTest {

    private static final Logger LOG = LoggerFactory.getLogger(RefDataTest.class);
    
    public static void main(String[] args) throws InterruptedException {
        Country aus = Country.AUS;
        aus.getDescription();
        aus.getStates();
        
        LOG.debug("--------- Active -------------------");
        for (Country country : Country.effectiveValues()) {
            LOG.debug(country.toString() + " : " + country.getDialingCode() + " : " + country.getStates());
        }
        LOG.debug("---------- All ------------------");
        for (Country country : Country.sortedByName()) {
            LOG.debug(country.toString() + " : " + country.getEffectiveFrom() + " : " + country.getEffectiveTo());
        }
    }
}
