package com.github.vjgorla.refdata.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.github.vjgorla.refdata.util.NamingStrategy;

/**
 * 
 * @author Vijaya Gorla
 */
public class NamingStrategyTest {

    @Test
    public void givenSimpleClassName_whenGenerated_thenTypeCodeContainsUnderscores() {
        String typeCode = NamingStrategy.generateTypeCode(Test.class);
        assertThat(typeCode, is("TEST"));
    }

    @Test
    public void givenCamelCaseClassName_whenGenerated_thenTypeCodeContainsUnderscores() {
        String typeCode = NamingStrategy.generateTypeCode(NamingStrategyTest.class);
        assertThat(typeCode, is("NAMING_STRATEGY_TEST"));
    }
}
