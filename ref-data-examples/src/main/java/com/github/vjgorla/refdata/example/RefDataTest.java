package com.github.vjgorla.refdata.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefDataTest {

    private static final Logger LOG = LoggerFactory.getLogger(RefDataTest.class);
    
    public static void main(String[] args) throws InterruptedException {
        Country country1 = Country.AUS;
        country1.getDescription();
        for (Country country : Country.effectiveValues()) {
            LOG.debug(country.toString() + " : " + country.getStates());
        }
        LOG.debug("----------------------------");
        for (Country country : Country.sortedByName()) {
            LOG.debug(country.toString() + " : " + country.getEffectiveTo());
        }
//        LOG.debug("" + Country.AUS.getStates());
//        for (int i = 0; i < 100; i++) {
//            LOG.debug(Country.NZ + ":" + Country.NZ.getStates());
//            Thread.sleep(10 * 1000);
//        }
    }

}
